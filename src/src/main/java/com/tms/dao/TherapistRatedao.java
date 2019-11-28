package com.tms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.tms.util.Queries;

import org.apache.log4j.Logger;

import com.tms.bean.Flag;
import com.tms.bean.Therapist;
import com.tms.bean.TherapistRate;
import com.tms.database.ConnectionHelper;

public class TherapistRatedao {
	final static Logger logger = Logger.getLogger(TherapistRatedao.class);
	
public TherapistRate createTherapistRate(TherapistRate docRate)  {
		
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        int[] activitytype =null;
        String[] rateper = null;
        int[] rateperval = null;
        int therapistid = 0;
        float[] rate = null;
         int rateid = 0;
         try {
         	logger.debug("****Adding TherapistRate in db******");
         	//Insert Name values to Name table
             c = ConnectionHelper.getConnection();
             activitytype = docRate.getArrtActivityType();
             rateper =docRate.getArrtRateper();
             rateperval = docRate.getArrtRateperVal();
             rate = docRate.getArrtRate();
             therapistid = docRate.getTherapistid();
             logger.debug("therapistid:"+therapistid+"activity type length:"+activitytype.length);
             for(int i= 0; i<activitytype.length;i++){
            	  logger.debug("Query:"+Queries.getQuery("insert_rate"));
             ps = c.prepareStatement(Queries.getQuery("insert_rate"));
             ps.setInt(1, therapistid);
             ps.setInt(2, activitytype[i]);
             ps.setFloat(3, rate[i]);
             ps.setString(4, rateper[i]);
             ps.setInt(5, rateperval[i]);
             ps.executeUpdate();
             rs = ps.getGeneratedKeys();
             if(rs != null && rs.next()){
                 logger.debug("Generated Rate Id:" + "rateid "+rs.getInt(1));
                 rateid =rs.getInt(1);
             	}
             logger.debug("Rateid Generated:"+rateid);
             logger.info("Therapist Rate:Insert Rate:"+ps+":  Generated Rate ID:"+rateid);
             }
         }
         catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
 		} finally {
 			ConnectionHelper.close(c);
 		}
         return docRate;
}


public TherapistRate getTherapistRate(int therapistid) {
	logger.debug("********Therapist Rate Method called***********");
    Connection c = null;
    int[] activityid;
    float[] amount;
    String[] rateper;
    int[] rateperval;
    ArrayList<Integer> lactivityid = new ArrayList<Integer>();
    ArrayList<Float> lamount = new ArrayList<Float>();
    ArrayList<String> lrateper = new ArrayList<String>();
    ArrayList<Integer> lrateperval = new ArrayList<Integer>();
    TherapistRate tRate = new TherapistRate();
    try {
    	
    	
        c = ConnectionHelper.getConnection();
        logger.debug("Query:"+Queries.getQuery("get_rate"));
        PreparedStatement ps = c.prepareStatement(Queries.getQuery("get_rate"));
        ps.setInt(1, therapistid);
        ResultSet rs = ps.executeQuery();
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
        tRate.setArrtActivityType(activityid);
        tRate.setArrtRate(amount);
        tRate.setArrtRateper(rateper);
        tRate.setArrtRateperVal(rateperval);
        
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
	} finally {
		ConnectionHelper.close(c);
	}
    return tRate;
}
	
