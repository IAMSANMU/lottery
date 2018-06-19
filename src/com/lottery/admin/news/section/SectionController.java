package com.lottery.admin.news.section;

import java.util.Calendar;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.NewsSection;
import com.lottery.common.utils.BeanKit;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;

@Before(AdminLoginInter.class)
public class SectionController extends BaseController {

	private SectionService service = new SectionService();

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
		Page<NewsSection> page = service.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<NewsSection> json = new DataTablesJson<NewsSection>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());

	}

	public void add() {
		setAttr("section", new NewsSection());
		render("edit.html");
	}

	public void save() {
		NewsSection model = getModel(NewsSection.class, "", true);

		JsonResult<NewsSection> json = new JsonResult<NewsSection>();
		if (StrKit.isBlank(model.getName())) {
			json.setMessage("名称不能为空");
			json.setSuccess(false);
		} else {
			Calendar cal = Calendar.getInstance();
			model.setCreateTime(cal.getTime());
			model.setIsDel(false);

			service.save(model);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void edit() {
		int id = getParaToInt("id");
		NewsSection section = service.get(id);
		if (section == null) {
			renderText("类别不存在");
		} else {
			setAttr("section", section);
			render("edit.html");
		}
		
	}

	public void update() {
		NewsSection model = getModel(NewsSection.class, "", true);

		JsonResult<NewsSection> json = new JsonResult<NewsSection>();

		if (StrKit.isBlank(model.getName())) {
			json.setMessage("名称不能为空");
			json.setSuccess(false);
		} else {
			NewsSection section = service.get(model.getId());
			if (section == null) {
				json.setMessage("类别不存在");
				json.setSuccess(false);
			} else {
				try {
					String[] excluds = new String[] { "createTime", "id", "isDel" };
					BeanKit.copyPropertiesExclude(model, section, excluds);
					service.update(section);
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
		JsonResult<NewsSection> json = new JsonResult<NewsSection>();
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
		JsonResult<NewsSection> json = new JsonResult<NewsSection>();
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
