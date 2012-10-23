package us.chotchki.webCiv;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import us.chotchki.installer.InstallerProcess;

public class WebCivStartup implements WebApplicationInitializer {
	private static final Logger log = LoggerFactory.getLogger(WebCivStartup.class);

	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
		log.info("Starting up WebCiv!");
		
		log.info("Installing database schema.");
		InstallerProcess ip = new InstallerProcess();
		try {
			ip.install("jdbc/webCiv");
		} catch (Exception e) {
			log.error("Install process failed", e);
			throw new ServletException(e);
		}
	}

}
