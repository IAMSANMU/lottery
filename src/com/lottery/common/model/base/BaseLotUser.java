package com.lottery.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseLotUser<M extends BaseLotUser<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer Id) {
		set("Id", Id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("Id");
	}

	public M setAccount(java.lang.String account) {
		set("account", account);
		return (M)this;
	}

	public java.lang.String getAccount() {
		return getStr("account");
	}

	public M setPwd(java.lang.String pwd) {
		set("pwd", pwd);
		return (M)this;
	}

	public java.lang.String getPwd() {
		return getStr("pwd");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setTel(java.lang.String tel) {
		set("tel", tel);
		return (M)this;
	}

	public java.lang.String getTel() {
		return getStr("tel");
	}

	public M setIsDel(java.lang.Boolean isDel) {
		set("isDel", isDel);
		return (M)this;
	}

	public java.lang.Boolean getIsDel() {
		return get("isDel");
	}

	public M setEmail(java.lang.String email) {
		set("email", email);
		return (M)this;
	}

	public java.lang.String getEmail() {
		return getStr("email");
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

	public M setIsStop(java.lang.Boolean isStop) {
		set("isStop", isStop);
		return (M)this;
	}

	public java.lang.Boolean getIsStop() {
		return get("isStop");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

}
