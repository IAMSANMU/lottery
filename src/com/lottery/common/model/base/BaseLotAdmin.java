package com.lottery.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseLotAdmin<M extends BaseLotAdmin<M>> extends Model<M> implements IBean {

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

	public M setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public M setIsDel(java.lang.Boolean isDel) {
		set("isDel", isDel);
		return (M)this;
	}

	public java.lang.Boolean getIsDel() {
		return get("isDel");
	}

}
