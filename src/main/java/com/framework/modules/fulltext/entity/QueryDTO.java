package com.framework.modules.fulltext.entity;

public class QueryDTO {
	
	private String searchText;
	
	private int size=10;
	
	private int form;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getForm() {
		return form;
	}
	
	public void setForm(int form) {
		this.form = form;
	}
}
