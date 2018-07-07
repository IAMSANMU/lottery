package com.lottery.Interceptor.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.lottery.common.model.LotAdmin;
import com.lottery.common.utils.Constant;

public class AdminLoginInter implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		Controller controller=inv.getController();
		HttpServletRequest request=controller.getRequest();
		String url=request.getRequestURI();
		controller.setAttr("requestURL", url);
		HttpSession session = controller.getSession();
		if (session == null) {
			controller.redirect("/admin");
		} else {
			LotAdmin admin = (LotAdmin) session.getAttribute(Constant.ADMIN_SESSION);
			if (admin != null) {
				inv.invoke();
			} else {
				controller.redirect("/admin");
			}
		}
	}

}
