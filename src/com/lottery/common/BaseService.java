package com.lottery.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.lottery.search.Operator;
import com.lottery.search.SearchModel;

public abstract class BaseService<T extends Model> {

	
	public abstract T dao();
	
	public abstract String tabName();

	public Page<T> getByPage(int pageIndex, int pageSize, String sort, String sortType,
			List<SearchModel> searchModels) {
		String sql = "from "+tabName()+" " + buildSqlByPage(sort, sortType, tabName(), searchModels);
		Page<T> list = dao().paginate(pageIndex, pageSize, "select * ", sql);
		return list;
	}

	public void update(T t) {
		t.update();
	}

	public void save(T t) {
		t.save();
	}

	public void logicDelete(String[] ids) {
		String sql = "update "+tabName()+" set isDel=1 where isDel=0 and id in (" + StrKit.join(ids, ",") + ") ";
		Db.update(sql);
	}

	public void restore(String[] ids) {
		String sql = "update "+tabName()+" set isDel=0 where isDel=1 and id in (" + StrKit.join(ids, ",") + ") ";
		Db.update(sql);
	}

	public T get(int id) {
		return (T)dao().findById(id);
	}

	protected String buildSqlByPage(String sort, String sortType, String alias, List<SearchModel> searchModels) {
		StringBuilder sql = new StringBuilder(" where 1=1");
		if (searchModels != null && searchModels.size() > 0) {
			for (SearchModel item : searchModels) {
				Operator op = Operator.index2Enum(item.getOperator());
				String value = item.getValue();
				if (op == Operator.Like) {
					value = "%" + value + "%";
				}
				sql.append(" and " + alias + "." + item.getColumn() + " " + op.getName() + " '" + value + "' ");
			}
		}
		if (StringUtils.isNotEmpty(sort)) {
			String desc = StringUtils.isEmpty(sortType) ? "asc" : (sortType.equals("desc") ? "desc" : "asc");
			sql.append(" order by " + alias + "." + sort + " " + desc);
		}
		return sql.toString();
	}

	public static void main(String[] args) {
		Operator op = Operator.index2Enum(2);
		System.out.println(op);
	}
}
