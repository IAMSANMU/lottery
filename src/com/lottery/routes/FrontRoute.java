package com.lottery.routes;

import com.jfinal.config.Routes;
import com.lottery.dc.DcController;

public class FrontRoute extends Routes {

	@Override
	public void config() {
		setBaseViewPath("/front");
		add("/", DcController.class,"/dc");
	}

}
