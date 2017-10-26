package com.lottery.odds.op;

import java.util.List;

import com.lottery.common.model.DcOp;

public class OpService {
	DcOp dao = new DcOp().dao();

	public List<DcOp> get(int matchId) {
		String sql = "select op.* From dc_op op Where  op.id in (Select Max(Id) From dc_op  where matchId =? group by company,isFirst)";
		return dao.find(sql, matchId);
	}

	public List<DcOp> getList(String term, String company) {
		String sql = "select * from dc_op op where op.id in (select MAX(id) from dc_op where term=? and company=?  group by company, matchId)";
		return dao.find(sql, term, company);
	}

}
