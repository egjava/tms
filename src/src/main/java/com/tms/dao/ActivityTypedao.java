package com.tms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.tms.util.Queries;

import org.apache.log4j.Logger;

import com.tms.bean.ActivityType;
import com.tms.bean.Flag;
import com.tms.database.ConnectionHelper;
import org.apache.log4j.Logger;

public class ActivityTypedao {
	final static Logger logger = Logger.getLogger(ActivityTypedao.class);
	public ActivityType createActivitytype(ActivityType actType)  {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("insert_activitytype"));
            ps = c.prepareStatement(Queries.getQuery("insert_activitytype"));
            ps.setString(1, actType.getActivitytype());
            ps.setInt(2, actType.getCptcode());
            ps.setFloat(3, actType.getRate());
            ps.setString(4, actType.getRateper());
            ps.setString(5, actType.getDesc());
            ps.executeUpdate();
            logger.info("Activity Type: Insert"+ps);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return actType;
    }
	
	//Search Activitytype to Modify
	
	public List<ActivityType> searchActivitytype(String actType)  {
		logger.debug("**********SearchActivityType Method is called in ActivityTypeDao*****");
		List<ActivityType> list = new ArrayList<ActivityType>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("search_activitytype"));
            String sql = Queries.getQuery("search_activitytype");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,  "%" + actType.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
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
	
	public List<ActivityType> fetchActivityType()  {
		logger.debug("**********FetchActivityType Method is called in ActivityTypedao*****");
		List<ActivityType> list = new ArrayList<ActivityType>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            Statement statement = c.createStatement();
            logger.debug("Query:"+Queries.getQuery("fetch_activitytype"));
            String sql = Queries.getQuery("fetch_activitytype");
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
	
	public ActivityType getActivityId(int id)  {
		logger.debug("**********getActivityid Method is called in ActivityTypeDao*****");
		ActivityType actType=null;
        Connection c = null;
    	
			
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("get_activitytype"));
            String sql = Queries.getQuery("get_activitytype");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,  id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               actType = processRow(rs);
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return actType;
    }
	
	
	public ActivityType updateActivitytype(ActivityType actType) {
		logger.debug("********Update Activity Type Method called***********");
        Connection c = null;
       
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("update_activitytype"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("update_activitytype"));
                    
            ps.setString(1, actType.getActivitytype());
            ps.setFloat(2, actType.getRate());
            ps.setInt(3, actType.getCptcode());
            ps.setString(4, actType.getRateper());
            ps.setString(5, actType.getDesc());
            ps.setInt(6, actType.getTypeid());
            ps.executeUpdate();
            logger.info("Activity Type: Update"+ps);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return actType;
    }
	
	public boolean removeActivityType(int actTypeid) {
		logger.debug("*****ActivityTypedao removeActivityType method called.********");
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("delete_activitytype"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("delete_activitytype"));
            ps.setInt(1, actTypeid);
            int count = ps.executeUpdate();
            logger.info("Activity Type: Delete"+ps);
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }
	
	protected ActivityType processRow(ResultSet rs) throws SQLException {
		   ActivityType actType = new ActivityType();
	       
	        actType.setActivitytype(rs.getString("activityname"));
	        actType.setTypeid(rs.getInt("activitytypeid"));
	        actType.setCptcode(rs.getInt("cptcode"));
	        actType.setRate(rs.getFloat("rate"));
	        actType.setRateper(rs.getString("rateper"));
	        actType.setDesc(rs.getString("description"));
	        return actType;
	    }

}
