package com.tms.bean;


public class Client {
	private String firstname="";
	private String lastname="";
	private String middlename="";
	private String sex;
	private String address1="";
	private String address2="";
	private String city="";
	private int zip;
	private String state="";
	private String workphone="";
	private String homephone="";
	private String mobile="";
	private String activitytype;
	private int cpt;
	private String dob;
	private String dov;
	private String activitydate;
	
	private int flagid;
	private String stheraphy;
	private float payment;
	private int[] diagnosiscode;
	private int therapistid;
	private int referralid;
	private String cname;
	private int clientid;
	private float[] waiver;
	private float[] netch;
	private String[]  arrtRateper;
	private int[] arrtRateperVal;
	private int[] arrtActivityType;
	private float[] arrtRate;
	private int nameid;

	
	private int[] activityid;
	private int acttypeid;
	private float camount;
	private String rateper;
	private int rateperval;
	private float newactwaiver;
	private float newactnetcharge;
	private String paymentdate;
	private int addressid;
	private int contactid;
	private int paymentid;
	
	
	public String getActivitydate() {
		return activitydate;
	}
	public void setActivitydate(String activitydate) {
		this.activitydate = activitydate;
	}
	

	public String getDov() {
		return dov;
	}
	public void setDov(String dov) {
		this.dov = dov;
	}
	public String[] getArrtRateper() {
		return arrtRateper;
	}
	public void setArrtRateper(String[] arrtRateper) {
		this.arrtRateper = arrtRateper;
	}
	public int[] getArrtRateperVal() {
		return arrtRateperVal;
	}
	public void setArrtRateperVal(int[] arrtRateperVal) {
		this.arrtRateperVal = arrtRateperVal;
	}
	public int[] getArrtActivityType() {
		return arrtActivityType;
	}
	public void setArrtActivityType(int[] arrtActivityType) {
		this.arrtActivityType = arrtActivityType;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public float[] getArrtRate() {
		return arrtRate;
	}
	public void setArrtRate(float[] arrtRate) {
		this.arrtRate = arrtRate;
	}
	
	public int getAddressid() {
		return addressid;
	}
	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}
	public int getContactid() {
		return contactid;
	}
	public void setContactid(int contactid) {
		this.contactid = contactid;
	}
	public int getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(int paymentid) {
		this.paymentid = paymentid;
	}
	public String getPaymentdate() {
		return paymentdate;
	}
	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}
	public float getNewactwaiver() {
		return newactwaiver;
	}
	public void setNewactwaiver(float newactwaiver) {
		this.newactwaiver = newactwaiver;
	}
	public float getNewactnetcharge() {
		return newactnetcharge;
	}
	public void setNewactnetcharge(float newactnetcharge) {
		this.newactnetcharge = newactnetcharge;
	}
	
	
	public int getActtypeid() {
		return acttypeid;
	}
	public void setActtypeid(int acttypeid) {
		this.acttypeid = acttypeid;
	}
	
	public float getCamount() {
		return camount;
	}
	public void setCamount(float camount) {
		this.camount = camount;
	}
	public String getRateper() {
		return rateper;
	}
	public void setRateper(String rateper) {
		this.rateper = rateper;
	}
	public int getRateperval() {
		return rateperval;
	}
	public void setRateperval(int rateperval) {
		this.rateperval = rateperval;
	}
	
	public int getNameid() {
		return nameid;
	}
	public void setNameid(int nameid) {
		this.nameid = nameid;
	}
	
	
	public int[] getActivityid() {
		return activityid;
	}
	public void setActivityid(int[] activityid) {
		this.activityid = activityid;
	}
	public float[] getWaiver() {
		return waiver;
	}
	public float[] getNetch() {
		return netch;
	}
	public void setWaiver(float[] waiver) {
		this.waiver = waiver;
	}
	public void setNetch(float[] netch) {
		this.netch = netch;
	}
	
	
	
	
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
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
	public int[] getDiagnosiscode() {
		return diagnosiscode;
	}
	public void setDiagnosiscode(int[] diagnosiscode) {
		this.diagnosiscode = diagnosiscode;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getWorkphone() {
		return workphone;
	}
	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}
	public String getHomephone() {
		return homephone;
	}
	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getActivitytype() {
		return activitytype;
	}
	public void setActivitytype(String activitytype) {
		this.activitytype = activitytype;
	}
	public int getCpt() {
		return cpt;
	}
	public void setCpt(int cpt) {
		this.cpt = cpt;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public int getFlagid() {
		return flagid;
	}
	public void setFlagid(int flagid) {
		this.flagid = flagid;
	}
	public String getStheraphy() {
		return stheraphy;
	}
	public void setStheraphy(String stheraphy) {
		this.stheraphy = stheraphy;
	}
	public float getPayment() {
		return payment;
	}
	public void setPayment(float payment) {
		this.payment = payment;
	}
	public int getTherapistid() {
		return therapistid;
	}
	public void setTherapistid(int therapistid) {
		this.therapistid = therapistid;
	}
	public int getReferralid() {
		return referralid;
	}
	public void setReferralid(int referralid) {
		this.referralid = referralid;
	}
	
	

}
