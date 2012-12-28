package us.chotchki.webCiv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.webCiv.db.dao.PlayerDao;
import us.chotchki.webCiv.db.dao.UserDao;
import us.chotchki.webCiv.db.pojo.Player;
import us.chotchki.webCiv.db.pojo.User;

@Service
public class PlayerService {
	@Autowired
	private PlayerDao playerDao = null;
	
	@Autowired
	private UserDao userDao = null;
	
	@Transactional
	public void register(Player p, User u){
		playerDao.create(p);
		userDao.create(u);
	}
}
