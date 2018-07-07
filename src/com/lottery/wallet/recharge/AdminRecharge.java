package com.lottery.wallet.recharge;

import java.math.BigDecimal;

import com.lottery.common.model.LotUser;

/**
 * 后台进行充值
 * @author IMOW
 *
 */
public class AdminRecharge extends RechargeBusiness {

	@Override
	public String doRecharge(LotUser user, BigDecimal money) throws Exception {
		return "后台手动充值";
	}

	

}
