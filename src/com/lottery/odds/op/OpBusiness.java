package com.lottery.odds.op;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lottery.common.model.DcArrange;
import com.lottery.common.model.DcOp;
import com.lottery.common.utils.DateUtil;
import com.lottery.common.utils.HttpUtil;
import com.lottery.odds.OddsBusiness;

public class OpBusiness {
	public static final String OP_ODDS_URL = "http://odds.500.com/fenxi/ouzhi-";
	private static Logger log = Logger.getLogger(OpBusiness.class);

	public static String getRealHtml(DcArrange match, String oddId) throws Exception {
		String url = OP_ODDS_URL + oddId;
		String html = OddsBusiness.getRealHtml(url, match.getHomeId(), match.getGuestId());
		return html;
	}

	/*
	 * 即时平均欧赔
	 */
	public static List<DcOp> getOp(String html) throws Exception {
		List<DcOp> list = new ArrayList<DcOp>();
		try {
			Document doc = Jsoup.parse(html);
			// 初盘欧赔
			DcOp firstOp = new DcOp();

			Double home = Double.parseDouble(doc.select("#avwinc1").first().text());
			Double draw = Double.parseDouble(doc.select("#avdrawc1").first().text());
			Double guest = Double.parseDouble(doc.select("#avlostc1").first().text());
			firstOp.setHome(home);
			firstOp.setHomeChange(0);
			firstOp.setDraw(draw);
			firstOp.setDrawChange(0);
			firstOp.setGuest(guest);
			firstOp.setGuestChange(0);
			firstOp.setCompany("avg");
			firstOp.setCreateTime(new Date());
			firstOp.setTime(firstOp.getCreateTime());
			firstOp.setIsFirst(true);

			DcOp nowOp=new DcOp();
			// 即时欧赔
			home = Double.parseDouble(doc.select("#avwinj1").first().text());
			draw = Double.parseDouble(doc.select("#avdrawj1").first().text());
			guest = Double.parseDouble(doc.select("#avlostj1").first().text());
			
			nowOp.setHome(home);
			nowOp.setDraw(draw);
			nowOp.setGuest(guest);
			nowOp.setCompany("avg");
			nowOp.setCreateTime(new Date());
			nowOp.setTime(nowOp.getCreateTime());
			nowOp.setIsFirst(false);
			
			list.add(firstOp);
			list.add(nowOp);

		} catch (Exception e) {
			log.error("--------[op抓取]抓取OP错误--------" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	

	public static void main(String[] args) {
		DcArrange dc = new DcArrange().setHomeId(867).setGuestId(1775);
		try {
			String html = getRealHtml(dc, "662467");
			List<DcOp> list=getOp(html);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
