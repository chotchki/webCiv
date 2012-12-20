package us.chotchki.webCiv.db.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	private String username = null;
	
	@Column
	private String password = null;
	
	@Column
	private boolean enabled = true;
	
	@Column
	private boolean admin = false;
	
	@Column
	private BigInteger playerId = null;
	
	public User() {}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public BigInteger getPlayerId() {
		return playerId;
	}
	public void setPlayerId(BigInteger playerId) {
		this.playerId = playerId;
	}
}
