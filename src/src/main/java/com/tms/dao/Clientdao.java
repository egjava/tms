package com.tms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tms.util.DateUtil;
import com.tms.util.Queries;
import com.tms.bean.ActivityType;
import com.tms.bean.Client;
import com.tms.bean.Diagnosis;
import com.tms.bean.Flag;
import com.tms.bean.Therapist;
import com.tms.bean.TherapistRate;
import com.tms.database.ConnectionHelper;

import org.apache.log4j.Logger;
public class Clientdao {
	final static Logger logger = Logger.getLogger(Clientdao.class);
	DateUtil dateUtil = new DateUtil();
	
public Client createClient(Client client)  {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
         int nameid = 0;
         int addressid = 0;
         int contactid= 0;
         int paymentid= 0;
         int activityno= 0;
         int therapistid= 0;
         int clientid =0;
         Date stheraphy =null;
         int[] diagnosiscd =null;
        try {
        	logger.debug("****Creating Client in db******");
        	//Insert Name values to Name table
            c = ConnectionHelper.getConnection();
            c.setAutoCommit(false);
            logger.debug("Query:"+Queries.getQuery("insert_clientname"));
            ps = c.prepareStatement(Queries.getQuery("insert_clientname"));
            ps.setString(1, client.getFirstname());
            ps.setString(2, client.getLastname());
            ps.setString(3, client.getMiddlename());
            ps.setString(4, client.getSex());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Client:Nameid "+rs.getInt(1));
                nameid =rs.getInt(1);
            }
            logger.info("Client: Insert"+ps+":  Generate Name:"+nameid);
            //Insert values into address table
            logger.debug("Query:"+Queries.getQuery("insert_clientaddress"));
            ps = c.prepareStatement(Queries.getQuery("insert_clientaddress"));
            ps.setString(1, client.getAddress1());
            ps.setString(2, client.getAddress2());
            ps.setString(3, client.getCity());
            ps.setString(4, client.getState());
            ps.setInt(5, client.getZip());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Client addressid "+rs.getInt(1));
                addressid =rs.getInt(1);
            }
            logger.info("Client: Insert Address"+ps+":  Generate addressid:"+addressid);
            //Insert values into contact table
            logger.debug("Query:"+Queries.getQuery("insert_clientcontact"));
            ps = c.prepareStatement(Queries.getQuery("insert_clientcontact"));
            ps.setString(1, client.getHomephone());
            ps.setString(2, client.getWorkphone());
            ps.setString(3, client.getMobile());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated client contact Id"+rs.getInt(1));
                contactid =rs.getInt(1);
            }
            logger.info("Client: Insert contact"+ps+":  Generate contactid:"+contactid);
            
          //Insert values into payment table
            logger.debug("Query:"+Queries.getQuery("insert_clientpayment"));
            ps = c.prepareStatement(Queries.getQuery("insert_clientpayment"));
            logger.debug("Activity Type:"+client.getActivitytype());
            ps.setString(1, client.getActivitytype());
            ps.setFloat(2, client.getPayment());
           stheraphy = dateUtil.convertDate(client.getStheraphy());
            ps.setDate(3, stheraphy);
            
            ps.setInt(4, client.getTherapistid());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Payment id"+rs.getInt(1));
                paymentid =rs.getInt(1);
            }
            logger.info("Client: Insert Payment"+ps+":  Generate paymentid:"+paymentid);
            //Insert values into Client table
            logger.debug("Query:"+Queries.getQuery("insert_client"));
            ps = c.prepareStatement(Queries.getQuery("insert_client"));
            ps.setInt(1, nameid);
            ps.setInt(2, addressid);
            ps.setInt(3, contactid);
            ps.setInt(4, paymentid);
            ps.setInt(5, client.getFlagid());
            ps.setInt(6, client.getCpt());
            Date dob = dateUtil.convertDate(client.getDob());
            ps.setDate(7, dob);
            ps.setInt(8, client.getReferralid());
            ps.setInt(9, client.getTherapistid());
            Date dov = dateUtil.convertDate(client.getDov());
            ps.setDate(10, dov);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Clientid"+rs.getInt(1));
                clientid =rs.getInt(1);
            }
            client.setClientid(clientid);
            logger.info("Client: Insert client"+ps+":  Generate clientid:"+clientid);
            //update clientid in payments table
            logger.debug("Query:"+Queries.getQuery("update_clientidInPayment"));
            ps = c.prepareStatement(Queries.getQuery("update_clientidInPayment"));
            ps.setInt(1, clientid);
            ps.setString(2, client.getActivitytype());
            ps.setInt(3, client.getTherapistid());
            ps.setInt(4, paymentid);
            stheraphy = dateUtil.convertDate(client.getStheraphy());
            ps.setDate(5, stheraphy);
            ps.setFloat(6, client.getPayment());
            ps.executeUpdate();
            logger.info("Client: Update client"+ps);
            
          //Insert values into client_diagnosis table
            diagnosiscd = client.getDiagnosiscode();
            logger.debug("Query:"+Queries.getQuery("insert_clientdiagnosis1"));
            for(int i=0;i<diagnosiscd.length;i++){
            ps = c.prepareStatement(Queries.getQuery("insert_clientdiagnosis1"));
            ps.setInt(1, clientid);
            ps.setInt(2, diagnosiscd[i]);
            ps.executeUpdate();	
            }
            logger.info("Client: Insert Diagnosis"+ps);
          //Insert values into Activity table
            logger.debug("Query:"+Queries.getQuery("insert_activity"));
            ps = c.prepareStatement(Queries.getQuery("insert_activity"));
            logger.debug("Activity Type:"+client.getActivitytype());
           
            ps.setInt(1, clientid);
            ps.setInt(2, client.getTherapistid());
            ps.setString(3, client.getActivitytype());
            ps.setInt(4, paymentid);
            stheraphy = dateUtil.convertDate(client.getStheraphy());
            ps.setDate(5, stheraphy);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Activity table No"+rs.getInt(1));
                activityno =rs.getInt(1);
            }
            logger.info("Client: Insert Activity"+ps);
            c.commit();
            logger.info("Client: Table Committed");
           
        }
        catch(SQLException e){
        	 try {
                 c.rollback();
                 e.printStackTrace();
                 throw new RuntimeException(e);
                 } catch(SQLException sqle){
            	sqle.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return client;
    }

public List<Client> fetchClient()  {
	logger.debug("**********FetchClient Method is called in ClientDao*****");
	List<Client> list = new ArrayList<Client>();
    Connection c = null;
	
    try {
        c = ConnectionHelper.getConnection();
        logger.debug("Query:"+Queries.getQuery("fetch_client"));
        String sql = Queries.getQuery("fetch_client");
        Statement statement = c.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(processRow(rs));
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return list;
}


//Remove Client Method 
public void removeClient(int clientid) {
	logger.debug("*****Clientdao removeClient method called.********");
    Connection c = null;
    PreparedStatement ps;
    ResultSet rs;
    int nameid=0;
    int addressid=0;
    int contactid=0;
    int paymentid=0;
    try {
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
        logger.debug("Query:"+Queries.getQuery("selectForRemove_client"));
        ps = c.prepareStatement(Queries.getQuery("selectForRemove_client"));
        ps.setInt(1, clientid);
        rs = ps.executeQuery();
        while (rs.next()) {
           nameid = rs.getInt("name_id");
           addressid = rs.getInt("address_id");
           contactid = rs.getInt("contact_id");
           paymentid  = rs.getInt("paymentid");
        }
      
        //Delete values from Name table
        logger.debug("Query:"+Queries.getQuery("delete_clientname"));
        ps = c.prepareStatement(Queries.getQuery("delete_clientname"));
        ps.setInt(1, nameid);
        ps.executeUpdate();
        logger.info("Client: Delete name "+ps);
      //Delete values from address table
        logger.debug("Query:"+Queries.getQuery("delete_clientaddress"));
        ps = c.prepareStatement(Queries.getQuery("delete_clientaddress"));
        ps.setInt(1, addressid);
        ps.executeUpdate();
        logger.info("Client: Delete address "+ps);
      //Delete values from contact table
        logger.debug("Query:"+Queries.getQuery("delete_clientcontact"));
        ps = c.prepareStatement(Queries.getQuery("delete_clientcontact"));
        ps.setInt(1, contactid);
        ps.executeUpdate();
        logger.info("Client: Delete contact "+ps);
      //Delete values from payment table
        logger.debug("Query:"+Queries.getQuery("delete_clientpayment"));
        ps = c.prepareStatement(Queries.getQuery("delete_clientpayment"));
        ps.setInt(1, paymentid);
        ps.setInt(2, clientid);
        ps.executeUpdate();
        logger.info("Client: Delete payment "+ps);
               
        //Delete values from client table
        logger.debug("Query:"+Queries.getQuery("delete_client"));
        ps = c.prepareStatement(Queries.getQuery("delete_client"));
        ps.setInt(1, clientid);
         ps.executeUpdate();
         logger.info("Client: Delete Client "+ps);
       //Delete values from client-diagnosis table
         logger.debug("Query:"+Queries.getQuery("delete_clientdiagnosis1"));
         ps = c.prepareStatement(Queries.getQuery("delete_clientdiagnosis1"));
         ps.setInt(1, clientid);
          ps.executeUpdate();
          logger.info("Client: Delete Diagnosis "+ps);
          //Delete values from actvity table
          logger.debug("Query:"+Queries.getQuery("delete_activity"));
          ps = c.prepareStatement(Queries.getQuery("delete_activity"));
          ps.setInt(1, clientid);
           ps.executeUpdate();
           logger.info("Client: Delete Activity "+ps);
           //Delete values from therapist_client table for waiver
           logger.debug("Query:"+Queries.getQuery("delete_therapist_client"));
           ps = c.prepareStatement(Queries.getQuery("delete_therapist_client"));
           ps.setInt(1, clientid);
            ps.executeUpdate();
            logger.info("Client: Delete Therapist Client "+ps);
           c.commit();
           logger.info("Client: Delete Committed");
        
        
    } 
    catch(SQLException e){
   	 try {
            c.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
       } catch(SQLException sqle){
       	sqle.printStackTrace();
       }
   }
    catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
}

	//Update Client

	public Client updateClient(Client client, int clientid) {
		Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        int nameid =0;
        int addressid =0;
        int contactid =0;
        int paymentid =0;
        int[] diagnosiscd =null;
        int[] exdiagnosisid =null;
        Date stheraphy =null;
		try{
			
			logger.debug("****Updating Client in db******");
			
        	//Insert Name values to Name table
            c = ConnectionHelper.getConnection();
            c.setAutoCommit(false);
            logger.debug("Query:"+Queries.getQuery("selectforUpdate_client"));
            ps =c.prepareStatement(Queries.getQuery("selectforUpdate_client"));
            ps.setInt(1,clientid);
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
            	nameid = rs.getInt("name_id");
            	addressid = rs.getInt("address_id");
            	contactid = rs.getInt("contact_id");
            	paymentid = rs.getInt("paymentid");
            }
            //Update Name table
            logger.debug("Query:"+Queries.getQuery("update_clientname"));
            ps = c.prepareStatement(Queries.getQuery("update_clientname"));
            ps.setString(1, client.getFirstname());
            ps.setString(2, client.getLastname());
            ps.setString(3, client.getMiddlename());
            ps.setString(4, client.getSex());
            ps.setInt(5,nameid);
            ps.executeUpdate();
            logger.info("Client: Update name "+ps);
            //Update Address table
            logger.debug("Query:"+Queries.getQuery("update_clientaddress"));
            ps = c.prepareStatement(Queries.getQuery("update_clientaddress"));
            ps.setString(1, client.getAddress1());
            ps.setString(2, client.getAddress2());
            ps.setString(3, client.getCity());
            ps.setString(4, client.getState());
            ps.setInt(5, client.getZip());
            ps.setInt(6, addressid);
            ps.executeUpdate();
            logger.info("Client: Update Adress "+ps);
            //Update values into contact table
            logger.debug("Query:"+Queries.getQuery("update_clientcontact"));
            ps = c.prepareStatement(Queries.getQuery("update_clientcontact"));
            ps.setString(1, client.getHomephone());
            ps.setString(2, client.getWorkphone());
            ps.setString(3, client.getMobile());
            ps.setInt(4, contactid);
            ps.executeUpdate();
            logger.info("Client: Update Contact "+ps);          
          //Update values into payment table
            logger.debug("Query:"+Queries.getQuery("update_clientpayment"));
            ps = c.prepareStatement(Queries.getQuery("update_clientpayment"));           
            ps.setString(1, client.getActivitytype());
            ps.setFloat(2, client.getPayment());
            stheraphy = dateUtil.convertDate(client.getStheraphy());
            ps.setDate(3, stheraphy);
            ps.setInt(4, paymentid);
            ps.setInt(5, client.getTherapistid());
            ps.setInt(6, clientid);
            ps.executeUpdate();
            logger.info("Client: Update Payment "+ps);
            //Update values into Client table
            logger.debug("Query:"+Queries.getQuery("update_client"));
            ps = c.prepareStatement(Queries.getQuery("update_client"));
            ps.setInt(1, client.getFlagid());
            ps.setInt(2, client.getCpt());
            Date dob = dateUtil.convertDate(client.getDob());
            ps.setDate(3, dob);
            ps.setInt(4, client.getReferralid());
            ps.setInt(5, client.getTherapistid());
            Date dov = dateUtil.convertDate(client.getDov());
            ps.setDate(6, dov);
            ps.setInt(7, clientid);
            ps.executeUpdate();
            logger.info("Client: Update Client "+ps);
            //Get diagnosisid from client_diagnosis table
            logger.debug("Query:"+Queries.getQuery("selectForUpdate_clientdiagnosis"));
            ps = c.prepareStatement(Queries.getQuery("selectForUpdate_clientdiagnosis"));
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            rs.next();
            int rowcount = rs.getInt("rowcount");
            rs.close();
            logger.debug("Rowcount:"+rowcount);
            exdiagnosisid = new int[rowcount];
            logger.debug("Query:"+Queries.getQuery("selectForUpdate_diagnosisid"));
            ps = c.prepareStatement(Queries.getQuery("selectForUpdate_diagnosisid"));
            ps.setInt(1, clientid);
            rs = ps.executeQuery();
            int k = 0;
            while(rs.next()){
            	exdiagnosisid[k] =rs.getInt("diagnosisid");
            	k++;
            }
          
            //Update client_diagnosis  table
            diagnosiscd = client.getDiagnosiscode();
            if(exdiagnosisid.length <= diagnosiscd.length){
            	for(int i=0;i<exdiagnosisid.length;i++){
            		  logger.debug("Query:"+Queries.getQuery("update_clientdiagnosis"));
            		ps = c.prepareStatement(Queries.getQuery("update_clientdiagnosis"));
            		logger.debug("diagnosiscode"+diagnosiscd[i]);
            		ps.setInt(1, diagnosiscd[i]);
            		ps.setInt(2, clientid);
            		ps.setInt(3, exdiagnosisid[i]);
            		ps.executeUpdate();	
            		  logger.info("Client: Update Diagnosis Code "+ps);
            	}
            }
            else{
            	for(int i=0;i<diagnosiscd.length;i++){
            		 logger.debug("Query:"+Queries.getQuery("update_clientdiagnosis"));
            		ps = c.prepareStatement(Queries.getQuery("update_clientdiagnosis"));
            		logger.debug("diagnosiscode"+diagnosiscd[i]);
            		ps.setInt(1, diagnosiscd[i]);
            		ps.setInt(2, clientid);
            		ps.setInt(3, exdiagnosisid[i]);
            		ps.executeUpdate();	
            		logger.info("Client: Update Diagnosis Code "+ps);
            	}
            }
           
            if(exdiagnosisid.length < diagnosiscd.length){
            	   	logger.debug("Inserting New DiagnosisCode"+diagnosiscd.length);
            	for(int i=exdiagnosisid.length; i<diagnosiscd.length;i++)
            	{
            		logger.debug("Exdiagnosis id length:"+exdiagnosisid.length+"Diagnpsiscd:"+diagnosiscd[i]);
            		 logger.debug("Query:"+Queries.getQuery("insert_clientdiagnosis"));
            		ps = c.prepareStatement(Queries.getQuery("insert_clientdiagnosis"));        		
            		ps.setInt(1, diagnosiscd[i]);        		
                    ps.setInt(2, clientid);
                    ps.executeUpdate();
                    logger.info("Client: Insert Diagnosis Code "+ps);
                    
            	}
            }
            //if a diagnosis code is removed
            if(exdiagnosisid.length > diagnosiscd.length){
            	
        	   	logger.debug("Deleting Diagnosis Code"+exdiagnosisid.length );
        	for(int i=diagnosiscd.length; i<exdiagnosisid.length;i++)
        	{
        		logger.debug("Deleting the class removed from db:"+exdiagnosisid[i]);
        		 logger.debug("Query:"+Queries.getQuery("delete_clientdiagnosis"));
        		ps = c.prepareStatement(Queries.getQuery("delete_clientdiagnosis"));        		
        		ps.setInt(1, exdiagnosisid[i]);  
                ps.executeUpdate();
                logger.info("Client: Delete Diagnosis Code "+ps);
        	}
        }
            
          //Update values into Activity table
            logger.debug("Query:"+Queries.getQuery("update_activity"));
            ps = c.prepareStatement(Queries.getQuery("update_activity"));
            ps.setInt(1, client.getTherapistid());
            ps.setString(2, client.getActivitytype());
            ps.setInt(3, paymentid);
            stheraphy = dateUtil.convertDate(client.getStheraphy());
            ps.setDate(4, stheraphy);
            ps.setInt(5, clientid);
            ps.executeUpdate();
            logger.info("Client: Update Activity "+ps);
            c.commit();
            logger.info("Client: Update Committed");
		}
		 catch(SQLException e){
        	 try {
                 c.rollback();
                 e.printStackTrace();
                 throw new RuntimeException(e);
            } catch(SQLException sqle){
            	sqle.printStackTrace();
            }
        }
		 catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				ConnectionHelper.close(c);
			}
	        return client;
		
	}
	
	public Client getClientRate(int therapistid,int clientid) {
		logger.debug("********Client Rate Method called***********"+clientid);
	    Connection c = null;
	    int[] activityid;
	    int nameid =0;
	    float[] amount;
	    String firstname ="";
	    String lastname ="";
	    String[] rateper;
	    int waiveractid=0;
	    float waiverk =0;
	    		float netchk=0;
	    int[] rateperval;
	    int[] actid;
	    float[] waiver;
		float[] netch;
	    ArrayList<Integer> lactivityid = new ArrayList<Integer>();
	    ArrayList<Float> lamount = new ArrayList<Float>();
	    ArrayList<Integer> lactid = new ArrayList<Integer>();
	    ArrayList<Float> lwaiver = new ArrayList<Float>();
	    ArrayList<Float> lnetch = new ArrayList<Float>();
	    ArrayList<String> lrateper = new ArrayList<String>();
	    ArrayList<Integer> lrateperval = new ArrayList<Integer>();
	    Client clientrate = new Client();
	    try {
	    	
	    	
	        c = ConnectionHelper.getConnection();
	        c.setAutoCommit(false);
	        logger.debug("Query:"+Queries.getQuery("select_clientnameid"));
	        PreparedStatement ps = c.prepareStatement(Queries.getQuery("select_clientnameid"));
	        ps.setInt(1, clientid);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	nameid = rs.getInt("name_id");
	        }
	        logger.debug("Query:"+Queries.getQuery("select_clientnamedetails"));
	        ps = c.prepareStatement(Queries.getQuery("select_clientnamedetails"));
	        ps.setInt(1, nameid);
	         rs = ps.executeQuery();
	        while (rs.next()) {
	        	firstname = rs.getString("firstname");
	        	lastname = rs.getString("lastname");
	        }
	        
	        logger.debug("Query:"+Queries.getQuery("select_rate"));
	        ps = c.prepareStatement(Queries.getQuery("select_rate"));
	        ps.setInt(1, therapistid);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	        	lactivityid.add(rs.getInt("activityid"));
	        	lamount.add(rs.getFloat("amount"));
	        	lrateper.add(rs.getString("rateper"));
	        	lrateperval.add(rs.getInt("rateperval"));
	         }
	        activityid = new int[lactivityid.size()];
	        amount = new float[lamount.size()];
	        rateper = new String[lrateper.size()];
	        rateperval = new int[lrateperval.size()];
	        
	        for(int i=0;i<lactivityid.size(); i++){
	        	activityid[i] = lactivityid.get(i);
	        	amount[i] 		= lamount.get(i);
	        	rateper[i] 		= lrateper.get(i);
	        	rateperval[i] 	= lrateperval.get(i);
	        }
	        //Get Waiver and Netcharge from therapist_clientrate
	        for(int i=0;i<lactivityid.size(); i++){
	        ps = c.prepareStatement("select activityid,waiver,netcharge from therapist_clientrate where therapistid=? and clientid=? and activityid=?");
	        ps.setInt(1, therapistid);
	        ps.setInt(2, clientid);
	        ps.setInt(3, lactivityid.get(i));
	        rs = ps.executeQuery();
	        while (rs.next()) {
	        	
	        	lactid.add(rs.getInt("activityid"));
	        	lwaiver.add(rs.getFloat("waiver"));
	        	lnetch.add(rs.getFloat("netcharge"));
	        	
	         }
	        }
	        logger.debug("lwaiver"+lwaiver.size());
        	logger.debug("lnetcharge"+lnetch.size());
        	actid = new int[lactid.size()];
        	waiver = new float[lwaiver.size()];
	        netch = new float[lnetch.size()];
	        if(lwaiver.size() >0){
	        	 
	        	
	        	if(lwaiver.size() == lactivityid.size()){
	        		
	        		for(int i=0;i<lactivityid.size(); i++){
	        			waiver[i] = lwaiver.get(i);
	        			
	        			if(lnetch.get(i) == 0)
	        				netch[i] 	= lamount.get(i);
	        			else
	        			netch[i] 		= lnetch.get(i);
	        		}
	        	}
	        	else{
	        		waiver = new float[lactivityid.size()];
	        		 netch = new float[lactivityid.size()];
	        		for(int i=0;i<lactid.size(); i++){
	        			actid[i] = lactid.get(i);
	        			
	        		}
	        			for(int k=0;k<lactivityid.size(); k++){
	        				
	        				for(int i=0;i<lwaiver.size(); i++){
	        					if(lactivityid.get(k).equals(actid[i]))
	        					{
	        						waiveractid = actid[i];
	        						waiverk = lwaiver.get(i);
	        						netchk = lnetch.get(i);
	        						break;
	        					}
	        				}
	        				
	        					
	        					if(lactivityid.get(k).equals(waiveractid)){
	        						waiver[k] = waiverk;
	        						if(netchk == 0 || netchk == 0.0)
	        							netch[k]  = lamount.get(k);
	        						else
	        						netch[k] = netchk;
	        					}
	        					else{
	        						
	        						waiver[k] = 0;
	        						netch[k] = lamount.get(k);
	        					}
	        				
	        				//}
	        			}
	        	}
	        }
	        clientrate.setArrtActivityType(activityid);
	        clientrate.setArrtRate(amount);
	        clientrate.setArrtRateper(rateper);
	        clientrate.setArrtRateperVal(rateperval);
	        clientrate.setWaiver(waiver);
	        clientrate.setNetch(netch);
	        clientrate.setFirstname(firstname);
	        clientrate.setLastname(lastname);
	        c.commit();
	        
	    } catch(SQLException e){
	      	 try {
	             c.rollback();
	             e.printStackTrace();
	             throw new RuntimeException(e);
	        } catch(SQLException sqle){
	        	sqle.printStackTrace();
	        }
	    }catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	    return clientrate;
	}
	
	public Client updateWaiver(Client waiver)  {
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    int therapistid= 0;
	    int clientid = 0;
	    int[] arrayActivitytype;
		float[] arrayWaiver;
		float[] arrayNetcharge;
		 int rowcount = 0;
	    try {
	        c = ConnectionHelper.getConnection();
	        arrayActivitytype =waiver.getActivityid();
	        arrayWaiver = waiver.getWaiver();
	        arrayNetcharge = waiver.getNetch();
	        therapistid = waiver.getTherapistid();
	        clientid = waiver.getClientid();
	        int lenWaiver = arrayWaiver.length;
	        int lenActType = arrayActivitytype.length;
	       logger.debug("lenWaiver:"+lenWaiver);
	       logger.debug("lenActType:"+lenActType);
	       for(int i= 0; i<arrayActivitytype.length;i++){
	        	
	       ps = c.prepareStatement("select count(*) as rowcount from therapist_clientrate where clientid=? and therapistid=? and activityid=?");
	       ps.setInt(1, clientid);	
	       ps.setInt(2, therapistid);
	       	logger.debug("Therapistid"+therapistid);
	        logger.debug("Clientid"+clientid);
	        ps.setInt(3, arrayActivitytype[i]);
	        logger.debug("ActvityType"+arrayActivitytype[i]);
	        rs = ps.executeQuery();
	        if(rs.next()){
	        	logger.debug("Inside next resultset");
	         rowcount = rs.getInt("rowcount");
	        }
	       
	        logger.debug("Rowcount:"+rowcount);
	       //rs.close();
	        if(rowcount >= 1){
	        	ps = c.prepareStatement("Update therapist_clientrate set waiver=?, netcharge=? where therapistid =? and clientid =? and activityid=?");
	        	ps.setFloat(1, arrayWaiver[i]);
			    ps.setFloat(2,arrayNetcharge[i]);
	        	ps.setInt(3, therapistid);
		        ps.setInt(4, clientid);
		        ps.setInt(5, arrayActivitytype[i]);
		        ps.executeUpdate();
		        logger.info("Client: Update therapist_clientrate :"+ps);
		       // ps.close();
	        }
	        else{
	        logger.debug("It is a new INsert");	 
	        PreparedStatement pst = c.prepareStatement("INSERT INTO therapist_clientrate (therapistid, clientid, activityid, waiver, netcharge) VALUES (?,?,?,?,?)");
	        pst.setInt(1, therapistid);
	        pst.setInt(2, clientid);
	        pst.setInt(3, arrayActivitytype[i]);
	        pst.setFloat(4, arrayWaiver[i]);
	        pst.setFloat(5,arrayNetcharge[i]);
	        pst.executeUpdate();
	        logger.info("Client: INsert Therapist client rate "+ps);
	        }
	       }
	        
	    }
	    catch(SQLException e){
	      	 try {
	            c.rollback();
	            e.printStackTrace();
	            throw new RuntimeException(e);
	       } catch(SQLException sqle){
	       	sqle.printStackTrace();
	       }
	   }catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	    return waiver;
	}

