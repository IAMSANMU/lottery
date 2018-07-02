package com.lottery.Interceptor.front;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.lottery.common.model.LotUser;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.JsonResult;

public class LoginInter implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller=inv.getController();
		HttpSession session = controller.getSession();
			
		if (session == null) {
			error(controller);
			
		} else {
			LotUser admin = (LotUser) session.getAttribute(Constant.SELLER_SESSION);
			if (admin != null ) {
				controller.setAttr("sessionUser", admin);
				inv.invoke();
			} else {
				error(controller);
			}
		}
	}
	private void error(Controller controller){
		boolean isPost=controller.getRequest().getMethod().equals("POST");
		if(isPost){
			JsonResult json=new JsonResult();
			json.setMessage("你未登录");
			json.setSuccess(false);
			controller.renderJson(json.toJsonString());
		}else{
			controller.redirect("/login");
		}
		
	}

}
