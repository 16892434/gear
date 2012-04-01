package org.gear.examples.jpa.repository;

import org.gear.examples.jpa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
