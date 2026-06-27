
package com.wasselni.common.model.common;

public class KeyValueModel<X, Y, Z> {

	private X key;
	private Y value;
	private Z extraValue;

	public X getKey() {
		return key;
	}

	public void setKey(X key) {
		this.key = key;
	}

	public Y getValue() {
		return value;
	}

	public void setValue(Y value) {
		this.value = value;
	}

	public Z getExtraValue() {
		return extraValue;
	}

	public void setExtraValue(Z extraValue) {
		this.extraValue = extraValue;
	}

}
