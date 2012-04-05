package org.gear.examples.jpa.repository;

import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gear.examples.jpa.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("rawtypes")
public class PersonSpecificationsTest {

	private static final String SEARCH_TERM = "Foo";
    private static final String SEARCH_TERM_LIKE_PATTERN = "foo%";
    
    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    
    @Mock
    private CriteriaQuery criteriaQueryMock;
    
    @Mock
    private Root<Person> personRootMock;
    @Mock
    private Path lastNamePathMock;
    
	@Mock
    private Expression lastNameToLowerExpressionMock;
    
    @Mock
    private Predicate lastNameIsLikePredicateMock;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void lastNameIsLike() {
    	when(personRootMock.get(Person_.lastName)).thenReturn(lastNamePathMock);
    	when(criteriaBuilderMock.lower(lastNamePathMock)).thenReturn(lastNameToLowerExpressionMock);
    	when(criteriaBuilderMock.like(lastNameToLowerExpressionMock, SEARCH_TERM_LIKE_PATTERN)).thenReturn(lastNameIsLikePredicateMock);
    	
    	Specification<Person> actual = PersonSpecifications.lastNameIsLike(SEARCH_TERM);
    	Predicate acutalPredicate = actual.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
    	
    	verify(personRootMock).get(Person_.lastName);
    	verifyNoMoreInteractions(personRootMock);
    	
    	verify(criteriaBuilderMock).lower(lastNamePathMock);
    	verify(criteriaBuilderMock).like(lastNameToLowerExpressionMock, SEARCH_TERM_LIKE_PATTERN);
    	verifyNoMoreInteractions(criteriaBuilderMock);
    	
    	verifyZeroInteractions(criteriaQueryMock, lastNamePathMock, lastNameIsLikePredicateMock);
    	
    	assertEquals(lastNameIsLikePredicateMock, acutalPredicate);
    }
}
