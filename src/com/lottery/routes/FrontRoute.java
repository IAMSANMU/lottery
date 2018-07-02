package com.lottery.routes;

import com.jfinal.config.Routes;
import com.lottery.dc.DcController;
import com.lottery.member.MemberController;
import com.lottery.member.wallet.WalletController;
import com.lottery.news.NewsController;
import com.lottery.user.LoginController;

public class FrontRoute extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/front");
		add("/", DcController.class,"/dc");
		add("/login", LoginController.class,"/user");
		add("/member", MemberController.class,"/user");
		add("/news",NewsController.class,"/news");
		add("/wallet",WalletController.class,"/wallet");
	}

}
