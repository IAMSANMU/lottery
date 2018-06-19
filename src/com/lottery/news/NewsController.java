package com.lottery.news;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.Interceptor.front.AbleInter;
import com.lottery.Interceptor.front.LoginInter;
import com.lottery.admin.news.NewsService;
import com.lottery.common.BaseController;
import com.lottery.common.model.NewsContext;

public class NewsController extends BaseController {

	private NewsService service = new NewsService();

	@Before({ LoginInter.class, AbleInter.class })
	public void index() {
		int pageIndex = getParaToInt(0, 1);
		int pageSize = 10;

		Page<NewsContext> list = service.getList(pageIndex, pageSize);
		setAttr("news", list);
		setAttr("rank",Rank());
		render("index.html");
	}
	private List<NewsContext> Rank(){
		return service.getList(1, 10).getList();
	}
	@Before({ LoginInter.class, AbleInter.class })
	public void info() {
		int id = getParaToInt(0);
		NewsContext entity = service.get(id);
		if (entity == null || !entity.getIsShow() || entity.getIsDel()) {
			renderText("文章不存在");
		}else{
			Integer view=entity.getViewCount();
			view=view==null?0:view;
			entity.setViewCount(view + 1);

			service.update(entity);

			if (entity.getType() == 1) {
				redirect(entity.getLink());
			}
			setAttr("rank",Rank());
			setAttr("news", entity);
			render("info.html");
		}
		
	}

}
