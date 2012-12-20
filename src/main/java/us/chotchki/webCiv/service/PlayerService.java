package us.chotchki.webCiv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.webCiv.db.dao.PlayerDao;
import us.chotchki.webCiv.db.pojo.Player;

@Service
public class PlayerService {
	@Autowired
	private PlayerDao playerDao = null;
	
	@Transactional
	public void create(Player p){
		playerDao.create(p);
	}
}
