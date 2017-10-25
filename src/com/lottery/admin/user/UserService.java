package com.lottery.admin.user;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.common.BaseService;
import com.lottery.common.model.LotUser;
import com.lottery.search.SearchModel;

public class UserService extends BaseService {
	private static final LotUser dao = new LotUser().dao();
	
	public Page<LotUser> getByPage(int pageIndex,int pageSize,String sort,String sortType,List<SearchModel> searchModels){
		String sql=super.buildSqlByPage("lot_user", sort, sortType, searchModels);
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
	
	
	
	
	
	
	
}
