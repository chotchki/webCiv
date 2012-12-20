package us.chotchki.webCiv.db.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "webciv.player")
public class Player {
	@Id
	@Column(columnDefinition="bigserial")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id = null;
	
	@Column
	private String nickname = null;
	
	@Column
	private String email = null;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
