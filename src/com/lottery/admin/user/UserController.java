package com.lottery.admin.user;

import java.util.Date;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.utils.BeanKit;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;


@Before(AdminLoginInter.class)
public class UserController extends BaseController {
	UserService userService = Duang.duang(UserService.class);

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
		Page<LotUser> page = userService.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<LotUser> json = new DataTablesJson<LotUser>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());

	}

	public void add() {
		render("add.html");
	}

	public void save() {
		LotUser model = getModel(LotUser.class, "", true);
		model.setIsDel(false);
		model.setIsStop(false);
		model.setCreateTime(new Date());
		String pwdSure = getPara("pwdSure");
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		if (!model.getPwd().equals(pwdSure)) {
			json.setMessage("密码不一致");
			json.setSuccess(false);
		} else {
			pwdSure = HashKit.md5(pwdSure);
			model.setPwd(pwdSure);
			userService.save(model);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void edit() {
		int id = getParaToInt("id");
		LotUser user = userService.get(id);
		if (user == null) {
			renderText("会员不存在");
		} else {
			setAttr("user", user);
			render("edit.html");
		}
	}

	public void update() {
		LotUser model = getModel(LotUser.class, "", true);
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		model.setIsStop(model.getIsStop() == null ? false : model.getIsStop());
		LotUser user = userService.get(model.getId());
		if (user == null) {
			json.setMessage("会员不存在");
			json.setSuccess(false);
		} else {
			try {
				String[] excluds = new String[] { "isDel", "pwd", "startTime", "endTime", "createTime" };
				BeanKit.copyPropertiesExclude(model, user, excluds);
				userService.update(user);
				json.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMessage("系统错误");
			}
		}
		renderText(json.toJsonString());
	}

	public void delete() {
		String ids = getPara("ids");
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		if (StrKit.isBlank(ids)) {
			json.setMessage("至少选择一条记录");
			json.setSuccess(false);
		} else {
			String[] idArr = ids.split(",");
			userService.logicDelete(idArr);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void restore() {
		String ids = getPara("ids");
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		if (StrKit.isBlank(ids)) {
			json.setMessage("至少选择一条记录");
			json.setSuccess(false);
		} else {
			String[] idArr = ids.split(",");
			userService.restore(idArr);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void pwd() {
		int id = getParaToInt("id");
		LotUser user = userService.get(id);
		if (user == null) {
			renderText("会员不存在");
		} else {
			setAttr("user", user);
			render("pwd.html");
		}
	}

	public void buy() {
		int id = getParaToInt("id");
		LotUser user = userService.get(id);
		if (user == null) {
			renderText("会员不存在");
		} else {
			setAttr("user", user);
			render("buy.html");
		}

	}

	public void doBuy() {
		Date startTime = getParaToDate("startTime");
		Date endTime = getParaToDate("endTime");
		int id = getParaToInt("id");
		String remark = getPara("remark");
		LotUser user = userService.get(id);
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		if (startTime.after(endTime)) {
			json.setMessage("日期有误");
			json.setSuccess(false);
		} else if (user == null) {
			json.setMessage("会员不存在");
			json.setSuccess(false);
		} else {
			user.setStartTime(startTime);
			user.setEndTime(endTime);
			userService.buy(user, remark);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

	public void pwdUpdate() {
		String pwd = getPara("pwd");
		String pwdSure = getPara("pwdSure");
		int id = getParaToInt("id");

		JsonResult<LotUser> json = new JsonResult<LotUser>();

		LotUser user = userService.get(id);
		if (user == null) {
			json.setMessage("会员不存在");
			json.setSuccess(false);
		} else if (StrKit.isBlank(pwd) || !pwd.equals(pwdSure)) {
			json.setMessage("密码不一致");
			json.setSuccess(false);
		} else {
			pwdSure = HashKit.md5(pwdSure);
			user.setPwd(pwdSure);
			userService.update(user);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}

}
