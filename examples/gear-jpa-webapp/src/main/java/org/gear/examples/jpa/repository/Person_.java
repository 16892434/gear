package org.gear.examples.jpa.repository;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.gear.examples.jpa.model.Person;

/**
 * A  meta model class used to create type safe queries from person
 * information.
 * @author Petri Kainulainen
 */
@StaticMetamodel(Person.class)
public class Person_ {
	public static volatile SingularAttribute<Person, String> lastName;
}