public Client addWaiver(Client waiver)  {
    Connection c = null;
    PreparedStatement ps = null;
    int therapistid= 0;
    int clientid = 0;
    int[] arrayActivitytype;
	float[] arrayWaiver;
	float[] arrayNetcharge;
    
    
    try {
        c = ConnectionHelper.getConnection();
        arrayActivitytype =waiver.getActivityid();
        arrayWaiver = waiver.getWaiver();
        arrayNetcharge = waiver.getNetch();
        therapistid = waiver.getTherapistid();
        clientid = waiver.getClientid();
        int lenWaiver = arrayWaiver.length;
        int lenActType = arrayActivitytype.length;
       logger.debug("lenWaiver:"+lenWaiver);
       logger.debug("lenActType:"+lenActType);
        
        for(int i= 0; i<arrayActivitytype.length;i++){
        	
        ps = c.prepareStatement("INSERT INTO therapist_clientrate (therapistid, clientid, activityid, waiver, netcharge) VALUES (?,?,?,?,?)");
        ps.setInt(1, therapistid);
        ps.setInt(2, clientid);
        ps.setInt(3, arrayActivitytype[i]);
        ps.setFloat(4, arrayWaiver[i]);
        ps.setFloat(5,arrayNetcharge[i]);
        ps.executeUpdate();
        logger.info("Client: Insert Therapist clientrate "+ps);
        }
        
    } 
    catch(SQLException e){
      	 try {
            c.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
       } catch(SQLException sqle){
       	sqle.printStackTrace();
       }
   }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return waiver;
}

