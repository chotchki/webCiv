package us.chotchki.webCiv.db.dao;

import org.springframework.stereotype.Repository;

import us.chotchki.webCiv.db.pojo.User;

@Repository
public class UserDao extends ParentDao {
	public void create(User u){
		getSession().save(u);
	}
	
	public User getByUsername(String username){
		return (User) getSession().byId(User.class).load(username);
	}
}
