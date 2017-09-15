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
import com.lottery.odds.dxp.DxpService;
import com.lottery.odds.op.OpService;
import com.lottery.odds.sp.SpService;
import com.lottery.odds.yp.YpService;
import com.lottery.pojo.MatchInfo;
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
	final String SP_TAG="S_";
	public void index() {
		String term = getPara("term");
		getInfo(term);
		render("index.html");
	}

	//获取亚盘指数
	public void ypOdd(){
		String matchId=getPara("matchId");
		if(StringUtils.isNotEmpty(matchId)){
			int mid=Integer.parseInt(matchId);
			
			
			//获取对阵让球信息
			
			DcArrange dcInfo=dcService.getById(mid);
			//获取sp 计算赢利比
			DcSpfSp dcSp= spService.getCurrentSp(mid);
			
			String company = "皇冠";
			List<DcYp> list=ypService.getHistoryList(mid, company);
			for (DcYp dcYp : list) {
				dcYp.getNewylb(dcSp, dcInfo.getRqs());
			}
			setAttr("list",list);
			render("ypData.html");
		}else{
			renderText("参数错误");
		}
		
		
		
		
	}
	
	
	public void dxpOdd(){
		String matchId=getPara("matchId");
		if(StringUtils.isNotEmpty(matchId)){
			int mid=Integer.parseInt(matchId);
			//获取对阵让球信息
			DcArrange dcInfo=dcService.getById(mid);
			
			String company = "皇冠";
			List<DcDxp> list=dxpService.getHistoryList(mid, company);
//			for (DcYp dcYp : list) {
//				dcYp.getNewylb(dcSp, dcInfo.getRqs());
//			}
			setAttr("list",list);
			render("ypData.html");
		}else{
			renderText("参数错误");
		}
		
	}
	
	public void opOdd(){
		
		
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
		
		setAttr("raceList", dcList);
		// 计算停售场次
		stopCount(dcList);
		// 获取期数列表
		getTermList();
		// 获取sp数据
		Map<String, DcSpfSp> spMap=getSp(currentTerm);
		// 获取yp数据
		getYp(currentTerm,dcMap,spMap);
		//欧赔
		getOp(currentTerm);
		//大小盘
		getDxp(currentTerm);
		
		//筛选信息
		getFilte(dcList);

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
		String company = "皇冠";
		List<DcDxp> dxpList=dxpService.getList(term.getTerm(), company);
		
		Map<String,DcDxp> dxpMap=new HashMap<String, DcDxp>();
		if(dxpMap!=null){
			for (DcDxp dcDxp : dxpList) {
				dxpMap.put(DXP_TAG+dcDxp.getMatchId(), dcDxp);
			}
		}
		setAttr("dxpMap", dxpMap);
	}
	private Map<String, DcSpfSp> getSp(LotteryTerm term) {
		List<DcSpfSp> spList = spService.getSpList(term.getTerm());
		Map<String,DcSpfSp> spMap=new HashMap<String, DcSpfSp>();
		for (DcSpfSp dcSpfSp : spList) {
			spMap.put(SP_TAG+dcSpfSp.getMatchId(), dcSpfSp);
		}
		setAttr("spMap",spMap);
		return spMap;
	}

	private void getYp(LotteryTerm term, Map<Integer,DcArrange> dcMap,Map<String,DcSpfSp> spMap) {
		String company = "皇冠";
		List<DcYp> ypList = ypService.getList(term.getTerm(), company);
		Map<String, DcYp> ypMap = new HashMap<String, DcYp>();
		for (DcYp yp : ypList) {
			if (yp != null && yp.getCompany() != null) {
				int matchId=yp.getMatchId();
				DcArrange dcArrange= dcMap.get(matchId);
				DcSpfSp dcSp=spMap.get(SP_TAG+matchId);
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

	public void getFilte(List<DcArrange> raceList) {
		Map<String, MatchInfo> matchInfoMap = new LinkedHashMap<String, MatchInfo>();
		Map<String, String> termMap = new LinkedHashMap<String, String>();
		Map<String, Integer> rqCountMap = new LinkedHashMap<String, Integer>();
		String nowDateText = DateUtil.getDateFormat(new Date(), "MM月dd日");
		int rq = 0;
		int brq = 0;
		for (DcArrange race : raceList) {
			String gameName = race.getMatchName();
			MatchInfo info=matchInfoMap.get(gameName);
			if(info==null){
				info=new MatchInfo();
				info.setMatchColor(race.getMatchColor());
				info.setMatchName(gameName);
			}
			int gameCnt=info.getMatchCount();
			if (race.getStatus() == 0) {// 只统计销售的赛事
				gameCnt++;
			}
			info.setMatchCount(gameCnt);
			matchInfoMap.put(gameName,info );

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
		setAttr("gameCountMap", matchInfoMap);
		setAttr("rqCountMap", rqCountMap);

	}


	private void stopCount(List<DcArrange> dcList) {
		int cnt = 0;
		Map<String, Boolean> isShow = new HashMap<String, Boolean>();
		for (DcArrange dc : dcList) {
			if (dc.getStatus() != 0) {
				cnt++;
			} else {
				isShow.put(SHOWTIME_TAG+dc.getIntTime(), true);
			}
		}
		setAttr("stopCount", cnt);
		setAttr("isShowMap", isShow);
	}
}
