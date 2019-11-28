package com.tms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.tms.bean.Flag;
import com.tms.bean.Report;
import com.tms.database.ConnectionHelper;
import com.tms.util.DateUtil;
import com.tms.util.Queries;



public class Reportdao {
	final static Logger logger = Logger.getLogger(Reportdao.class);
	DateUtil dateUtil = new DateUtil();
	
	public List<Report> fetchReportWTC(Report report)  {
		List<Report> reportVal = new ArrayList<Report>();
		List<Date> activityDate = new ArrayList<Date>();
		List<Integer> priorActivityID = new ArrayList<Integer>();
		List<Integer> activityID = new ArrayList<Integer>();
		List<Date> paymentDate = new ArrayList<Date>();
		List<Integer> payActivityid = new ArrayList<Integer>();
		List<String> payActivityname = new ArrayList<String>();
		List<Float> fullPayment = new ArrayList<Float>();
		List<Integer> payonlyactivityid = new ArrayList<Integer>();
		List<Date> payonlypaymentdate = new ArrayList<Date>();
		List<Float> payonlyamount = new ArrayList<Float>();
		DecimalFormat df = new DecimalFormat("###.##");
        df.setMaximumFractionDigits(2);
        Date fromDate=null;
        Date toDate=null;
        String paymentDt = "";
        String payonlyPaymentDt = "";
        String activityDt ="";
		float rate =0;
		float waiver =0;
		float netcharge =0;
		float priorRate =0;
		float priorWaiver =0;
		float priorNetcharge =0;
		float priorPayment =0;
		float priorBalance =0;
		float priorCharge = 0;
		float payment =0;
		float balance = 0;
		int clientid=0;
		int repSize=0;
		boolean payAtyID=false;
		boolean priorAtyID=false;
		boolean payOnly = false;
		boolean payOnlyActivityid = false;
		boolean priorBal = false;
		String activityname="";
		String payonlyActivityName="";
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
            ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
            ps.setInt(1, report.getNameid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	clientid = rs.getInt("clientid");
            }
            report.setClientid(clientid);
            
            
            logger.debug("Query:"+Queries.getQuery("fetch_activity"));
            ps = c.prepareStatement(Queries.getQuery("fetch_activity"));
            ps.setInt(1, report.getTherapistid());
            ps.setInt(2, report.getClientid());
            fromDate = dateUtil.convertDate(report.getFromdate());
            ps.setDate(3, fromDate);
            toDate = dateUtil.convertDate(report.getTodate());
            ps.setDate(4, toDate);
            rs= ps.executeQuery();
            while (rs.next()) {
            	
            	activityDate.add(rs.getDate("activitydate"));
            	activityID.add(rs.getInt("activityid"));
            }
            
            logger.debug("Query:"+Queries.getQuery("fetch_prioractivity"));
            ps = c.prepareStatement(Queries.getQuery("fetch_prioractivity"));
            ps.setInt(1, report.getTherapistid());
            ps.setInt(2, report.getClientid());
            ps.setDate(3, fromDate);
            rs= ps.executeQuery();
            while (rs.next()) {
            	priorActivityID.add(rs.getInt("activityid"));
            }
         
          
            
            
 //To display SUM Payments made between the selected date
            
            logger.debug("Query:"+Queries.getQuery("fetch_fullpayment"));
            ps = c.prepareStatement(Queries.getQuery("fetch_fullpayment"));
            ps.setInt(1, report.getTherapistid());
            ps.setInt(2, report.getClientid());
            ps.setDate(3, fromDate);
            ps.setDate(4, toDate);
            
            rs = ps.executeQuery();
            while (rs.next()) {
            	payActivityid.add(rs.getInt("activityid"));
            	fullPayment.add(rs.getFloat("payment"));
            	paymentDate.add(rs.getDate("paymentdate"));
            	payActivityname.add(rs.getString("activityname"));
            	}
          
            if(activityID.size()<payActivityid.size()){
            	payAtyID = true;
            	for(int i=0;i<activityID.size();i++){
            		for(int k=0;k<payActivityid.size();k++){
            			if(activityID.get(i) == payActivityid.get(k)){
            				
            				payActivityid.remove(k);
            				fullPayment.remove(k);
            				paymentDate.remove(k);
            				payActivityname.remove(k);
            			}
            			
            		}	
            	}
            }
            
            //If any payment made on any other date other than activity date
            logger.debug("Query:"+Queries.getQuery("fetch_paymentbwdate"));
            ps = c.prepareStatement(Queries.getQuery("fetch_paymentbwdate"));
            ps.setInt(1, report.getTherapistid());
            ps.setInt(2, report.getClientid());
            ps.setDate(3, fromDate);
            ps.setDate(4, toDate);
            ps.setInt(5, report.getTherapistid());
            ps.setInt(6, report.getClientid());
            ps.setDate(7, fromDate);
            ps.setDate(8, toDate);
            
            rs = ps.executeQuery();
            while (rs.next()) {
            	payonlyactivityid.add(rs.getInt("activityid"));
            	payonlyamount.add(rs.getFloat("amount"));
            	payonlypaymentdate.add(rs.getDate("paymentdate"));
            	
            	}
            
          
          if(priorActivityID.size() >= 1){
          	repSize = activityID.size()+1;
          	priorAtyID = true;}
          else
          	repSize = activityID.size();
        
          if(payAtyID){
        	  
        	  repSize = repSize + payActivityid.size();
        	  
          }
          if(payonlyactivityid.size() >=1){
        	  
        	  repSize = repSize + payonlyactivityid.size();
        	  
          }
        	 
          
          Report rep[] = new Report[repSize];
          /*if(payAtyID){
            	
            	for(int l=0;l<payActivityid.size();l++)
            	{
            		System.out.println("inside for loop");
            		rep[l] = new Report();
            		 rep[l].setActivity(payActivityname.get(l)+"- P/O");
            		 System.out.println("ActivityName:"+payActivityname.get(l));
            		 paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
            		 System.out.println("paymentDt:"+paymentDt);
                     rep[l].setActivitydate(paymentDt);
                     rep[l].setNetcharge(0);
                     rep[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
                     rep[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                    
                    		 reportVal.add(rep[l]);
            	
            	}
            }
    		*/
            
            
            for(int i=0;i<repSize;i++){
            	
            	if(i< activityID.size()){
            		rep[i] = new Report();

            	logger.debug("Query:"+Queries.getQuery("fetch_activityname"));
                ps = c.prepareStatement(Queries.getQuery("fetch_activityname"));
                ps.setInt(1, activityID.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                activityname=rs.getString("activityname");
                
                }
                
                rep[i].setActivity(activityname);
                activityDt = dateUtil.convertStringToDate(activityDate.get(i));
                rep[i].setActivitydate(activityDt);
                rep[i].setActivityid(activityID.get(i));
                
                
                logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                ps.setInt(1, activityID.get(i));
                ps.setInt(2, report.getTherapistid());
                rs = ps.executeQuery();
                while (rs.next()) {
                	rate = rs.getFloat("amount");
                }
                
               
                logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                ps.setInt(1, report.getTherapistid());
                ps.setInt(2, report.getClientid());
                ps.setInt(3, activityID.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                	waiver = rs.getFloat("waiver");
                }
              
                netcharge = rate - waiver;
               
                
                rep[i].setNetcharge(Float.parseFloat(df.format(netcharge)));
              
                logger.debug("Query:"+Queries.getQuery("fetch_paidamount"));
                ps = c.prepareStatement(Queries.getQuery("fetch_paidamount"));
                ps.setInt(1, report.getTherapistid());
                ps.setInt(2, report.getClientid());
                ps.setInt(3, activityID.get(i));
                ps.setDate(4, activityDate.get(i));
               // ps.setDate(5, toDate);
                rs = ps.executeQuery();
                while (rs.next()) {
                	//paymentt.add(rs.getFloat("amount"));
                	payment = rs.getFloat("payment");
                }
             
                rep[i].setPayment(Float.parseFloat(df.format(payment)));
                balance = netcharge - payment;
              
                //lanceList.add(balance);
                //stem.out.println("balance:"+balance);
                rep[i].setBalance(Float.parseFloat(df.format(balance)));
                reportVal.add(rep[i]);
                rate=0;waiver=0;netcharge=0;payment=0;balance=0;
            	}
            	/*else if( priorActivityID.size() >= 1){*/
            	
            		if( priorAtyID && !priorBal){
            		
            		priorBal = true;
            		for(int k=0;k<priorActivityID.size();k++){
            			
            			logger.debug("Query:"+Queries.getQuery("fetch_prioractivityid"));
                        ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                        ps.setInt(1, priorActivityID.get(k));
                        ps.setInt(2, report.getTherapistid());
                        rs = ps.executeQuery();
                        while (rs.next()) {
                        	priorRate = rs.getFloat("amount");
                        }
                        
                        
                         logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                         ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                         ps.setInt(1, priorActivityID.get(k));
                         ps.setInt(2, report.getTherapistid());
                         rs = ps.executeQuery();
                         while (rs.next()) {
                         	priorRate = rs.getFloat("amount");
                         }
                      
                         
                         logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                         ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                        ps.setInt(1, report.getTherapistid());
                         ps.setInt(2, report.getClientid());
                         ps.setInt(3, priorActivityID.get(k));
                         rs = ps.executeQuery();
                         while (rs.next()) {
                         	priorWaiver = rs.getFloat("waiver");
                         	
                         }
                         
                         priorCharge = priorRate - priorWaiver;
                         priorNetcharge += priorCharge;
                       
                         priorRate=0;priorWaiver=0;priorCharge=0;
                         
            		}
            	       
               	 	logger.debug("Query:"+Queries.getQuery("fetch_priorbalance"));
                    ps = c.prepareStatement(Queries.getQuery("fetch_priorbalance"));
                    ps.setInt(1, report.getTherapistid());
                    ps.setInt(2, report.getClientid());
                    ps.setDate(3, fromDate);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                    	priorPayment = rs.getFloat("payment");
                    }
                   
                    priorBalance = priorNetcharge - priorPayment;
                   
                  
                    DateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fromDate);
                    cal.add(Calendar.DATE, -1);
                    String dateInString = dateFormat.format(cal.getTime());
                  
                    java.util.Date date = dateFormat.parse(dateInString);
                    Date priorDate = new Date(date.getTime());
                    rep[i] = new Report();
                    rep[i].setActivity("Prior Balance");
                    String priorDt = dateUtil.convertStringToDate(priorDate);
                    rep[i].setActivitydate(priorDt);
                    rep[i].setNetcharge(0);
                    rep[i].setPayment(0);
                    rep[i].setBalance(Float.parseFloat(df.format(priorBalance)));
                    priorNetcharge=0; priorPayment=0;
                    reportVal.add(rep[i]);
            	}
            	//else if(i < payActivityid.size() ){
            		
            		
          		
            	
            		if(payAtyID && !payOnly){
                    	payOnly = true;
                    	
                    	for(int l=0;l<payActivityid.size();l++)
                    	{
                    		
                    		rep[l] = new Report();
                    		 rep[l].setActivity(payActivityname.get(l)+"- P/O");
                    		 
                    		 paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
                    		 
                             rep[l].setActivitydate(paymentDt);
                             rep[l].setNetcharge(0);
                             rep[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
                             rep[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                            
                            		 reportVal.add(rep[l]);
                    	
                    	}
                    }
            		
               		if(payonlyactivityid.size() >=1 && !payOnlyActivityid){
               			payOnlyActivityid = true;
                    	
                    	for(int l=0;l<payonlyactivityid.size();l++)
                    	{
                    		payonlyActivityName ="";
                    		rep[l] = new Report();
                    		
                    		logger.debug("Query:"+Queries.getQuery("fetch_activityname"));
                            ps = c.prepareStatement(Queries.getQuery("fetch_activityname"));
                            ps.setInt(1, payonlyactivityid.get(l));
                            rs = ps.executeQuery();
                            while (rs.next()) {
                            payonlyActivityName=rs.getString("activityname");
                            
                            }
                    		 rep[l].setActivity(payonlyActivityName +"- P/O");
                    		 
                    		 payonlyPaymentDt = dateUtil.convertStringToDate(payonlypaymentdate.get(l));
                    		 
                             rep[l].setActivitydate(payonlyPaymentDt);
                             rep[l].setNetcharge(0);
                             rep[l].setPayment(Float.parseFloat(df.format(payonlyamount.get(l))));
                             rep[l].setBalance(Float.parseFloat(df.format(-payonlyamount.get(l))));
                            
                            		 reportVal.add(rep[l]);
                    	
                    	}
                    }
            		
            	
 
            }
        
    		
            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return reportVal;
	}
	
