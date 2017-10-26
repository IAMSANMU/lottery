package com.lottery.Interceptor.admin;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.Constant;

public class AdminLoginInter implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		HttpSession session = inv.getController().getSession();
		if (session == null) {
			inv.getController().redirect("/admin");
		} else {
			LotAdmin admin = (LotAdmin) session.getAttribute(Constant.ADMIN_SESSION);
			if (admin != null) {
				inv.invoke();
			} else {
				inv.getController().redirect("/admin");
			}
		}
	}

}
