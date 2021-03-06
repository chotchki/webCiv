package us.chotchki.webCiv.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import us.chotchki.installer.InstallerProcess;

public class WebCivStartup implements WebApplicationInitializer {
	private static final Logger log = LoggerFactory.getLogger(WebCivStartup.class);

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		log.info("Starting up WebCiv!");

		AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.setServletContext(sc);
		root.scan("us.chotchki.webCiv");
		root.refresh();
		
		sc.addListener(new ContextLoaderListener(root));

		//Run the installer
		log.info("Installing database schema.");
		InstallerProcess ip = new InstallerProcess();
		try {
			DataSource ds = (DataSource) root.getBean("dataSource");
			ip.install(ds);
		} catch (Exception e) {
			log.error("Install process failed", e);
			throw new ServletException(e);
		}
		
		
		
		//Handles requests into the application
		ServletRegistration.Dynamic appServlet = sc.addServlet("appServlet", new DispatcherServlet(new GenericWebApplicationContext()));
		if(appServlet != null){
			appServlet.setLoadOnStartup(1);
			Set<String> mappingConflicts = appServlet.addMapping("/");
			if (!mappingConflicts.isEmpty()) {
				throw new IllegalStateException("'appServlet' could not be mapped to '/' due "
						+ "to an existing mapping. This is a known issue under Tomcat versions "
						+ "<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");
			}
		}
		
		//Force cookie use for sessions
		sc.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		
		//Load the revision number if it exists
		String version = null;
		try (InputStream is = sc.getResourceAsStream("META-INF/MANIFEST.MF")){
			if(is != null){
				Properties props = new Properties();
				props.load(is);
				version = props.getProperty("Implementation-Build");
			}
		} catch (IOException e){
			log.info("Unable to get the app version from the manifest");
		}
		if(version == null){
			version = "DevBuild";
		}
		sc.setAttribute("version", version);
	}

}
