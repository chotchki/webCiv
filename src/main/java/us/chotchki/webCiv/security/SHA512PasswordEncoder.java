package us.chotchki.webCiv.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SHA512PasswordEncoder implements PasswordEncoder {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private int iterations;
	private final String algorithum = "HmacSHA512";
	
	public SHA512PasswordEncoder() throws NoSuchAlgorithmException, InvalidKeyException{
		//Start by benchmarking the iterations - we want to hit between .5 and .6 secs
		SecretKey testKey = createKey();

		//We'll start with a reasonable number, we'll time it and try to guess how far we should move the next test
		long duration = 0;
		iterations = 40000;
		int loop = 0;
		while(duration < 500 || duration > 600){
			DateTime start = new DateTime();
			calculateHash(testKey, iterations, "test");
			DateTime end = new DateTime();
			Interval interval = new Interval(start, end);
			duration = interval.toDurationMillis();
			if(duration > 600){
				iterations = (int) (iterations - (iterations * .3));
				log.debug("DOWN");
			} else if (duration < 500){
				iterations = (int) (iterations + (iterations * .3));
				log.debug("UP");
			}
			loop++;
		}
		log.debug("Hash iterations are set to {} taking {} millisecs and {} loops", iterations, duration, loop);
	}

	@Override
	public String encodePassword(String rawPassword, Object salt)
			throws DataAccessException {
		StringBuilder password = new StringBuilder("1$" + iterations + "$");

		try {
			SecretKey hmac_key = createKey();

			password.append(Base64.encodeBase64URLSafeString(hmac_key
					.getEncoded()));
			password.append("$");

			password.append(this.calculateHash(hmac_key, iterations,
					rawPassword));
		} catch (NoSuchAlgorithmException e) {
			throw new InvalidDataAccessApiUsageException(
					"Could not use hmac512", e);
		} catch (InvalidKeyException e) {
			throw new InvalidDataAccessApiUsageException("Bad hmac512 key", e);
		}
		return password.toString();
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		log.info("Checking hash " + encPass + " to Pass " + rawPass);
		String[] hash_parts = encPass.split("\\$");
		if (!hash_parts[0].equals("1")) {
			log.error("Bad hash version! " + hash_parts[0]);
			return false;
		}

		int valid_iter = Integer.parseInt(hash_parts[1]);
		SecretKey hmac_key = new SecretKeySpec(
				Base64.decodeBase64(hash_parts[2]), algorithum);

		try {
			String calcPass = this.calculateHash(hmac_key, valid_iter, rawPass);

			if (calcPass.equals(hash_parts[3])) {
				return true;
			} else {
				log.error("Hash " + hash_parts[3]
						+ " does not match, calculated " + calcPass);
				return false;
			}
		} catch (Exception e) {
			log.error("Could not run validators.", e);
			return false;
		}
	}

	private String calculateHash(SecretKey key, int iterations, String password)
			throws InvalidKeyException, NoSuchAlgorithmException {
		ArrayList<byte[]> hmacs = new ArrayList<byte[]>(iterations);

		byte[] output = new byte[64];
		Mac hasher = Mac.getInstance(algorithum);
		for (int i = 0; i < iterations; i++) {
			hasher.init(key);
			hasher.update(password.getBytes());
			hasher.update(output);
			output = hasher.doFinal();
			hmacs.add(output);
		}

		hasher.init(key);
		for (int i = hmacs.size() - 1; i >= 0; i--) {
			hasher.update(hmacs.get(i));
		}
		return Base64.encodeBase64URLSafeString(hasher.doFinal());
	}
	
	private SecretKey createKey() throws NoSuchAlgorithmException{
		KeyGenerator keygen = KeyGenerator.getInstance(algorithum);
		keygen.init(512);
		return keygen.generateKey();
	}
}