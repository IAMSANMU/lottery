package com.lottery.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcDxp;
import com.lottery.common.model.DcOp;
import com.lottery.common.model.DcSpfSp;
import com.lottery.common.model.DcYp;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.dc.DcService;
import com.lottery.odds.dxp.DxpBusiness;
import com.lottery.odds.dxp.DxpService;
import com.lottery.odds.op.OpBusiness;
import com.lottery.odds.op.OpService;
import com.lottery.odds.sp.SpBusiness;
import com.lottery.odds.sp.SpService;
import com.lottery.odds.yp.YpBusiness;
import com.lottery.odds.yp.YpService;
import com.lottery.term.TermService;

/*
 * 抓取单场亚盘,欧赔 大小盘信息
 */
public class DcOddsTask implements Runnable {
	private DcService dcService = new DcService();
	private TermService termService = new TermService();
	private YpService ypService = new YpService();
	private OpService opService = new OpService();
	private DxpService dxpService = new DxpService();
	private SpService spService = new SpService();
	private static Logger log = Logger.getLogger(DcOddsTask.class);

	public void run() {
		LotteryTerm term = termService.getCurrentTerm();
		if (term != null) {
			List<DcArrange> dcList = dcService.getDcList(term);
			
			try {
				log.info("---[sp抓取]北单sp维护开始-----");
				snatchSp(term);
				log.info("---[sp抓取]北单sp维护结束-----");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("---[sp抓取]北单sp维护错误-----");
			}
			
			try {
				log.info("----------[yp抓取]抓取亚盘开始-------------");
				snatchYp(dcList);
				log.info("----------[yp抓取]抓取亚盘结束-------------");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("----------[yp抓取]抓取亚盘错误-------------");
			}

			try {
				log.info("----------[op抓取]抓取op开始-------------");
				snatchOp(dcList);
				log.info("----------[op抓取]抓取op结束-------------");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("----------[op抓取]抓取op错误-------------");
			}
			try {
				log.info("----------[dxp抓取]抓取大小盘开始-------------");
				snatchDxp(dcList);
				log.info("----------[dxp抓取]抓取大小盘结束-------------");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("----------[dxp抓取]抓取大小盘错误-------------");
			}
		}

	}

	private void snatchSp(LotteryTerm term) throws Exception {
		List<DcSpfSp> list = SpBusiness.snatchOkSp(term.getTerm());
		Map<String, DcSpfSp> dbMap = new HashMap<String, DcSpfSp>();
		// 判断数据库sp是否有变化
		List<DcSpfSp> dbList = spService.getSpList(term.getTerm());
		for (DcSpfSp db : dbList) {
			dbMap.put(db.getLineId(), db);
		}

		List<DcArrange> dbDcList = dcService.getDcList(term);
		Map<String, DcArrange> dbDcMap = new HashMap<String, DcArrange>();
		for (DcArrange dc : dbDcList) {
			dbDcMap.put(dc.getLineId(), dc);
		}

		for (DcSpfSp sp : list) {
			DcArrange dc = dbDcMap.get(sp.getLineId());
			if (dc != null) {
				if (dbMap.containsKey(sp.getLineId())) {
					DcSpfSp db = dbMap.get(sp.getLineId());
					if (!db.getSpStr().equals(sp.getSpStr())) {
						// sp有变化保存
						log.info("[sp抓取]===term" + sp.getTerm() + "的lineId的" + sp.getLineId() + "sp有变化保存");
						sp.setMatchId(dc.getId()).save();
					}
				} else { // sp不存在 ,保存数据
					log.info("[sp抓取]===term" + sp.getTerm() + "的lineId的" + sp.getLineId() + "抓取新sp保存");
					sp.setMatchId(dc.getId()).save();
				}
			}
		}

	}
	
