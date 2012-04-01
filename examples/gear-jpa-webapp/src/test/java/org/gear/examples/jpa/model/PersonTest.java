package org.gear.examples.jpa.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {

    private static final String FIRST_NAME = "Foo";
    private static final String FIRST_NAME_UPDATED = "Foo1";
    private static final String LAST_NAME = "Bar";
    private static final String LAST_NAME_UPDATED = "Bar1";
    
    private Person built;
    
    @Before
    public void setup() {
    	built = Person.getBuilder(FIRST_NAME, LAST_NAME).built();
    }
    
    @After
    public void teardown() {
    	built = null;
    }
    
    @Test
    public void build() {
    	
    	assertEquals(FIRST_NAME, built.getFirstName());
    	assertEquals(LAST_NAME, built.getLastName());
    	assertEquals(0, built.getVersion());
    	
    	assertNull(built.getCreationTime());
    	assertNull(built.getModifitionTime());
    	assertNull(built.getId());
    }
    
    @Test
    public void getName() {
    	
    	String expectedName = contructName(FIRST_NAME, LAST_NAME);
    	assertEquals(expectedName, built.getName());
    }
    
    private String contructName(String firstName, String lastName) {
    	StringBuilder name = new StringBuilder();
    	
    	name.append(firstName);
    	name.append(" ");
    	name.append(lastName);
    	
    	return name.toString();
    }
    
    @Test
    public void prePersist() {
    	built.prePersist();
    	
    	Date creationTime = built.getCreationTime();
    	Date modifactionTime = built.getModifitionTime();
    	
    	assertNotNull(creationTime);
    	assertNotNull(modifactionTime);
    	assertEquals(creationTime, modifactionTime);
    }
    
    @Test
    public void preUpdate() {
    	built.prePersist();
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
    	
    	built.preUpdate();
    	
    	Date creationTime = built.getCreationTime();
    	Date modifactionTime = built.getModifitionTime();
    	
    	assertNotNull(creationTime);
    	assertNotNull(modifactionTime);
    	assertTrue(modifactionTime.after(creationTime));
    }
    
    @Test
    public void update() {
    	built.update(FIRST_NAME_UPDATED, LAST_NAME_UPDATED);
    	
    	assertEquals(FIRST_NAME_UPDATED, built.getFirstName());
    	assertEquals(LAST_NAME_UPDATED, built.getLastName());
    }
}
