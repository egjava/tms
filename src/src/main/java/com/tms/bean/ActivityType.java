package com.tms.bean;

public class ActivityType {
	private int typeid;
	private String activitytype="";
	private float rate;
	private int cptcode;
	private String rateper;
	private String desc="";
	
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	
	public String getActivitytype() {
		return activitytype;
	}
	public void setActivitytype(String activitytype) {
		this.activitytype = activitytype;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public int getCptcode() {
		return cptcode;
	}
	public void setCptcode(int cptcode) {
		this.cptcode = cptcode;
	}
	public String getRateper() {
		return rateper;
	}
	public void setRateper(String rateper) {
		this.rateper = rateper;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}