//Add Payment
public Client addPayment(Client client)  {
	
    Connection c = null;
    ResultSet rs= null;
    PreparedStatement ps = null;
    String sql="";
    int clientid=0;
    Date paymentDate = null;
    try {
    	logger.debug("****Add New Payments for Client ******"+client.getNameid());
    	//Insert Name values to Name table
        c = ConnectionHelper.getConnection();
        
        ps = c.prepareStatement("select clientid from client where name_id=?");
        ps.setInt(1, client.getNameid());
        rs = ps.executeQuery();
        while (rs.next()) {
        	clientid = rs.getInt("clientid");
        	
        }
        
        
        ps = c.prepareStatement("insert into payment(activityid, paymentdate, amount,clientid,therapistid) values(?,?,?,?,?)");
        ps.setString(1,  client.getActivitytype());
      
        /*DateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(client.getPaymentdate());
        cal.add(Calendar.DATE, 1);
        String dateInString = dateFormat.format(cal.getTime());
        System.out.println(dateFormat.format(cal.getTime()));
        java.util.Date date = dateFormat.parse(dateInString);
        Date paymentdate = new Date(date.getTime());*/
        paymentDate = dateUtil.convertDate(client.getPaymentdate());
        ps.setDate(2, paymentDate);
        ps.setFloat(3, client.getCamount());
        ps.setInt(4, clientid);
        ps.setInt(5, client.getTherapistid());
        ps.executeUpdate();
        logger.info("Client: Payment"+ps);
    
        
    }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return client;
}
public Client updateNewActivityForClient(Client client)  {
	
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs =null;
    int clientid =0;
    int nameid =0;
    int paymentid = 0;
    String sql="";
    String firstname ="";
    String lastname = "";
    Date paymentDate = null;
    boolean ddlValue = false;
    boolean paymentQuery = false;
    boolean activityQuery = false;
    try {
    	logger.debug("****Update New Activity for Client in db******");
    	//Insert Name values to Name table
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
        logger.debug("Query:"+Queries.getQuery("selectNameid"));
        ps = c.prepareStatement(Queries.getQuery("selectNameid"));
        String cname = client.getCname();
        String cnameArr[] = cname.split(" ");
        
        if(cnameArr.length>=1)
        {
        	firstname = cnameArr[0];
        	lastname = cnameArr[1];
        }
        else
        {
        	firstname = cnameArr[0];
        	lastname = "";
        }
        ps.setString(1, firstname);
        ps.setString(2,  lastname);
        rs = ps.executeQuery();
        while (rs.next()) {
            nameid = rs.getInt("nameid");
        }
        
        logger.debug("Query:"+Queries.getQuery("selectClientid"));
        ps = c.prepareStatement(Queries.getQuery("selectClientid"));
        ps.setInt(1,  nameid);
        rs = ps.executeQuery();
        while (rs.next()) {
            clientid = rs.getInt("clientid");
        }
        
        ps = c.prepareStatement("select * from therapist_clientrate where clientid =? and activityid=? ");
        ps.setInt(1,  clientid);
        ps.setInt(2,  client.getActtypeid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
            ddlValue = true;
        }
        //Update values in to therapist_clientrate
        if(ddlValue){
        	ps = c.prepareStatement("Update therapist_clientrate set waiver =?, netcharge=?, therapistid=? where clientid=?  and activityid=?");
            ps.setFloat(1, client.getNewactwaiver());
            ps.setFloat(2, client.getNewactnetcharge());
            ps.setInt(3, client.getTherapistid());
            ps.setInt(4, clientid);
            ps.setInt(5, client.getActtypeid());
            ps.executeUpdate();
            logger.info("Client: Update therapist_clientrate activity "+ps);
        }
        //Insert values in to therapist_clientrate
        if(!ddlValue){
        ps = c.prepareStatement("INSERT INTO therapist_clientrate (clientid,therapistid,activityid, waiver, netcharge) VALUES (?,?,?,?,?)");
        ps.setInt(1, clientid);
        ps.setInt(2, client.getTherapistid());
        ps.setInt(3, client.getActtypeid());
        ps.setFloat(4, client.getNewactwaiver());
        ps.setFloat(5, client.getNewactnetcharge());
        ps.executeUpdate();
        logger.info("Client: Insert therapist_clientrate activity "+ps);
        }
        
        ps = c.prepareStatement("select paymentid from payment where clientid =? and activityid=?");
        ps.setInt(1,  clientid);
        ps.setInt(2,  client.getActtypeid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
            paymentQuery = true;
            paymentid =rs.getInt("paymentid");
        }
        
        //Insert values in to payment table
        if(!paymentQuery) {     
        	ps = c.prepareStatement("INSERT INTO payment (activityid,paymentdate,amount,clientid,therapistid) VALUES (?,?,?,?,?)");
        	ps.setInt(1, client.getActtypeid());
        	paymentDate = dateUtil.convertDate(client.getPaymentdate());
        	ps.setDate(2, paymentDate);
        	ps.setFloat(3, client.getPayment());
        	ps.setInt(4, clientid);
        	ps.setInt(5, client.getTherapistid());
        	ps.executeUpdate();
        	rs = ps.getGeneratedKeys();
        	if(rs != null && rs.next()){
        		logger.debug("Generated Payment Id"+rs.getInt(1));
        		paymentid =rs.getInt(1);
        	}
        	logger.info("Client: INsert Payment activity "+ps);
        }
        if(paymentQuery){
        	
        	logger.debug("Query:"+Queries.getQuery("updatePayment"));
            ps = c.prepareStatement(Queries.getQuery("updatePayment"));
        	paymentDate = dateUtil.convertDate(client.getPaymentdate());
        	ps.setDate(1, paymentDate);
        	
        	ps.setFloat(2, client.getPayment());
        	ps.setInt(3, client.getTherapistid());
        	ps.setInt(4, clientid);
        	ps.setInt(5, client.getActtypeid());
        	ps.executeUpdate();
        
        	logger.info("Client: Update Payment activity "+ps);
        }
        
        ps = c.prepareStatement("select * from activity where clientid =? and activityid=?");
        ps.setInt(1,  clientid);
        ps.setInt(2,  client.getActtypeid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
            activityQuery = true;
        }
        
        if(!activityQuery){
        	//Insert values in to activity table
        	ps = c.prepareStatement("INSERT INTO activity (clientid,therapistid,paymentid,activitydate,activityid,activitytime) VALUES (?,?,?,?,?,?)");
        	ps.setInt(1, clientid);
        	ps.setInt(2, client.getTherapistid());
        	ps.setInt(3, paymentid);
        	paymentDate = dateUtil.convertDate(client.getPaymentdate());
        	ps.setDate(4, paymentDate);
        	ps.setInt(5,client.getActtypeid());
        	ps.setTimestamp(6,getCurrentTimeStamp());
        	ps.executeUpdate();
        	logger.info("Client: INsert activity "+ps);
        }
        
        if(activityQuery){
        	
        	logger.debug("Query:"+Queries.getQuery("updateActivity"));
            ps = c.prepareStatement(Queries.getQuery("updateActivity"));
            ps.setInt(1, paymentid);
        	paymentDate = dateUtil.convertDate(client.getPaymentdate());
        	ps.setDate(2, paymentDate);
        	ps.setInt(3, client.getTherapistid());
        	ps.setTimestamp(4,getCurrentTimeStamp());
        	ps.setInt(5, clientid);
        	ps.setInt(6, client.getActtypeid());
        	ps.executeUpdate();
        
        	logger.info("Client: Update activity "+ps);
        	
        	
        }
        c.commit();
        logger.info("Client: Committed");
        
    } catch(SQLException e){
      	 try {
             c.rollback();
             e.printStackTrace();
             throw new RuntimeException(e);
        } catch(SQLException sqle){
        	sqle.printStackTrace();
        }
    }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return client;
}

