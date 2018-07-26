package com.lottery.admin.user;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.lottery.common.BaseController;
import com.lottery.common.model.UserWalletlog;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;
import com.lottery.wallet.WalletLogService;

public class WalletLogController extends BaseController {

	private WalletLogService logService=new WalletLogService();
	
	public void index() {
		String tab = getPara("tab");
		tab = tab == null ? "list" : tab;
		setAttr("tab", tab);
		render("index.html");
	}

	public void list() {
		ListSearchModel searchBean = getBean(ListSearchModel.class, "");
		List<SearchModel> searchList = getBeanList(SearchModel.class, "searchModel");
		searchBean.setSearchModels(searchList);
		
		Page<UserWalletlog> page = logService.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<UserWalletlog> json = new DataTablesJson<UserWalletlog>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());
	}
	
}