	private void snatchOp(List<DcArrange> matchList) throws Exception {
		for (DcArrange match : matchList) {
			String oddId = match.getOddId();
			try {
				if (oddId != null) {
					List<DcOp> opList = opService.get(match.getId());
					Map<String, DcOp> dbFirstOpMap = new HashMap<String, DcOp>();
					Map<String, DcOp> dbNowOpMap = new HashMap<String, DcOp>();
					for (DcOp dbOp : opList) {
						if (dbOp.getIsFirst()) {
							dbFirstOpMap.put(dbOp.getCompany(), dbOp);
						} else {
							dbNowOpMap.put(dbOp.getCompany(), dbOp);
						}
					}
					// 抓取亚盘信息
					String html = OpBusiness.getRealHtml(match, oddId);
					List<DcOp> snatchList = OpBusiness.getOp(html);
					Map<String, DcOp> firstOpMap = new HashMap<String, DcOp>();
					Map<String, DcOp> nowOpMap = new HashMap<String, DcOp>();

					for (DcOp op : snatchList) {
						if (op.getIsFirst()) {
							nowOpMap.put(op.getCompany(), op);
						} else {
							firstOpMap.put(op.getCompany(), op);
						}
					}
					// 保存初盘
					for (String company : firstOpMap.keySet()) {
						if (!dbFirstOpMap.containsKey(company)) {
							DcOp op = firstOpMap.get(company);
							op.setMatchId(match.getId());
							op.setTerm(match.getTerm());
							op.setLineId(match.getLineId());
							op.save();
						}
					}
					// 保存即时盘
					for (String company : nowOpMap.keySet()) {
						DcOp dbNowOp = nowOpMap.get(company);
						DcOp nowOp = nowOpMap.get(company);
						if (dbNowOp == null) {
							nowOp.setMatchId(match.getId());
							nowOp.setTerm(match.getTerm());
							nowOp.setLineId(match.getLineId());
							// 判断homeChange drawChange guestChnage 跟初盘比
							DcOp firstOp = firstOpMap.get(company);
							nowOp.setHomeChange(getChange(firstOp.getHome(), nowOp.getHome()));
							nowOp.setDrawChange(getChange(firstOp.getDraw(), nowOp.getDraw()));
							nowOp.setGuestChange(getChange(firstOp.getGuest(), nowOp.getGuest()));
							nowOp.save();
						} else {
							// 判断是否有变化
							int homeChange = getChange(dbNowOp.getHome(), nowOp.getHome());
							int drawChange = getChange(dbNowOp.getDraw(), nowOp.getDraw());
							int guestChange = getChange(dbNowOp.getGuest(), nowOp.getGuest());
							// 有一个变化就保存数据
							if (homeChange != 0 || drawChange != 0 || guestChange != 0) {
								nowOp.setHomeChange(homeChange);
								nowOp.setDrawChange(drawChange);
								nowOp.setGuestChange(guestChange);
								nowOp.setMatchId(match.getId());
								nowOp.setTerm(match.getTerm());
								nowOp.setLineId(match.getLineId());
								nowOp.save();
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("---------[op抓取]抓取oddId=" + oddId + "op信息错误-------" + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
	}

	private int getChange(Double oldSp, Double newSp) {
		return oldSp.doubleValue() == newSp.doubleValue() ? 0 : (oldSp.doubleValue() > newSp.doubleValue() ? 1 : -1);
	}

	private void snatchYp(List<DcArrange> matchList) throws Exception {
		for (DcArrange match : matchList) {
			String oddId = match.getOddId();
			try {
				if (oddId != null) {
					// 获取数据库亚盘
					List<DcYp> ypList = ypService.get(match.getId());
					Map<String, DcYp> dbFirstYpMap = new HashMap<String, DcYp>();
					Map<String, DcYp> dbNowYpMap = new HashMap<String, DcYp>();
					for (DcYp dbYp : ypList) {
						if (dbYp.getIsFirst()) {
							dbFirstYpMap.put(dbYp.getCompany(), dbYp);
						} else {
							dbNowYpMap.put(dbYp.getCompany(), dbYp);
						}
					}
					// 抓取亚盘信息
					String html = YpBusiness.getRealHtml(match, oddId);
					List<DcYp> nowYpList = YpBusiness.getNowYp(html);// 即时盘
					List<DcYp> firstYpList = YpBusiness.getFirstYp(html);// 初盘
					Map<String, DcYp> firstYpMap = new HashMap<String, DcYp>();
					Map<String, DcYp> nowYpMap = new HashMap<String, DcYp>();

					for (DcYp nowYp : nowYpList) {
						nowYpMap.put(nowYp.getCompany(), nowYp);
					}
					for (DcYp firstYp : firstYpList) {
						firstYpMap.put(firstYp.getCompany(), firstYp);
					}
					// 保存初盘
					for (String company : firstYpMap.keySet()) {
						if (!dbFirstYpMap.containsKey(company)) {
							DcYp yp = firstYpMap.get(company);
							yp.setMatchId(match.getId());
							yp.setTerm(match.getTerm());
							yp.setLineId(match.getLineId());
							yp.save();
						}
					}
					// 保存即时盘
					for (String company : nowYpMap.keySet()) {
						DcYp dbNowYp = dbNowYpMap.get(company);
						DcYp nowYp = nowYpMap.get(company);
						DcYp firstYp=firstYpMap.get(company);
						
						//初盘不等于即时盘, 则保存
						if(firstYp!=null && nowYp.getTime().compareTo(firstYp.getTime())!=0){
							//即时盘跟数据库对比
							if (dbNowYp == null) {
								nowYp.setMatchId(match.getId());
								nowYp.setTerm(match.getTerm());
								nowYp.setLineId(match.getLineId());
								nowYp.save();
							} else {
								// 判断时间是否一致,不一致就保持
								//抓取到的时间要大于数据库的时间
								if (nowYp.getTime().compareTo(dbNowYp.getTime())==1) {
									nowYp.setMatchId(match.getId());
									nowYp.setTerm(match.getTerm());
									nowYp.setLineId(match.getLineId());
									nowYp.save();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("---------[yp抓取]抓取oddId=" + oddId + "亚盘信息错误-------" + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
	}

	private void snatchDxp(List<DcArrange> matchList) throws Exception {
		for (DcArrange match : matchList) {
			String oddId = match.getOddId();
			try {
				if (oddId != null) {
					List<DcDxp> dxpList = dxpService.get(match.getId());
					Map<String, DcDxp> dbFirstDxpMap = new HashMap<String, DcDxp>();
					Map<String, DcDxp> dbNowDxpMap = new HashMap<String, DcDxp>();
					for (DcDxp dbDxp : dxpList) {
						if (dbDxp.getIsFirst()) {
							dbFirstDxpMap.put(dbDxp.getCompany(), dbDxp);
						} else {
							dbNowDxpMap.put(dbDxp.getCompany(), dbDxp);
						}
					}
					// 抓取亚盘信息
					String html = DxpBusiness.getRealHtml(match, oddId);
					List<DcDxp> nowDxpList = DxpBusiness.getNowDxp(html);// 即时盘
					List<DcDxp> firstDxpList = DxpBusiness.getFirstDxp(html);// 初盘
					Map<String, DcDxp> firstDxpMap = new HashMap<String, DcDxp>();
					Map<String, DcDxp> nowDxpMap = new HashMap<String, DcDxp>();

					for (DcDxp nowDxp : nowDxpList) {
						nowDxpMap.put(nowDxp.getCompany(), nowDxp);
					}
					for (DcDxp firstDxp : firstDxpList) {
						firstDxpMap.put(firstDxp.getCompany(), firstDxp);
					}
					// 保存初盘
					for (String company : firstDxpMap.keySet()) {
						if (!dbFirstDxpMap.containsKey(company)) {
							DcDxp dxp = firstDxpMap.get(company);
							dxp.setMatchId(match.getId());
							dxp.setTerm(match.getTerm());
							dxp.setLineId(match.getLineId());
							dxp.save();
						}
					}
					// 保存即时盘
					for (String company : nowDxpMap.keySet()) {
						DcDxp dbNowDxp = dbNowDxpMap.get(company);
						DcDxp nowDxp = nowDxpMap.get(company);
						DcDxp firstDxp=firstDxpMap.get(company);
						
						//初盘不等于即时盘, 则保存
						if(firstDxp!=null && nowDxp.getTime().compareTo(firstDxp.getTime())!=0){
							if (dbNowDxp == null) {
								nowDxp.setMatchId(match.getId());
								nowDxp.setTerm(match.getTerm());
								nowDxp.setLineId(match.getLineId());
								nowDxp.save();
							} else {
								// 判断时间是否一致,不一致就保持
								//抓取到的时间要大于数据库的时间
								if (nowDxp.getTime().compareTo(dbNowDxp.getTime())==1) {
									nowDxp.setMatchId(match.getId());
									nowDxp.setTerm(match.getTerm());
									nowDxp.setLineId(match.getLineId());
									nowDxp.save();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("---------[DXP抓取]抓取oddId=" + oddId + "DXP信息错误-------" + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
	}
	public static void main(String[] args) {
		Calendar cal=Calendar.getInstance();
		Date a=cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date b=cal.getTime();
		System.out.println(DateUtil.getDateTimeFormat(a));
		System.out.println(DateUtil.getDateTimeFormat(b));
		System.out.println(b.compareTo(a));
	}

}
