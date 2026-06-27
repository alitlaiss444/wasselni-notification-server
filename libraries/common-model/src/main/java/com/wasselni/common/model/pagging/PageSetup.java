package com.wasselni.common.model.pagging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wasselni.common.model.common.DefaultValidationMessage;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageSetup {

	@NotNull(message = DefaultValidationMessage.PAGE_SIZE + DefaultValidationMessage.MANDATORY)
	@Min(value = 1, message = DefaultValidationMessage.PAGE_SIZE + DefaultValidationMessage.MANDATORY)
	@Max(value = 100, message = DefaultValidationMessage.PAGE_SIZE + DefaultValidationMessage.MANDATORY)
	private Integer pageSize;

	@NotNull(message = DefaultValidationMessage.PAGE_NUMBER + DefaultValidationMessage.MANDATORY)
	@Min(value = 1, message = DefaultValidationMessage.PAGE_NUMBER + DefaultValidationMessage.MANDATORY)
	@Max(value = 100000, message = DefaultValidationMessage.PAGE_SIZE + DefaultValidationMessage.MANDATORY)
	private Integer pageNumber;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

}
