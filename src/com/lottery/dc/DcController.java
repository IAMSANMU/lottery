package com.lottery.dc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcSpfSp;
import com.lottery.common.model.DcYp;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.pojo.DcBetInfo;
import com.lottery.sp.SpService;
import com.lottery.term.TermService;
import com.lottery.yp.YpService;

public class DcController extends Controller {
	DcService dcService = new DcService();
	TermService termService = new TermService();
	YpService ypService = new YpService();
	SpService spService = new SpService();

	public void index() {

		String term = getAttr("term");
		getInfo(term);
		render("index.jsp");
	}

	private void getInfo(String term) {
		LotteryTerm currentTerm = null;
		if (StringUtils.isNotEmpty(term)) {
			currentTerm = termService.getTerm(term);
		} else {
			currentTerm = termService.getCurrentTerm();
		}
		// 获取对阵数据
		setAttr("currentTerm", currentTerm.getTerm());

		List<DcArrange> dcList = dcService.getDcList(currentTerm);
		setAttr("dcList", dcList);
		// 计算停售场次
		stopCount(dcList);
		// 获取期数列表
		getTermList();

		// 获取sp数据
		
		// 获取yp数据
		
		
		//获取投注比
		
		
	}
	private void getYp(LotteryTerm term,List<DcArrange> raceList){
		
		String company=DcYp.COMAPNY.get("皇冠");
		List<DcYp> ypList = ypService.getList(term.getTerm(),company);
		
		Map<String, DcYp> ypMap = new HashMap<String, DcYp>();
		for (DcYp yp : ypList) {
			if (yp!=null&&yp.getCompany() != null) {
				ypMap.put(String.valueOf(yp.getMatchId()), yp);
			}
		}
		setAttr(company + "YpMap", ypMap);
		//计算盈利比
		
		
		
		
		
		
	}

	private void getTermList() {
		List<LotteryTerm> termList = termService.getTermByPage(1, 40).getList();
		setAttr("termList", termList);
	}

	public void setAttr(List<DcArrange> raceList) {
		Map<String, Integer> gameCountMap = new LinkedHashMap<String, Integer>();
		Map<String, String> termMap = new LinkedHashMap<String, String>();
		Map<String, Integer> rqCountMap = new LinkedHashMap<String, Integer>();
		String nowDateText = DateUtil.getDateFormat(new Date(), "MM月dd日");
		int rq = 0;
		int brq = 0;
		for (DcArrange race : raceList) {
			String gameName = race.getMatchName();
			Integer gameCount = gameCountMap.get(gameName);
			if (gameCount == null) {
				gameCount = 0;
			}
			if (race.getStatus() == 0) {// 只统计销售的赛事
				gameCount++;
			}
			gameCountMap.put(gameName, gameCount);

			Date showDate = DateUtil.formatDate(race.getMatchDate(), "yyyy-MM-dd");
			String dateText = DateUtil.getDateFormat(showDate, "MM月dd日");
			if (nowDateText.equals(dateText)) {
				dateText += "(今天)";
			}
			termMap.put(dateText, DateUtil.getDateFormat(showDate, "yyyyMMdd"));
			if (race.getStatus() == 0) {// 只统计销售的赛事
				if (race.getRqs() == 0) {
					brq++;
				} else {
					rq++;
				}
			}

		}
		rqCountMap.put("rq", rq);
		rqCountMap.put("brq", brq);
		setAttr("termCountMap", termMap);
		setAttr("gameCountMap", gameCountMap);
		setAttr("rqCountMap", rqCountMap);
		

	}

