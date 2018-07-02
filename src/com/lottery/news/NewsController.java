package com.lottery.news;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.front.AbleInter;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.admin.news.NewsService;
import com.lottery.buy.BuyService;
import com.lottery.common.BaseController;
import com.lottery.common.model.DcArrange;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.NewsContext;
import com.lottery.common.utils.JsonResult;
import com.lottery.dc.DcService;

public class NewsController extends BaseController {

	private NewsService service = new NewsService();
	private BuyService buyService = Duang.duang(BuyService.class);
	private DcService dcService = new DcService();

	@Before({ LoginInter.class, AbleInter.class })
	public void index() {
		int pageIndex = getParaToInt(0, 1);
		int pageSize = 10;

		Page<NewsContext> list = service.getList(pageIndex, pageSize);
		setAttr("news", list);
		setAttr("rank", Rank());
		render("index.html");
	}

	private List<NewsContext> Rank() {
		return service.getList(1, 10).getList();
	}

	@Before({ LoginInter.class, AbleInter.class })
	public void info() {
		int id = getParaToInt(0);
		NewsContext entity = service.get(id);
		if (entity == null || !entity.getIsShow() || entity.getIsDel()) {
			renderText("文章不存在");
		} else {
			// 判断用户是否已购买
			LotUser loginUser = loginUser();
			// 判断用户是否已购买
			boolean hasBuy = buyService.HasbuyNews(id, loginUser.getId());
			setAttr("hasBuy", hasBuy);

			// 查询比赛开始时间
			DcArrange dc = dcService.get(entity.getTerm(), entity.getLineId());
			if (dc != null) {
				setAttr("endTime", dc.getMatchTime());
			}

			Integer view = entity.getViewCount();
			view = view == null ? 0 : view;
			entity.setViewCount(view + 1);

			service.update(entity);

			if (entity.getType() == 1) {
				redirect(entity.getLink());
			}
			setAttr("rank", Rank());
			setAttr("news", entity);
			render("info.html");
		}
	}

	/**
	 * 购买文章
	 */
	@Before({ LoginInter.class })
	public void buy() {
		int newsId = getParaToInt();
		LotUser loginUser = loginUser();
		JsonResult<NewsContext> json = new JsonResult<NewsContext>();
		try {
			boolean hasBuy = buyService.BuyNews(newsId, loginUser);
			if (hasBuy) {
				NewsContext news = service.get(newsId);
				if (news == null) {
					json.setMessage("文章不存在");
					json.setSuccess(false);
				} else {
					json.setSuccess(true);
					json.setMessage(news.getContext());
				}
			} else {
				json.setMessage("购买失败");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			json.setMessage("购买失败:" + e.getMessage());
			json.setSuccess(false);
		}
		renderJson(json.toJsonString());
	}

}
