package com.wasselni.common.utils.format.xml;

import java.util.List;

public class FormatLine {

	private List<Variable> Variable;
	private List<UniqueKey> UniqueKey;

	public List<Variable> getVariable() {
		return Variable;
	}

	public void setVariable(List<Variable> variable) {
		Variable = variable;
	}

	public List<UniqueKey> getUniqueKey() {
		return UniqueKey;
	}

	public void setUniqueKey(List<UniqueKey> uniqueKey) {
		UniqueKey = uniqueKey;
	}

}