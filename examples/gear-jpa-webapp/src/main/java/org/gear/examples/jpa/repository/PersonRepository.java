package org.gear.examples.jpa.repository;

import java.util.List;

import org.gear.examples.jpa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Petri Kainulainen
 */
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    /**
     * Finds a person by using the last name as a search criteria.
     * @param lastName
     * @return  A list of persons whose last name is an exact match with the given last name.
     *          If no persons is found, this method returns an empty list.
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")
    public List<Person> find(@Param("lastName") String lastName);

    /**
     * Finds person by using the last name as a search criteria.
     * @param lastName
     * @return  A list of persons whose last name is an exact match with the given last name.
     *          If no persons is found, this method returns null.
     */
    public List<Person> findByName(String lastName);
    
    /**
     * Finds persons by using the last name as a search criteria.
     * @param lastName  
     * @return  A list of persons which last name is an exact match with the given last name.
     *          If no persons is found, this method returns an empty list.
     */
    public List<Person> findByLastName(String lastName);

}
