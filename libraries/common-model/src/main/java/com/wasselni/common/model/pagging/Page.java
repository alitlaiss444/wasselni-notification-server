package com.wasselni.common.model.pagging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<E> implements Serializable {

	private static final long serialVersionUID = 5260366255403526787L;

	private int pageNumber;
	private int pagesAvailable;
	private int totalCount;
	private List<E> pageItems = new ArrayList<E>();

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPagesAvailable(int pagesAvailable) {
		this.pagesAvailable = pagesAvailable;
	}

	public void setPageItems(List<E> pageItems) {
		this.pageItems = pageItems;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPagesAvailable() {
		return pagesAvailable;
	}

	public List<E> getPageItems() {
		return pageItems;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}