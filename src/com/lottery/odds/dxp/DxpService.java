package com.lottery.odds.dxp;

import java.util.List;

import com.lottery.common.model.DcDxp;

public class DxpService {
	private static DcDxp dao = new DcDxp().dao();

	public List<DcDxp> get(int matchId) {
		String sql = "select yp.* From dc_dxp yp Where  yp.id in (Select Max(tmpYp.Id) From dc_dxp  tmpYp where tmpYp.matchId =:matchId group by company,isFirst)";
		return dao.find(sql, matchId);
	}
	public List<DcDxp> getList(String term,String company){
		String sql="select * from dc_dxp yp where yp.id in (select MAX(id) from dc_dxp where term=? and company=?  group by company, matchId)";
		
		return dao.find(sql,term,company);
		
		
	}

}
