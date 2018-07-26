package com.lottery.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseLotteryTerm<M extends BaseLotteryTerm<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setTerm(java.lang.String term) {
		set("term", term);
		return (M)this;
	}

	public java.lang.String getTerm() {
		return getStr("term");
	}

	public M setStartTime(java.util.Date startTime) {
		set("startTime", startTime);
		return (M)this;
	}

	public java.util.Date getStartTime() {
		return get("startTime");
	}

	public M setEndTime(java.util.Date endTime) {
		set("endTime", endTime);
		return (M)this;
	}

	public java.util.Date getEndTime() {
		return get("endTime");
	}

	public M setStatus(java.lang.Integer status) {
		set("status", status);
		return (M)this;
	}

	public java.lang.Integer getStatus() {
		return getInt("status");
	}

	public M setIsCurrent(java.lang.Integer isCurrent) {
		set("isCurrent", isCurrent);
		return (M)this;
	}

	public java.lang.Integer getIsCurrent() {
		return getInt("isCurrent");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

}
