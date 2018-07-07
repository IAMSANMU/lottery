package com.lottery.wallet;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.NewsContext;
import com.lottery.common.model.UserRecharge;
import com.lottery.common.model.UserWallet;
import com.lottery.common.model.UserWalletlog;
import com.lottery.common.model.UserWalletlog.TransTypeEnum;
import com.lottery.common.utils.Constant;
import com.lottery.wallet.recharge.RechargeBusiness;
import com.lottery.wallet.recharge.RechargeFactory;

public class WalletService   {

	private static Logger log = Logger.getLogger(WalletLogService.class);
	private WalletLogService logService = new WalletLogService();
	private UserWallet dao = new UserWallet().dao();

	public UserWallet getByUserId(int userId) {
		String sql = "select * from user_wallet where userId=?";
		return dao.findFirst(sql, userId);
	}

	/**
	 * 购买新闻
	 * 
	 * @param user
	 *            用户
	 * @param money
	 *            产生的金额
	 * @param outId
	 *            产生的流水id
	 * @param remark
	 *            备注
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void BuyNews(LotUser user, NewsContext news) throws Exception {
		use(user, news.getMoney(), news.getId().toString(), "购买文章,文章id" + news.getId());
	}

	/**
	 * 钱包使用
	 * 
	 * @param user
	 *            用户
	 * @param money
	 *            产生的金额
	 * @param outId
	 *            产生的流水id
	 * @param remark
	 *            备注
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void use(LotUser user, BigDecimal money, String outId, String remark) throws Exception {
		useOrRecharge(user, money, true, outId, remark);
		log.info("[钱包消费" + user.getAccount() + "]成功:" + remark);

	}

	/**
	 * 钱包充值
	 * 
	 * @param user
	 *            用户
	 * @param money
	 *            产生的金额
	 * @param outId
	 *            产生的流水id
	 * @param remark
	 *            备注
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void recharge(LotUser user, BigDecimal money, String outId, String remark) throws Exception {
		useOrRecharge(user, money, false, outId, remark);
		log.info("[钱包充值" + user.getAccount() + "]成功:" + remark);
	}

	/**
	 * 使用还是充钱
	 * 
	 * @param user
	 *            用户
	 * @param money
	 *            产生的金额
	 * @param isUse
	 *            是否使用
	 * @param outId
	 *            产生的流水id
	 * @param remark
	 *            备注
	 * @throws Exception
	 */
	@Before(Tx.class)
	private void useOrRecharge(LotUser user, BigDecimal money, boolean isUse, String outId, String remark)
			throws Exception {
		money = money.abs();// 绝对值
		// 获取用户钱包
		UserWallet wallet = getByUserId(user.getId());
		// 消费类型
		int transType = isUse ? TransTypeEnum.Out.ordinal() : TransTypeEnum.In.ordinal();
		// 剩余额度
		BigDecimal balance = null;
		String sql = "";
		if (isUse) {
			balance = wallet.getBalance().subtract(money);
			if (balance.compareTo(BigDecimal.ZERO) == -1) {// 余额小于0
				throw new Exception("余额不足");
			}
			sql = "update user_wallet set balance=balance-? where id=? ";
		} else {
			balance = wallet.getBalance().add(money);
			sql = "update user_wallet set balance=balance+? where id=? ";
		}

		Db.update(sql, money, wallet.getId());
		// 插入日志
		UserWalletlog log = new UserWalletlog();
		log.setUserId(user.getId());
		log.setAccount(user.getAccount());
		log.setBalance(balance);
		log.setOutId(outId);
		log.setCreateTime(new Date());
		log.setTransType(transType);
		log.setTransMoney(money);
		log.setRemark(remark);
		logService.save(log);

	}
	/**
	 * 前台预充值
	 * @param user
	 * @param money
	 * @param type
	 * @throws Exception
	 */
	public void AddRechargeLog(LotUser user,BigDecimal money,String type) throws Exception{
		money=money.abs();
		if(type.equals(Constant.WECHAT_RECHARGE)){
			UserRecharge log=new UserRecharge();
			log.setUserId(user.getId());
			log.setAccount(user.getAccount());
			log.setCreateTime(new Date());
			log.setMoney(money);
			log.setRechargeType(type);
			log.setStatus(1);
			log.save();
		}else {
			throw new Exception("充值方式错误");
		}
	}
	
	
	
	/**
	 * 充值
	 * @param user
	 * @param money
	 * @param type
	 */
	public void doRecharge(LotUser user,BigDecimal money,String type) throws Exception{
		RechargeBusiness bus=RechargeFactory.createRecharge(type);
		if(bus==null){
			log.error("[钱包充值" + user.getAccount() + "]失败:充值方式错误");
			throw new Exception("充值方式错误");
		}
		try {
			String lineNo=bus.doRecharge(user, money);
			recharge(user, money, lineNo, type+"充值"+money);
		} catch (Exception e) {
			log.error("[钱包充值" + user.getAccount() + "]失败:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
}
