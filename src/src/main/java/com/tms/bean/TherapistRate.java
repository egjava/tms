package com.tms.bean;

public class TherapistRate {
	
	private String[]  arrtRateper;
	private int[] arrtRateperVal;
	private int[] arrtActivityType;
	private float[] arrtRate;
	private int therapistid;
	
	public int getTherapistid() {
		return therapistid;
	}
	public void setTherapistid(int therapistid) {
		this.therapistid = therapistid;
	}
	public int[] getArrtActivityType() {
		return arrtActivityType;
	}
	public void setArrtActivityType(int[] arrtActivityType) {
		this.arrtActivityType = arrtActivityType;
	}
	public int[] getArrtRateperVal() {
		return arrtRateperVal;
	}
	public void setArrtRateperVal(int[] arrtRateperVal) {
		this.arrtRateperVal = arrtRateperVal;
	}
	public String[] getArrtRateper() {
		return arrtRateper;
	}
	public void setArrtRateper(String[] arrtRateper) {
		this.arrtRateper = arrtRateper;
	}
	public float[] getArrtRate() {
		return arrtRate;
	}
	public void setArrtRate(float[] arrtRate) {
		this.arrtRate = arrtRate;
	}
	
	

}
