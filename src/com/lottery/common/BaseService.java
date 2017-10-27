package com.lottery.common; 

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lottery.search.Operator;
import com.lottery.search.SearchModel;

public class BaseService {
	protected String buildSqlByPage(String sort,String sortType,String alias,List<SearchModel> searchModels){
		StringBuilder sql=new StringBuilder(" where 1=1");
		if(searchModels !=null && searchModels.size()>0){
			for (SearchModel item : searchModels) {
				Operator op=Operator.index2Enum(item.getOperator());
				String value=item.getValue();
				if(op==Operator.Like){
					value="%"+value+"%";
				}
				sql.append(" and "+alias+"."+item.getColumn()+" "+ op.getName()+" '"+ value+"' ");
			}
		}
		if(StringUtils.isNotEmpty(sort)){
			String desc=StringUtils.isEmpty(sortType)?"asc":(sortType.equals("desc")?"desc":"asc");
			sql.append(" order by "+alias+"."+sort+ " "+desc);
		}
		return sql.toString();
	}
	public static void main(String[] args) {
		Operator op=Operator.index2Enum(2);
		System.out.println(op);
	}
}
