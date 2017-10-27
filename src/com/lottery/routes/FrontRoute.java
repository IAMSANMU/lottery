package com.lottery.routes;

import com.jfinal.config.Routes;
import com.lottery.dc.DcController;
import com.lottery.user.LoginController;
import com.lottery.user.MemberController;

public class FrontRoute extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/front");
		add("/", DcController.class,"/dc");
		add("/login", LoginController.class,"/user");
		add("/member", MemberController.class,"/user");
	}

}