public void removeNewActivity(Client client) {
	logger.debug("*****ClientDao removeNewActivity method called.********");
    Connection c = null;
    String firstname="";
    String lastname="";
    PreparedStatement ps =null;
    ResultSet rs = null;
    int nameid=0;
    int clientid=0;
    Date paymentDate = null;
    try {
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
      
        logger.debug("Query:"+Queries.getQuery("selectNameid"));
        ps = c.prepareStatement(Queries.getQuery("selectNameid"));
        String cname = client.getCname();
        String cnameArr[] = cname.split(" ");
        
        if(cnameArr.length>=1)
        {
        	firstname = cnameArr[0];
        	lastname = cnameArr[1];
        }
        else
        {
        	firstname = cnameArr[0];
        	lastname = "";
        }
        ps.setString(1, firstname);
        ps.setString(2,  lastname);
        rs = ps.executeQuery();
        while (rs.next()) {
            nameid = rs.getInt("nameid");
        }
     
        logger.debug("Query:"+Queries.getQuery("selectClientid"));
        ps = c.prepareStatement(Queries.getQuery("selectClientid"));
        ps.setInt(1,  nameid);
        rs = ps.executeQuery();
        while (rs.next()) {
            clientid = rs.getInt("clientid");
        }
        
       
        logger.debug("Query:"+Queries.getQuery("deleteActivityTCR"));
        ps = c.prepareStatement(Queries.getQuery("deleteActivityTCR"));        
        ps.setInt(1, client.getTherapistid());
        ps.setInt(2, clientid);
        ps.setInt(3, client.getActtypeid());
       
        logger.info("Client:Delete NewActivity-therapist_clientrate "+ps);
             		
              
        logger.debug("Query:"+Queries.getQuery("deleteActivityPayment"));
        ps = c.prepareStatement(Queries.getQuery("deleteActivityPayment"));        
        ps.setInt(1, client.getTherapistid());
        ps.setInt(2, clientid);
        ps.setInt(3, client.getActtypeid());
        paymentDate = dateUtil.convertDate(client.getPaymentdate());
        ps.setDate(4, paymentDate);
       
        logger.info("Client:Delete NewActivity-payment from payment "+ps);
       		
        logger.debug("Query:"+Queries.getQuery("deleteNewActivity"));
        ps = c.prepareStatement(Queries.getQuery("deleteNewActivity"));        
        ps.setInt(1, client.getTherapistid());
        ps.setInt(2, clientid);
        paymentDate = dateUtil.convertDate(client.getPaymentdate());
        ps.setDate(3, paymentDate);
        ps.setInt(4, client.getActtypeid());
      
        logger.info("Client:Delete Activity from therapist_clientrate "+ps);
        c.commit();
        logger.info("Delete Activity: Committed");
       
        
    }catch(SQLException e){
      	 try {
             c.rollback();
             e.printStackTrace();
             throw new RuntimeException(e);
        } catch(SQLException sqle){
        	sqle.printStackTrace();
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
   
}
public Client addNewActivityForClient(Client client)  {
	
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs =null;
    int clientid =0;
    int paymentid = 0;
    String sql="";
    Date paymentDate = null;
    boolean ddlValue = false;
    try {
    	logger.debug("****Add New Activity for Client in db******");
    	//Insert Name values to Name table
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
        sql = "select clientid from client where name_id=?";
        ps = c.prepareStatement(sql);
        ps.setInt(1,  client.getNameid());
        rs = ps.executeQuery();
        while (rs.next()) {
            clientid = rs.getInt("clientid");
        }
        
        ps = c.prepareStatement("select * from therapist_clientrate where clientid =? and activityid=? and therapistid=?");
        ps.setInt(1,  clientid);
        ps.setInt(2,  client.getActtypeid());
        ps.setInt(3,  client.getTherapistid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
            ddlValue = true;
        }
        //Update values in to therapist_clientrate
        if(ddlValue){
        	ps = c.prepareStatement("Update therapist_clientrate set waiver =?, netcharge=? where clientid=? and therapistid=? and activityid=?");
            ps.setFloat(1, client.getNewactwaiver());
            ps.setFloat(2, client.getNewactnetcharge());
            ps.setInt(3, clientid);
            ps.setInt(4, client.getTherapistid());
            ps.setInt(5, client.getActtypeid());
            ps.executeUpdate();
            logger.info("Client: Update therapist_clientrate activity "+ps);
        }
        
        		//Insert values in to therapist_clientrate
        if(!ddlValue){
        ps = c.prepareStatement("INSERT INTO therapist_clientrate (clientid,therapistid,activityid, waiver, netcharge) VALUES (?,?,?,?,?)");
        ps.setInt(1, clientid);
        ps.setInt(2, client.getTherapistid());
        ps.setInt(3, client.getActtypeid());
        ps.setFloat(4, client.getNewactwaiver());
        ps.setFloat(5, client.getNewactnetcharge());
        ps.executeUpdate();
        logger.info("Client: Insert therapist_clientrate activity "+ps);
        }
       
        
        //Insert values in to payment table
        ps = c.prepareStatement("INSERT INTO payment (activityid,paymentdate,amount,clientid,therapistid) VALUES (?,?,?,?,?)");
        ps.setInt(1, client.getActtypeid());
       
        paymentDate = dateUtil.convertDate(client.getPaymentdate());
        ps.setDate(2, paymentDate);
        ps.setFloat(3, client.getPayment());
        ps.setInt(4, clientid);
        ps.setInt(5, client.getTherapistid());
        ps.executeUpdate();
        rs = ps.getGeneratedKeys();
        if(rs != null && rs.next()){
            logger.debug("Generated Payment Id"+rs.getInt(1));
            paymentid =rs.getInt(1);
        }
        logger.info("Client: INsert Payment activity "+ps);
        
       
        //Insert values in to activity table
        ps = c.prepareStatement("INSERT INTO activity (clientid,therapistid,paymentid,activitydate,activityid,activitytime) VALUES (?,?,?,?,?,?)");
        ps.setInt(1, clientid);
        ps.setInt(2, client.getTherapistid());
        ps.setInt(3, paymentid);
        paymentDate = dateUtil.convertDate(client.getPaymentdate());
        ps.setDate(4, paymentDate);
        ps.setInt(5,client.getActtypeid());
        ps.setTimestamp(6,getCurrentTimeStamp());
        ps.executeUpdate();
        logger.info("Client: INsert activity "+ps);
        c.commit();
        logger.info("Client: Committed");
        
    } catch(SQLException e){
      	 try {
             c.rollback();
             e.printStackTrace();
             throw new RuntimeException(e);
        } catch(SQLException sqle){
        	sqle.printStackTrace();
        }
    }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return client;
}

private static java.sql.Timestamp getCurrentTimeStamp() {

	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());

}

public List<Client> searchNewActivityTC(Client client) {
	logger.debug("**********SearchNewActivity Method is called in ClientDao*****");
	List<Client> list = new ArrayList<Client>();
    Connection c = null;
    PreparedStatement ps =null;
    ResultSet rs = null;
    int clientid=0;
    int therapistid=0;
    String sql="";
    String activityDt="";
    try {
    	c = ConnectionHelper.getConnection();
    	logger.debug("Query:"+Queries.getQuery("selectClientid"));
     	sql = Queries.getQuery("selectClientid");
     	ps = c.prepareStatement(sql);
        ps.setInt(1, client.getNameid());
        rs = ps.executeQuery();
        while (rs.next()) {
        	clientid = rs.getInt("clientid");
        }
            
        
        logger.debug("Query:"+Queries.getQuery("selectActivity"));
    	sql = Queries.getQuery("selectActivity");
        ps = c.prepareStatement(sql);
        ps.setInt(1,  client.getTherapistid());
        ps.setInt(2,  clientid);
        ps.setInt(3,  client.getActtypeid());
        ps.setInt(4,  client.getActtypeid());
        rs = ps.executeQuery();
        while (rs.next()) {
            Client clientRow = new Client();
            clientRow.setClientid(clientid);
            clientRow.setTherapistid(client.getTherapistid());
            clientRow.setPaymentid(rs.getInt("paymentid"));
            clientRow.setActtypeid(rs.getInt("activityid"));
            clientRow.setActivitytype(rs.getString("activityname"));
            activityDt = dateUtil.convertStringToDate(rs.getDate("activitydate"));
            clientRow.setActivitydate(activityDt);
           
            list.add(clientRow);
            }
       
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return list;
}

public List<Client> searchClient(String firstname, String lastname)  {
	logger.debug("**********SearchClientt Method is called in ClientDao*****");
	List<Client> list = new ArrayList<Client>();
    Connection c = null;
	String sql = "SELECT * FROM Name as e " +
		"WHERE UPPER(firstname) LIKE ? " +	
		"and UPPER(lastname) Like ? " +
		"and sex is not null order by lastname";
    try {
        c = ConnectionHelper.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1,  "%" + firstname.toUpperCase() + "%");
        ps.setString(2,  "%" + lastname.toUpperCase() + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(processRow(rs));
        }
       // System.out.println("List Therapist Values:"+list.size()+",");
    } 
    catch(SQLException e){
      	 try {
            c.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
       } catch(SQLException sqle){
       	sqle.printStackTrace();
       }
   }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return list;
}

public Client findClientActivityRate(int therapistid, int nameid, int acttypeid)  {
	logger.debug("**********findClientActivityRate Method is called in ClientDao*****");
	
    Connection c = null;
    Client client = new Client();
	String sql = "SELECT * FROM rate where doctorid=? and activityid=?";
	int clientid = 0;
    try {
    	c = ConnectionHelper.getConnection();
    	c.setAutoCommit(false);
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1,  therapistid);
        ps.setInt(2,  acttypeid);
        logger.debug("Therapistid:"+therapistid+", acttypeid:"+acttypeid+", nameid:"+nameid);
        ResultSet rs = ps.executeQuery();
        if(rs != null && rs.next()){
            client.setActtypeid(rs.getInt("activityid"));
            client.setTherapistid(therapistid);
            client.setCamount(rs.getFloat("amount"));
            client.setRateper(rs.getString("rateper"));
            client.setRateperval(rs.getInt("rateperval"));
        }
       else{
    	   logger.debug("No records for that activity and therapist");
    	   client.setActtypeid((0));
    	   client.setTherapistid(therapistid);
    	   client.setCamount(0);
    	   client.setRateper("");
    	   client.setRateperval(0);
       }
       //To get the client id
        sql = "SELECT clientid FROM client where name_id=?";
        ps = c.prepareStatement(sql);
        ps.setInt(1,  nameid);
        rs = ps.executeQuery();
        
        if(rs != null && rs.next()){
        	clientid = rs.getInt("clientid");
        }
        
        sql = "SELECT * FROM therapist_clientrate where therapistid=? and activityid=? and clientid=?";
        ps = c.prepareStatement(sql);
        ps.setInt(1,  therapistid);
        ps.setInt(2,  acttypeid);
        ps.setInt(3,  clientid);
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
        	client.setNewactnetcharge(rs.getFloat("netcharge"));
        	client.setNewactwaiver(rs.getFloat("waiver"));
        }
        else{
        	logger.debug("No records for that activity,client and therapist");
        	client.setNewactwaiver(0);
        	client.setNewactnetcharge(client.getCamount());
        }
        c.commit();
    }catch(SQLException e){
   	 try {
         c.rollback();
         e.printStackTrace();
         throw new RuntimeException(e);
    } catch(SQLException sqle){
    	sqle.printStackTrace();
    }
}
    catch(Exception e){ 
    	e.printStackTrace();
    	throw new RuntimeException(e);
    } finally {
	ConnectionHelper.close(c);
    }
	return client;
}

