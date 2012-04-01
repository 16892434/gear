package org.gear.examples.jpa.service;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gear.examples.jpa.dto.PersonDTO;
import org.gear.examples.jpa.dto.SearchDTO;
import org.gear.examples.jpa.model.Person;
import org.gear.examples.jpa.model.PersonTestUtil;
import org.gear.examples.jpa.repository.PersonRepository;
import org.gear.examples.jpa.service.impl.RepositoryPersonService;
import org.gear.examples.jpa.util.PersonNotFoundException;
import org.gear.examples.jpa.util.SearchType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RepositoryPersonServiceTest {

    private static final Long PERSON_ID = Long.valueOf(5);
    private static final String FIRST_NAME = "Foo";
    private static final String FIRST_NAME_UPDATED = "FooUpdated";
    private static final String LAST_NAME = "Bar";
    private static final String LAST_NAME_UPDATED = "BarUpdated";
    
    @InjectMocks
    private RepositoryPersonService personService;
    
    @Mock
    private PersonRepository personRepositoryMock;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	
    	personService = new RepositoryPersonService();
    	personService.setPersonRepository(personRepositoryMock);
    }
    
    @Test
    public void create() {
    	PersonDTO created = PersonTestUtil.createDTO(null, FIRST_NAME, LAST_NAME);
    	Person persisted = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
    	
    	when(personRepositoryMock.save(any(Person.class))).thenReturn(persisted);
    	
    	Person returned = personService.create(created);
    	
    	ArgumentCaptor<Person> personArgument = ArgumentCaptor.forClass(Person.class);
    	verify(personRepositoryMock).save(personArgument.capture());
    	verifyNoMoreInteractions(personRepositoryMock);
    	
    	assertPeron(created, personArgument.getValue());
    	assertEquals(persisted, returned);
    }

	private void assertPeron(PersonDTO expected, Person actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
	}
	
	@Test
	public void delete() {
		Person deleted = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
		when(personRepositoryMock.findOne(PERSON_ID)).thenReturn(deleted);
		
		Person returned = personService.delete(PERSON_ID);
		
		verify(personRepositoryMock).findOne(PERSON_ID);
		verify(personRepositoryMock).delete(deleted);
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(returned, deleted);
	}
	
	@Test(expected = PersonNotFoundException.class)
	public void deleteWhenPersonIsNotFound() throws PersonNotFoundException {
		when(personRepositoryMock.findOne(PERSON_ID)).thenReturn(null);
		
		personService.delete(PERSON_ID);
		
		verify(personRepositoryMock).findOne(PERSON_ID);
		verifyNoMoreInteractions(personRepositoryMock);
	}
	
	@Test
	public void findAll() {
		List<Person> persons = new ArrayList<Person>();
		when(personRepositoryMock.findAll()).thenReturn(persons);
		
		List<Person> returned = personService.findAll();
		
		verify(personRepositoryMock).findAll();
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(persons, returned);
	}
	
	@Test
	public void findById() {
		Person person = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
		when(personRepositoryMock.findOne(PERSON_ID)).thenReturn(person);
		
		Person returned = personService.findById(PERSON_ID);
		
		verify(personRepositoryMock).findOne(PERSON_ID);
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(person, returned);
	}
	
	@Test
	public void searchWhenSearchTypeIsMethodName() {
		SearchDTO searchCriteria = createSearchDTO(LAST_NAME, SearchType.METHOD_NAME);
		List<Person> expected = new ArrayList<Person>();
		when(personRepositoryMock.findByLastName(searchCriteria.getSearchTerm())).thenReturn(expected);
		
		List<Person> returned = personService.search(searchCriteria);
		
		verify(personRepositoryMock).findByLastName(searchCriteria.getSearchTerm());
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(expected, returned);
	}
	
	public SearchDTO createSearchDTO(String searchTerm, SearchType searchType) {
		SearchDTO searchCriteria = new SearchDTO();
		searchCriteria.setSearchTerm(searchTerm);
		searchCriteria.setSearchType(searchType);
		return searchCriteria;
	}
	
	@Test
	public void searchWhenSearchTypeIsNamedQuery() {
		SearchDTO searchCriteria = createSearchDTO(LAST_NAME, SearchType.NAMED_QUERY);
		List<Person> expected = new ArrayList<Person>();
		when(personRepositoryMock.findByName(searchCriteria.getSearchTerm())).thenReturn(expected);
		
		List<Person> returned = personService.search(searchCriteria);
		
		verify(personRepositoryMock).findByName(searchCriteria.getSearchTerm());
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(expected, returned);
	}
	
	@Test
	public void searchWhenSearchTypeIsQueryAnnotation() {
		SearchDTO searchCriteria = createSearchDTO(LAST_NAME, SearchType.QUERY_ANNOTATION);
		List<Person> expected = new ArrayList<Person>();
		when(personRepositoryMock.find(searchCriteria.getSearchTerm())).thenReturn(expected);
		
		List<Person> returned = personService.search(searchCriteria);
		
		verify(personRepositoryMock).find(searchCriteria.getSearchTerm());
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertEquals(expected, returned);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void searchWhenSearchTypeIsNull() {
		SearchDTO searchCriteria = createSearchDTO(LAST_NAME, null);
		
		personService.search(searchCriteria);
		
		verifyZeroInteractions(personRepositoryMock);
	}
	
	@Test
	public void update() throws PersonNotFoundException {
		PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME_UPDATED, LAST_NAME_UPDATED);
		Person person = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
		
		when(personRepositoryMock.findOne(PERSON_ID)).thenReturn(person);
		
		Person returned = personService.update(updated);
		
		verify(personRepositoryMock).findOne(PERSON_ID);
		verifyNoMoreInteractions(personRepositoryMock);
		
		assertPerson(updated, returned);
	}
	
	@Test(expected = PersonNotFoundException.class)
	public void updateWhenPersonIsNotFound() throws PersonNotFoundException {
		PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME_UPDATED, LAST_NAME_UPDATED);
		
		when(personRepositoryMock.findOne(updated.getId())).thenReturn(null);
		
		personService.update(updated);
		
		verify(personRepositoryMock).findOne(updated.getId());
		verifyNoMoreInteractions(personRepositoryMock);
	}
	
	private void assertPerson(PersonDTO expected, Person actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
	}
}