	private void getYlb(List<DcArrange> dcList,LotteryTerm term) {
//			Map<Integer, DcSpfSp> tzbMap = spService.getSpList(term);
//			
//			Map<Integer, DcBetInfo> infoMap=new HashMap<Integer, DcBetInfo>();
//			for(Integer tzbKey:tzbMap.keySet()){
//				LotteryDcSpfaward award=tzbMap.get(tzbKey);
//				DcBetInfo betInfo=new DcBetInfo();
//				betInfo.setWinBetRate(1/award.getHomeWinAward());
//				betInfo.setDrawBetRate(1/award.getDrawAward());
//				betInfo.setLoseBetRate(1/award.getGuestWinAward());
//				infoMap.put(tzbKey, betInfo);
//			}
//			//亚赔
//			Map<String, List<FootballYp>> footballYpMap = new HashMap<String, List<FootballYp>>();
//			CustomerContextHolder.setCustomerType(DataSourceMap.SLAVE);
//			List<FootballYp> ypList = footballDataService.getDcYpDateCompany(term);
//			for (String company : FootballYp.COMAPNY.values()) {
//				Map<String, FootballYp> ypMap = new HashMap<String, FootballYp>();
//				if(company.equals("huangguan") || company.equals("ribo")){
//					getRequest().setAttribute(company + "YpMap", ypMap);
//				}
//			}
//			for (FootballYp yp : ypList) {
//				if (yp!=null&&yp.getCompany() != null) {
//					Map<String, FootballYp> tempYpMap = (Map<String, FootballYp>) getRequest().getAttribute(
//							FootballYp.COMAPNY.get(yp.getCompany()) + "YpMap");
//					if (tempYpMap != null) {
//						tempYpMap.put(String.valueOf(yp.getMatchId()), yp);
//					}
//				}
//				if(footballYpMap.get(FootballYp.COMAPNY.get(yp.getCompany()))!=null){
//					footballYpMap.get(FootballYp.COMAPNY.get(yp.getCompany())).add(yp);
//				}else{
//					List<FootballYp> list = new ArrayList<FootballYp>();
//					list.add(yp);
//					footballYpMap.put(FootballYp.COMAPNY.get(yp.getCompany()), list);
//				}
//			}
//			Map<String, String> ypylbMap = FootballYp.getNewYlbJsonMapByWbwDcBetCompanyInfo(footballYpMap, infoMap, raceList);
//			getRequest().setAttribute("ypylbMap", ypylbMap);
//			//欧赔
//			CustomerContextHolder.setCustomerType(DataSourceMap.SLAVE);
//			List<FootballOp> opList = footballDataService.getDcOpDate(term);
//			for (String company : FootballOp.COMAPNY.values()) {
//				Map<String, FootballOp> opMap = new HashMap<String, FootballOp>();
//				if(company.equals("huangguan")||company.equals("avg")){
//					getRequest().setAttribute(company + "OpMap", opMap);
//				}
//			}
//			for (FootballOp op : opList) {
//				if (op!=null&&op.getCompany() != null) {
//					Map<String, FootballOp> tempOpMap = (Map<String, FootballOp>) getRequest().getAttribute(
//							FootballOp.COMAPNY.get(op.getCompany()) + "OpMap");
//					if (tempOpMap != null) {
//						tempOpMap.put(String.valueOf(op.getMatchId()), op);
//					}
//				}
//			}
//			//新欧赔盈利比
//			Map<String, String> opylbMap = new HashMap<String, String>();
//			for(DcArrange race : raceList){
//				L2:
//				for (String company : FootballYp.COMAPNY.values()) {
//					List<FootballYp> list = footballYpMap.get(company);
//					if(list != null){
//						for(FootballYp yp : list){
//							if(yp.getMatchId() != null && race.getOutid() != null && yp.getMatchId().equals(new Integer(race.getOutid()))){
//								opylbMap.putAll(getWinRate(ypylbMap, race, yp));
//								break L2;
//							}
//						}
//					}
//				}
//			}
//			getRequest().setAttribute("opylbMap", opylbMap);
//			//大小指数
//			CustomerContextHolder.setCustomerType(DataSourceMap.SLAVE);
//			Map<String, FootballDxp> dxpMap = footballDataService.getDxpData(term);
//			getRequest().setAttribute("dxpMap", dxpMap);
//			CustomerContextHolder.setCustomerType(DataSourceMap.SLAVE);
//			Map<String, LotteryDcJqsaward> jqsAwardMap = lotteryDcSpService.getJQSAward(term);
//			Map<String, String> dxpylbMap = FootballDxp.getNewYlbJsonMap(dxpMap, jqsAwardMap, raceList);
//			getRequest().setAttribute("dxpylbMap", dxpylbMap);
	}

	private void stopCount(List<DcArrange> dcList) {
		int cnt = 0;
		Map<String, Boolean> isShow = new HashMap<String, Boolean>();
		for (DcArrange dc : dcList) {
			if (dc.getStatus() != 1) {
				cnt++;
			} else {
				isShow.put(dc.getIntTime(), true);
			}
		}
		setAttr("stopCount", cnt);
		setAttr("isShowMap", isShow);
	}
}