	public List<Report> fetchReportWC(Report report)  {
		List<Report> reportVal = new ArrayList<Report>();
		List<Date> activityDate = new ArrayList<Date>();
		List<Integer> activityID = new ArrayList<Integer>();
		List<Date> paymentDate = new ArrayList<Date>();
		List<Integer> payActivityid = new ArrayList<Integer>();
		List<String> payActivityname = new ArrayList<String>();
		List<Float> fullPayment = new ArrayList<Float>();
		List<Integer> priorActivityID = new ArrayList<Integer>();
		List<Integer> payonlyactivityid = new ArrayList<Integer>();
		List<Date> payonlypaymentdate = new ArrayList<Date>();
		List<Float> payonlyamount = new ArrayList<Float>();
		
		DecimalFormat df = new DecimalFormat("###.##");
        df.setMaximumFractionDigits(2);
        Date fromDate=null;
        Date toDate =  null;
        String paymentDt="";
		float rate =0;
		float waiver =0;
		float netcharge =0;
		float payment =0;
		float balance = 0;
		float priorRate =0;
		float priorWaiver =0;
		float priorNetcharge =0;
		float priorPayment =0;
		float priorBalance =0;
		float priorCharge = 0;
		int clientid=0;
		int repSize=0;
		int therapistid=0;
		int priorTherapistid =0;
		boolean payAtyID = false;
		boolean priorAtyID=false;
		boolean priorBal=false;
		boolean payOnly = false;
		boolean payOnlyActivityid = false;
		String payonlyPaymentDt = "";
		String payonlyActivityName = "";
		String activityname="";
		String activityDt = "";
        Connection c = null;
        String priorDt ="";
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
            ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
            ps.setInt(1, report.getNameid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	clientid = rs.getInt("clientid");
            }
            report.setClientid(clientid);
          
           
            logger.debug("Query:"+Queries.getQuery("fetchWC_prioractivity"));
            ps = c.prepareStatement(Queries.getQuery("fetchWC_prioractivity"));
          
            ps.setInt(1, report.getClientid());
             fromDate = dateUtil.convertDate(report.getFromdate());
            ps.setDate(2, fromDate);
            rs= ps.executeQuery();
            while (rs.next()) {
            	priorActivityID.add(rs.getInt("activityid"));
            }
            
            logger.debug("Query:"+Queries.getQuery("fetchWC_activity"));
            ps = c.prepareStatement(Queries.getQuery("fetchWC_activity"));
          
            ps.setInt(1, report.getClientid());
            ps.setDate(2, fromDate);
            toDate = dateUtil.convertDate(report.getTodate());
            ps.setDate(3, toDate);
            rs= ps.executeQuery();
            while (rs.next()) {
            
            	activityDate.add(rs.getDate("activitydate"));
            	activityID.add(rs.getInt("activityid"));
            }
         
            
            
            //To display SUM Payments made between the selected date
                       
                       logger.debug("Query:"+Queries.getQuery("fetchWC_fullpayment"));
                       ps = c.prepareStatement(Queries.getQuery("fetchWC_fullpayment"));
                      
                       ps.setInt(1, report.getClientid());
                       ps.setDate(2, fromDate);
                       ps.setDate(3, toDate);
                       
                       rs = ps.executeQuery();
                       while (rs.next()) {
                       	payActivityid.add(rs.getInt("activityid"));
                       	fullPayment.add(rs.getFloat("payment"));
                       	paymentDate.add(rs.getDate("paymentdate"));
                       	payActivityname.add(rs.getString("activityname"));
                       	}
                     
                       if(activityID.size()<payActivityid.size()){
                       	payAtyID = true;
                       	for(int i=0;i<activityID.size();i++){
                       		for(int k=0;k<payActivityid.size();k++){
                       			if(activityID.get(i) == payActivityid.get(k)){
                       				
                       				payActivityid.remove(k);
                       				fullPayment.remove(k);
                       				paymentDate.remove(k);
                       				payActivityname.remove(k);
                       			}
                       			
                       		}	
                       	}
                       }
                       
                       //If any payment made on any other date other than activity date
                       logger.debug("Query:"+Queries.getQuery("fetchWC_paymentbwdate"));
                       ps = c.prepareStatement(Queries.getQuery("fetchWC_paymentbwdate"));
                       
                       ps.setInt(1, report.getClientid());
                       ps.setDate(2, fromDate);
                       ps.setDate(3, toDate);
                      
                       ps.setInt(4, report.getClientid());
                       ps.setDate(5, fromDate);
                       ps.setDate(6, toDate);
                       
                       rs = ps.executeQuery();
                       while (rs.next()) {
                       	payonlyactivityid.add(rs.getInt("activityid"));
                       	payonlyamount.add(rs.getFloat("amount"));
                       	payonlypaymentdate.add(rs.getDate("paymentdate"));
                       	
                       	}
                                       
                     if(priorActivityID.size() >= 1){
                     	repSize = activityID.size()+1;
                     	priorAtyID = true;}
                     else
                     	repSize = activityID.size();
                   
                     if(payAtyID){
                   	  repSize = repSize + payActivityid.size();
                     }
                     if(payonlyactivityid.size() >=1)
                   	  repSize = repSize + payonlyactivityid.size();
                     
            
          
        
            Report rep[] = new Report[repSize];
            
           
       
           /* if(payAtyID){
            	for(int l=0;l<payActivityid.size();l++)
            	{
            		rep[l] = new Report();
            		 rep[l].setActivity(payActivityname.get(l)+"- P/O");
            		 paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
                     rep[l].setActivitydate(paymentDt);
                     rep[l].setNetcharge(0);
                     rep[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
                     rep[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                    
                    		 reportVal.add(rep[l]);
            	
            	}
            }*/
    		
            
            
            for(int i=0;i<repSize;i++){
            	
            	 
            	if(i< activityID.size()){
            	
            		rep[i] = new Report();
            	logger.debug("Query:"+Queries.getQuery("fetch_activityname"));
                ps = c.prepareStatement(Queries.getQuery("fetch_activityname"));
                ps.setInt(1, activityID.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                activityname=rs.getString("activityname");
                }
             
                rep[i].setActivity(activityname);
                activityDt = dateUtil.convertStringToDate(activityDate.get(i));
                rep[i].setActivitydate(activityDt);
                rep[i].setActivityid(activityID.get(i));
                
                logger.debug("Query:"+Queries.getQuery("fetchWC_Therpistid"));
                ps = c.prepareStatement(Queries.getQuery("fetchWC_Therpistid"));
               
                ps.setInt(1, report.getClientid());
                ps.setInt(2, activityID.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                	therapistid = rs.getInt("therapistid");
                	
                }
                
                logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                ps.setInt(1, activityID.get(i));
            
                ps.setInt(2, therapistid);
                rs = ps.executeQuery();
                while (rs.next()) {
                	rate = rs.getFloat("amount");
                }
            
                
                logger.debug("Query:"+Queries.getQuery("fetchWC_waiver"));
                ps = c.prepareStatement(Queries.getQuery("fetchWC_waiver"));
               
                ps.setInt(1, report.getClientid());
                ps.setInt(2, activityID.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                	waiver = rs.getFloat("waiver");
                	
                }
                
                netcharge = rate - waiver;
            
                rep[i].setNetcharge(Float.parseFloat(df.format(netcharge)));
             
                
                logger.debug("Query:"+Queries.getQuery("fetchWC_paidamount"));
                ps = c.prepareStatement(Queries.getQuery("fetchWC_paidamount"));
               
                ps.setInt(1, report.getClientid());
                ps.setInt(2, activityID.get(i));
                ps.setDate(3, activityDate.get(i));
               
                
                rs = ps.executeQuery();
                while (rs.next()) {
                	//paymentt.add(rs.getFloat("amount"));
                	payment = rs.getFloat("payment");
                }
                rep[i].setPayment(Float.parseFloat(df.format(payment)));
                balance = netcharge - payment;
                //lanceList.add(balance);
                //stem.out.println("balance:"+balance);
                rep[i].setBalance(Float.parseFloat(df.format(balance)));
                
                reportVal.add(rep[i]);
                rate=0;waiver=0;netcharge=0;payment=0;balance=0;
            	}
            	
            		if( priorAtyID && !priorBal){
                		
                		priorBal = true;
            	
            		for(int k=0;k<priorActivityID.size();k++){
            			
            			 logger.debug("Query:"+Queries.getQuery("fetchWC_Therpistid"));
                         ps = c.prepareStatement(Queries.getQuery("fetchWC_Therpistid"));
                        
                         ps.setInt(1, report.getClientid());
                         ps.setInt(2, priorActivityID.get(k));
                         rs = ps.executeQuery();
                         while (rs.next()) {
                         	priorTherapistid = rs.getInt("therapistid");
                         	
                         }
                         
                         logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                         ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                         ps.setInt(1, priorActivityID.get(k));
                     
                         ps.setInt(2, priorTherapistid);
                         rs = ps.executeQuery();
                         while (rs.next()) {
                         	priorRate = rs.getFloat("amount");
                         }
                      
                         
                         logger.debug("Query:"+Queries.getQuery("fetchWC_waiver"));
                         ps = c.prepareStatement(Queries.getQuery("fetchWC_waiver"));
                        
                         ps.setInt(1, report.getClientid());
                         ps.setInt(2, priorActivityID.get(k));
                         rs = ps.executeQuery();
                         while (rs.next()) {
                         	priorWaiver = rs.getFloat("waiver");
                         	
                         }
                         
                         priorCharge = priorRate - priorWaiver;
                     
                        
                   
                         priorNetcharge += priorCharge;
                   
                  
                         priorRate=0;priorWaiver=0;priorCharge=0;
                        
                       
            			
            		}
            	       
               	 	logger.debug("Query:"+Queries.getQuery("fetchWC_priorbalance"));
                    ps = c.prepareStatement(Queries.getQuery("fetchWC_priorbalance"));
                   
                    ps.setInt(1, report.getClientid());
                    ps.setDate(2, fromDate);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                    	priorPayment = rs.getFloat("payment");
                    }
             
                    priorBalance = priorNetcharge - priorPayment;
                    
                   
             
                    DateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fromDate);
                    cal.add(Calendar.DATE, -1);
                    String dateInString = dateFormat.format(cal.getTime());
                
                    java.util.Date date = dateFormat.parse(dateInString);
                    Date priorDate = new Date(date.getTime());
                    rep[i] = new Report();
                    rep[i].setActivity("Prior Balance");
                    priorDt = dateUtil.convertStringToDate(priorDate);
                    rep[i].setActivitydate(priorDt);
                    rep[i].setNetcharge(0);
                    rep[i].setPayment(0);
                    rep[i].setBalance(Float.parseFloat(df.format(priorBalance)));
                    priorNetcharge=0; priorPayment=0;
                    reportVal.add(rep[i]);
                     
            	}
            	
             	
        		if(payAtyID && !payOnly){
                	payOnly = true;
                	
                	for(int l=0;l<payActivityid.size();l++)
                	{
                		
                		rep[l] = new Report();
                		 rep[l].setActivity(payActivityname.get(l)+"- P/O");
                		 
                		 paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
                		 
                         rep[l].setActivitydate(paymentDt);
                         rep[l].setNetcharge(0);
                         rep[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
                         rep[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                        
                        		 reportVal.add(rep[l]);
                	
                	}
                }
        		
          		if(payonlyactivityid.size() >=1 && !payOnlyActivityid){
           			payOnlyActivityid = true;
                	
                	for(int l=0;l<payonlyactivityid.size();l++)
                	{
                		payonlyActivityName ="";
                		rep[l] = new Report();
                		
                		logger.debug("Query:"+Queries.getQuery("fetch_activityname"));
                        ps = c.prepareStatement(Queries.getQuery("fetch_activityname"));
                        ps.setInt(1, payonlyactivityid.get(l));
                        rs = ps.executeQuery();
                        while (rs.next()) {
                        payonlyActivityName=rs.getString("activityname");
                       
                        }
                		 rep[l].setActivity(payonlyActivityName +"- P/O");
                		 
                		 payonlyPaymentDt = dateUtil.convertStringToDate(payonlypaymentdate.get(l));
                		 
                         rep[l].setActivitydate(payonlyPaymentDt);
                         rep[l].setNetcharge(0);
                         rep[l].setPayment(Float.parseFloat(df.format(payonlyamount.get(l))));
                         rep[l].setBalance(Float.parseFloat(df.format(-payonlyamount.get(l))));
                        
                        		 reportVal.add(rep[l]);
                	
                	}
                }
            }
            
            
    		
            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return reportVal;
	}
}
