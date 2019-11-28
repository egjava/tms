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

import com.tms.bean.Report;
import com.tms.bean.ReportGeneration;
import com.tms.database.ConnectionHelper;
import com.tms.util.DateUtil;
import com.tms.util.Queries;



public class ReportGenerationdao {
	final static Logger logger = Logger.getLogger(ReportGenerationdao.class);
	DateUtil dateUtil = new DateUtil();
	public List<ReportGeneration> generateReportBI(ReportGeneration bins)  {
		
		List<ReportGeneration> reportBI = new ArrayList<ReportGeneration>();
		List<Date> activityDate = new ArrayList<Date>();
		List<Integer> activityID = new ArrayList<Integer>();
		List<Integer> cptcode = new ArrayList<Integer>();
		List<String> aName = new ArrayList<String>();
		Date fromDate= null;
		Date toDate = null;
		String activityDt="";
		float rate =0;
		float waiver =0;
		float netcharge =0;
		//float total=0;
		int clientid=0;
		Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
        	
        	 c = ConnectionHelper.getConnection();
        	 if(bins.getFlagid() == 0){
        		 logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
        		 ps.setInt(1, bins.getNameid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
        	 else{
        		 logger.debug("Query:"+Queries.getQuery("fetchForReportclientWflagid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReportclientWflagid"));
        		 ps.setInt(1, bins.getNameid());
        		 ps.setInt(2, bins.getFlagid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
             bins.setClientid(clientid);
                        
             
             logger.debug("Query:"+Queries.getQuery("fetchForCNameBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForCNameBI"));
             ps.setInt(1, bins.getNameid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setCfirstname(rs.getString("firstname"));
             	bins.setClastname(rs.getString("lastname"));
             	bins.setCmiddlename(rs.getString("middlename"));
             	}
             String clientName = bins.getCfirstname() + " " +bins.getCmiddlename() + " " +bins.getClastname();    
             bins.setClientname( clientName);
             
              //To get address for the client
             logger.debug("Query:"+Queries.getQuery("fetchForCAddressBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForCAddressBI"));
             ps.setInt(1, bins.getClientid());
          
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setCaddress1(rs.getString("address1"));
             	bins.setCaddress2(rs.getString("address2"));
             	bins.setCcity(rs.getString("city"));
             	bins.setCstate(rs.getString("state"));
             	bins.setCzipcode(rs.getInt("zip"));
             	}
           
             //To get first diagnosis code for a client
             ps = c.prepareStatement(Queries.getQuery("fetchForCdiagnosisBI"));
             ps.setInt(1, bins.getClientid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setDiagnosiscode(rs.getString("name"));
             	
             	}
             
             
             //To get Therapist Name and address
             
             logger.debug("Query:"+Queries.getQuery("fetchForNameBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForNameBI"));
             ps.setInt(1, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setFirstname(rs.getString("firstname"));
             	bins.setLastname(rs.getString("lastname"));
             	bins.setMiddlename(rs.getString("middlename"));
             	}
             String therapistName = bins.getFirstname() + " " +bins.getMiddlename() + " " +bins.getLastname();    
             bins.setTherapistname(therapistName);
             
             logger.debug("Query:"+Queries.getQuery("fetchForAddressBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForAddressBI"));
             ps.setInt(1, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setAddress1(rs.getString("address1"));
             	bins.setAddress2(rs.getString("address2"));
             	bins.setCity(rs.getString("city"));
             	bins.setState(rs.getString("state"));
             	bins.setZipcode(rs.getInt("zip"));
             	}
             
             //To get Therapist Mobile and EIN, License Number
             logger.debug("Query:"+Queries.getQuery("fetchForContactBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForContactBI"));
             ps.setInt(1, bins.getTherapistid());
             ps.setInt(2, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setEin(rs.getString("ein"));
             	bins.setLicense(rs.getString("license"));
             	bins.setMobile(rs.getString("mobile"));             	
             	}
             
             reportBI.add(bins);
             
             logger.debug("Query:"+Queries.getQuery("genRepBIActivity"));
             ps = c.prepareStatement(Queries.getQuery("genRepBIActivity"));
           
             ps.setInt(1, bins.getClientid());
             ps.setInt(2, bins.getTherapistid());
             fromDate = dateUtil.convertDate(bins.getFromdate());
             ps.setDate(3, fromDate);
             toDate = dateUtil.convertDate(bins.getTodate());
             ps.setDate(4, toDate);
             rs= ps.executeQuery();
             while (rs.next()) {
             	
             	activityDate.add(rs.getDate("activitydate"));
             	activityID.add(rs.getInt("activityid"));
             	cptcode.add(rs.getInt("cptcode"));
             	aName.add(rs.getString("activityname"));
             }
             ReportGeneration bInsurance[] = new ReportGeneration[activityID.size()];
             for(int i=0;i<activityID.size();i++){
            	 bInsurance[i] = new ReportGeneration();
            	                  
                 logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                 ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                 ps.setInt(1, activityID.get(i));
                 ps.setInt(2, bins.getTherapistid());
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 	rate = rs.getFloat("amount");
                 }
                 
                 logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                 ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                 ps.setInt(1, bins.getTherapistid());
                 ps.setInt(2, bins.getClientid());
                 ps.setInt(3, activityID.get(i));
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 	waiver = rs.getFloat("waiver");
                 }
                 netcharge = rate - waiver;
                 //total += netcharge;
               
                 activityDt = dateUtil.convertStringToDate(activityDate.get(i));
                 bInsurance[i].setActivityDate(activityDt);
                 bInsurance[i].setCptcode(cptcode.get(i));
                 bInsurance[i].setNetcharge(netcharge);
                 
                 reportBI.add(bInsurance[i]);
                 
                 rate=0;waiver=0;netcharge=0;
                
             }
           
             
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return reportBI;
        
       
	}
	
public List<ReportGeneration> generateReportBS(ReportGeneration bins)  {
		
		List<ReportGeneration> reportBS = new ArrayList<ReportGeneration>();
		List<Date> activityDate = new ArrayList<Date>();
		List<Integer> activityID = new ArrayList<Integer>();
		List<Integer> priorActivityID = new ArrayList<Integer>();
		List<Integer> cptcode = new ArrayList<Integer>();
		List<String> aName = new ArrayList<String>();
		List<Date> paymentDate = new ArrayList<Date>();
		List<Integer> payActivityid = new ArrayList<Integer>();
		List<String> payActivityname = new ArrayList<String>();
		List<Float> fullPayment = new ArrayList<Float>();
		List<Integer> payonlyactivityid = new ArrayList<Integer>();
		List<Date> payonlypaymentdate = new ArrayList<Date>();
		List<Float> payonlyamount = new ArrayList<Float>();
		boolean payAtyID=false;
		float rate =0;
		float waiver =0;
		float netcharge =0;
		float payment=0;
		float priorCharge=0;
		float priorWaiver=0;
		float priorNetcharge=0;
		float priorRate=0;
		float priorPayment=0;
		float priorBalance=0;
		int clientid=0;
		float balance=0;
		int repBSSize =0;
	
		boolean priorAtyID=false;
		boolean payOnly = false;
		boolean payOnlyActivityid = false;
		boolean priorBal = false;
		Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DecimalFormat df = new DecimalFormat("###.##");
        df.setMaximumFractionDigits(2);
        Date fromDate = null;
        Date toDate = null;
        String activityDt = "";
        String paymentDt = "";
        String priorDt= null;
        String payonlyPaymentDt = "";
		String payonlyActivityName = "";
        
        try{
        	
        	 c = ConnectionHelper.getConnection();
        	 if(bins.getFlagid() == 0){
        		 logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
        		 ps.setInt(1, bins.getNameid());
        		 
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
        	 else{
        		 logger.debug("Query:"+Queries.getQuery("fetchForReportclientWflagid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReportclientWflagid"));
        		 ps.setInt(1, bins.getNameid());
        		 ps.setInt(2, bins.getFlagid());
        		 
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
             bins.setClientid(clientid);
                         
             
             logger.debug("Query:"+Queries.getQuery("fetchForCNameBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForCNameBI"));
             ps.setInt(1, bins.getNameid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setCfirstname(rs.getString("firstname"));
             	bins.setClastname(rs.getString("lastname"));
             	bins.setCmiddlename(rs.getString("middlename"));
             	}
             String clientName = bins.getCfirstname() + " " +bins.getCmiddlename() + " " +bins.getClastname();    
             bins.setClientname( clientName);
             
              //To get address for the client
             logger.debug("Query:"+Queries.getQuery("fetchForCAddressBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForCAddressBI"));
             ps.setInt(1, bins.getClientid());
             
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setCaddress1(rs.getString("address1"));
             	bins.setCaddress2(rs.getString("address2"));
             	bins.setCcity(rs.getString("city"));
             	bins.setCstate(rs.getString("state"));
             	bins.setCzipcode(rs.getInt("zip"));
             	}
             
                        
             
             //To get Therapist Name and address
             
             logger.debug("Query:"+Queries.getQuery("fetchForNameBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForNameBI"));
             ps.setInt(1, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setFirstname(rs.getString("firstname"));
             	bins.setLastname(rs.getString("lastname"));
             	bins.setMiddlename(rs.getString("middlename"));
             	}
             String therapistName = bins.getFirstname() + " " +bins.getMiddlename() + " " +bins.getLastname();    
             bins.setTherapistname(therapistName);
             
             logger.debug("Query:"+Queries.getQuery("fetchForAddressBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForAddressBI"));
             ps.setInt(1, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setAddress1(rs.getString("address1"));
             	bins.setAddress2(rs.getString("address2"));
             	bins.setCity(rs.getString("city"));
             	bins.setState(rs.getString("state"));
             	bins.setZipcode(rs.getInt("zip"));
             	}
             
             //To get Therapist Mobile and EIN, License Number
             logger.debug("Query:"+Queries.getQuery("fetchForContactBI"));
             ps = c.prepareStatement(Queries.getQuery("fetchForContactBI"));
             ps.setInt(1, bins.getTherapistid());
             ps.setInt(2, bins.getTherapistid());
             rs = ps.executeQuery();
             while (rs.next()) {
             	bins.setEin(rs.getString("ein"));
             	bins.setLicense(rs.getString("license"));
             	bins.setMobile(rs.getString("mobile"));             	
             	}
             
             reportBS.add(bins);
             
             logger.debug("Query:"+Queries.getQuery("genRepBIActivity"));
             ps = c.prepareStatement(Queries.getQuery("genRepBIActivity"));
           
             ps.setInt(1, bins.getClientid());
             ps.setInt(2, bins.getTherapistid());
             fromDate = dateUtil.convertDate(bins.getFromdate());
             toDate = dateUtil.convertDate(bins.getTodate());
             ps.setDate(3, fromDate);
             ps.setDate(4, toDate);
             rs= ps.executeQuery();
             while (rs.next()) {
             
             	activityDate.add(rs.getDate("activitydate"));
             	activityID.add(rs.getInt("activityid"));
             	cptcode.add(rs.getInt("cptcode"));
             	aName.add(rs.getString("activityname"));
             }
             //Check if there is any prior balance
             logger.debug("Query:"+Queries.getQuery("fetch_prioractivity"));
             ps = c.prepareStatement(Queries.getQuery("fetch_prioractivity"));
             ps.setInt(1,bins.getTherapistid());
             ps.setInt(2, bins.getClientid());
             ps.setDate(3, fromDate);
             rs= ps.executeQuery();
             while (rs.next()) {
             	priorActivityID.add(rs.getInt("activityid"));
             }
             
           
          /*2   if(priorActivityID.size() >= 1){
             	repBSSize = activityID.size()+1;}
             else
            	 repBSSize = activityID.size();*/
             
             
             
             //To display Any Payments made between the selected date
             
             logger.debug("Query:"+Queries.getQuery("fetch_fullpayment"));
             ps = c.prepareStatement(Queries.getQuery("fetch_fullpayment"));
             ps.setInt(1, bins.getTherapistid());
             ps.setInt(2, bins.getClientid());
             ps.setDate(3, fromDate);
             ps.setDate(4, toDate);
             
             rs = ps.executeQuery();
             while (rs.next()) {
             	payActivityid.add(rs.getInt("activityid"));
             	fullPayment.add(rs.getFloat("payment"));
             	//paymentDt = dateUtil.convertStringToDate(rs.getDate("paymentdate"));
             	paymentDate.add(rs.getDate("paymentdate"));
             	payActivityname.add(rs.getString("activityname"));
             	}
           
             if(activityID.size()<payActivityid.size()){
             	payAtyID = true;
             	for(int i=0;i<activityID.size();i++){
             		for(int k=0;k<payActivityid.size();k++){
             			if(activityID.get(i) == payActivityid.get(k)){
             				payActivityid.remove(i);
             				fullPayment.remove(i);
             				paymentDate.remove(i);
             				payActivityname.remove(i);
             			}
             			
             		}	
             	}
             }
             
             //If any payment made on any other date other than activity date
             logger.debug("Query:"+Queries.getQuery("fetch_paymentbwdate"));
             ps = c.prepareStatement(Queries.getQuery("fetch_paymentbwdate"));
             ps.setInt(1, bins.getTherapistid());
             ps.setInt(2, bins.getClientid());
             ps.setDate(3, fromDate);
             ps.setDate(4, toDate);
             ps.setInt(5, bins.getTherapistid());
             ps.setInt(6, bins.getClientid());
             ps.setDate(7, fromDate);
             ps.setDate(8, toDate);
             
             rs = ps.executeQuery();
             while (rs.next()) {
             	payonlyactivityid.add(rs.getInt("activityid"));
             	payonlyamount.add(rs.getFloat("amount"));
             	payonlypaymentdate.add(rs.getDate("paymentdate"));
             	
             	}
             
             
           if(priorActivityID.size() >= 1){
        	   repBSSize = activityID.size()+1;
           	priorAtyID = true;}
           else
        	   repBSSize = activityID.size();
           
           if(payAtyID){
         	  
         	 repBSSize = repBSSize + payActivityid.size();
         	  
           }
           if(payonlyactivityid.size() >=1){
         	  
         	 repBSSize = repBSSize + payonlyactivityid.size();
         	  
           }
           
     
     
    	 
      
      ReportGeneration bSummary[] = new ReportGeneration[repBSSize];
           
          /*1   if(payAtyID){
             	for(int l=0;l<payActivityid.size();l++)
             	{
             		bSummary[l] = new ReportGeneration();
             		bSummary[l].setActivityname(payActivityname.get(l)+"- P/O");
             		paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
             		bSummary[l].setActivityDate(paymentDt);
             		bSummary[l].setNetcharge(0);
             		bSummary[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
             		bSummary[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                     
             		  reportBS.add(bSummary[l]);
             	
             	}
             }*/
             
             for(int i=0;i<repBSSize;i++){
            	
            	 if(i< activityID.size()){
            		 bSummary[i] = new ReportGeneration();         
                 logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                 ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                 ps.setInt(1, activityID.get(i));
                 ps.setInt(2, bins.getTherapistid());
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 	rate = rs.getFloat("amount");
                 }
                 
                 logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                 ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                 ps.setInt(1, bins.getTherapistid());
                 ps.setInt(2, bins.getClientid());
                 ps.setInt(3, activityID.get(i));
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 	waiver = rs.getFloat("waiver");
                 }
                 netcharge = rate - waiver;
              
                 
                 logger.debug("Query:"+Queries.getQuery("fetch_paidamount"));
                 ps = c.prepareStatement(Queries.getQuery("fetch_paidamount"));
                 ps.setInt(1, bins.getTherapistid());
                 ps.setInt(2, bins.getClientid());
                 ps.setInt(3, activityID.get(i));
                 ps.setDate(4, activityDate.get(i));
                // ps.setDate(4, fromDate);
                // ps.setDate(5, toDate);
                 rs = ps.executeQuery();
                 while (rs.next()) {
                 	payment = rs.getFloat("payment");
                 }
                 balance = netcharge - payment;
                
               activityDt = dateUtil.convertStringToDate(activityDate.get(i));
                 bSummary[i].setActivityDate(activityDt);
                 bSummary[i].setActivityname(aName.get(i));
                 bSummary[i].setNetcharge(netcharge);
                 bSummary[i].setPayment(Float.parseFloat(df.format(payment)));
                 bSummary[i].setBalance(Float.parseFloat(df.format(balance)));
                 reportBS.add(bSummary[i]);
                rate=0;waiver=0;netcharge=0;payment=0;balance=0;
             }
            	// else
            		 if( priorAtyID && !priorBal){
            		 //This is for Prior Balance
            			 
                 		priorBal = true;
             		
             		for(int k=0;k<priorActivityID.size();k++){
             			
             			                         
                          logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                          ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                          ps.setInt(1, priorActivityID.get(k));
                          ps.setInt(2, bins.getTherapistid());
                          rs = ps.executeQuery();
                          while (rs.next()) {
                          	priorRate = rs.getFloat("amount");
                          }
                         
                          
                          logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                          ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                          ps.setInt(1, bins.getTherapistid());
                          ps.setInt(2, bins.getClientid());
                          ps.setInt(3, priorActivityID.get(k));
                          rs = ps.executeQuery();
                          while (rs.next()) {
                          	priorWaiver = rs.getFloat("waiver");
                          }
                          
                          priorCharge = priorRate - priorWaiver;
                          priorNetcharge += priorCharge;
                          
                          priorRate =0;priorWaiver=0;priorCharge=0;
             		}
             	       
                	 logger.debug("Query:"+Queries.getQuery("fetch_priorbalance"));
                     ps = c.prepareStatement(Queries.getQuery("fetch_priorbalance"));
                     ps.setInt(1, bins.getTherapistid());
                     ps.setInt(2, bins.getClientid());
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
                     bSummary[i] = new ReportGeneration();     
                     priorDt = dateUtil.convertStringToDate(priorDate);
                     bSummary[i].setActivityDate(priorDt);
                     bSummary[i].setActivityname("Prior Balance");
                     bSummary[i].setNetcharge(0);
                     bSummary[i].setPayment(0);
                     bSummary[i].setBalance(Float.parseFloat(df.format(priorBalance)));
                     reportBS.add(bSummary[i]);
                     priorBalance=0;priorPayment=0;priorNetcharge=0;
                 }
            		 
            		 
                 	
             		if(payAtyID && !payOnly){
                     	payOnly = true;
                     	
                     	for(int l=0;l<payActivityid.size();l++)
                     	{
                     		
                     		bSummary[l] = new ReportGeneration();   
                     		bSummary[l].setActivityname(payActivityname.get(l)+"- P/O");
                     		 
                     		 paymentDt = dateUtil.convertStringToDate(paymentDate.get(l));
                     		 
                     		bSummary[l].setActivityDate(paymentDt);
                     		bSummary[l].setNetcharge(0);
                     		bSummary[l].setPayment(Float.parseFloat(df.format(fullPayment.get(l))));
                     		bSummary[l].setBalance(Float.parseFloat(df.format(-fullPayment.get(l))));
                             
                     		reportBS.add(bSummary[l]);
                     	
                     	}
                     }
             		
             		if(payonlyactivityid.size() >=1 && !payOnlyActivityid){
               			payOnlyActivityid = true;
                    	
                    	for(int l=0;l<payonlyactivityid.size();l++)
                    	{
                    		payonlyActivityName ="";
                    		bSummary[l] =new ReportGeneration();      
                    		
                    		logger.debug("Query:"+Queries.getQuery("fetch_activityname"));
                            ps = c.prepareStatement(Queries.getQuery("fetch_activityname"));
                            ps.setInt(1, payonlyactivityid.get(l));
                            rs = ps.executeQuery();
                            while (rs.next()) {
                            payonlyActivityName=rs.getString("activityname");
                          
                            }
                            bSummary[l].setActivityname(payonlyActivityName +"- P/O");
                    		 
                    		 payonlyPaymentDt = dateUtil.convertStringToDate(payonlypaymentdate.get(l));
                    		 
                    		 bSummary[l].setActivityDate(payonlyPaymentDt);
                    		 bSummary[l].setNetcharge(0);
                    		 bSummary[l].setPayment(Float.parseFloat(df.format(payonlyamount.get(l))));
                    		 bSummary[l].setBalance(Float.parseFloat(df.format(-payonlyamount.get(l))));
                            
                             reportBS.add(bSummary[l]);
                    	
                    	}
                    }
             		
             }
            
           
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return reportBS;
        
       
	}

public List<ReportGeneration> generateReportTOT(ReportGeneration bTOT)  {
	List<ReportGeneration> reportTOT = new ArrayList<ReportGeneration>();
	List<Integer> clientIdList = new ArrayList<Integer>();
	List<Integer> nameIdList = new ArrayList<Integer>();
	List<Integer> priorActivityID = null;
	
	List<Integer> activityIDList = null;
	DateUtil dateUtil = new DateUtil();
	 DecimalFormat df = new DecimalFormat("###.##");
     df.setMaximumFractionDigits(2);
	Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int clientid=0;
    String firstName="";
    String lastName="";
    String clientName="";
    String therapistName="";
    int repActTOTSize =0;
    float charge=0;
    float netCharge=0;
    float waiver=0;
    float netWaiver=0;
    float payment=0;
    float netPayment=0;
    float grossCharge =0;
    float totbal =0;
    float balance=0;
    float priorBalance=0;
    float priorWaiver=0;
    float priorPayment=0;
    float priorCharge =0;
    float priorRate=0;
    float priorNetcharge=0;
    Date fromDate= null;
    Date toDate=null;
    int nameid=0;
    
    try{
    	
   	 	c = ConnectionHelper.getConnection();
   	 	nameid = bTOT.getNameid();
   	 	fromDate= dateUtil.convertDate(bTOT.getFromdate());
   	 	toDate = dateUtil.convertDate(bTOT.getTodate());
   	 	
   	 	if(nameid == 0){
   	 		if(bTOT.getFlagid() == 0){
   	 			//Get all the Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientid"));
   	 			ps.setInt(1, bTOT.getTherapistid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}
   	 		}
   	 		else{
   	 			//Get all the Clients with flagid
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps.setInt(1, bTOT.getTherapistid());
   	 			ps.setInt(2, bTOT.getFlagid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}	
   	 		}
   	 		//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bTOT.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			
			
   	 		ReportGeneration bTherapistMoney[] = new ReportGeneration[clientIdList.size()];
   	 		for(int i=0;i<clientIdList.size();i++){
   	 			bTherapistMoney[i] = new ReportGeneration();
   	 			
   	 			//Get The Name of all Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
   	 			ps.setInt(1, nameIdList.get(i));
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				firstName = rs.getString("firstname");
   	 				lastName = rs.getString("lastname");
   	 			}
   	 			clientName = firstName+" "+lastName;
   	 			bTherapistMoney[i].setClientname(clientName);
   	 			bTherapistMoney[i].setTherapistname(therapistName);
   	 			bTherapistMoney[i].setFromdate(bTOT.getFromdate());
   	 			bTherapistMoney[i].setTodate(bTOT.getTodate());
   	 			//Get The Activity of a Client
   	 			activityIDList = new ArrayList<Integer>();
   	 			priorActivityID = new ArrayList<Integer>();
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMActivity"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMActivity"));
   	 			ps.setInt(1, clientIdList.get(i));
   	 			ps.setInt(2, bTOT.getTherapistid());
   	 			ps.setDate(3, fromDate);
   	 			ps.setDate(4, toDate);
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 			
   	 				activityIDList.add(rs.getInt("activityid"));
   	 			}
   	 		
   	 	
   	 			//Check if there is any prior balance
                logger.debug("Query:"+Queries.getQuery("fetch_prioractivity"));
                ps = c.prepareStatement(Queries.getQuery("fetch_prioractivity"));
                ps.setInt(1,bTOT.getTherapistid());
                ps.setInt(2, clientIdList.get(i));
                ps.setDate(3, fromDate);
                rs= ps.executeQuery();
                while (rs.next()) {
                	
                	priorActivityID.add(rs.getInt("activityid"));
                }
                if(priorActivityID.size() >= 1){
                 	repActTOTSize = activityIDList.size()+1;}
                 else{
                	 repActTOTSize = activityIDList.size();}
                
   	 			netPayment=0;netCharge=0;netWaiver=0;
   	 			
   	 			for(int k=0;k<repActTOTSize;k++){
   	 			
   	 				if(k<activityIDList.size()){
   	 				
   	 					
   	 				//Get The Rate for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMRate"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMRate"));
   	   	 			ps.setInt(1, bTOT.getTherapistid());
   	   	 			ps.setInt(2, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				charge = rs.getFloat("rate");
   	   	 			}
   	   	 		
   	   	 			netCharge += charge;
   	   	 			
   	   	 			
   	   	 			//Get The waiver for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps.setInt(1, bTOT.getTherapistid());
   	   	 			ps.setInt(2, clientIdList.get(i));
	   	 			ps.setInt(3, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				waiver = rs.getFloat("waiver");
   	   	 			}
   	   	 			netWaiver += waiver;
   	   	 		
   	 				charge=0;waiver=0;
   	 			
   	 			}
   	 		else{

               		 //This is for Prior Balance
                		
                		
                		
                		for(int l=0;l<priorActivityID.size();l++){
                			                         
                             logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                             ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                             ps.setInt(1, priorActivityID.get(l));
                             ps.setInt(2, bTOT.getTherapistid());
                             rs = ps.executeQuery();
                             while (rs.next()) {
                             	priorRate = rs.getFloat("amount");
                             }
                            
                             
                             logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                             ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                             ps.setInt(1, bTOT.getTherapistid());
                             ps.setInt(2, clientIdList.get(i));
                             ps.setInt(3, priorActivityID.get(l));
                             rs = ps.executeQuery();
                             while (rs.next()) {
                             	priorWaiver = rs.getFloat("waiver");
                             }
                             
                             priorCharge = priorRate - priorWaiver;
                             priorNetcharge += priorCharge;
                            
                             priorRate =0;priorWaiver=0;priorCharge=0;
                		}
                	       
                   	 logger.debug("Query:"+Queries.getQuery("fetch_priorbalance"));
                        ps = c.prepareStatement(Queries.getQuery("fetch_priorbalance"));
                        ps.setInt(1, bTOT.getTherapistid());
                        ps.setInt(2, clientIdList.get(i));
                        ps.setDate(3, fromDate);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                        	priorPayment = rs.getFloat("payment");
                        }
                      
                        priorBalance = priorNetcharge - priorPayment;
                        

                       
                       
                       
                        
                    
   	 				}
   	 			
   	 			}
   	 			
