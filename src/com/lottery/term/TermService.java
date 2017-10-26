package com.lottery.term;


import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.lottery.common.model.LotteryTerm;

public class TermService {
	LotteryTerm dao = new LotteryTerm().dao();
	
	
	public Page<LotteryTerm> getTermByPage(int pageIndex,int pageSize){
		return dao.paginate(pageIndex, pageSize, "select * ","from lottery_term order by term desc");
	}
	
	
	public LotteryTerm getCurrentTerm(){
		String sql="select * from lottery_term where isCurrent=1";
		return dao.findFirst(sql);
	}
	public void saveTerm(LotteryTerm term){
		term.save();
	}
	public void update(LotteryTerm term){
		term.update();
	}
	public LotteryTerm getTerm(String termNo){
		String sql="select * from lottery_term where term=?";
		return dao.findFirst(sql,termNo);
	}
	public LotteryTerm getPrevTerm(String currentTerm){
		LotteryTerm term=null;
		List<LotteryTerm> list=dao.paginate(1, 1, "select * ","from lottery_term where term < "+currentTerm+" order by term desc").getList();
		if(!list.isEmpty()){
			term=list.get(0);
		}
		return term;
	}
	
}
