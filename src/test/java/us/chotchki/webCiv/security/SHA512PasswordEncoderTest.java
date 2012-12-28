package us.chotchki.webCiv.security;

import static org.junit.Assert.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class SHA512PasswordEncoderTest {

	@Test
	public void testSHA512PasswordEncoder() throws InvalidKeyException, NoSuchAlgorithmException {
		new SHA512PasswordEncoder();
	}

}