   	 				//Get The Payment for activity of a Client
	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMPayment"));
	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMPayment"));
	   	 			ps.setInt(1, bTOT.getTherapistid());
	   	 			ps.setInt(2, clientIdList.get(i));
	   	 			ps.setDate(3,fromDate);
	   	 			ps.setDate(4,toDate);
	   	 			rs = ps.executeQuery();
	   	 			while (rs.next()) {
	   	 			netPayment = rs.getFloat("payment");
	   	 			}
	   	 		
   	 			
   	 			bTherapistMoney[i].setNetcharge(Float.parseFloat(df.format(netCharge)));
   	 			bTherapistMoney[i].setNetwaiver(Float.parseFloat(df.format(netWaiver)));
   	 			bTherapistMoney[i].setNetpayment(Float.parseFloat(df.format(netPayment)));
   	 			 grossCharge = netCharge - netWaiver;
   	 			 balance = grossCharge - netPayment;
   	 			totbal = balance + priorBalance;
   	 			bTherapistMoney[i].setBalance(Float.parseFloat(df.format(totbal)));
   	 			bTherapistMoney[i].setPriorBalance(Float.parseFloat(df.format(priorBalance)));
   	 					reportTOT.add(bTherapistMoney[i]);
            
   	 				priorBalance=0;priorPayment=0;priorNetcharge=0;
   	 			
   	 		}
   	 		
   	 	}//This is for an individual client
   	 	else{
   	 		
   	 		 if(bTOT.getFlagid() == 0){
        		 logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
        		 ps.setInt(1, bTOT.getNameid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
        	 else{
        		 
        		 logger.debug("Query:"+Queries.getQuery("fetchForReportclientWflagid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReportclientWflagid"));
        		 ps.setInt(1, bTOT.getNameid());
        		 ps.setInt(2, bTOT.getFlagid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
              		
        	 }
   	 		bTOT.setClientid(clientid);
   	 		activityIDList = new ArrayList<Integer>();	
   	 		priorActivityID = new ArrayList<Integer>();
   	 	//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bTOT.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			 ReportGeneration bTherapistMoney = new ReportGeneration();
			if(clientid != 0){
   	 			
   	 		 	//Get The Name of the client
	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
	 			ps.setInt(1, bTOT.getNameid());
	 			rs = ps.executeQuery();
	 			while (rs.next()) {
	 				firstName = rs.getString("firstname");
	 				lastName = rs.getString("lastname");
	 			}
	 			clientName = firstName+" "+lastName;
	 			
	 			//Get The Activity of a Client
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMActivity"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMActivity"));
   	 			ps.setInt(1, bTOT.getClientid());
   	 			ps.setInt(2, bTOT.getTherapistid());
   	 			ps.setDate(3, fromDate);
   	 			ps.setDate(4, toDate);
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				activityIDList.add(rs.getInt("activityid"));
   	 			}
   	 			
   	 		//Check if there is any prior balance
                logger.debug("Query:"+Queries.getQuery("fetch_prioractivity"));
                ps = c.prepareStatement(Queries.getQuery("fetch_prioractivity"));
                ps.setInt(1,bTOT.getTherapistid());
                ps.setInt(2, bTOT.getClientid());
                ps.setDate(3, fromDate);
                rs= ps.executeQuery();
                while (rs.next()) {
                	
                	priorActivityID.add(rs.getInt("activityid"));
                }
                if(priorActivityID.size() >= 1){
                 	repActTOTSize = activityIDList.size()+1;}
                 else{
                	 repActTOTSize = activityIDList.size();}
               
   	 			netPayment=0;netCharge=0;netWaiver=0;
   	 		
   	 			
   	 		
   	 		
   	 			for(int k=0;k<repActTOTSize;k++){
   	 				if(k<activityIDList.size()){
   	 				//Get The Rate for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMRate"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMRate"));
   	   	 			ps.setInt(1, bTOT.getTherapistid());
   	   	 			ps.setInt(2, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				charge = rs.getFloat("rate");
   	   	 			}
   	   	 			netCharge += charge;
   	   	 			
   	   	 			//Get The waiver for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps.setInt(1, bTOT.getTherapistid());
   	   	 			ps.setInt(2, bTOT.getClientid());
	   	 			ps.setInt(3, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				waiver = rs.getFloat("waiver");
   	   	 			}
   	   	 			netWaiver += waiver;
   	   	 			
   	   	 		charge=0;waiver=0;
   	 				}
   	 				else{


                  		 //This is for Prior Balance
          
                   		for(int l=0;l<priorActivityID.size();l++){
                   			                         
                                logger.debug("Query:"+Queries.getQuery("fetch_rate"));
                                ps = c.prepareStatement(Queries.getQuery("fetch_rate"));
                                ps.setInt(1, priorActivityID.get(l));
                                ps.setInt(2, bTOT.getTherapistid());
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                	priorRate = rs.getFloat("amount");
                                }
                               
                                
                                logger.debug("Query:"+Queries.getQuery("fetch_waiver"));
                                ps = c.prepareStatement(Queries.getQuery("fetch_waiver"));
                                ps.setInt(1, bTOT.getTherapistid());
                                ps.setInt(2, bTOT.getClientid());
                                ps.setInt(3, priorActivityID.get(l));
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                	priorWaiver = rs.getFloat("waiver");
                                }
                                
                                priorCharge = priorRate - priorWaiver;
                                priorNetcharge += priorCharge;
                               
                                priorRate =0;priorWaiver=0;priorCharge=0;
                   		}
                   	       
                      	 logger.debug("Query:"+Queries.getQuery("fetch_priorbalance"));
                           ps = c.prepareStatement(Queries.getQuery("fetch_priorbalance"));
                           ps.setInt(1, bTOT.getTherapistid());
                           ps.setInt(2, bTOT.getClientid());
                           ps.setDate(3, fromDate);
                           rs = ps.executeQuery();
                           while (rs.next()) {
                           	priorPayment = rs.getFloat("payment");
                           }
                         
                           priorBalance = priorNetcharge - priorPayment;
                                               
      	 				
   	 				}

   	 				
   	 			}
   	 			
   	 				//Get The Payment for activity of a Client
	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMPayment"));
	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMPayment"));
	   	 			ps.setInt(1, bTOT.getTherapistid());
	   	 			ps.setInt(2, bTOT.getClientid());
	   	 			ps.setDate(3,fromDate);
	   	 			ps.setDate(4,toDate);
	   	 			rs = ps.executeQuery();
	   	 			while (rs.next()) {
	   	 			netPayment = rs.getFloat("payment");
	   	 			}
   	 			
	   	 			
	   	 			
   	 			bTherapistMoney.setClientname(clientName);
   	 			bTherapistMoney.setTherapistname(therapistName);
	 			bTherapistMoney.setFromdate(bTOT.getFromdate());
	 			bTherapistMoney.setTodate(bTOT.getTodate());
   	 			bTherapistMoney.setNetcharge(Float.parseFloat(df.format(netCharge)));
   	 			bTherapistMoney.setNetwaiver(Float.parseFloat(df.format(netWaiver)));
   	 			bTherapistMoney.setNetpayment(Float.parseFloat(df.format(netPayment)));
   	 			grossCharge = netCharge - netWaiver;
	 			 balance = grossCharge - netPayment;
	 			totbal = balance + priorBalance;
	 			bTherapistMoney.setBalance(Float.parseFloat(df.format(totbal)));
	 			bTherapistMoney.setPriorBalance(Float.parseFloat(df.format(priorBalance)));
   	 			reportTOT.add(bTherapistMoney);
            
			}	
			else{
				bTherapistMoney.setTherapistname(therapistName);
				bTherapistMoney.setClientname(" - ");
				bTherapistMoney.setFromdate(bTOT.getFromdate());
	 			bTherapistMoney.setTodate(bTOT.getTodate());
	 			reportTOT.add(bTherapistMoney);
			}
   	 		
   	 	
   	 	}
    }
    catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
	
	return reportTOT;
}

