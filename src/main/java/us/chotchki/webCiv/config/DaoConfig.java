package us.chotchki.webCiv.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DaoConfig {
	private static final String jndiPath = "jdbc/webCiv";
	
	 @Bean
	 public DataSource dataSource() throws Exception {
		 Context initCtx = new InitialContext();
		 Context envCtx = (Context) initCtx.lookup("java:comp/env");
		 DataSource ds = (DataSource) envCtx.lookup(jndiPath);
		 if (ds == null) {
			 throw new Exception("Lookup failed for the installer using the path " + jndiPath);
		 }
		 return ds;
	 }
	
	@Bean
	public SessionFactory sessionFactory(DataSource ds){
		LocalSessionFactoryBuilder lsfb = new LocalSessionFactoryBuilder(ds);
		lsfb.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
		lsfb.scanPackages("us.chotchki.webCiv.db.pojo");
		return lsfb.buildSessionFactory();
	}
	
	@Bean(name = "transactionManager")
	public HibernateTransactionManager transaction(SessionFactory sf){
		return new HibernateTransactionManager(sf);
	}
}
