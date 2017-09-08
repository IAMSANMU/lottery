package com.lottery.term;


import com.jfinal.plugin.activerecord.Page;
import com.lottery.common.model.LotteryTerm;

public class TermService {
	private static final LotteryTerm dao = new LotteryTerm().dao();
	
	
	public Page<LotteryTerm> getTermByPage(int pageIndex,int pageSize){
		return dao.paginate(pageIndex, pageSize, "select * ","from term order by createTime desc");
	}
	
	
	public LotteryTerm getCurrentTerm(){
		String sql="select * from term where isCurrent=1";
		return dao.findFirst(sql);
	}
	public void saveTerm(LotteryTerm term){
		term.save();
	}
	public void update(LotteryTerm term){
		term.update();
	}
	public LotteryTerm getTerm(String termNo){
		String sql="select * from term where term=?";
		return dao.findFirst(sql,termNo);
	}
	
}
