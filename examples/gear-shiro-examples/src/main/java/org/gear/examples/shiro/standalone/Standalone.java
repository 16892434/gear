package org.gear.examples.shiro.standalone;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Standalone {

	public static void main(String [] args) {
		// init
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:quickstart/shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		
		//This is for Standalone (single-VM) applications that don't use a configuration container (Spring, JBoss, etc)
        //See its JavaDoc for our feelings on this.
		SecurityUtils.setSecurityManager(securityManager);
		
		//Now you are ready to access the Subject, as shown in the Quickstart:
		Subject currentUser = SecurityUtils.getSubject();
		
		//anything else you want to do with the Subject (see the Quickstart for examples).

        currentUser.logout();

        System.exit(0);
	}
}
