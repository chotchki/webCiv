package us.chotchki.webCiv.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import us.chotchki.installer.InstallerProcess;

public class WebCivStartup implements WebApplicationInitializer {
	private static final Logger log = LoggerFactory.getLogger(WebCivStartup.class);

	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		log.info("Starting up WebCiv!");

		log.info("Installing database schema.");
		InstallerProcess ip = new InstallerProcess();
		try {
			ip.install("jdbc/webCiv");
		} catch (Exception e) {
			log.error("Install process failed", e);
			throw new ServletException(e);
		}

		AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.scan("us.chotchki.webCiv");

		sc.addListener(new ContextLoaderListener(root));
		

		//Sitemesh Setup
		FilterRegistration.Dynamic sitemeshReg = sc.addFilter("sitemesh3", ConfigurableSiteMeshFilter.class);
		if(sitemeshReg != null){
			sitemeshReg.addMappingForUrlPatterns(null, false, "/*");
		}
		
		//Secures the application
		FilterRegistration.Dynamic securityFilterReg = sc.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"));
		if(securityFilterReg != null){
			securityFilterReg.addMappingForUrlPatterns(null, false, "/*");
		}
		
		//Force UTF-8
		FilterRegistration.Dynamic charEncodingfilterReg = sc.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
		if(charEncodingfilterReg != null){
			charEncodingfilterReg.setInitParameter("encoding", "UTF-8");
			charEncodingfilterReg.setInitParameter("forceEncoding", "true");
			charEncodingfilterReg.addMappingForUrlPatterns(null, false, "/*");
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
	}

}
