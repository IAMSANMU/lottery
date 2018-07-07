package com.lottery.wallet;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.common.BaseController;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.UserWallet;
import com.lottery.common.model.UserWalletlog;
import com.lottery.common.utils.Constant;
import com.lottery.common.utils.JsonResult;


@Before(LoginInter.class)
public class WalletController extends BaseController  {
	private WalletService walletService=Duang.duang(WalletService.class);
	private WalletLogService walletLogService=new WalletLogService();
	
	public void index(){
		setAttr("column","log");
		//获取用户钱包
		LotUser user=getSessionAttr(Constant.SELLER_SESSION);
		UserWallet wallet=walletService.getByUserId(user.getId());
		setAttr("wallet",wallet);
		
		int transType=getParaToInt("transType", -1);
		setAttr("transType",transType);
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
		Page<UserWalletlog>  logList=walletLogService.getLogList(pageIndex, pageSize, transType, start, end);
		setAttr("logList",logList);
		
		render("index.html");
	}
	
	public void recharge(){
		setAttr("column","recharge");
		LotUser user=getSessionAttr(Constant.SELLER_SESSION);
		UserWallet wallet=walletService.getByUserId(user.getId());
		setAttr("wallet",wallet);
		
		render("recharge.html");
	}
	
	
	public void doRecharge(){
		BigDecimal amount=new BigDecimal(getPara("amount"));
		String rechargeType=getPara("type");
		JsonResult json=new JsonResult();
		if(amount.compareTo(BigDecimal.ONE)==-1){
			//充值不能小于1
			json.setMessage("充值金额最低不能小于1");
			json.setSuccess(false);
		}else if(!rechargeType.equals(Constant.WECHAT_RECHARGE)){
			json.setMessage("支付类型错误");
			json.setSuccess(false);
		}else{
			try{
				LotUser user=getSessionAttr(Constant.SELLER_SESSION);
				walletService.AddRechargeLog(user,amount,rechargeType);
				json.setSuccess(true);
			}catch(Exception e){
				json.setMessage("充值失败:"+e.getMessage());
				json.setSuccess(false);
			}
		}
		renderJson(json.toJsonString());
	}
//	public void doRecharge(){
//		BigDecimal amount=new BigDecimal(getPara("amount"));
//		String rechargeType=getPara("type");
//		JsonResult json=new JsonResult();
//		if(amount.compareTo(BigDecimal.ONE)==-1){
//			//充值不能小于1
//			json.setMessage("充值金额最低不能小于1");
//			json.setSuccess(false);
//		}else if(!rechargeType.equals(Constant.WECHAT_RECHARGE) && !rechargeType.equals(Constant.ALIPAY_RECHARGE)){
//			json.setMessage("支付类型错误");
//			json.setSuccess(false);
//		}else{
//			try{
//				LotUser user=getSessionAttr(Constant.SELLER_SESSION);
//				walletService.doRecharge(user,amount,rechargeType);
//				json.setSuccess(true);
//			}catch(Exception e){
//				json.setMessage("充值失败:"+e.getMessage());
//				json.setSuccess(false);
//			}
//		}
//		renderJson(json.toJsonString());
//	}
}
