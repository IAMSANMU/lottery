package com.lottery.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.dc.DcBusiness;
import com.lottery.dc.DcService;
import com.lottery.term.TermService;

/*
 * 抓取赛事信息
 */
public class DcInfoTask implements Runnable {

	private DcService dcService = new DcService();
	private TermService termService = new TermService();

	private static Logger log = Logger.getLogger(TermTask.class);

	@Override
	public void run() {
		LotteryTerm term = termService.getCurrentTerm();
		if (term != null) {
			LotteryTerm prevTerm = termService.getPrevTerm(term.getTerm());
			try {
				log.info("---[对阵抓取]北单赛事维护开始-----");
				snatchMatch(term);
				log.info("---[对阵抓取]北单期数维护结束-----");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("---北单期数维护错误-----");
			}
		
			try {
				log.info("---[对阵截止]北单赛事截止开始-----");
				stop(term);
				// 更新上一期
				if (prevTerm != null) {
					stop(prevTerm);
				}
				log.info("---[对阵截止]北单赛事截止结束-----");
			} catch (Exception e1) {
				e1.printStackTrace();
				log.info("---[对阵截止]北单赛事截止错误-----");
			}
			try {
				log.info("---[赛果抓取" + term.getTerm() + "]北单赛事赛果抓取开始-----");
				snatchScore(term);
				log.info("---[赛果抓取" + term.getTerm() + "]北单赛事赛果抓取结束-----");
			} catch (Exception e1) {
				e1.printStackTrace();
				log.info("---[赛果抓取" + term.getTerm() + "]北单赛事赛果抓取错误-----");
			}
			try {
				log.info("---[赛果抓取" + prevTerm.getTerm() + "]北单赛事赛果抓取开始-----");
				snatchScore(prevTerm);
				log.info("---[赛果抓取" + prevTerm.getTerm() + "]北单赛事赛果抓取结束-----");
			} catch (Exception e1) {
				e1.printStackTrace();
				log.info("---[赛果抓取" + prevTerm.getTerm() + "]北单赛事赛果抓取错误-----");
			}
		}

	}



	private void snatchScore(LotteryTerm term) {
		List<DcArrange> snatchList = DcBusiness.snatchDcScore(term.getTerm());

		List<DcArrange> dbList = dcService.getDcList(term);
		Map<String, DcArrange> dbMap = new HashMap<String, DcArrange>();
		for (DcArrange dc : dbList) {
			dbMap.put(dc.getLineId(), dc);
		}
		for (DcArrange dc : snatchList) {
			if (dbMap.containsKey(dc.getLineId())) {
				DcArrange dbDc = dbMap.get(dc.getLineId());
				String half = dc.getHalfScore();
				String whole = dc.getWholeScore();
				String dbHalf = dbDc.getHalfScore();
				String dbWhole = dbDc.getWholeScore();
				if (!half.equals(dbHalf) || !whole.equals(dbWhole)) {
					dbDc.setHalfScore(half);
					dbDc.setWholeScore(whole);
					dbDc.update();
					log.info("----[对阵信息]保存比赛结果 "+dbDc.getHome()+"VS"+dbDc.getGuest()+"----");
				}
			}
		}
	}

	private void stop(LotteryTerm term) {
		dcService.stopMatch(term);
	}

	private void snatchMatch(LotteryTerm term) {
		List<DcArrange> snatchList = DcBusiness.snatchDcMatch(term.getTerm());
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
				log.info("---[对阵信息]北单彩期" + term.getTerm() + "抓取赛程:" + boutIndex + "--主队VS客队:" + dc.getHome() + "vs"
						+ dc.getGuest() + ";比赛时间:" + DateUtil.getDateTimeFormat(dc.getMatchTime()) + ";让球:"
						+ dc.getRqs() + "---");
			} else {

			}
		}
	}

}