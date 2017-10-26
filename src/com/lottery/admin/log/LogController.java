package com.lottery.admin.log;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;


@Before(AdminLoginInter.class)
public class LogController extends BaseController {
	private LogService logService=new LogService();
	
	public void index(){
		render("index.html");
	}
	public void list(){
		ListSearchModel searchBean = getBean(ListSearchModel.class, "");
		List<SearchModel> searchList = getBeanList(SearchModel.class, "searchModel");
		searchBean.setSearchModels(searchList);
		
		Page<LotUserBuylog> page = logService.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<LotUserBuylog> json = new DataTablesJson<LotUserBuylog>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());
	}
}
