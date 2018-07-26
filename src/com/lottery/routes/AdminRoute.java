package com.lottery.routes;

import com.jfinal.config.Routes;
import com.lottery.admin.AdminController;
import com.lottery.admin.LoginController;
import com.lottery.admin.log.LogController;
import com.lottery.admin.news.NewsController;
import com.lottery.admin.news.section.SectionController;
import com.lottery.admin.news.template.TemplateController;
import com.lottery.admin.sys.SysErrorController;
import com.lottery.admin.upload.UploadController;
import com.lottery.admin.user.RechargeController;
import com.lottery.admin.user.UserController;
import com.lottery.admin.user.WalletLogController;

public class AdminRoute  extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/admin");
		add("/admin", LoginController.class,"/");
		add("/admin/admin", AdminController.class,"/admin");
		add("/admin/user", UserController.class,"/user");
		add("/admin/log", LogController.class,"/log");
		add("/admin/section", SectionController.class,"/section");
		add("/admin/template", TemplateController.class,"/template");
		add("/admin/news", NewsController.class,"/news");
		add("/admin/upload", UploadController.class,"/upload");
		add("/admin/walletLog", WalletLogController.class,"/walletLog");
		add("/admin/recharge", RechargeController.class,"/recharge");
		add("/admin/syserror", SysErrorController.class,"/syserror");
	}

}