public TherapistRate updateTherapistRate(TherapistRate docRate, int therapistid) {
	logger.debug("********Update Therapist Rate Method called***********"+ therapistid);
    Connection c = null;
    PreparedStatement ps = null;
    int[] activityid;
    float[] amount;
    String[] rateper;
    int[] rateperval;
    int actIDDelete=0;
    int rateid[];
    int newrateid=0;
    int rowcount =0;
    ResultSetMetaData rsmd =null;
    ResultSet rs;
    
    try {
        c = ConnectionHelper.getConnection();
        c.setAutoCommit(false);
        //Get Rate id from rate table
        logger.debug("Query:"+Queries.getQuery("selectforupdate_rate"));
        ps = c.prepareStatement(Queries.getQuery("selectforupdate_rate"));
        ps.setInt(1, therapistid);
        rs = ps.executeQuery();
        rs.next();
        rowcount = rs.getInt("rowcount");
        rs.close();
        logger.debug("Rowcount:"+rowcount);
        rateid = new int[rowcount];
        logger.debug("Query:"+Queries.getQuery("select_rateid"));
        ps = c.prepareStatement(Queries.getQuery("select_rateid"));
        ps.setInt(1, therapistid);
        rs = ps.executeQuery();
        int k = 0;
        while(rs.next()){
        	rateid[k] =rs.getInt("rateid");
        	k++;
        }
        //Update therapist Rate table
        activityid = docRate.getArrtActivityType();
        amount = docRate.getArrtRate();
        rateper = docRate.getArrtRateper();
        rateperval = docRate.getArrtRateperVal();
        
        if(rateid.length <= activityid.length){
        	for(int i=0;i<rateid.length;i++){
        		 logger.debug("Query:"+Queries.getQuery("update_rate"));
        		ps = c.prepareStatement(Queries.getQuery("update_rate"));
        		ps.setInt(1, activityid[i]);
        		ps.setFloat(2, amount[i]);
        		ps.setString(3, rateper[i]);
        		ps.setInt(4, rateperval[i]);
        	
        		ps.setInt(5, therapistid);
        		ps.setInt(6, rateid[i]);
        		ps.executeUpdate();
        	}
        	 logger.info("TherapistRate:Update Rate:"+ps);
        }
        else{
        	//If a class is removed
        	for(int i=0;i<activityid.length;i++){
        		 logger.debug("Query:"+Queries.getQuery("updateIfRemoved_rate"));
        		ps = c.prepareStatement(Queries.getQuery("updateIfRemoved_rate"));
        		ps.setInt(1, activityid[i]);
        		ps.setFloat(2, amount[i]);
        		ps.setString(3, rateper[i]);
        		ps.setInt(4, rateperval[i]);
        		ps.setInt(5, therapistid);
        		ps.setInt(6, rateid[i]);
        		ps.executeUpdate();
        	}
        	 logger.info("TherapistRate:Update IF Removed Class Rate:"+ps);
        }
        //If a class is removed
        if(rateid.length > activityid.length){
        	
    	   	logger.debug("Deleting Activity Types"+rateid.length);
    	for(int i=activityid.length; i<rateid.length;i++)
    	{
    		logger.debug("Query:"+Queries.getQuery("selectToRemove_actid"));
            ps = c.prepareStatement(Queries.getQuery("selectToRemove_actid"));
          
            ps.setInt(1, rateid[i]);
            rs = ps.executeQuery();
           
            while(rs.next()){
            	actIDDelete = rs.getInt("activityid");
            	
            }
            
    		logger.debug("Deleting the class removed from db:"+rateid[i]);
    		 logger.debug("Query:"+Queries.getQuery("delete_rate"));
    		ps = c.prepareStatement(Queries.getQuery("delete_rate"));        		
    		ps.setInt(1, rateid[i]);  
            ps.executeUpdate();
            logger.info("TherapistRate:Delete Therapist Rate:"+ps);
            logger.debug("Deleting values from therapist_clientrate:"+therapistid);
            ps = c.prepareStatement(Queries.getQuery("delete_clientrate"));        		
    		ps.setInt(1, therapistid);  
    		ps.setInt(2, actIDDelete); 
            ps.executeUpdate();
            logger.info("TherapistRate:Delete Client Rate:"+ps);
    	}
    }
      //If a new class is added
        if(rateid.length < activityid.length){
        	
        	   	logger.debug("Inserting New Activity Types"+activityid.length);
        	for(int i=rateid.length; i<activityid.length;i++)
        	{
        		 logger.debug("Query:"+Queries.getQuery("insertIfClassAdded_rate"));
        		ps = c.prepareStatement(Queries.getQuery("insertIfClassAdded_rate"));        		
        		ps.setInt(1, therapistid);        		
                ps.setInt(2, activityid[i]);        		
                ps.setFloat(3, amount[i]);               
                ps.setString(4, rateper[i]); 
                ps.setInt(5, rateperval[i]);
                
                ps.executeUpdate();
                rs = ps.getGeneratedKeys(); 
               
                if(rs != null && rs.next()){
                    logger.debug("Generated Rate Id for new insert in update:" + "rateid "+rs.getInt(1));
                    newrateid =rs.getInt(1);
                	}
                logger.info("TherapistRate:Insert TherapistRate If New Class Added:"+ps+":  Generate Rate ID:"+newrateid);
        	}
        }
    c.commit();
      logger.info("TherapistRate: Update Commmitted");      
        
    } catch(SQLException e){
   	 try {
         c.rollback();
         e.printStackTrace();
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
    return docRate;
}

}
