package com.lottery.admin;

import com.jfinal.aop.Before;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.lottery.Interceptor.admin.AdminLoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.JsonResult;

@Before(AdminLoginInter.class)
public class AdminController extends BaseController {
	AdminService adminService = new AdminService();

	// 查询列表
	public void index() {

	}

	public void pwd() {
		LotAdmin admin = loginAdmin();
		setAttr("admin", admin);
		render("pwd.html");
	}

	public void pwdUpdate() {
		String oldPwd = getPara("oldPwd");
		String pwd = getPara("pwd");
		String pwdSure = getPara("pwdSure");

		JsonResult<LotAdmin> json = new JsonResult<LotAdmin>();

		if (StrKit.isBlank(oldPwd)) {
			json.setMessage("旧密码不能为空");
			json.setSuccess(false);
		} else if (StrKit.isBlank(pwd) || !pwd.equals(pwdSure)) {
			json.setMessage("密码不一致");
			json.setSuccess(false);
		} else {
			
			LotAdmin loginAdmin=loginAdmin();
			LotAdmin admin = adminService.get(loginAdmin.getId());
			//验证旧密码
			oldPwd=HashKit.md5(oldPwd);
			if(!oldPwd.equals(admin.getPwd())){
				json.setMessage("旧密码错误");
				json.setSuccess(false);
			}else{
				pwdSure = HashKit.md5(pwdSure);
				admin.setPwd(pwdSure);
				adminService.update(admin);
				json.setSuccess(true);
			}
		}
		renderText(json.toJsonString());
	}
}
