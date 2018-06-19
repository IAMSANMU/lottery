package com.lottery.admin.news;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.NewsContext;
import com.lottery.search.SearchModel;

public class NewsService extends BaseService<NewsContext> {
	private NewsContext dao=new NewsContext().dao();
	
	
	public Page<NewsContext> getList(int pageIndex,int pageSize){
		
		String sql = "from news_context where isDel=0 and isShow=1 order by isTop desc,pushTime desc ";
		Page<NewsContext> list=dao.paginate(pageIndex, pageSize, "select * ",sql.toString());
		return list;
	}
	
	
	
	@Override
	public Page<NewsContext> getByPage(int pageIndex, int pageSize, String sort, String sortType,
			List<SearchModel> searchModels){
		StringBuilder sql= new StringBuilder(" from news_context l inner join news_section u on l.sectionId=u.id");
		sql.append(super.buildSqlByPage(sort, sortType, "l", searchModels));
		
		Page<NewsContext> list=dao.paginate(pageIndex, pageSize, "select l.*,u.name ",sql.toString());
		return list;
	}
	
	@Override
	public NewsContext dao() {
		return dao;
	}
	@Override
	public String tabName() {
		return TableMapping.me().getTable(NewsContext.class).getName();
	}
	
}
