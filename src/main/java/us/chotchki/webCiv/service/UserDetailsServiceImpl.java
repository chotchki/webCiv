package us.chotchki.webCiv.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.webCiv.db.dao.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserDao userDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		us.chotchki.webCiv.db.pojo.User user = userDao.getByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("User not found");
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(user.isAdmin() == true){
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		User principle = new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isEnabled(), user.isEnabled(), user.isEnabled(), authorities);
		return principle;
	}

}
