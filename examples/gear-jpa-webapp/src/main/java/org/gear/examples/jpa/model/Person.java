package org.gear.examples.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An entity class which contains the information of a single person.
 * @author Petri Kainulainen
 */
@Entity
@NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(?1)")
@Table(name = "persons")
public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name="creation_time", nullable=false)
	private Date creationTime;
	@Column(name="first_name", nullable=false)
	private String firstName;
	@Column(name="last_name", nullable=false)
	private String lastName;
	@Column(name="modifition_time", nullable=false)
	private Date modifitionTime;
	@Version
	private long version = 0;
	
	public Long getId() {
		return id;
	}
	
    /**
     * This setter method should only be used by unit tests.
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreationTime() {
		return this.creationTime;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	@Transient
	public String getName() {
		StringBuilder name = new StringBuilder();
		
		name.append(firstName);
		name.append(" ");
		name.append(lastName);
		
		return name.toString();
	}
	
	public Date getModifitionTime() {
		return this.modifitionTime;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void update(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.modifitionTime = new Date();
	}
	
	@PrePersist
	public void prePersist() {
		Date now = new Date();
		this.creationTime = now;
		this.modifitionTime = now;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
    /**
     * Gets a builder which is used to create Person objects.
     * @param firstName The first name of the created user.
     * @param lastName  The last name of the created user.
     * @return  A new Builder instance.
     */
	public static Builder getBuilder(String firstName, String lastName) {
		return new Builder(firstName, lastName);
	}
	
    /**
     * A Builder class used to create new Person objects.
     */
	public static class Builder {
		Person built;
		
        /**
         * Creates a new Builder instance.
         * @param firstName The first name of the created Person object.
         * @param lastName  The last name of the created Person object.
         */
		public Builder(String firstName, String lastName) {
			built = new Person();
			built.firstName = firstName;
			built.lastName = lastName;
		}
		
        /**
         * Builds the new Person object.
         * @return  The created Person object.
         */
		public Person built() {
			return built;
		}
	}
}
