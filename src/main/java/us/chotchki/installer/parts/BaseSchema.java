package us.chotchki.installer.parts;

import us.chotchki.installer.Part;

import java.sql.Connection;

public class BaseSchema extends Part {

	public BaseSchema(Connection conn) {
		super(conn);
	}

	@Override
	public long priority() {
		return 1;
	}
}
