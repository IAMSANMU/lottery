package com.lottery.common.utils;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class DataTablesJson<T> {
	private long recordsTotal;
	private long recordsFiltered;

	private List<T> data;

	public String ToJsonString() {
		return JSONObject.toJSONString(this);
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
