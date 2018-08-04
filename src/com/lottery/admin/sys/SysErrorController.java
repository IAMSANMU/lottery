package com.lottery.admin.sys;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.SysError;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;

@Before(AdminLoginInter.class)
public class SysErrorController extends BaseController {

	private SysErrorService service=new SysErrorService();
	
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
		Page<SysError> page = service.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<SysError> json = new DataTablesJson<SysError>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());
	}
	
	public void handle(){
		JsonResult<SysError> json =new JsonResult<SysError>();
		try {
			int id=getParaToInt();
			SysError entity=service.get(id);
			entity.setIsHandle(true);
			service.update(entity);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("系统错误:"+e.getMessage());
		}
		renderJson(json.toJsonString());
		
	}
}
