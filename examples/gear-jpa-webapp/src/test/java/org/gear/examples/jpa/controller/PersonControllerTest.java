package org.gear.examples.jpa.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
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
import org.gear.examples.jpa.service.PersonService;
import org.gear.examples.jpa.util.PersonNotFoundException;
import org.gear.examples.jpa.util.SearchType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class PersonControllerTest extends AbstractTestController {

    private static final String FIELD_NAME_FIRST_NAME = "firstName";
    private static final String FIELD_NAME_LAST_NAME = "lastName";
    
    private static final Long PERSON_ID = Long.valueOf(5);
    private static final String FIRST_NAME = "Foo";
    private static final String FIRST_NAME_UPDATED = "FooUpdated";
    private static final String LAST_NAME = "Bar";
    private static final String LAST_NAME_UPDATED = "BarUpdated";
    
    @InjectMocks
    private PersonController controller;
    @Mock
    private PersonService personServiceMock;
    
    @Override
    public void setUpTest() {
    	MockitoAnnotations.initMocks(this);
    	
    	controller = new PersonController();
    	controller.setMessageSource(getMessageSourceMock());
    	controller.setPersonService(personServiceMock);
    }
    
    @Test
    public void delete() throws PersonNotFoundException {
    	Person deleted = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
    	when(personServiceMock.delete(PERSON_ID)).thenReturn(deleted);
    	
    	initMessageSourceForFeedbackMessage(PersonController.FEEDBACK_MESSAGE_KEY_PERSON_DELETED);
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	String view = controller.delete(PERSON_ID, attributes);
    	
    	verify(personServiceMock).delete(PERSON_ID);
    	verifyNoMoreInteractions(personServiceMock);
    	assertFeedbackMessage(attributes, PersonController.FEEDBACK_MESSAGE_KEY_PERSON_DELETED);
    	
    	String expectedView = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedView, view);
    }
    
    @Test
    public void deleteWhenPersonIsNotFound() throws PersonNotFoundException {
    	when(personServiceMock.delete(PERSON_ID)).thenThrow(new PersonNotFoundException());
    	
    	initMessageSourceForErrorMessage(PersonController.ERROR_MESSAGE_KEY_DELETED_PERSON_WAS_NOT_FOUND);
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	String view = controller.delete(PERSON_ID, attributes);
    	
    	verify(personServiceMock).delete(PERSON_ID);
    	verifyNoMoreInteractions(personServiceMock);
    	assertErrorMessage(attributes, PersonController.ERROR_MESSAGE_KEY_DELETED_PERSON_WAS_NOT_FOUND);
    	
    	String expectedView = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedView, view);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void search() {
    	SearchDTO searchCriteria = createSearchCriteria(LAST_NAME, SearchType.METHOD_NAME);
    	List<Person> expected = new ArrayList<Person>();
    	when(personServiceMock.search(searchCriteria.getSearchTerm())).thenReturn(expected);
    	
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	String view = controller.search(searchCriteria, model);
    	
    	verify(personServiceMock).search(searchCriteria.getSearchTerm());
    	verifyNoMoreInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_SEARCH_RESULT_VIEW, view);
		List<Person> actual = (List<Person>) model.asMap().get(PersonController.MODEL_ATTRIBUTE_PERSONS);
		assertEquals(expected, actual);
    }
    
    private SearchDTO createSearchCriteria(String searchTerm, SearchType searchType) {
    	SearchDTO searchCriteria = new SearchDTO();
    	
    	searchCriteria.setSearchTerm(searchTerm);
    	searchCriteria.setSearchType(searchType);
    	
    	return searchCriteria;
    }
    
    @Test
    public void showPersonCreateForm() {
    	Model model = new BindingAwareModelMap();
    	
    	String view = controller.showCreatePersonForm(model);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_ADD_FORM_VIEW, view);
    	
    	PersonDTO added = (PersonDTO) model.asMap().get(PersonController.MODEL_ATTRIBUTE_PERSON);
    	assertNotNull(added);
    	
    	assertNull(added.getId());
    	assertNull(added.getFirstName());
    	assertNull(added.getLastName());
    }
    
    @Test
    public void submitCreatePersonForm() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/create", "POST");
    	
    	PersonDTO created = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME, LAST_NAME);
    	Person model = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
    	when(personServiceMock.create(created)).thenReturn(model);
    	
    	initMessageSourceForFeedbackMessage(PersonController.FEEDBACK_MESSAGE_KEY_PERSON_CREATED);
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	BindingResult result = bindAndValidate(mockRequest, created);
    	
    	String view = controller.submitCreatePersonForm(created, result, attributes);
    	
    	verify(personServiceMock).create(created);
    	verifyNoMoreInteractions(personServiceMock);
    	
    	String expectedViewPath = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedViewPath, view);
    	
    	assertFeedbackMessage(attributes, PersonController.FEEDBACK_MESSAGE_KEY_PERSON_CREATED);
    	
    	verify(personServiceMock).create(created);
    	verifyNoMoreInteractions(personServiceMock);
    }
    
    @Test
    public void submitEmptyCreatePersonForm() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/create", "POST");
    	
    	PersonDTO created = new PersonDTO();
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	BindingResult result = bindAndValidate(mockRequest, created);
    	
    	String view = controller.submitCreatePersonForm(created, result, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_ADD_FORM_VIEW, view);
    	assertFieldErrors(result, FIELD_NAME_FIRST_NAME, FIELD_NAME_LAST_NAME);
    }
    
    @Test
    public void submitCreatePersonFormWithEmptyFirstName() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/create", "POST");
    	
    	PersonDTO created = PersonTestUtil.createDTO(null, null, LAST_NAME);
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	BindingResult result = bindAndValidate(mockRequest, created);
    	
    	String view = controller.submitCreatePersonForm(created, result, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_ADD_FORM_VIEW, view);
    	assertFieldErrors(result, FIELD_NAME_FIRST_NAME);
    }
    
    @Test
    public void submitCreatePersonFormWithEmptyLastName() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/create", "POST");
    	
    	PersonDTO created = PersonTestUtil.createDTO(null, FIRST_NAME, null);
    	
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	BindingResult result = bindAndValidate(mockRequest, created);
    	
    	String view = controller.submitCreatePersonForm(created, result, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_ADD_FORM_VIEW, view);
    	assertFieldErrors(result, FIELD_NAME_LAST_NAME);
    }
    
    @Test
    public void showEditPersonForm() {
    	Person person = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
    	when(personServiceMock.findById(PERSON_ID)).thenReturn(person);
    	
    	Model model = new BindingAwareModelMap();
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.showEditPersonForm(PERSON_ID, model, attributes);
    	
    	verify(personServiceMock).findById(PERSON_ID);
    	verifyNoMoreInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_EDIT_FORM_VIEW, view);
    	
    	PersonDTO formObject = (PersonDTO) model.asMap().get(PersonController.MODEL_ATTRIBUTE_PERSON);
    	
    	assertNotNull(formObject);
    	assertEquals(person.getId(), formObject.getId());
    	assertEquals(person.getFirstName(), formObject.getFirstName());
    	assertEquals(person.getLastName(), formObject.getLastName());
    }
    
    @Test
    public void showEditPersonFormWhenPersonNotFound() {
    	when(personServiceMock.findById(PERSON_ID)).thenReturn(null);
    	
    	initMessageSourceForErrorMessage(PersonController.ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
    	
    	Model model = new BindingAwareModelMap();
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.showEditPersonForm(PERSON_ID, model, attributes);
    	
    	verify(personServiceMock).findById(PERSON_ID);
    	verifyNoMoreInteractions(personServiceMock);
    	
    	String expectedView = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedView, view);
    	
    	assertErrorMessage(attributes, PersonController.ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
    }
    
    @Test
    public void submitEditPersonForm() throws PersonNotFoundException {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/edit", "POST");
    	PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME, LAST_NAME);
    	Person person = PersonTestUtil.createModelObject(PERSON_ID, FIRST_NAME, LAST_NAME);
    	
    	when(personServiceMock.update(updated)).thenReturn(person);
    	
    	initMessageSourceForFeedbackMessage(PersonController.FEEDBACK_MESSAGE_KEY_PERSON_EDITED);
    	
    	BindingResult bindingResult = bindAndValidate(mockRequest, updated);
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.submitEditPersonForm(updated, bindingResult, attributes);
    	
    	verify(personServiceMock).update(updated);
    	verifyNoMoreInteractions(personServiceMock);
    	
    	String expectedView = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedView, view);
    	
    	assertFeedbackMessage(attributes, PersonController.FEEDBACK_MESSAGE_KEY_PERSON_EDITED);
    	
    	assertEquals(updated.getFirstName(), person.getFirstName());
    	assertEquals(updated.getLastName(), person.getLastName());
    }
    
    @Test
    public void submitEditPersonFormWhenPersonIsNotFound() throws PersonNotFoundException {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/edit", "POST");
    	PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME_UPDATED, LAST_NAME_UPDATED);
    	
    	when(personServiceMock.update(updated)).thenThrow(new PersonNotFoundException());
    	initMessageSourceForErrorMessage(PersonController.ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
    	
    	BindingResult bindingResult = bindAndValidate(mockRequest, updated);
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.submitEditPersonForm(updated, bindingResult, attributes);
    	
    	verify(personServiceMock).update(updated);
    	verifyNoMoreInteractions(personServiceMock);
    	
    	String expectedView = createExpectedRedirectViewPath(PersonController.REQUEST_MAPPING_LIST);
    	assertEquals(expectedView, view);
    	
    	assertErrorMessage(attributes, PersonController.ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
    }
    
    @Test
    public void submitEmptyEditPersonForm() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/edit", "POST");
    	PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, null, null);
    	
    	BindingResult bindingResult = bindAndValidate(mockRequest, updated);
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.submitEditPersonForm(updated, bindingResult, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_EDIT_FORM_VIEW, view);
    	assertFieldErrors(bindingResult, FIELD_NAME_FIRST_NAME, FIELD_NAME_LAST_NAME);
    }
    
    @Test
    public void submitEditPersonFormWhenFirstNameIsEmpty() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/edit", "POST");
    	PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, null, LAST_NAME);
    	
    	BindingResult bindingResult = bindAndValidate(mockRequest, updated);
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.submitEditPersonForm(updated, bindingResult, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_EDIT_FORM_VIEW, view);
    	assertFieldErrors(bindingResult, FIELD_NAME_FIRST_NAME);
    }
    
    @Test
    public void submitEditPersonFormWhenLastNameIsEmpty() {
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("/person/edit", "POST");
    	PersonDTO updated = PersonTestUtil.createDTO(PERSON_ID, FIRST_NAME, null);
    	
    	BindingResult bindingResult = bindAndValidate(mockRequest, updated);
    	RedirectAttributes attributes = new RedirectAttributesModelMap();
    	
    	String view = controller.submitEditPersonForm(updated, bindingResult, attributes);
    	
    	verifyZeroInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_EDIT_FORM_VIEW, view);
    	assertFieldErrors(bindingResult, FIELD_NAME_LAST_NAME);
    }
    
    @Test
    public void showList() {
    	List<Person> persons = new ArrayList<Person>();
    	when(personServiceMock.findAll()).thenReturn(persons);
    	
    	Model model = new BindingAwareModelMap();
    	String view = controller.showList(model);
    	
    	verify(personServiceMock).findAll();
    	verifyNoMoreInteractions(personServiceMock);
    	
    	assertEquals(PersonController.PERSON_LIST_VIEW, view);
    	assertEquals(persons, model.asMap().get(PersonController.MODEL_ATTRIBUTE_PERSONS));
    	
    	SearchDTO searchCriteria = (SearchDTO) model.asMap().get(PersonController.MODEL_ATTRIBUTE_SEARCHCRITERIA);
    	assertNotNull(searchCriteria);
    	assertNull(searchCriteria.getSearchTerm());
    	assertNull(searchCriteria.getSearchType());
    }
}
