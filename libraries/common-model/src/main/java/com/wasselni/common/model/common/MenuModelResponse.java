
package com.wasselni.common.model.common;

import java.util.List;

public class MenuModelResponse {

	private String name;
	private List<String> subMenu;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<String> subMenu) {
		this.subMenu = subMenu;
	}

}
