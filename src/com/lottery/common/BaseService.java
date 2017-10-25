package com.lottery.common; 

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lottery.search.Operator;
import com.lottery.search.SearchModel;

public class BaseService {
	public String buildSqlByPage(String tableName,String sort,String sortType,List<SearchModel> searchModels){
		StringBuilder sql=new StringBuilder(" from "+tableName+" where 1=1");
		if(searchModels !=null && searchModels.size()>0){
			for (SearchModel item : searchModels) {
				Operator op=Operator.index2Enum(item.getOperator());
				String value=item.getValue();
				if(op==Operator.Like){
					value="%"+value+"%";
				}
				sql.append(" and "+item.getColumn()+" "+ op.getName()+" '"+ value+"' ");
			}
		}
		if(StringUtils.isNotEmpty(sort)){
			String desc=StringUtils.isEmpty(sortType)?"asc":(sortType.equals("desc")?"desc":"asc");
			sql.append(" order by "+sort+ " "+desc);
		}
		return sql.toString();
	}
	public static void main(String[] args) {
		Operator op=Operator.index2Enum(2);
		System.out.println(op);
	}
}
