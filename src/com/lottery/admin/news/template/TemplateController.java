package com.lottery.admin.news.template;

import java.util.Calendar;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.NewsTemplate;
import com.lottery.common.utils.BeanKit;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;

@Before(AdminLoginInter.class)
public class TemplateController extends BaseController {

	private TemplateService service = new TemplateService();

	public void index() {
		String tab = getPara("tab");
		tab = tab == null ? "list" : tab;
		setAttr("tab", tab);
		render("index.html");
	}

	public void List() {
		ListSearchModel searchBean = getBean(ListSearchModel.class, "");
		List<SearchModel> searchList = getBeanList(SearchModel.class, "searchModel");
		searchBean.setSearchModels(searchList);
		Page<NewsTemplate> page = service.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<NewsTemplate> json = new DataTablesJson<NewsTemplate>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());
	}
	
	
	public void add() {
		render("add.html");
	}

	public void save() {
		NewsTemplate model = getBean(NewsTemplate.class, "", true);
		
		
		JsonResult<NewsTemplate> json = new JsonResult<NewsTemplate>();
		if (StrKit.isBlank(model.getName())) {
			json.setMessage("名称不能为空");
			json.setSuccess(false);
		} else {
			Calendar cal=Calendar.getInstance();
			model.setCreateTime(cal.getTime());
			model.setIsDel(false);
			
			service.save(model);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void edit() {
		int id = getParaToInt("id");
		NewsTemplate template = service.get(id);
		if (template == null) {
			renderText("模板不存在");
		} else {
			setAttr("template", template);
			render("edit.html");
		}
	}

	public void update() {
		NewsTemplate model = getBean(NewsTemplate.class, "", true);
		JsonResult<NewsTemplate> json = new JsonResult<NewsTemplate>();
		
		if(StrKit.isBlank(model.getName())){
			
			json.setMessage("名称不能为空");
			json.setSuccess(false);
		}else{
			NewsTemplate template = service.get(model.getId());
			if (template == null) {
				json.setMessage("模板不存在");
				json.setSuccess(false);
			} else {
				try {
					String[] excluds = new String[] { "createTime", "id", "isDel","decodeContext"  };
					BeanKit.copyPropertiesExclude(model, template, excluds);
					service.update(template);
					json.setSuccess(true);
				} catch (Exception e) {
					e.printStackTrace();
					json.setSuccess(false);
					json.setMessage("系统错误");
				}
			}
		}
		renderText(json.toJsonString());
	}

	public void delete() {
		String ids = getPara("ids");
		JsonResult<NewsTemplate> json = new JsonResult<NewsTemplate>();
		if (StrKit.isBlank(ids)) {
			json.setMessage("至少选择一条记录");
			json.setSuccess(false);
		} else {
			String[] idArr = ids.split(",");
			service.logicDelete(idArr);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void restore() {
		String ids = getPara("ids");
		JsonResult<NewsTemplate> json = new JsonResult<NewsTemplate>();
		if (StrKit.isBlank(ids)) {
			json.setMessage("至少选择一条记录");
			json.setSuccess(false);
		} else {
			String[] idArr = ids.split(",");
			service.restore(idArr);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

}
