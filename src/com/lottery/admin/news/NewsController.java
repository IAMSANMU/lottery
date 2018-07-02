package com.lottery.admin.news;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.admin.news.section.SectionService;
import com.lottery.admin.news.template.TemplateService;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.model.NewsContext;
import com.lottery.common.model.NewsSection;
import com.lottery.common.model.NewsTemplate;
import com.lottery.common.utils.BeanKit;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;

@Before(AdminLoginInter.class)
public class NewsController extends BaseController {
	private NewsService service = new NewsService();
	private SectionService sectionService = new SectionService();
	private TemplateService templateService = new TemplateService();

	public void index() {
		String tab = getPara("tab");
		tab = tab == null ? "list" : tab;
		setAttr("tab", tab);

		List<NewsSection> sectionList = sectionService.getAll();
		setAttr("sectionList", sectionList);
		render("index.html");
	}

	public void list() {
		ListSearchModel searchBean = getBean(ListSearchModel.class, "");
		List<SearchModel> searchList = getBeanList(SearchModel.class, "searchModel");
		searchBean.setSearchModels(searchList);
		Page<NewsContext> page = service.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<NewsContext> json = new DataTablesJson<NewsContext>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());

	}

	public void add() {
		LotAdmin admin = (LotAdmin) getSessionAttr(Constant.ADMIN_SESSION);

		NewsContext entity = new NewsContext();
		entity.setPushTime(new Date());
		entity.setAuthor(admin.getName());
		entity.setIsShow(true);
		entity.setType(0);

		List<NewsSection> sectionList = sectionService.getAll();
		// 获取模板列表
		List<NewsTemplate> templateList = templateService.getAll();

		setAttr("templateList", templateList);
		setAttr("sectionList", sectionList);
		setAttr("news", entity);

		render("edit.html");
	}

	public void save() {
		NewsContext model = getBean(NewsContext.class, "", true);

		model.setIsTop(model.getIsTop()==null?false:model.getIsTop());
		model.setIsShow(model.getIsShow()==null?false:model.getIsShow());
		JsonResult<NewsContext> json = new JsonResult<NewsContext>();

		if (StrKit.isBlank(model.getTitle())) {
			json.setMessage("标题不能为空");
			json.setSuccess(false);
		} else if (model.getSectionId() == null) {
			json.setMessage("栏目不能为空");
			json.setSuccess(false);
		}else if(!model.getIsFree()&&model.getMoney()==null) {
			json.setMessage("收费资讯,请填写金额");
			json.setSuccess(false);
		}
		else {
			model.setCreatetime(new Date());
			model.setIsDel(false);
			model.setStatus(2);

			service.save(model);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void edit() {
		int id = getParaToInt("id");
		NewsContext news = service.get(id);
		if (news == null) {
			renderText("文章不存在");
		} else {
			List<NewsSection> sectionList = sectionService.getAll();
			// 获取模板列表
			List<NewsTemplate> templateList = templateService.getAll();

			setAttr("templateList", templateList);
			setAttr("sectionList", sectionList);

			setAttr("news", news);
			render("edit.html");
		}

	}

	public void update() {
		NewsContext model = getBean(NewsContext.class, "", true);
		model.setIsTop(model.getIsTop()==null?false:model.getIsTop());
		model.setIsShow(model.getIsShow()==null?false:model.getIsShow());
		JsonResult<NewsContext> json = new JsonResult<NewsContext>();

		if (StrKit.isBlank(model.getTitle())) {
			json.setMessage("文章标题不能为空");
			json.setSuccess(false);
		} else {
			NewsContext news = service.get(model.getId());
			if (news == null) {
				json.setMessage("文章不存在");
				json.setSuccess(false);
			} else {
				try {
					String[] excluds = new String[] { "createTime", "id", "isDel", "status", "decodeContext","decodeFreeContext" };
					BeanKit.copyPropertiesExclude(model, news, excluds);
					service.update(news);
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
		JsonResult<NewsContext> json = new JsonResult<NewsContext>();
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
		JsonResult<NewsContext> json = new JsonResult<NewsContext>();
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
