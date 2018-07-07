package com.lottery.wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.UserWalletlog;

public class WalletLogService extends BaseService {

	private UserWalletlog dao=new UserWalletlog().dao();
	
	/**
	 * 查询日志
	 * @param pageIndex
	 * @param pageSize
	 * @param transType
	 * @param start
	 * @param end
	 * @return
	 */
	public Page<UserWalletlog> getLogList(int pageIndex, int pageSize, int transType,Date start,Date end){
		StringBuilder sb=new StringBuilder("from user_walletLog where 1=1 ");
		sb.append(" and createTime >=? and createTime <=?");
		if(transType!=-1){
			sb.append(" and transType=?");
		}
		sb.append(" order by createTime desc");
		
		List<Object> param=new ArrayList<Object>();
		param.add(start);
		param.add(end);
		if(transType!=-1){
			param.add(transType);
		}
		Page<UserWalletlog> list = dao.paginate(pageIndex, pageSize, "select * ", sb.toString(),param.toArray());
		return list;
	}



	@Override
	public Model dao() {
		return dao;
	}

	@Override
	public String tabName() {
		return TableMapping.me().getTable(UserWalletlog.class).getName();
	}
}
