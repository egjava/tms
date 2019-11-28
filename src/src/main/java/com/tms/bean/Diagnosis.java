package com.tms.bean;

public class Diagnosis {
	private String diagcode ="";
	private String desc  = "";
	private int diagnosisid;
	
	public int getDiagnosisid() {
		return diagnosisid;
	}
	public void setDiagnosisid(int diagnosisid) {
		this.diagnosisid = diagnosisid;
	}
	public String getDiagcode() {
		return diagcode;
	}
	public void setDiagcode(String diagcode) {
		this.diagcode = diagcode;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	

}
