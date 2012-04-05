package org.gear.examples.jpa;

import org.gear.examples.jpa.controller.AbstractControllerTest;
import org.gear.examples.jpa.controller.PersonControllerTest;
import org.gear.examples.jpa.model.PersonTest;
import org.gear.examples.jpa.repository.PersonSpecificationsTest;
import org.gear.examples.jpa.service.RepositoryPersonServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	AbstractControllerTest.class,
	PersonControllerTest.class,
	PersonTest.class,
	PersonSpecificationsTest.class,
	RepositoryPersonServiceTest.class
})
public class AllTest {

}
