package org.gear.examples.jpa.dto;

import org.gear.examples.jpa.util.SearchType;

/**
 * A DTO class which is used as a form object in the search form.
 * @author Petri Kainulainen
 */
public class SearchDTO {

	private String searchTerm;
	private SearchType searchType;
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public SearchType getSearchType() {
		return searchType;
	}
	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}
}
