package com.lottery.wallet.recharge;

import java.math.BigDecimal;

import com.lottery.common.model.LotUser;

public class WechatRecharge extends RechargeBusiness {

	@Override
	public String doRecharge(LotUser user, BigDecimal money) {
		return "weChatPay";
	}

}
