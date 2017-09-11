package com.lottery.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcSpfSp;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.dc.DcBusiness;
import com.lottery.dc.DcService;
import com.lottery.odds.sp.SpBusiness;
import com.lottery.odds.sp.SpService;
import com.lottery.term.TermService;

/*
 * 抓取赛事信息
 */
public class DcInfoTask implements Runnable {

	private DcService dcService = new DcService();
	private TermService termService = new TermService();
	private SpService spService = new SpService();
	private static Logger log = Logger.getLogger(TermTask.class);

	@Override
	public void run() {
		LotteryTerm term = termService.getCurrentTerm();
		try {
			log.info("---[对阵截止]北单赛事截止开始-----");
			stop(term);
			log.info("---[对阵截止]北单赛事截止结束-----");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.info("---[对阵截止]北单赛事截止错误-----");
		}
		try {
			log.info("---[对阵抓取]北单赛事维护开始-----");
			snatchMatch(term);
			log.info("---[对阵抓取]北单期数维护结束-----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("---北单期数维护错误-----");
		}
		try {
			log.info("---[sp抓取]北单sp维护开始-----");
			snatchSp(term);
			log.info("---[sp抓取]北单sp维护结束-----");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("---[sp抓取]北单sp维护错误-----");
		}

	}

	private void snatchSp(LotteryTerm term) throws Exception {
		List<DcSpfSp> list = SpBusiness.snatchOkSp(term.getTerm());
		Map<String,DcSpfSp> snatchMap=new HashMap<String,DcSpfSp>();
		for (DcSpfSp dc : list) {
			snatchMap.put(dc.getLineId(), dc);
		}
		//判断数据库sp是否有变化
		List<DcSpfSp> dbList=spService.getSpList(term.getTerm());
		for (DcSpfSp db : dbList) {
			if(snatchMap.containsKey(db.getLineId())){
				DcSpfSp snatchSp=snatchMap.get(db.getLineId());
				if(!db.getSpStr().equals(snatchSp.getSpStr())){
					//sp有变化保存
					log.info("[sp抓取]===term"+snatchSp.getTerm()+"的lineId的"+snatchSp.getLineId()+"sp有变化保存");
					snatchSp.setMatchId(db.getId()).save();
				}
			}
		}
	}

	private void stop(LotteryTerm term) {
		dcService.stopMatch(term);
	}

	private void snatchMatch(LotteryTerm term) {
		List<DcArrange> snatchList = DcBusiness.snatchDcMatch();
		List<DcArrange> dbList = dcService.getDcList(term);
		Map<String, DcArrange> dbMap = new HashMap<String, DcArrange>();
		for (DcArrange dc : dbList) {
			dbMap.put(dc.getLineId(), dc);
		}
		for (DcArrange dc : snatchList) {
			String boutIndex = dc.getLineId();
			if (!dbMap.containsKey(boutIndex)) {
				dc.save();
				dbMap.put(boutIndex, dc);
				log.info("---北单彩期" + term.getTerm() + "抓取赛程:" + boutIndex + "--主队VS客队:" + dc.getHome() + "vs"
						+ dc.getGuest() + ";比赛时间:" + DateUtil.getDateTimeFormat(dc.getMatchTime()) + ";让球:"
						+ dc.getRqs() + "---");
			}
		}
	}

}