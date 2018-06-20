package com.lottery.user;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.captcha.CaptchaRender;
import com.jfinal.kit.HashKit;
import com.lottery.admin.user.UserService;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.JsonResult;
import com.lottery.validator.LoginValidator;
import com.lottery.validator.RegeditValidator;

public class LoginController extends BaseController {
	private UserService userService=new UserService();
	
	
	public void index(){
		render("login.html");
	}
	public void logout(){
		getSession().invalidate();
		setCookie("JSESSIONID","",-1);
		render("login.html");
		
	}
	public void img() {
		CaptchaRender img = new CaptchaRender();
		render(img);
	}
	
	public void regedit(){
		render("regedit.html");
	}
	@Before(RegeditValidator.class)
	public void doRegedit(){
		LotUser user=getBean(LotUser.class,"");
		user.setCreateTime(new Date());
		user.setIsStop(false);
		user.setIsDel(false);
		user.setPwd(HashKit.md5(user.getPwd()));
		JsonResult<LotUser> json=new JsonResult<LotUser>();
		
		if(userService.checkAccount(user.getAccount())){
			json.setSuccess(false);
			json.setMessage("账号已存在");
		}else{
			userService.save(user);
			json.setSuccess(true);
		}
		
	
		renderText(json.toJsonString());
	}
	@Before(LoginValidator.class)
	public void login(){
		String account=getPara("account");
		String pwd=getPara("pwd");
		LotUser user=userService.login(account, pwd);
		if(user==null){
			setAttr("nameMsg","账号密码错误");
			setAttr("error",true);
			render("login.html");
		}else{
			setSessionAttr(Constant.SELLER_SESSION, user);
			setCookie("JSESSIONID", getSession().getId(),86400);
			redirect("/news");
		}
	}
}
