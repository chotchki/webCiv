package us.chotchki.webCiv.db.dao;

import us.chotchki.webCiv.db.pojo.User;

public class UserDao extends ParentDao {
	public void create(User u){
		getSession().save(u);
	}
}
