package com.lottery.admin.sys;

import java.util.Date;

import com.jfinal.plugin.activerecord.TableMapping;
import com.lottery.common.BaseService;
import com.lottery.common.model.SysError;

public class SysErrorService extends BaseService<SysError> {

	private SysError dao = new SysError().dao();

	public void add(String name,String url,String remark){
		SysError model=new SysError();
		model.setCreateTime(new Date());
		model.setUrl(url);
		model.setName(name);
		model.setRemark(remark);
		model.setIsHandle(false);
		model.save();
	}
	
	@Override
	public SysError dao() {
		return dao;
	}

	@Override
	public String tabName() {
		return TableMapping.me().getTable(SysError.class).getName();
	}

}
