package com.tms.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {
	
	public String convertStringToDate(Date indate)
	{
	   String dateString = null;
	   SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
	   
	   try{
		dateString = sdfr.format( indate );
	   }catch (Exception ex ){
		ex.printStackTrace();
	   }
	 
	   return dateString;
	}
	
	public Date convertDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;

		try {
			date = (java.util.Date) formatter.parse(dateInString);
			
		

		} catch (ParseException e) {
			e.printStackTrace();
		}
		  java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

}
