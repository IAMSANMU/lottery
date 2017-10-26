package com.lottery.routes;

import com.jfinal.config.Routes;
import com.lottery.admin.AdminController;
import com.lottery.admin.LoginController;
import com.lottery.admin.log.LogController;
import com.lottery.admin.user.UserController;

public class AdminRoute  extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/Admin");
		add("/admin", LoginController.class,"/");
		add("/admin/admin", AdminController.class,"/admin");
		add("/admin/user", UserController.class,"/user");
		add("/admin/log", LogController.class,"/log");
	}

}
