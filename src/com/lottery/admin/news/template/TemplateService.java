package com.lottery.admin.news.template;


import java.util.List;

import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.NewsTemplate;

public class TemplateService extends BaseService<NewsTemplate> {

	private NewsTemplate dao=new NewsTemplate().dao();
	@Override
	public NewsTemplate dao() {
		return dao;
	}
	@Override
	public String tabName() {
		return TableMapping.me().getTable(NewsTemplate.class).getName();
	}

	public List<NewsTemplate> getAll(){
		String sql="select * from news_template where isDel=0";
		return dao.find(sql);
	}
}
