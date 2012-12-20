package us.chotchki.installer;

import java.sql.Connection;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.chotchki.installer.classpath.ScanningLoader;

public class InstallerProcess implements ServletContextListener {
	private static Logger log = LoggerFactory.getLogger(InstallerProcess.class);

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String installerJndiPath = sce.getServletContext().getInitParameter(
				"us.chotchki.installer.InstallerProcess.jndi");
		if (installerJndiPath == null) {
			log.error("Unable to get the context parameter for installer.");
			return;
		}

		try {
			install(installerJndiPath);
		} catch(Exception e){
			log.error("Installation Process Failed!", e);
		}
	}

	public void install(String jndiPath) throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup(jndiPath);
		if (ds == null) {
			throw new Exception("Lookup failed for the installer using the path " + jndiPath);
		}
		install(ds);
	}
	
	public void install(DataSource ds) throws Exception {
		try (Connection conn = ds.getConnection()){
			if (conn == null) {
				throw new Exception("Unable to establish a database connection");
			}
			conn.setAutoCommit(false);
			
			List<Part> installParts = ScanningLoader.createParts(conn);	

			for (Part p : installParts) {
				if (p.isInstalled()) {
					log.info("Part {} is already installed", p.getClass()
							.getName());
				} else {
					log.info("Installing Part {}", p.getClass().getName());
					p.install();
				}
			}
			conn.commit();
			log.info("Installation Complete!");

			InstallerFlag.INSTANCE.enabled.set(false);
			log.info("Disabled the install filter");
		}
		
	}	
}
