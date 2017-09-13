package com.lottery.odds.yp;

import java.util.List;

import com.lottery.common.model.DcYp;

public class YpService {
	private static DcYp dao = new DcYp().dao();

	public List<DcYp> get(int matchId) {
		String sql = "select yp.* From dc_yp yp Where  yp.id in (Select Max(tmpYp.Id) From dc_yp  tmpYp where tmpYp.matchId =? group by company,isFirst)";
		return dao.find(sql, matchId);
	}
	public List<DcYp> getList(String term,String company){
		String sql="select * from dc_yp yp where yp.id in (select MAX(id) from dc_yp where term=? and company=?  group by company, matchId)";
		
		return dao.find(sql,term,company);
	}
	public List<DcYp> getHistoryList(int matchId,String company){
		String sql="select * from dc_yp yp  where company=? and matchId=? order by time desc";
		return dao.find(sql,company,matchId);
	}

}
