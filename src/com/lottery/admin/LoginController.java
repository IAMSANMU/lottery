package com.lottery.admin;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.JsonResult;

public class LoginController extends Controller {
	AdminService adminService=new AdminService();
	public void index(){
		
		render("login.html");
	}
	
	public void login(){
		String account=getPara("name");
		String md5pwd=getPara("password");
		LotAdmin lotAdmin= adminService.login(account, md5pwd);
		JsonResult json=new JsonResult();
		if(lotAdmin==null){
			json.setSuccess(false);
			json.setMessage("账号或者密码错误");
		}else{
			if(lotAdmin.getIsDel()){
				json.setSuccess(false);
				json.setMessage("账号已停用");
			}else{
				json.setSuccess(true);
				getSession().setAttribute(Constant.ADMIN_SESSION, lotAdmin);
			}
		}
		
		renderText(JSONObject.toJSONString(json));
	}
	public void logout(){
		removeSessionAttr(Constant.ADMIN_SESSION);
		render("login.html");
	}
}
