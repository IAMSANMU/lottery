package com.lottery.wallet.recharge;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.UserRecharge;

public class RechargeService extends BaseService<UserRecharge> {

	private UserRecharge dao=new UserRecharge().dao();
			
	@Override
	public UserRecharge dao() {
		return dao;
	}

	@Override
	public String tabName() {
		return TableMapping.me().getTable(UserRecharge.class).getName();
	}
	
	/**
	 * 查询日志
	 * @param pageIndex
	 * @param pageSize
	 * @param start
	 * @param end
	 * @return
	 */
	public Page<UserRecharge> getLogList(int pageIndex, int pageSize,Date start,Date end){
		StringBuilder sb=new StringBuilder("from user_recharge where 1=1 ");
		sb.append(" and createTime >=? and createTime <=?");
		sb.append(" order by createTime desc");
		
		List<Object> param=new ArrayList<Object>();
		param.add(start);
		param.add(end);
		Page<UserRecharge> list = dao.paginate(pageIndex, pageSize, "select * ", sb.toString(),param.toArray());
		return list;
	}

	
	
}
