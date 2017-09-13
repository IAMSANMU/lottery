package com.lottery.task;

import org.apache.log4j.Logger;

import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.term.TermBusiness;
import com.lottery.term.TermService;

/*
 * 维护彩期类
 */
public class TermTask implements Runnable{
	static TermService lotteryTermService=new TermService();
	private static Logger log=Logger.getLogger(TermTask.class);
	@Override
	public void run() {
		//抓到新期数 插入期数,关闭其他期数
		try {
			log.info("---[彩期抓取]北单期数维护开始-----");
			updateTerm();
			log.info("---[彩期抓取]北单期数维护结束-----");
		} catch (Exception e) {
			log.error("---[彩期抓取]北单期数维护出现错误-----");
			e.printStackTrace();
		}
	}
	
	private LotteryTerm buildNewTerm(LotteryTerm snatchTerm){
		log.info("-------[彩期抓取]插入新期数"+snatchTerm.getTerm()+"----------");
		snatchTerm.setIsCurrent(1);
		snatchTerm.setStatus(1);
		snatchTerm.save();
		return snatchTerm;
	}
	public LotteryTerm updateTerm() {
		LotteryTerm dbTerm=lotteryTermService.getCurrentTerm();
		LotteryTerm snatchTerm =TermBusiness.snatchAIBOTerm();
		LotteryTerm term=null;
		if (dbTerm == null) {
			if (snatchTerm != null) {
				// 插入彩期
				term=buildNewTerm(snatchTerm);
			}
		} else {
			if (snatchTerm != null) {
				if (dbTerm.getTerm().equals(snatchTerm.getTerm())) {
					String before=DateUtil.getDateTimeFormat(dbTerm.getEndTime());
					String after=DateUtil.getDateTimeFormat(snatchTerm.getEndTime());
					if (!before.equals(after)) {
						// 更新截止时间
						dbTerm.setEndTime(snatchTerm.getEndTime());
						dbTerm.update();
						log.info("----[彩期抓取]更新彩期" + dbTerm.getTerm() + "的截止时间before:" + before + ",after:" + after + "---");
					}
				}else{
					//更新彩期
					dbTerm.setIsCurrent(0).update();
					//彩期不一致 插入新
					term=buildNewTerm(snatchTerm);
				}
			}
		}
		return term;
	}
	
}
