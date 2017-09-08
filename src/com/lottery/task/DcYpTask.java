package com.lottery.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcYp;
import com.lottery.common.model.LotteryTerm;
import com.lottery.dc.DcService;
import com.lottery.term.TermService;
import com.lottery.yp.YpBusiness;
import com.lottery.yp.YpService;

/*
 * 抓取压盘信息
 */
public class DcYpTask implements Runnable {
	private DcService dcService=new DcService();
	private TermService termService=new TermService();
	private YpService ypService=new YpService();
	private static Logger log=Logger.getLogger(DcYpTask.class);
	public void run() {
		log.info("----------[yp抓取]抓取亚盘开始-------------");
		LotteryTerm term=termService.getCurrentTerm();
		List<DcArrange> dcList=dcService.getDcList(term);
		snacthYp(dcList);
		log.info("----------[yp抓取]抓取亚盘结束-------------");
	}
	public void snacthYp(List<DcArrange> matchList) {
		for (DcArrange match : matchList) {
			String oddId = match.getOddId();
			try {
				if (oddId != null) {
					//获取数据库亚盘
					List<DcYp> ypList = ypService.get(match.getId());
					Map<String, DcYp> dbFirstYpMap=new HashMap<String, DcYp>();
					Map<String, DcYp> dbNowYpMap=new HashMap<String, DcYp>();
					for(DcYp dbYp:ypList){
						if(dbYp.getIsFirst()){
							dbFirstYpMap.put(dbYp.getCompany(), dbYp);
						}else{
							dbNowYpMap.put(dbYp.getCompany(), dbYp);
						}
					}
					//抓取亚盘信息
					String html =YpBusiness.getRealHtml(match, oddId);
					List<DcYp> nowYpList = YpBusiness.GetNowYp(html);// 即时盘
					List<DcYp> firstYpList = YpBusiness.GetFirstYp(html);// 初盘
					Map<String, DcYp> firstYpMap=new HashMap<String, DcYp>();
					Map<String, DcYp> nowYpMap=new HashMap<String, DcYp>();
					
					for(DcYp nowYp:nowYpList){
						nowYpMap.put(nowYp.getCompany(), nowYp);
					}
					for(DcYp firstYp:firstYpList){
						firstYpMap.put(firstYp.getCompany(), firstYp);
					}
					//保存初盘
					for(String company:firstYpMap.keySet()){
						if(!dbFirstYpMap.containsKey(company)){
							DcYp yp=firstYpMap.get(company);
							yp.setMatchId(match.getId());
							yp.setTerm(match.getTerm());
							yp.setLineId(match.getLineId());
							yp.save();
						}
					}
					//保存即时盘
					for(String company:nowYpMap.keySet()){
						DcYp dbNowYp=dbNowYpMap.get(company);
						DcYp nowYp=nowYpMap.get(company);
						if(dbNowYp==null){
							nowYp.setMatchId(match.getId());
							nowYp.setTerm(match.getTerm());
							nowYp.setLineId(match.getLineId());
							nowYp.save();
						}else{
							//判断时间是否一致,不一致就保持
							if(!dbNowYp.getTime().equals(nowYp.getTime())){
								nowYp.save();
							}
						}
					}
				}
			}  catch (Exception e) {
				log.error("---------[yp抓取]抓取oddId="+oddId+"亚盘信息错误-------"+e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
