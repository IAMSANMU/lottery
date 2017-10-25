package com.lottery.admin;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.JsonResult;

public class AdminController extends Controller  {
	AdminService adminService=new AdminService();
	//查询列表
	public void index(){
		
	}
	
	public void pwd() {
		LotAdmin admin=getSessionAttr(Constant.ADMIN_SESSION);
		if (admin == null) {
			renderText("账号不存在");
		} else {
			setAttr("admin", admin);
			render("pwd.html");
		}
	}

	public void pwdUpdate() {
		String pwd = getPara("pwd");
		String pwdSure = getPara("pwdSure");
		int id = getParaToInt("Id");

		JsonResult<LotAdmin> json = new JsonResult<LotAdmin>();

		LotAdmin admin = adminService.get(id);
		if (admin == null) {
			json.setMessage("账号不存在");
			json.setSuccess(false);
		} else if (StrKit.isBlank(pwd) || !pwd.equals(pwdSure)) {
			json.setMessage("密码不一致");
			json.setSuccess(false);
		} else {
			pwdSure = HashKit.md5(pwdSure);
			admin.setPwd(pwdSure);
			adminService.update(admin);
			json.setSuccess(true);
		}
		renderText(JSONObject.toJSONString(json));
	}
}
