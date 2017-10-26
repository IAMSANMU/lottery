package com.lottery.admin.log;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.lottery.common.BaseService;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.search.SearchModel;

public class LogService extends BaseService {

	private LotUserBuylog dao=new LotUserBuylog().dao();
	
	public Page<LotUserBuylog> getByPage(int pageIndex,int pageSize,String sort,String sortType,List<SearchModel> searchModels){
		StringBuilder sql= new StringBuilder(" from lot_user_buylog l inner join lot_user u on l.userId=u.id");
		
		sql.append(super.buildSqlByPage(sort, sortType, "u", searchModels));
		
		
		
		Page<LotUserBuylog> list=dao.paginate(pageIndex, pageSize, "select l.*,u.name,u.account,u.tel ",sql.toString());
		
		
		return	list;
	}
}
 