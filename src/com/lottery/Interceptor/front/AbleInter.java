package com.lottery.Interceptor.front;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.lottery.common.model.LotUser;
import com.lottery.common.utils.Constant;

public class AbleInter implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		HttpSession session = inv.getController().getSession();
		if (session != null) {
			LotUser admin = (LotUser) session.getAttribute(Constant.SELLER_SESSION);
			if (admin != null) {
				if (admin.getAbleLook()) {
					inv.getController().setAttr("sessionUser", admin);
					inv.invoke();
				} else {
					inv.getController().redirect("/member");
				}
			}
		}
	}

}
