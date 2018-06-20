package com.lottery.admin.log;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.LotUserBuylog;
import com.lottery.common.model.NewsSection;
import com.lottery.search.SearchModel;

public  class LogService extends BaseService<LotUserBuylog> {

	private LotUserBuylog dao=new LotUserBuylog().dao();
	
	@Override
	public  Page<LotUserBuylog> getByPage(int pageIndex,int pageSize,String sort,String sortType,List<SearchModel> searchModels){
		StringBuilder sql= new StringBuilder(" from lot_user_buylog l inner join lot_user u on l.userId=u.id");
		
		sql.append(super.buildSqlByPage(sort, sortType, "u", searchModels));
		
		Page<LotUserBuylog> list=dao.paginate(pageIndex, pageSize, "select l.*,u.name,u.account,u.tel ",sql.toString());
		
		
		return	list;
	}
	
	public List<LotUserBuylog> getList(int userId){
		return dao.find("select * from lot_user_buylog where userId=? order by createTime desc",userId);
	}

	@Override
	public LotUserBuylog dao() {
		return dao;
	}

	@Override
	public String tabName() {
		return TableMapping.me().getTable(LotUserBuylog.class).getName();
	}
}
 