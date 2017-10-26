package com.lottery.admin.user;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.lottery.common.BaseService;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.search.SearchModel;

public class UserService extends BaseService {
	private LotUser dao = new LotUser().dao();
	public Page<LotUser> getByPage(int pageIndex,int pageSize,String sort,String sortType,List<SearchModel> searchModels){
		String sql="from lot_user "+super.buildSqlByPage(sort, sortType,"lot_user", searchModels);
		Page<LotUser> list=dao.paginate(pageIndex, pageSize, "select * ",sql);
		return	list;
	}
	
	public void update(LotUser lotUser){
		lotUser.update();
	}
	public void save(LotUser lotUser){
		lotUser.save();
	}
	public void logicDelete(String[] ids){
		String sql="update lot_user set isDel=1 where isDel=0 and id in ("+StrKit.join(ids,",")+") ";
		Db.update(sql);
	}
	public void restore(String[] ids){
		String sql="update lot_user set isDel=0 where isDel=1 and id in ("+StrKit.join(ids,",")+") ";
		Db.update(sql);
	}
	public LotUser login(String account,String pwd){
		String sql="select * from lot_user where account=? and pwd=? ";
		return dao.findFirst(sql,account,pwd);
	}
	
	public LotUser get(int id){
		return dao.findById(id);
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
	
	
	
	
	
	
	
}
