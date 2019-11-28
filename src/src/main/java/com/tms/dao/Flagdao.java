package com.tms.dao;



import com.tms.bean.Flag;
import com.tms.database.ConnectionHelper;
import com.tms.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Flagdao {
	final static Logger logger = Logger.getLogger(Flagdao.class);
	public Flag createFlag(Flag flag)  {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("insert_flag"));
            ps = c.prepareStatement(Queries.getQuery("insert_flag"));
            ps.setString(1, flag.getFlagname());
            ps.executeUpdate();
            logger.info("Flag: Insert"+ps);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return flag;
    }
	
	//To add a new referral source
	public Flag createReferralSource(Flag refsource)  {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("insert_referral"));
            ps = c.prepareStatement(Queries.getQuery("insert_referral"));
            ps.setString(1, refsource.getReferralsource());
            ps.executeUpdate();
            logger.info("Referral Source: Insert"+ps);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return refsource;
    }
	
	public Flag updateReferralSource(Flag refsource, int refid) {
		logger.info("********Update ReferralSource Method called***********");
        Connection c = null;
       
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("update_referral"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("update_referral"));
           
            ps.setString(1, refsource.getReferralsource());
            ps.setInt(2, refid);
            ps.executeUpdate();
            logger.info("ReferralSource: Update"+ps);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return refsource;
    }
	
	public List<Flag> searchReferralSource(String refsource)  {
		logger.debug("**********SearchReferralSource Method is called in FlagDao*****");
		List<Flag> list = new ArrayList<Flag>();
        Connection c = null;
       
    	
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("select_referral"));
        	String sql = Queries.getQuery("select_referral");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,  "%" + refsource.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processReferralRow(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
	
	
	public Flag getReferralId(int refid)  {
		logger.debug("**********getReferralId Method is called in FlagDao*****");
		Flag refsource=null;
        Connection c = null;
	
			
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("search_referral"));
        	String sql = Queries.getQuery("search_referral");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,  refid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               refsource = processReferralRow(rs);
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return refsource;
    }
	
	public List<Flag> fetchReferralSource()  {
		logger.debug("**********FetchReferral Method is called in FlagDao*****");
		List<Flag> list = new ArrayList<Flag>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            Statement statement = c.createStatement();
            logger.debug("Query:"+Queries.getQuery("fetch_referral"));
        	String sql = Queries.getQuery("fetch_referral");
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(processReferralRow(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
	
	public boolean removeReferralSource(int refid) {
		logger.debug("*****Flagdao removeReferralSource method called.********");
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("delete_referral"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("delete_referral"));
            ps.setInt(1, refid);
            int count = ps.executeUpdate();
            logger.info("Referral Source: Delete"+ps);
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }
	  
	
	public List<Flag> fetchFlag()  {
		logger.debug("**********FetchFlag Method is called in FlagDao*****");
		List<Flag> list = new ArrayList<Flag>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("fetch_flag"));
            String sql = Queries.getQuery("fetch_flag");
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
	
	
	public Flag updateFlag(Flag flag) {
		logger.debug("********Update Flag Method called***********");
        Connection c = null;
       
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("update_flag"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("update_flag"));
            logger.debug("oldflagid:"+flag.getFlagid());
            logger.debug("NewFlagName:"+flag.getFlagname());
            ps.setString(1, flag.getFlagname());
            ps.setInt(2, flag.getFlagid());
            ps.executeUpdate();
            logger.info("Flag: Update"+ps);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return flag;
    }
	
	public boolean removeFlag(int flagid) {
		logger.debug("*****Flagdao removeFlag method called.********");
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("delete_flag"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("delete_flag"));
            ps.setInt(1, flagid);
            int count = ps.executeUpdate();
            logger.info("Flag: Insert"+ps);
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }
	
	
	public List<Flag> searchFlag(String flagValue)  {
		logger.debug("**********SearchFlag Method is called in FlagDao*****");
		List<Flag> list = new ArrayList<Flag>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("search_flag"));
            String sql = Queries.getQuery("search_flag");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,  "%" + flagValue.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
            logger.debug("List Flag Values:"+list.size()+",");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
	   protected Flag processRow(ResultSet rs) throws SQLException {
		   Flag flag = new Flag();
	        flag.setFlagid(rs.getInt("flagid"));
	        flag.setFlagname(rs.getString("flagname"));
	        return flag;
	    }
	   
	   protected Flag processReferralRow(ResultSet rs) throws SQLException {
		   Flag refsource = new Flag();
	        refsource.setReferralsource(rs.getString("referralsource"));
	        refsource.setReferralid(rs.getInt("refid"));
	        return refsource;
	    }

}
