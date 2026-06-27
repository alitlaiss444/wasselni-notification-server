
package com.wasselni.common.model.pagging;

import java.util.List;

public class GetsPaggingResponse<T> {

	private Integer totalCount;

	private List<T> list;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
