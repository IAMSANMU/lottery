package com.lottery.wallet.recharge;

import com.lottery.common.utils.Constant;

public class RechargeFactory {
	public static RechargeBusiness createRecharge(String type){
		RechargeBusiness bus=null;
		if(type.equals(Constant.WECHAT_RECHARGE)){
			bus=new WechatRecharge();
		}else if(type.equals(Constant.ALIPAY_RECHARGE)){
			bus=new AlipayRecharge();
		}else if(type.equals(Constant.ADMIN_RECHARGE)){
			bus=new AdminRecharge();
		}
		return bus;
	}
}
