package com.lottery.dc;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcDxp;
import com.lottery.common.model.DcOp;
import com.lottery.common.model.DcSpfSp;
import com.lottery.common.model.DcYp;
import com.lottery.common.model.LotteryTerm;
import com.lottery.common.utils.DateUtil;
import com.lottery.odds.OddsBusiness;
import com.lottery.odds.dxp.DxpService;
import com.lottery.odds.op.OpService;
import com.lottery.odds.sp.SpService;
import com.lottery.odds.yp.YpService;
import com.lottery.term.TermService;

public class DcController extends Controller {
	DcService dcService = new DcService();
	TermService termService = new TermService();
	YpService ypService = new YpService();
	OpService opService = new OpService();
	DxpService dxpService = new DxpService();
	SpService spService = new SpService();
	final String OP_TAG="O_";
	final String YP_TAG="Y_";
	final String DXP_TAG="D_";
	final String SHOWTIME_TAG="T_";
	public void index() {
		String term = getAttr("term");
		getInfo(term);
		render("index.html");
	}

	private void getInfo(String term) {
		boolean isBack=false;
		LotteryTerm currentTerm = null;
		LotteryTerm dbTerm=termService.getCurrentTerm();
		if (StringUtils.isNotEmpty(term)) {
			currentTerm = termService.getTerm(term);
			isBack=!dbTerm.getTerm().equals(currentTerm);
		} else {
			currentTerm = termService.getCurrentTerm();
		}
		setAttr("isBack",isBack);
		// 获取对阵数据
		setAttr("currentTerm", currentTerm);
		setAttr("nowTerm",dbTerm);

		List<DcArrange> dcList = dcService.getDcList(currentTerm);
		Map<Integer,DcArrange> dcMap=new HashMap<Integer, DcArrange>();
		if(dcList!=null){
			for (DcArrange dc : dcList) {
				dcMap.put(dc.getId(), dc);
			}
		}
		
		setAttr("dcList", dcList);
		// 计算停售场次
		stopCount(dcList);
		// 获取期数列表
		getTermList();
		// 获取sp数据
		Map<Integer, DcSpfSp> spMap=getSp(currentTerm);
		// 获取yp数据
		getYp(currentTerm,dcMap,spMap);
		//欧赔
		getOp(currentTerm);
		//大小盘
		getDxp(currentTerm);

	}
	private void getOp(LotteryTerm term){
		List<DcOp> opList=opService.getList(term.getTerm(), "avg");
		Map<String,DcOp> opMap=new HashMap<String, DcOp>();
		if(opList!=null){
			for (DcOp dcOp : opList) {
				opMap.put(OP_TAG+dcOp.getMatchId(), dcOp);
			}
		}
		
		setAttr("opMap",opMap);
	}
	private void getDxp(LotteryTerm term){
		String company = OddsBusiness.COMAPNY.get("皇冠");
		List<DcDxp> dxpList=dxpService.getList(term.getTerm(), company);
		
		Map<String,DcDxp> dxpMap=new HashMap<String, DcDxp>();
		if(dxpMap!=null){
			for (DcDxp dcDxp : dxpList) {
				dxpMap.put(DXP_TAG+dcDxp.getMatchId(), dcDxp);
			}
		}
		setAttr("dxpMap", dxpMap);
	}
	private Map<Integer, DcSpfSp> getSp(LotteryTerm term) {
		List<DcSpfSp> spList = spService.getSpList(term.getTerm());
		Map<Integer,DcSpfSp> spMap=new HashMap<Integer, DcSpfSp>();
		for (DcSpfSp dcSpfSp : spList) {
			spMap.put(dcSpfSp.getMatchId(), dcSpfSp);
		}
		setAttr("spMap",spMap);
		return spMap;
	}

	private void getYp(LotteryTerm term, Map<Integer,DcArrange> dcMap,Map<Integer,DcSpfSp> spMap) {
		String company = OddsBusiness.COMAPNY.get("皇冠");
		List<DcYp> ypList = ypService.getList(term.getTerm(), company);
		Map<String, DcYp> ypMap = new HashMap<String, DcYp>();
		for (DcYp yp : ypList) {
			if (yp != null && yp.getCompany() != null) {
				int matchId=yp.getMatchId();
				DcArrange dcArrange= dcMap.get(matchId);
				DcSpfSp dcSp=spMap.get(matchId);
				ypMap.put(YP_TAG+matchId, yp);
				yp.getNewylb(dcSp, dcArrange.getRqs());
			}
		}
		setAttr("ypMap", ypMap);
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


	private void stopCount(List<DcArrange> dcList) {
		int cnt = 0;
		Map<String, Boolean> isShow = new HashMap<String, Boolean>();
		for (DcArrange dc : dcList) {
			if (dc.getStatus() != 1) {
				cnt++;
			} else {
				isShow.put(SHOWTIME_TAG+dc.getIntTime(), true);
			}
		}
		setAttr("stopCount", cnt);
		setAttr("isShowMap", isShow);
	}
}
