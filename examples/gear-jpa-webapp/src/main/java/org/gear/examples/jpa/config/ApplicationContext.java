package org.gear.examples.jpa.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * An application context Java configuration class. The usage of Java configuration
 * requires Spring Framework 3.0 or higher with following exceptions:
 * <ul>
 *     <li>@EnableWebMvc annotation requires Spring Framework 3.1</li>
 * </ul>
 * @author Petri Kainulainen
 */
@Configuration
@ComponentScan(basePackages = {"org.gear.examples.jpa.controller"})
@EnableWebMvc
@ImportResource("classpath:applicationContext.xml")
@PropertySource("classpath:application.properties")
public class ApplicationContext {

	private static final String VIEW_RESOLVER_PREFIX = "/WEB-INF/jsp/";
	private static final String VIEW_RESOLVER_SUFFIX = ".jsp";
	
	private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    private static final String PROPERTY_NAME_MESSAGESOURCE_BASENAME = "message.source.basename";
    private static final String PROPERTY_NAME_MESSAGESOURCE_USE_CODE_AS_DEFAULT_MESSAGE = "message.source.use.code.as.default.message";

    @Resource
    private Environment environment;
    
    @Bean
    public DataSource dataSource() {
    	BoneCPDataSource dataSource = new BoneCPDataSource();
    	dataSource.setDriverClass(PROPERTY_NAME_DATABASE_DRIVER);
    	dataSource.setJdbcUrl(PROPERTY_NAME_DATABASE_URL);
    	dataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
    	dataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);
    	
    	return dataSource;
    }
    
    @Bean
    public JpaTransactionManager transactionManager() throws ClassNotFoundException {
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
    	
    	transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
    	
    	return transactionManager;
    }

    @Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() throws ClassNotFoundException {
    	LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    	
    	entityManagerFactoryBean.setDataSource(dataSource());
    	entityManagerFactoryBean.setPackagesToScan(environment.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
    	entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
    	
    	Properties jpaProperties = new Properties();
    	jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
    	jpaProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
    	jpaProperties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY));
    	jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, environment.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
    	
    	entityManagerFactoryBean.setJpaProperties(jpaProperties);
    	
		return entityManagerFactoryBean;
	}
    
    @Bean
    public MessageSource messageSource() {
    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    	
    	messageSource.setBasename(PROPERTY_NAME_MESSAGESOURCE_BASENAME);
    	messageSource.setUseCodeAsDefaultMessage(Boolean.parseBoolean(environment.getRequiredProperty(PROPERTY_NAME_MESSAGESOURCE_USE_CODE_AS_DEFAULT_MESSAGE)));
    	
    	return messageSource;
    }
    
    @Bean
    public ViewResolver viewResolver() {
    	InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    	
    	viewResolver.setViewClass(JstlView.class);
    	viewResolver.setPrefix(VIEW_RESOLVER_PREFIX);
    	viewResolver.setSuffix(VIEW_RESOLVER_SUFFIX);
    	
    	return viewResolver;
    }
}
