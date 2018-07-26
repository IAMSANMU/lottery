package com.lottery.admin.user;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.lottery.common.BaseService;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.common.model.UserWallet;

public  class UserService extends BaseService<LotUser> {
	private LotUser dao = new LotUser().dao();
	
	public LotUser login(String account,String pwd){
		String sql="select * from lot_user where account=? and pwd=? ";
		return dao.findFirst(sql,account,pwd);
	}
	
	public boolean checkAccount(String account){
		String sql="select count(*) from lot_user where account=?  ";
		return Db.queryInt(sql,account)>0;
	}

	@Before(Tx.class)
	public void buy(LotUser user,String remark) {
		LotUserBuylog log=new LotUserBuylog();
		log.setCreateTime(new Date());
		log.setStartTime(user.getStartTime());
		log.setEndTime(user.getEndTime());
		log.setUserId(user.getId());
		log.setRemark(remark);
		
		log.save();
		user.update(); 
	}

	@Override
	public LotUser dao() {
		return dao;
	}

	@Override
	public String tabName() {
		return TableMapping.me().getTable(LotUser.class).getName();
	}
	@Override
	@Before(Tx.class)
	public void save(LotUser user){
		user.save();
		//增加钱包
		UserWallet wallet=new UserWallet();
		wallet.setAccount(user.getAccount());
		wallet.setBalance(BigDecimal.ZERO);
		wallet.setUserId(user.getId());
		wallet.save();
	}
	
	
	
	
	
	
}
