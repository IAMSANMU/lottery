package com.lottery.admin;

import com.lottery.common.model.LotAdmin;

public class AdminService {
	private static final LotAdmin dao = new LotAdmin().dao();
	
	public LotAdmin login(String account,String pwd){
		String sql="select * from lot_admin where account=? and pwd=? ";
		return dao.findFirst(sql,account,pwd);
	}
	
	public LotAdmin get(int id){
		return dao.findById(id);
	}
	public void update(LotAdmin admin){
		admin.update();
	}
	
	
	
	
	
	
}
