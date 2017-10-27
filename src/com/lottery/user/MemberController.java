package com.lottery.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.admin.log.LogService;
import com.lottery.admin.user.UserService;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.common.utils.Constant;

@Before(LoginInter.class)
public class MemberController extends BaseController {
	private UserService userService=new UserService();
	private LogService logService=new LogService();
	
	public void index(){
		//查询列表
		LotUser user=getSessionAttr(Constant.SELLER_SESSION);
		List<LotUserBuylog> list=logService.getList(user.getId());
		setAttr("list",list);
		render("info.html");
	}
	
}
