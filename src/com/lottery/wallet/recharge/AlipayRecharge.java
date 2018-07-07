package com.lottery.wallet.recharge;

import java.math.BigDecimal;

import com.lottery.common.model.LotUser;

public class AlipayRecharge extends RechargeBusiness {

	@Override
	public String doRecharge(LotUser user, BigDecimal money) {
		return "alipay";
	}

}
