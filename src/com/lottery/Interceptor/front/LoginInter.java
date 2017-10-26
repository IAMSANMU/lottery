package com.lottery.Interceptor.front;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.Constant;

public class LoginInter implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		HttpSession session = inv.getController().getSession();
		if (session == null) {
			inv.getController().redirect("/login");
		} else {
			LotAdmin admin = (LotAdmin) session.getAttribute(Constant.SELLER_SESSION);
			if (admin != null) {
				inv.invoke();
			} else {
				inv.getController().redirect("/login");
			}
		}
	}

}
