package us.chotchki.installer;

import us.chotchki.installer.util.Slf4jPrintWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Part implements Comparable<Part> {
	private static Logger log = LoggerFactory.getLogger(Part.class);
	private static final String installTable = "webCivInstaller".toLowerCase(); 
	protected final ScriptRunner scriptRunner;
	protected final SqlRunner sqlRunner;
	protected final Slf4jPrintWriter errorWriter;
	
	public Part(Connection conn) {
		scriptRunner = new ScriptRunner(conn);
		scriptRunner.setSendFullScript(true);
		scriptRunner.setLogWriter(new Slf4jPrintWriter(false));
		errorWriter = new Slf4jPrintWriter(true);
		scriptRunner.setErrorLogWriter(errorWriter);
		
		sqlRunner = new SqlRunner(conn);
	}
	
	public abstract long priority();
	
	public int compareTo(Part o) {
		return new Long(this.priority()).compareTo(o.priority());
	}
	
	public boolean isInstalled() throws Exception {
		Map<String, Object> row = this.sqlRunner.selectOne("SELECT count(*) as count FROM " + installTable + " WHERE partName = ? and installed = true", this.getClass().getSimpleName());
		long count = (Long) row.get("COUNT");
		if(count == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void install() throws Exception {
		//Get the current class with its full name
		String loaderLocation = this.getClass().getName();
		loaderLocation = loaderLocation.replaceAll("\\.", "/");
		loaderLocation = "/" + loaderLocation + ".sql";
		
		InputStream is = this.getClass().getResourceAsStream(loaderLocation);
		if(is == null) {
			throw new Exception("Loader script " + loaderLocation + " does not exist.");
		}
		
		InputStreamReader rdr = new InputStreamReader(is);
		
		log.info("Starting step {}", this.getClass().getSimpleName());
		this.preinstall();
		
		log.info("Running install script {}", loaderLocation);
		scriptRunner.runScript(rdr);
		
		if(errorWriter.wasWrittenTo()){
			throw new SQLException("The install part " + this.getClass().getName() + " failed.");
		}
		
		log.info("Script Complete, updating status");
		this.postinstall();
	}
	
	protected void preinstall() throws Exception {
		this.sqlRunner.delete("DELETE FROM " + installTable + " WHERE partName = ?", this.getClass().getSimpleName());
		this.sqlRunner.update("INSERT INTO " + installTable + " (partName) VALUES (?);", this.getClass().getSimpleName());
	}
	
	protected void postinstall() throws Exception {
		this.sqlRunner.update("UPDATE " + installTable + " SET installed = true WHERE partName = ? ", this.getClass().getSimpleName());
	}
}
