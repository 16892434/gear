package org.gear.examples.shiro.quickstart;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quickstart {

	private static final Logger logger = LoggerFactory.getLogger(Quickstart.class);
	
	public static void main(String [] args) {
		// The easiest way to create a Shiro SecurityManager with configured
        // realms, users, roles and permissions is to use the simple INI config.
        // We'll do that by using a factory that can ingest a .ini file and
        // return a SecurityManager instance:

        // Use the shiro.ini file at the root of the classpath
        // (file: and url: prefixes load from files and urls respectively):
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:quickstart/shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		
		// for this simple example quickstart, make the SecurityManager
        // accessible as a JVM singleton.  Most applications wouldn't do this
        // and instead rely on their container configuration or web.xml for
        // webapps.  That is outside the scope of this simple quickstart, so
        // we'll just do the bare minimum so you can continue to get a feel
        // for things.
		SecurityUtils.setSecurityManager(securityManager);
		
		// Now that a simple Shiro environment is set up, let's see what you can do:

        // get the currently executing user:
		Subject currentUser = SecurityUtils.getSubject();
		
		// Do some stuff with a Session (no need for a web or EJB container!!!)
		Session session = currentUser.getSession();
		session.setAttribute("someKey", "aValue");
		String value = (String) session.getAttribute("someKey");
		if(value.equals("aValue")) {
			logger.info("Retrieved the correct value ! [" + value + "]");
		}
		
		// let's login the current user so we can check against roles and permissions:
		if(!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken("guest", "guest");
			token.setRememberMe(true);
			
			try {
				currentUser.login(token);
			}catch(UnknownAccountException e) {
				logger.info("There is no user with username of " + token.getPrincipal());
				System.exit(0);
			}catch(IncorrectCredentialsException e) {
				logger.info("Password for account " + token.getPrincipal() + " was incorrect!");
				System.exit(0);
			}catch(LockedAccountException e) {
				logger.info("The account for username " + token.getPrincipal() + " is locked." + 
						" Please contract your administrator to unlock it.");
				System.exit(0);
			}catch(AuthenticationException e) {
				logger.info("unexpected exception ...");
				logger.error(e.getMessage(), e);
				System.exit(0);
			}
		}
		
		//say who they are:
        //print their identifying principal (in this case, a username):
		logger.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
		
		// test a role
		if(currentUser.hasRole("schwartz")) {
			logger.info("May the Schwartz be with you!");
		} else {
			logger.info("Hello, mere mortal.");
		}
		
		// test a typed permission (not instance-level)
		if(currentUser.isPermitted("lightsaber:weild")) {
			logger.info("You may use a lightsaber ring. Use it wisely.");
		} else {
			logger.info("Sorry, lightsaber rings are for schwartz masters only.");
		}
		
		// a (very powerful) Instance Level permission
		if(currentUser.isPermitted("winnebago:drive:eagle5")) {
			logger.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5" + 
					" Here are the keys - have fun!");
		} else {
			logger.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
		}
		
		// all done - log out
		currentUser.logout();
		
		System.exit(0);
	}
}
