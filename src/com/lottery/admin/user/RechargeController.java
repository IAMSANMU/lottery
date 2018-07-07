package com.lottery.admin.user;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.UserRecharge;
import com.lottery.common.utils.DataTablesJson;
import com.lottery.common.utils.JsonResult;
import com.lottery.search.ListSearchModel;
import com.lottery.search.SearchModel;
import com.lottery.wallet.WalletService;
import com.lottery.wallet.recharge.RechargeService;

@Before(AdminLoginInter.class)
public class RechargeController extends BaseController {
	UserService userService = Duang.duang(UserService.class);
	WalletService walletService = Duang.duang(WalletService.class);
	RechargeService rechargeService = new RechargeService();

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
		Page<UserRecharge> page = rechargeService.getByPage(searchBean.getPageIndex(), searchBean.getPageSize(),
				searchBean.getOrder(), searchBean.getOrderType(), searchBean.getSearchModels());
		DataTablesJson<UserRecharge> json = new DataTablesJson<UserRecharge>();
		json.setRecordsFiltered(page.getTotalRow());
		json.setRecordsTotal(page.getTotalRow());
		json.setData(page.getList());
		renderJson(json.toJsonString());
	}
	
	
	public void ok(){
		JsonResult<UserRecharge> json=new JsonResult<UserRecharge>();
		int id=getParaToInt();
		UserRecharge rechargeLog=(UserRecharge) rechargeService.get(id);
		if(rechargeLog==null){
			json.setSuccess(false);
			json.setMessage("记录不存在");
		}else{
			rechargeLog.setStatus(2);
			rechargeLog.setUpdateTime(new Date());
			rechargeService.update(rechargeLog);
			json.setSuccess(true);
		}
		renderJson(json.toJsonString());
	}
	
	
	public void delete() {
		String ids = getPara("ids");
		JsonResult<LotUser> json = new JsonResult<LotUser>();
		if (StrKit.isBlank(ids)) {
			json.setMessage("至少选择一条记录");
			json.setSuccess(false);
		} else {
			String[] idArr = ids.split(",");
			rechargeService.logicDelete(idArr);
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
			rechargeService.restore(idArr);
			json.setSuccess(true);
		}
		renderText(json.toJsonString());
	}
	
	
	
	
}