public Client fetchClientDetails(int cnameid)  {
	logger.debug("**********FetchClientDetails Method is called in Clientdao*****");
	//List<Client> clientlist = new ArrayList<Client>();
	Client client = new Client();
    Connection c = null;
    String stheraphy = null;
	String sql = "SELECT * from client where name_id=?";
    try {
        c = ConnectionHelper.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1,  cnameid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        	client.setClientid(rs.getInt("clientid"));
        	client.setNameid(rs.getInt("name_id"));
        	client.setAddressid(rs.getInt("address_id"));
        	client.setContactid(rs.getInt("contact_id"));
        	client.setPaymentid(rs.getInt("paymentid"));
        	client.setFlagid(rs.getInt("flagid"));
        	client.setCpt(rs.getInt("cpt"));
        	String dob = dateUtil.convertStringToDate(rs.getDate("dob"));
        	client.setDob(dob);
        	client.setReferralid(rs.getInt("referralid"));
        	client.setTherapistid(rs.getInt("therapistid"));
        	String dov = dateUtil.convertStringToDate(rs.getDate("dov"));
        	client.setDov(dov);
        }
        
        //To fetch Name values
        sql = "select * from Name where nameid=?";
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  cnameid);
        rs = ps.executeQuery();
        while (rs.next()) {
        	client.setFirstname(rs.getString("firstname"));
        	client.setLastname(rs.getString("lastname"));
        	client.setMiddlename(rs.getString("middlename"));
        	client.setSex(rs.getString("sex"));
        }
        
        //To fetch address
        
        sql = "select * from address where address_id=?";
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  client.getAddressid());
        rs = ps.executeQuery();
        while (rs.next()) {
        	client.setAddress1(rs.getString("address1"));
        	client.setAddress2(rs.getString("address2"));
        	client.setCity(rs.getString("city"));
        	client.setState(rs.getString("state"));
        	client.setZip(rs.getInt("zip"));
        }
        
        //To fetch phone details
        
        sql = "select * from contact where contact_id=?";
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  client.getContactid());
        rs = ps.executeQuery();
        while (rs.next()) {
        	client.setHomephone(rs.getString("homephone"));
        	client.setWorkphone(rs.getString("workphone"));
        	client.setMobile(rs.getString("mobile"));
        }
        
        //To fetch payment details
        sql = "select * from payment where paymentid=?";
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  client.getPaymentid());
        rs =ps.executeQuery();
        while (rs.next()) {
        	client.setActtypeid(rs.getInt("activityid"));
        	 stheraphy = dateUtil.convertStringToDate(rs.getDate("paymentdate"));
        	client.setStheraphy(stheraphy);
        	client.setPayment(rs.getFloat("amount"));
        }
        
      //To fetch diagnosis details
        sql = "select diagnosisid from client_diagnosis where clientid=?";
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  client.getClientid());
        rs =ps.executeQuery();
        int[] diagnosisid;
        ArrayList<Integer> diagnosiscd = new ArrayList<Integer>();
        while (rs.next()) {
        	diagnosiscd.add(rs.getInt("diagnosisid"));
        }
        diagnosisid = new int[diagnosiscd.size()];
        for(int i=0;i<diagnosiscd.size(); i++){
        	diagnosisid[i] = diagnosiscd.get(i);
        }
        client.setDiagnosiscode(diagnosisid);
      
        
    } 
    catch(SQLException e){
      	 try {
            c.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
       } catch(SQLException sqle){
       	sqle.printStackTrace();
       }
   }catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return client;
}