public List<ReportGeneration> generateReportTM(ReportGeneration bTM)  {
	List<ReportGeneration> reportTM = new ArrayList<ReportGeneration>();
	List<Integer> clientIdList = new ArrayList<Integer>();
	List<Integer> nameIdList = new ArrayList<Integer>();
	List<Integer> activityIDList = null;
	DateUtil dateUtil = new DateUtil();
	 DecimalFormat df = new DecimalFormat("###.##");
     df.setMaximumFractionDigits(2);
	Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int clientid=0;
    String firstName="";
    String lastName="";
    String clientName="";
    String therapistName="";
    float charge=0;
    float netCharge=0;
    float waiver=0;
    float netWaiver=0;
    float payment=0;
    float netPayment=0;
    Date fromDate= null;
    Date toDate=null;
    int nameid=0;
    
    try{
    	
   	 	c = ConnectionHelper.getConnection();
   	 	nameid = bTM.getNameid();
   	 	fromDate= dateUtil.convertDate(bTM.getFromdate());
   	 	toDate = dateUtil.convertDate(bTM.getTodate());
   	 	
   	 	if(nameid == 0){
   	 		if(bTM.getFlagid() == 0){
   	 			//Get all the Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientid"));
   	 			ps.setInt(1, bTM.getTherapistid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}
   	 		}
   	 		else{
   	 			//Get all the Clients with flagid
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps.setInt(1, bTM.getTherapistid());
   	 			ps.setInt(2, bTM.getFlagid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}	
   	 		}
   	 		//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bTM.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			
			
   	 		ReportGeneration bTherapistMoney[] = new ReportGeneration[clientIdList.size()];
   	 		for(int i=0;i<clientIdList.size();i++){
   	 			bTherapistMoney[i] = new ReportGeneration();
   	 			
   	 			//Get The Name of all Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
   	 			ps.setInt(1, nameIdList.get(i));
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				firstName = rs.getString("firstname");
   	 				lastName = rs.getString("lastname");
   	 			}
   	 			clientName = firstName+" "+lastName;
   	 			bTherapistMoney[i].setClientname(clientName);
   	 			bTherapistMoney[i].setTherapistname(therapistName);
   	 			bTherapistMoney[i].setFromdate(bTM.getFromdate());
   	 			bTherapistMoney[i].setTodate(bTM.getTodate());
   	 			//Get The Activity of a Client
   	 			activityIDList = new ArrayList<Integer>();
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMActivity"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMActivity"));
   	 			ps.setInt(1, clientIdList.get(i));
   	 			ps.setInt(2, bTM.getTherapistid());
   	 			ps.setDate(3, fromDate);
   	 			ps.setDate(4, toDate);
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 			
   	 				activityIDList.add(rs.getInt("activityid"));
   	 			}
   	 			netPayment=0;netCharge=0;netWaiver=0;
   	 			
   	 			for(int k=0;k<activityIDList.size();k++){
   	 			
   	 				//Get The Rate for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMRate"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMRate"));
   	   	 			ps.setInt(1, bTM.getTherapistid());
   	   	 			ps.setInt(2, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				charge = rs.getFloat("rate");
   	   	 			}
   	   	 		
   	   	 			netCharge += charge;
   	   	 			
   	   	 			
   	   	 			//Get The waiver for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps.setInt(1, bTM.getTherapistid());
   	   	 			ps.setInt(2, clientIdList.get(i));
	   	 			ps.setInt(3, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				waiver = rs.getFloat("waiver");
   	   	 			}
   	   	 			netWaiver += waiver;
   	   	 		
	   	 			
   	   	 			
   	   	 		
   	 				charge=0;waiver=0;
   	 			}
   	 			
   	 				//Get The Payment for activity of a Client
	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMPayment"));
	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMPayment"));
	   	 			ps.setInt(1, bTM.getTherapistid());
	   	 			ps.setInt(2, clientIdList.get(i));
	   	 			ps.setDate(3,fromDate);
	   	 			ps.setDate(4,toDate);
	   	 			rs = ps.executeQuery();
	   	 			while (rs.next()) {
	   	 			netPayment = rs.getFloat("payment");
	   	 			}
	   	 			
	   	 			
	   	 			
   	 			bTherapistMoney[i].setNetcharge(Float.parseFloat(df.format(netCharge)));
   	 			bTherapistMoney[i].setNetwaiver(Float.parseFloat(df.format(netWaiver)));
   	 			bTherapistMoney[i].setNetpayment(Float.parseFloat(df.format(netPayment)));
   	 		 
   	 			reportTM.add(bTherapistMoney[i]);
            
   	 			
   	 		}
   	 		
   	 	}
   	 	else{
   	 		
   	 		 if(bTM.getFlagid() == 0){
        		 logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
        		 ps.setInt(1, bTM.getNameid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
        	 else{
        		 
        		 logger.debug("Query:"+Queries.getQuery("fetchForReportclientWflagid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReportclientWflagid"));
        		 ps.setInt(1, bTM.getNameid());
        		 ps.setInt(2, bTM.getFlagid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
              		
        	 }
   	 		bTM.setClientid(clientid);
   	 		activityIDList = new ArrayList<Integer>();	
   	 		
   	 	//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bTM.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			 ReportGeneration bTherapistMoney = new ReportGeneration();
			if(clientid != 0){
   	 			
   	 		 	//Get The Name of the client
	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
	 			ps.setInt(1, bTM.getNameid());
	 			rs = ps.executeQuery();
	 			while (rs.next()) {
	 				firstName = rs.getString("firstname");
	 				lastName = rs.getString("lastname");
	 			}
	 			clientName = firstName+" "+lastName;
	 			
	 			//Get The Activity of a Client
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMActivity"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMActivity"));
   	 			ps.setInt(1, bTM.getClientid());
   	 			ps.setInt(2, bTM.getTherapistid());
   	 			ps.setDate(3, fromDate);
   	 			ps.setDate(4, toDate);
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				activityIDList.add(rs.getInt("activityid"));
   	 			}
   	 			
   	 		
   	 		
   	 			for(int k=0;k<activityIDList.size();k++){
   	 				
   	 				//Get The Rate for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMRate"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMRate"));
   	   	 			ps.setInt(1, bTM.getTherapistid());
   	   	 			ps.setInt(2, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				charge = rs.getFloat("rate");
   	   	 			}
   	   	 			netCharge += charge;
   	   	 			
   	   	 			//Get The waiver for activity of a Client
   	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMWaiver"));
   	   	 			ps.setInt(1, bTM.getTherapistid());
   	   	 			ps.setInt(2, bTM.getClientid());
	   	 			ps.setInt(3, activityIDList.get(k));
   	   	 			rs = ps.executeQuery();
   	   	 			while (rs.next()) {
   	   	 				waiver = rs.getFloat("waiver");
   	   	 			}
   	   	 			netWaiver += waiver;
   	   	 			
   	   	 		
   	 				charge=0;waiver=0;
   	 			}
   	 			
   	 			//Get The Payment for activity of a Client
	   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMICPayment"));
	   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMICPayment"));
	   	 			ps.setInt(1, bTM.getTherapistid());
	   	 			ps.setInt(2, bTM.getClientid());
	   	 			//ps.setInt(3, activityIDList.get(k));
	   	 			ps.setDate(3,fromDate);
	   	 			ps.setDate(4,toDate);
	   	 			rs = ps.executeQuery();
	   	 			while (rs.next()) {
	   	 			netPayment = rs.getFloat("payment");
	   	 			}
	   	 			
   	 			bTherapistMoney.setClientname(clientName);
   	 			bTherapistMoney.setTherapistname(therapistName);
	 			bTherapistMoney.setFromdate(bTM.getFromdate());
	 			bTherapistMoney.setTodate(bTM.getTodate());
   	 			bTherapistMoney.setNetcharge(Float.parseFloat(df.format(netCharge)));
   	 			bTherapistMoney.setNetwaiver(Float.parseFloat(df.format(netWaiver)));
   	 			bTherapistMoney.setNetpayment(Float.parseFloat(df.format(netPayment)));
			
   	 			reportTM.add(bTherapistMoney);
            
			}	
			else{
				bTherapistMoney.setTherapistname(therapistName);
				bTherapistMoney.setClientname(" - ");
				bTherapistMoney.setFromdate(bTM.getFromdate());
	 			bTherapistMoney.setTodate(bTM.getTodate());
	 			reportTM.add(bTherapistMoney);
			}
   	 		
   	 	
   	 	}
    }
    catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
	
	return reportTM;
}

