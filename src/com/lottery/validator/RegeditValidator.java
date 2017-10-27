package com.lottery.validator;

import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.lottery.common.utils.JsonResult;

public class RegeditValidator extends Validator {
	private final String Msg="message";
	
	protected void validate(Controller c) {
		validateRequired("account",Msg, "请您输入用户名!");
		validateRequired("pwd", Msg, "请您输入密码!");
		validateRequired("pwdSure", Msg, "请您输入验证码!");
		validateEqualField("pwdSure", "pwd",Msg, "密码不一致!" );
		
		String inputRandomCode = c.getPara("randomCode");
		if (CaptchaRender.validate(c, inputRandomCode) == false) {
			addError(Msg, "验证码不正确!");
		}
	}
	protected void handleError(Controller c) {
		c.keepPara("account");
		JsonResult json=new JsonResult();
		json.setSuccess(false);
		json.setMessage(c.getAttrForStr(Msg));
		c.renderText(json.toJsonString());
	}
	

}
