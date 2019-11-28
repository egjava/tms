package com.tms.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Flag {
	private String flagname="";
	private String oldflagname="";
	private String referralsource="";
	private int referralid=0;
	private int flagid = 0;
	
	public int getReferralid() {
		return referralid;
	}

	public void setReferralid(int referralid) {
		this.referralid = referralid;
	}

	public void setReferralsource(String referralsource) {
		this.referralsource = referralsource;
	}

	public int getFlagid() {
		return flagid;
	}

	public void setFlagid(int flagid) {
		this.flagid = flagid;
	}
	
	public String getReferralsource() {
		return referralsource;
	}

	public void setReferalsource(String refsource) {
		this.referralsource = referralsource;
	}

	public String getFlagname() {
		return flagname;
	}

	public void setFlagname(String flagname) {
		this.flagname = flagname;
	}
	public String getOldflagname() {
		return oldflagname;
	}

	public void setOldflagname(String oldflagname) {
		this.oldflagname = oldflagname;
	}

	

}
