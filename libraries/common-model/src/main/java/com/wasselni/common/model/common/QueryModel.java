/**
 * 
 */
package com.wasselni.common.model.common;

public class QueryModel {

	private String sql;
	private Object[] params;
	private int[] types;

	public QueryModel(String sql, Object[] params, int[] types) {
		super();
		this.sql = sql;
		this.params = params;
		this.types = types;
	}

	public QueryModel(String sql, Object[] params) {
		super();
		this.sql = sql;
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

}
