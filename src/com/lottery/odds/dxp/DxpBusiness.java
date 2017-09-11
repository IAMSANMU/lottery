package com.lottery.odds.dxp;

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
import com.lottery.common.model.DcDxp;
import com.lottery.common.utils.DateUtil;
import com.lottery.common.utils.HttpUtil;
import com.lottery.odds.OddsBusiness;

public class DxpBusiness {
	public static final String DXP_ODDS_URL = "http://odds.500.com/fenxi/daxiao-";
	private static Logger log = Logger.getLogger(DxpBusiness.class);

	public static String getRealHtml(DcArrange match, String oddId) throws Exception {
		String url = DXP_ODDS_URL + oddId;
		String html = OddsBusiness.getRealHtml(url, match.getHomeId(), match.getGuestId());
		return html;
	}

	/*
	 * 即时大小盘
	 */
	public static List<DcDxp> getNowDxp(String html) throws Exception {
		List<DcDxp> list = new ArrayList<DcDxp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim();
				String cllCompany = OddsBusiness.WBW2CLLCOMPANY.get(company);
				if (StringUtils.isNotEmpty(cllCompany)) {
					DcDxp dxp = new DcDxp();
					dxp.setCompany(cllCompany);

					Element bigTd = trEle.child(2);
					String changeStr = bigTd.attr("class").trim();
					int bigChange = 0;
					if (changeStr.equals("red_up")) {
						bigChange = 1;
					} else if (changeStr.equals("green_down")) {
						bigChange = -1;
					}

					dxp.setBig(Double.parseDouble(bigTd.text()));
					dxp.setBigChange(bigChange);
					Element pankouTd = trEle.child(3);

					int pkChange = 0;
					String pk = pankouTd.text();
					dxp.setPankou(pk);
					dxp.setPankouChange(pkChange);

					Element smallTd = trEle.child(2);
					changeStr = smallTd.attr("class").trim();
					int smallChange = 0;
					if (changeStr.equals("red_up")) {
						smallChange = 1;
					} else if (changeStr.equals("green_down")) {
						smallChange = -1;
					}
					dxp.setSmall(Double.parseDouble(smallTd.text()));
					dxp.setSmallChange(smallChange);
					String timeStr = trEle.child(5).text().trim();
					Date time = DateUtil.formatDate(DateUtil.getNowYear()+"-"+timeStr, "yyyy-MM-dd HH:mm");
					dxp.setTime(time);
					dxp.setCreateTime(new Date());
					dxp.setIsFirst(false);
					list.add(dxp);
				}
			}
		} catch (Exception e) {
			log.error("--------[dxp抓取]抓取dxp亚盘错误--------"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	/*
	 * 初盘
	 */
	public static List<DcDxp> getFirstDxp(String html) throws Exception {

		List<DcDxp> list = new ArrayList<DcDxp>();
		try {
			Document doc = Jsoup.parse(html);
			Elements trEles = doc.select("#datatb tr[id]");
			for (Element trEle : trEles) {
				String company = trEle.child(1).text().trim();
				String cllCompany = OddsBusiness.WBW2CLLCOMPANY.get(company);
				if (!StringUtils.isNotEmpty(cllCompany)) {
					DcDxp dxp = new DcDxp();
					dxp.setCompany(cllCompany);

					Element bigTd = trEle.child(6);
					String changeStr = bigTd.attr("class").trim();
					int bigChange = 0;

					dxp.setBig(Double.parseDouble(bigTd.text()));
					dxp.setBigChange(bigChange);
					Element pankouTd = trEle.child(7);

					int pkChange = 0;
					String pk = pankouTd.text();
					dxp.setPankou(pk);
					dxp.setPankouChange(pkChange);

					Element smallTd = trEle.child(8);
					int smallChange = 0;
					dxp.setSmall(Double.parseDouble(smallTd.text()));
					dxp.setSmallChange(smallChange);
					String timeStr = trEle.child(9).text().trim();
					Date time = DateUtil.formatDate(DateUtil.getNowYear()+"-"+timeStr, "yyyy-MM-dd HH:mm");
					dxp.setTime(time);
					dxp.setCreateTime(new Date());
					dxp.setIsFirst(false);
					list.add(dxp);
				}
			}
		} catch (Exception e) {
			log.error("--------[dxp抓取]抓取初盘错误--------"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	public static void main(String[] args) {
		DcArrange dc = new DcArrange().setHomeId(867).setGuestId(1775);
		try {
			String html = getRealHtml(dc, "698100");
			getNowDxp(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
