package com.lottery.dc;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.lottery.common.model.DcArrange;
import com.lottery.common.model.LotteryTerm;


public class DcService {
	private static final DcArrange dao = new DcArrange().dao();
	
	public DcArrange getById(int id){
		return dao.findById(id);
	}
	
	public void stopMatch(LotteryTerm currentTerm){
		String sql="update dc_arrange  set  status=1 where endTime <=now() and term=?";
		Db.update(sql,currentTerm.getTerm());
	}
	/*
	 * 获取可用比赛列表
	 */
	public List<DcArrange> getDcList(LotteryTerm currentTerm){
		String sql="select * from Dc_arrange where term=?";
		return dao.find(sql,currentTerm.getTerm());
	}
}
