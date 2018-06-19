package com.lottery.common.model;

import java.util.Date;

import com.lottery.common.model.base.BaseLotUser;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class LotUser extends BaseLotUser<LotUser> {
	
	private Boolean ableLook;

	public Boolean getAbleLook() {
		ableLook=false;
		if(getStartTime()!=null&&getEndTime()!=null){
			Date now=new Date();
			ableLook=now.after(getStartTime())&&now.before(getEndTime());
		}
		return ableLook ;
	}
}
