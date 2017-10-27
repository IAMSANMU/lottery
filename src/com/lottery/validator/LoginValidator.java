package com.lottery.validator;

import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator {

	
	protected void validate(Controller c) {
		validateRequired("account", "nameMsg", "请您输入用户名!");
		validateRequired("pwd", "pwdMsg", "请您输入密码!");
		validateRequired("randomCode", "codeMsg", "请您输入验证码!");
		
		String inputRandomCode = c.getPara("randomCode");
		if (CaptchaRender.validate(c, inputRandomCode) == false) {
			addError("codeMsg", "验证码不正确!");
		}
	}
	protected void handleError(Controller c) {
		c.keepPara("account");
		c.render("login.html");
	}
	

}
