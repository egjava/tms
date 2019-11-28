package com.tms.bean;



public class ReportGeneration {
	
	private int therapistid=0;
	private String firstname="";
	private String lastname="";
	private String middlename="";
	private String address1="";
	private String address2="";
	private String city="";
	private int zipcode;
	private String state="";
	private String mobile="";
	private String ein="";
	private String license="";
	private String cfirstname="";
	private String clastname="";
	private String cmiddlename="";
	private String caddress1="";
	private String caddress2="";
	private String ccity="";
	private int czipcode;
	private String cstate="";
	private String diagnosiscode;
	private String activityDate;
	private int cptcode;
	private float netcharge;
	private float netwaiver;
	private float netpayment;
	private float totalBill;
	private int flagid;
	private String fromdate;
	private String todate;
	private int clientid=0;
	private int selClientid=0;
	
	
	private int nameid=0;
	private String clientname="";
	private String therapistname ="";
	private float payment;
	private float balance;
	private float priorBalance;
	private String activityname="";
	
	
	public float getNetwaiver() {
		return netwaiver;
	}
	public void setNetwaiver(float netwaiver) {
		this.netwaiver = netwaiver;
	}
	public float getNetpayment() {
		return netpayment;
	}
	public void setNetpayment(float netpayment) {
		this.netpayment = netpayment;
	}
	
	public String getActivityname() {
		return activityname;
	}
	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}
	public float getPayment() {
		return payment;
	}
	public void setPayment(float payment) {
		this.payment = payment;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public float getPriorBalance() {
		return priorBalance;
	}
	public void setPriorBalance(float priorBalance) {
		this.priorBalance = priorBalance;
	}
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	public int getSelClientid() {
		return selClientid;
	}
	public void setSelClientid(int selClientid) {
		this.selClientid = selClientid;
	}
	public String getTherapistname() {
		return therapistname;
	}
	public void setTherapistname(String therapistname) {
		this.therapistname = therapistname;
	}

	
	public int getNameid() {
		return nameid;
	}
	public void setNameid(int nameid) {
		this.nameid = nameid;
	}
	public int getFlagid() {
		return flagid;
	}
	public void setFlagid(int flagid) {
		this.flagid = flagid;
	}
	public String getFromdate() {
	
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	
	public float getTotalBill() {
		return totalBill;
	}
	public void setTotalBill(float totalBill) {
		this.totalBill = totalBill;
	}
	public int getTherapistid() {
		return therapistid;
	}
	public void setTherapistid(int therapistid) {
		this.therapistid = therapistid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getZipcode() {
		return zipcode;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEin() {
		return ein;
	}
	public void setEin(String ein) {
		this.ein = ein;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getCfirstname() {
		return cfirstname;
	}
	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}
	public String getClastname() {
		return clastname;
	}
	public void setClastname(String clastname) {
		this.clastname = clastname;
	}
	public String getCmiddlename() {
		return cmiddlename;
	}
	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename;
	}
	public String getCaddress1() {
		return caddress1;
	}
	public void setCaddress1(String caddress1) {
		this.caddress1 = caddress1;
	}
	public String getCaddress2() {
		return caddress2;
	}
	public void setCaddress2(String caddress2) {
		this.caddress2 = caddress2;
	}
	public String getCcity() {
		return ccity;
	}
	public void setCcity(String ccity) {
		this.ccity = ccity;
	}
	public int getCzipcode() {
		return czipcode;
	}
	public void setCzipcode(int czipcode) {
		this.czipcode = czipcode;
	}
	public String getCstate() {
		return cstate;
	}
	public void setCstate(String cstate) {
		this.cstate = cstate;
	}
	public String getDiagnosiscode() {
		return diagnosiscode;
	}
	public void setDiagnosiscode(String diagnosiscode) {
		this.diagnosiscode = diagnosiscode;
	}
	public String getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}
	public int getCptcode() {
		return cptcode;
	}
	public void setCptcode(int cptcode) {
		this.cptcode = cptcode;
	}
	public float getNetcharge() {
		return netcharge;
	}
	public void setNetcharge(float netcharge) {
		this.netcharge = netcharge;
	}
}
