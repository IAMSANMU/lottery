package com.lottery.admin.news.section;


import java.util.List;

import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.NewsSection;

public class SectionService extends BaseService<NewsSection> {

	private NewsSection dao=new NewsSection().dao();
	@Override
	public NewsSection dao() {
		return dao;
	}
	@Override
	public String tabName() {
		return TableMapping.me().getTable(NewsSection.class).getName();
	}

	
	public List<NewsSection> getAll(){
		String sql="select * from news_section where isDel=0";
		return dao.find(sql);
	}
	

	
}
