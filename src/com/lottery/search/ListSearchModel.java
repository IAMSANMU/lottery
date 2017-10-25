package com.lottery.search;

import java.util.List;

public class ListSearchModel {

	private int draw;
	private int pageIndex;
	private int pageSize;
	private String order;
	private String orderType;
	private List<SearchModel> searchModels;

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public List<SearchModel> getSearchModels() {
		return searchModels;
	}

	public void setSearchModels(List<SearchModel> searchModels) {
		this.searchModels = searchModels;
	}

}
