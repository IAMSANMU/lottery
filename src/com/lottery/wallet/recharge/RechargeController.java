package com.lottery.wallet.recharge;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.UserRecharge;
import com.lottery.common.model.UserWallet;
import com.lottery.common.utils.Constant;
import com.lottery.wallet.WalletService;

@Before(LoginInter.class)
public class RechargeController extends BaseController {

	private RechargeService service=new RechargeService();
	private WalletService walletService=Duang.duang(WalletService.class);
	public void index(){
		setAttr("column","recharge_log");
		
		LotUser user=getSessionAttr(Constant.SELLER_SESSION);
		UserWallet wallet=walletService.getByUserId(user.getId());
		setAttr("wallet",wallet);
		
		int dateType=getParaToInt("dateType",1);
		setAttr("dateType",dateType);
		Date start,end;
		if(dateType==1){//默认查询最近一周
			end=new Date();
			long time=end.getTime()-7*24*3600*1000;
			start=new Date(time);
		}else{
			String startStr=getPara("start")+" 00:00:00";
			String endStr=getPara("end")+" 23:59:59";
			start=DateKit.toDate(startStr);
			end=DateKit.toDate(endStr);
		}
		setAttr("start",DateKit.toStr(start));
		setAttr("end",DateKit.toStr(end));
		
		
		int pageIndex=getParaToInt("pageIndex",1);
		int pageSize=10;
		
		//根据条件查询
		Page<UserRecharge>  logList=service.getLogList(pageIndex, pageSize,  start, end);
		setAttr("logList",logList);
		render("recharge_log.html");
		
	}
}
