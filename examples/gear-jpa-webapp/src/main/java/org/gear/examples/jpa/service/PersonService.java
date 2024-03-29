package org.gear.examples.jpa.service;

import java.util.List;

import org.gear.examples.jpa.dto.PersonDTO;
import org.gear.examples.jpa.dto.SearchDTO;
import org.gear.examples.jpa.model.Person;
import org.gear.examples.jpa.util.PersonNotFoundException;

/**
 * Declares methods used to obtain and modify person information.
 * @author Petri Kainulainen
 */
public interface PersonService {

    /**
     * Creates a new person.
     * @param created   The information of the created person.
     * @return  The created person.
     */
    public Person create(PersonDTO created);

    /**
     * Deletes a person.
     * @param personId  The id of the deleted person.
     * @return  The deleted person.
     * @throws PersonNotFoundException  if no person is found with the given id.
     */
    public Person delete(Long personId) throws PersonNotFoundException;

    /**
     * Finds all persons.
     * @return  A list of persons.
     */
    public List<Person> findAll();

    /**
     * Finds person by id.
     * @param id    The id of the wanted person.
     * @return  The found person. If no person is found, this method returns null.
     */
    public Person findById(Long id);

    /**
     * Updates the information of a person.
     * @param updated   The information of the updated person.
     * @return  The updated person.
     * @throws PersonNotFoundException  if no person is found with given id.
     */
    public Person update(PersonDTO updated) throws PersonNotFoundException;
    
    /**
     * Searches persons by using the search criteria given as a parameter.
     * @param searchCriteria
     * @return  A list of persons matching with the search criteria. If no persons is found, this method
     *          returns an empty list.
     * @throws IllegalArgumentException if search type is not given.
     */
    public List<Person> search(SearchDTO searchCriteria);
    
    /**
     * Searches persons by using the given search term as a parameter.
     * @param searchTerm
     * @return  A list of persons whose last name begins with the given search term. If no persons is found, this method
     *          returns an empty list. This search is case insensitive.
     */
    public List<Person> search(String searchTerm);
}
