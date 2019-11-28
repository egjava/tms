package com.tms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.tms.util.Queries;

import com.tms.bean.ActivityType;
import com.tms.bean.Diagnosis;
import com.tms.database.ConnectionHelper;
import org.apache.log4j.Logger;

public class Diagnosisdao {
	final static Logger logger = Logger.getLogger(Diagnosisdao.class);
	public Diagnosis createDiagnosisCode(Diagnosis dcode)  {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("insert_diagnosis"));
            ps = c.prepareStatement(Queries.getQuery("insert_diagnosis"));
            ps.setString(1, dcode.getDiagcode());
            ps.setString(2, dcode.getDesc());
            ps.executeUpdate();
            logger.info("Diagnosis Code: Insert"+ps);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return dcode;
    }
	
	public Diagnosis getDiagnosisId(int id)  {
		logger.debug("**********getDiagnosisid Method is called in DiagnosisCodeDao*****");
		Diagnosis diagcode=null;
        Connection c = null;
    	
			
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("get_diagnosis"));
            String sql = Queries.getQuery("get_diagnosis");	
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,  id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               diagcode = processRow(rs);
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return diagcode;
    }
	
	
	public Diagnosis updateDiagnosisCode(Diagnosis diagcode, int diagnosisid) {
		logger.debug("********Update Activity Type Method called***********");
        Connection c = null;
       
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("update_diagnosis"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("update_diagnosis"));
                    
            ps.setString(1, diagcode.getDiagcode());
            ps.setString(2, diagcode.getDesc());
            ps.setInt(3, diagnosisid);
            ps.executeUpdate();
            logger.info("Diagnosis Code: Update"+ps);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return diagcode;
    }
	
	//Search Diagnosis Code to Modify
	
	public List<Diagnosis> searchDiagnosisCode(String diagcode)  {
		logger.debug("**********Diagnosisdao SearchDiagnosisCode Method called *****");
		List<Diagnosis> list = new ArrayList<Diagnosis>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("search_diagnosis"));
            String sql = Queries.getQuery("search_diagnosis");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,  "%" + diagcode.toUpperCase() + "%");
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
	
	public boolean removeDiagnosisCode(int diagnosisid) {
		logger.debug("*****Diagnosisdao removeDiagnosisCode method called.********");
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("delete_diagnosis"));
            PreparedStatement ps = c.prepareStatement(Queries.getQuery("delete_diagnosis"));
            ps.setInt(1, diagnosisid);
            int count = ps.executeUpdate();
            logger.info("Diagnosis Code: Delete"+ps);
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }
	
	public List<Diagnosis> fetchDiagnosisCode()  {
		logger.debug("**********FetchDiagnosisCode Method is called in Diagnosisdao*****");
		List<Diagnosis> list = new ArrayList<Diagnosis>();
        Connection c = null;
    	
        try {
            c = ConnectionHelper.getConnection();
            Statement statement = c.createStatement();
            logger.debug("Query:"+Queries.getQuery("fetch_diagnosis"));
            String sql = Queries.getQuery("fetch_diagnosis");
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
	
	protected Diagnosis processRow(ResultSet rs) throws SQLException {
		   Diagnosis diagcode = new Diagnosis();
	        diagcode.setDiagcode(rs.getString("name"));
	        diagcode.setDesc(rs.getString("description"));
	        diagcode.setDiagnosisid(rs.getInt("diagnosis_id"));
	        return diagcode;
	    }

}
