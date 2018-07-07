package com.lottery.wallet.recharge;

import java.math.BigDecimal;

import com.lottery.common.model.LotUser;

public abstract class RechargeBusiness {

	/**
	 * 充值操作
	 * @param user
	 * @param money
	 * @return 返回操作流水
	 */
	public abstract String doRecharge(LotUser user,BigDecimal money) throws Exception;
	
}
