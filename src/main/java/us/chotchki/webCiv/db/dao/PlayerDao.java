package us.chotchki.webCiv.db.dao;

import org.springframework.stereotype.Repository;

import us.chotchki.webCiv.db.pojo.Player;

@Repository
public class PlayerDao extends ParentDao{
	public void create(Player p) {
		getSession().save(p);
	}
}
