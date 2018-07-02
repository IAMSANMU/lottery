package com.lottery.member;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.admin.log.LogService;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.common.model.UserWallet;
import com.lottery.common.utils.Constant;
import com.lottery.wallet.WalletService;

@Before(LoginInter.class)
public class MemberController extends BaseController {
	private WalletService walletService=Duang.duang(WalletService.class);
	private LogService logService=new LogService();
	
	public void index(){
		setAttr("column","member");
		//查询列表
		LotUser user=getSessionAttr(Constant.SELLER_SESSION);
		List<LotUserBuylog> list=logService.getList(user.getId());
		UserWallet wallet=walletService.getByUserId(user.getId());
		setAttr("wallet",wallet);
		setAttr("list",list);
		render("info.html");
	}
	
}
