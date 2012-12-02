package us.chotchki.installer.parts;

import java.sql.Connection;

import us.chotchki.installer.Part;

public class UserSignUp extends Part {

	public UserSignUp(Connection conn) {
		super(conn);
	}

	@Override
	public long priority() {
		return 2;
	}
}
