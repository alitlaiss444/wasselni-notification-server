package com.wasselni.common.model.audit;

public class ClassAttribute {

	private String attribute;
	private Object value;

	public ClassAttribute(String attribute, Object value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