public List<ReportGeneration> generateReportCL(ReportGeneration bCL)  {
	List<ReportGeneration> reportCL = new ArrayList<ReportGeneration>();
	List<Integer> clientIdList = new ArrayList<Integer>();
	List<Integer> nameIdList = new ArrayList<Integer>();
	
	Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int clientid=0;
    String firstName="";
    String lastName="";
    String lastNameUpTo4Char = "";
    String clientName="";
    String therapistName="";
    String mobile="";
    int nameid=0;
    
    try{
    	
   	 	c = ConnectionHelper.getConnection();
   	 	nameid = bCL.getNameid();
   	 	
   	 	
   	 	if(nameid == 0){
   	 		if(bCL.getFlagid() == 0){
   	 			//Get all the Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientid"));
   	 			ps.setInt(1, bCL.getTherapistid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}
   	 		}
   	 		else{
   	 			//Get all the Clients with flagid
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMClientidWFlag"));
   	 			ps.setInt(1, bCL.getTherapistid());
   	 			ps.setInt(2, bCL.getFlagid());
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				clientIdList.add(rs.getInt("clientid"));
   	 				nameIdList.add(rs.getInt("name_id"));
   	 			}	
   	 		}
   	 		//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bCL.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			
			
   	 		ReportGeneration bClientList[] = new ReportGeneration[clientIdList.size()];
   	 		for(int i=0;i<clientIdList.size();i++){
   	 		bClientList[i] = new ReportGeneration();
   	 			
   	 			//Get The Name of all Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
   	 			ps.setInt(1, nameIdList.get(i));
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				firstName = rs.getString("firstname");
   	 				lastName = rs.getString("lastname");
   	 			}
   	 		
			if(lastName!=null)
				lastNameUpTo4Char = lastName.substring(0, Math.min(lastName.length(), 4));
   	 			
				clientName = firstName+" "+lastNameUpTo4Char;
   	 			bClientList[i].setClientname(clientName);
   	 			bClientList[i].setTherapistname(therapistName);
   	 			
   	 			//Get The Mobile Number of all Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForCLMobile"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForCLMobile"));
   	 			ps.setInt(1, clientIdList.get(i));
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				mobile = rs.getString("mobile");
   	 			}
   	 			bClientList[i].setMobile(mobile);
   	 		 
   	 			reportCL.add(bClientList[i]);
            
   	 			
   	 		}
   	 		
   	 	}
   	 	else{
   	 		
   	 		 if(bCL.getFlagid() == 0){
        		 logger.debug("Query:"+Queries.getQuery("fetchForReport_clientid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReport_clientid"));
        		 ps.setInt(1, bCL.getNameid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
        	 }
        	 else{
        		 
        		 logger.debug("Query:"+Queries.getQuery("fetchForReportclientWflagid"));
        		 ps = c.prepareStatement(Queries.getQuery("fetchForReportclientWflagid"));
        		 ps.setInt(1, bCL.getNameid());
        		 ps.setInt(2, bCL.getFlagid());
        		
        		 rs = ps.executeQuery();
              		while (rs.next()) {
              		clientid = rs.getInt("clientid");
              		}
              		
        	 }
   	 		bCL.setClientid(clientid);
   	 		
   	 		
   	 	//Get Therapist Name
   	 		logger.debug("Query:"+Queries.getQuery("fetchForTMTherapist"));
			ps = c.prepareStatement(Queries.getQuery("fetchForTMTherapist"));
			ps.setInt(1, bCL.getTherapistid());
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
			}
			therapistName = firstName+" "+lastName;
			 ReportGeneration bClientList = new ReportGeneration();
			if(clientid != 0){
   	 			
   	 		 	//Get The Name of the client
	 			logger.debug("Query:"+Queries.getQuery("fetchForTMNameid"));
	 			ps = c.prepareStatement(Queries.getQuery("fetchForTMNameid"));
	 			ps.setInt(1, bCL.getNameid());
	 			rs = ps.executeQuery();
	 			while (rs.next()) {
	 				firstName = rs.getString("firstname");
	 				lastName = rs.getString("lastname");
	 			}
	 			if(lastName!=null)
					lastNameUpTo4Char = lastName.substring(0, Math.min(lastName.length(), 4));
	   	 			
	 			clientName = firstName+" "+lastNameUpTo4Char;
	 			
	 			//Get The Mobile Number of all Clients
   	 			logger.debug("Query:"+Queries.getQuery("fetchForCLMobile"));
   	 			ps = c.prepareStatement(Queries.getQuery("fetchForCLMobile"));
   	 			ps.setInt(1, clientid);
   	 			rs = ps.executeQuery();
   	 			while (rs.next()) {
   	 				mobile = rs.getString("mobile");
   	 			}
   	 			bClientList.setMobile(mobile);
   	 			bClientList.setClientname(clientName);
   	 			bClientList.setTherapistname(therapistName);
	 			
   	 			reportCL.add(bClientList);
            
			}	
			else{
				bClientList.setTherapistname(therapistName);
				bClientList.setClientname(" - ");
				bClientList.setMobile("-");
	 			reportCL.add(bClientList);
			}
   	 		
   	 	
   	 	}
    }
    catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
	
	return reportCL;
}
}
