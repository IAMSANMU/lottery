package com.lottery.routes;

import com.jfinal.config.Routes;

public class AdminRoute  extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/Admin");
	}

}
