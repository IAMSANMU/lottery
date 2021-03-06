package com.lottery.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDcYp<M extends BaseDcYp<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setMatchId(java.lang.Integer matchId) {
		set("matchId", matchId);
		return (M)this;
	}

	public java.lang.Integer getMatchId() {
		return getInt("matchId");
	}

	public M setHome(java.lang.Double home) {
		set("home", home);
		return (M)this;
	}

	public java.lang.Double getHome() {
		return getDouble("home");
	}

	public M setHomeChange(java.lang.Integer homeChange) {
		set("homeChange", homeChange);
		return (M)this;
	}

	public java.lang.Integer getHomeChange() {
		return getInt("homeChange");
	}

	public M setPankou(java.lang.String pankou) {
		set("pankou", pankou);
		return (M)this;
	}

	public java.lang.String getPankou() {
		return getStr("pankou");
	}

	public M setPankouChange(java.lang.Integer pankouChange) {
		set("pankouChange", pankouChange);
		return (M)this;
	}

	public java.lang.Integer getPankouChange() {
		return getInt("pankouChange");
	}

	public M setGuest(java.lang.Double guest) {
		set("guest", guest);
		return (M)this;
	}

	public java.lang.Double getGuest() {
		return getDouble("guest");
	}

	public M setGuestChange(java.lang.Integer guestChange) {
		set("guestChange", guestChange);
		return (M)this;
	}

	public java.lang.Integer getGuestChange() {
		return getInt("guestChange");
	}

	public M setIsFirst(java.lang.Boolean isFirst) {
		set("isFirst", isFirst);
		return (M)this;
	}

	public java.lang.Boolean getIsFirst() {
		return get("isFirst");
	}

	public M setTime(java.util.Date time) {
		set("time", time);
		return (M)this;
	}

	public java.util.Date getTime() {
		return get("time");
	}

	public M setNumPankou(java.lang.Double numPankou) {
		set("numPankou", numPankou);
		return (M)this;
	}

	public java.lang.Double getNumPankou() {
		return getDouble("numPankou");
	}

	public M setCompany(java.lang.String company) {
		set("company", company);
		return (M)this;
	}

	public java.lang.String getCompany() {
		return getStr("company");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public M setTerm(java.lang.String term) {
		set("term", term);
		return (M)this;
	}

	public java.lang.String getTerm() {
		return getStr("term");
	}

	public M setLineId(java.lang.String lineId) {
		set("lineId", lineId);
		return (M)this;
	}

	public java.lang.String getLineId() {
		return getStr("lineId");
	}

}