protected Client processNewActivityRow(ResultSet rs) throws SQLException {
	   Client client = new Client();
	  
	   client.setNameid(rs.getInt("nameid"));
	   client.setFirstname(rs.getString("firstname"));
	   client.setLastname(rs.getString("lastname"));
	   client.setMiddlename(rs.getString("middlename"));
	   
  return client;
}

public Client getNewActivityDetails(Client client)  {
	logger.debug("**********Fetch New Activity Details is called in Clientdao*****");
	
	Client clientDet = new Client();
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs =null;
    try {
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
        logger.debug("Query:"+Queries.getQuery("selectRateWActid"));
        String sql = Queries.getQuery("selectRateWActid");
        ps =  c.prepareStatement(sql);
        ps.setInt(1,  client.getTherapistid());
        ps.setInt(2, client.getActtypeid());
        rs =ps.executeQuery();
       
        if(rs != null && rs.next()){
        	clientDet.setClientid(client.getClientid());
        	clientDet.setTherapistid(client.getTherapistid());
        	clientDet.setCamount(rs.getFloat("amount"));
        	clientDet.setRateper(rs.getString("rateper"));
        	clientDet.setRateperval(rs.getInt("rateperval"));
        	
        	}
        else{
     	   logger.debug("No records for that activity and therapist");
     	  clientDet.setClientid(client.getClientid());
     	  clientDet.setTherapistid(client.getTherapistid());
     	  clientDet.setCamount(0);
     	  clientDet.setRateper("");
     	  clientDet.setRateperval(0);
     	  
        }
        
        logger.debug("Query:"+Queries.getQuery("selectWaiver"));
        sql = Queries.getQuery("selectWaiver");
        ps = c.prepareStatement(sql);
        ps.setInt(1,  client.getTherapistid());
        ps.setInt(2,  client.getActtypeid());
        ps.setInt(3,  client.getClientid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
        	clientDet.setNewactnetcharge(rs.getFloat("netcharge"));
        	clientDet.setNewactwaiver(rs.getFloat("waiver"));
       	 
        }
        else{
        	logger.debug("No records for that activity,client and therapist");
        	clientDet.setNewactwaiver(0);
        	clientDet.setNewactnetcharge(client.getCamount());
        }
        logger.debug("Query:"+Queries.getQuery("selectPayment"));
        sql = Queries.getQuery("selectPayment");
        ps = c.prepareStatement(sql);
        ps.setInt(1,  client.getTherapistid());
        ps.setInt(2,  client.getClientid());
        ps.setInt(3,  client.getActtypeid());
       
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
        	clientDet.setPayment(rs.getFloat("amount"));
        	
        }
        else{
        	logger.debug("No records for that activity,client and therapist");
        	clientDet.setPayment(0);
        }
        clientDet.setClientid(client.getClientid());
        clientDet.setActtypeid(client.getActtypeid());
        clientDet.setActivitydate(client.getActivitydate());
        logger.debug("Query:"+Queries.getQuery("selectClientName"));
        sql = Queries.getQuery("selectClientName");
        ps = c.prepareStatement(sql);
        ps.setInt(1,  client.getClientid());
        rs = ps.executeQuery();
        if(rs != null && rs.next()){
        	clientDet.setFirstname(rs.getString("firstname"));
        	clientDet.setLastname(rs.getString("lastname"));
        }
       
            c.commit();
        
    } catch (SQLException e) {
    	 try {
             c.rollback();
             e.printStackTrace();
             throw new RuntimeException(e);
             } catch(SQLException sqle){
        	sqle.printStackTrace();
        }
       
	} finally {
		ConnectionHelper.close(c);
	}
    return clientDet;
}


protected Client processRow(ResultSet rs) throws SQLException {
	   Client client = new Client();
	   client.setNameid(rs.getInt("nameid"));
	   client.setFirstname(rs.getString("firstname"));
	   client.setLastname(rs.getString("lastname"));
	   client.setMiddlename(rs.getString("middlename"));
	   
     return client;
 }


}
