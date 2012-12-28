package us.chotchki.webCiv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.webCiv.db.dao.PlayerDao;
import us.chotchki.webCiv.db.dao.UserDao;
import us.chotchki.webCiv.db.pojo.Player;
import us.chotchki.webCiv.db.pojo.User;
import us.chotchki.webCiv.security.SHA512PasswordEncoder;

@Service
public class PlayerService {
	@Autowired
	private PlayerDao playerDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	SHA512PasswordEncoder sHA512PasswordEncoder;
	
	@Transactional
	public void register(Player p, User u){
		playerDao.create(p);
		
		//Hash the user password
		u.setPassword(sHA512PasswordEncoder.encodePassword(u.getPassword(), null));
		u.setPlayerId(p.getId());
		
		userDao.create(u);
		
	}
}
