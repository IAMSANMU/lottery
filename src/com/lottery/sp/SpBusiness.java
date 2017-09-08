package com.lottery.sp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lottery.common.model.DcSpfSp;
import com.lottery.common.utils.HttpUtil;
public class SpBusiness {
	private static final String W500_SP_URL = "http://trade.500.com/bjdc//?expect=";
	private static final String OKOOO_SP_URL = "http://www.okooo.com/Upload/xml/danchang/WDL.xml";
	private static final String OKOOO_DC_URL = "http://www.okooo.com/danchang/";
	private static Logger log = Logger.getLogger(SpBusiness.class);

	private static String snatchOkCurrentTerm() {
		String term = "";
		try {
			String url = OKOOO_DC_URL;
			String html = HttpUtil.getUrl(url);
			// 解析html
			Document doc = Jsoup.parse(html);
			term = doc.select("#SelectLotteryNo").first().val();
		} catch (Exception e) {
			log.error("[sp抓取]===抓取澳客当前期错误===");
			e.printStackTrace();
		}
		return term;
	}

	public static List<DcSpfSp> snatchOkSp(String dcTerm) throws Exception {
		List<DcSpfSp> list = new ArrayList<DcSpfSp>();
		try {
			String url = OKOOO_SP_URL;
			String currentTerm = snatchOkCurrentTerm();
			if (!currentTerm.equals(dcTerm)) {
				log.error("[sp抓取]===澳客北单当前期与本地不一致===");
			} else {
				String html = HttpUtil.getUrl(url);
				// 解析xml
				Document doc = Jsoup.parse(html);
				Elements rows = doc.select("w");
				for (Element ele : rows) {
					DcSpfSp sp = new DcSpfSp();
					sp.setLineId(ele.attr("n"));
					sp.setTerm(currentTerm);
					sp.setHomeSp(new BigDecimal(ele.attr("c1")));
					sp.setDrawSp(new BigDecimal(ele.attr("c3")));
					sp.setGuestSp(new BigDecimal(ele.attr("c5")));
					sp.setLastUpDate(new Date());
					list.add(sp);
				}
			}
		} catch (Exception e) {
			log.error("[sp抓取]===sp抓取出现错误===");
			e.printStackTrace();
			throw e;
		}
		return list;

	}

	public static void main(String[] args) {
	}
}
