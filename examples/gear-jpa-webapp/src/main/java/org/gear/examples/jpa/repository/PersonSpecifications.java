package org.gear.examples.jpa.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gear.examples.jpa.model.Person;
import org.springframework.data.jpa.domain.Specification;

/**
 * A class which is used to create Specification objects which are used
 * to create JPA criteria queries for person information.
 * @author Petri Kainulainen
 */
public class PersonSpecifications {

    /**
     * Creates a specification used to find persons whose last name begins with
     * the given search term. This search is case insensitive.
     * @param searchTerm
     * @return
     */
	public static Specification<Person> lastNameIsLike(final String searchTerm) {
		return new Specification<Person>() {
			@Override
			public Predicate toPredicate(Root<Person> personRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String likePattern = getLikePattern(searchTerm);
				return cb.like(cb.lower(personRoot.<String>get(Person_.lastName)), likePattern);
			}
			
			private String getLikePattern(String searchTerm) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(searchTerm.toLowerCase());
				pattern.append("%");
				return pattern.toString();
			}
		};
	}
	
}
