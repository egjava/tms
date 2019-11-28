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
import com.tms.bean.Therapist;
import com.tms.database.ConnectionHelper;

public class Therapistdao {
	final static Logger logger = Logger.getLogger(Therapistdao.class);
	public Therapist createTherapist(Therapist doc)  {
		
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
         int nameid = 0;
         int addressid = 0;
         int contactid= 0;
         int officeid= 0;
         int therapistid= 0;
        try {
        	logger.debug("****Creating Therapist in db******");
        	//Insert Name values to Name table
            c = ConnectionHelper.getConnection();
            c.setAutoCommit(false);
            logger.debug("Query:"+Queries.getQuery("insert_name"));
            ps = c.prepareStatement(Queries.getQuery("insert_name"));
            ps.setString(1, doc.getFirstname());
            ps.setString(2, doc.getLastname());
            ps.setString(3, doc.getMiddlename());
           
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated Emp Id:Nameid "+rs.getInt(1));
                nameid =rs.getInt(1);
            }
            logger.info("Therapist: Insert Name:"+ps+ ":   Generated Name ID "+nameid);
            //Insert values into address table
            logger.debug("Query:"+Queries.getQuery("insert_address"));
            ps = c.prepareStatement(Queries.getQuery("insert_address"));
            ps.setString(1, doc.getAddress1());
            ps.setString(2, doc.getAddress2());
            ps.setString(3, doc.getCity());
            ps.setString(4, doc.getState());
            ps.setInt(5, doc.getZipcode());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated addressid "+rs.getInt(1));
                addressid =rs.getInt(1);
            }
            logger.info("Therapist: Insert ADDRESS:"+ps+ ":   Generated Address ID "+addressid);
            //Insert values into contact table
            logger.debug("Query:"+Queries.getQuery("insert_contact"));
            ps = c.prepareStatement(Queries.getQuery("insert_contact"));
            ps.setString(1, doc.getHomephone());
            ps.setString(2, doc.getWorkphone());
            ps.setString(3, doc.getMobile());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated contact Id"+rs.getInt(1));
                contactid =rs.getInt(1);
            }
            logger.info("Therapist: Insert Contact:"+ps+ ":   Generated contact ID "+contactid);
            //Insert values into Office table
            logger.debug("Query:"+Queries.getQuery("insert_office"));
            ps = c.prepareStatement(Queries.getQuery("insert_office"));
            ps.setString(1, doc.getId());
            ps.setString(2, doc.getLicense());
            ps.setString(3, doc.getEin());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated OfficeId "+rs.getInt(1));
                officeid =rs.getInt(1);
            }
            logger.info("Therapist: Insert office:"+ps+ ":   Generated officeid "+officeid);
            //Insert values into Therapist table
            logger.debug("Query:"+Queries.getQuery("insert_therapist"));
            ps = c.prepareStatement(Queries.getQuery("insert_therapist"));
            ps.setInt(1, nameid);
            ps.setInt(2, addressid);
            ps.setInt(3, contactid);
            ps.setInt(4, officeid);
            ps.setInt(5, doc.getFlagid());
           
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                logger.debug("Generated TherapistID "+rs.getInt(1));
                therapistid =rs.getInt(1);
            }
            logger.info("Therapist: Insert Therapist:"+ps+ ":   Generated therapistid "+therapistid);
            doc.setTherapistid(therapistid);
            c.commit();
            logger.info("Therapist: Insert Therapist Committed");
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
        return doc;
    }
	public List<Therapist> searchTherapist(String firstname, String lastname)  {
		logger.debug("**********SearchTherapist Method is called in TherapistDao*****");
		List<Therapist> list = new ArrayList<Therapist>();
        Connection c = null;
    
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("select_therapist"));
        	String sql = Queries.getQuery("select_therapist");
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,  "%" + firstname.toUpperCase() + "%");
            ps.setString(2,  "%" + lastname.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
           // logger.debug("List Therapist Values:"+list.size()+",");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
	
	//Update Therapist
	
	public Therapist updateTherapist(Therapist doc, int nameid) {
		logger.debug("********Update Therapist Method called***********"+nameid);
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int therapistid =0;
        int addressid=0;
        int contactid=0;
        int officeid=0;
        int flagid=0;
        
       
        try {
            c = ConnectionHelper.getConnection();
            c.setAutoCommit(false);
            logger.debug("Query:"+Queries.getQuery("fetch_therapist"));
            //Fetch all ids from therapist table
            ps = c.prepareStatement(Queries.getQuery("fetch_therapist"));
            ps.setInt(1, nameid);
           
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
                logger.debug("Data fetched from therapist table ");
                therapistid = rs.getInt("therapistid");
                addressid = rs.getInt("address_id");
                contactid = rs.getInt("contact_id");
                officeid = rs.getInt("office_id");
                flagid= rs.getInt("flagid");
                
            }
            doc.setTherapistid(therapistid);
           // Update Therapist table
            logger.debug("Query:"+Queries.getQuery("update_therapist"));
            ps = c.prepareStatement(Queries.getQuery("update_therapist"));
            ps.setInt(1, doc.getFlagid());
            ps.setInt(2, nameid);
            ps.executeUpdate();
            logger.info("Therapist: Update Therapist:"+ps);
            //Update Name table 
            logger.debug("Query:"+Queries.getQuery("update_name"));
            ps = c.prepareStatement(Queries.getQuery("update_name"));
            ps.setString(1, doc.getFirstname());
            ps.setString(2, doc.getLastname());
            logger.debug("Middlename:"+doc.getMiddlename());
            ps.setString(3, doc.getMiddlename());
           
            ps.setInt(4, nameid);
            ps.executeUpdate();
            logger.info("Therapist: Update Name:"+ps);
            //Update address table
            logger.debug("Query:"+Queries.getQuery("update_address"));
            ps = c.prepareStatement(Queries.getQuery("update_address"));
            ps.setString(1, doc.getAddress1());
            ps.setString(2, doc.getAddress2());
            ps.setString(3, doc.getCity());
            ps.setString(4, doc.getState());
            ps.setInt(5, doc.getZipcode());
            ps.setInt(6, addressid);
            ps.executeUpdate();
            logger.info("Therapist: Update Address:"+ps);
          //Update contact table
            logger.debug("Query:"+Queries.getQuery("update_contact"));
            ps = c.prepareStatement(Queries.getQuery("update_contact"));
            ps.setString(1, doc.getHomephone());
            ps.setString(2, doc.getWorkphone());
            ps.setString(3, doc.getMobile());
            ps.setInt(4, contactid);
            ps.executeUpdate();
            logger.info("Therapist: Update Contact:"+ps);
          //Update office table
            logger.debug("Query:"+Queries.getQuery("update_office"));
            ps = c.prepareStatement(Queries.getQuery("update_office"));
            ps.setString(1, doc.getId());
            ps.setString(2, doc.getLicense());
            ps.setString(3, doc.getEin());
            ps.setInt(4, officeid);
            ps.executeUpdate();
            logger.info("Therapist: Update Office:"+ps);
            c.commit();
            logger.info("Therapist: Update Therapist Commited");
            
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
        return doc;
    }
	
	public List<Therapist> fetchTherapist()  {
		logger.debug("**********FetchTherapist Method is called in TherapistDao*****");
		List<Therapist> list = new ArrayList<Therapist>();
		List<Integer> therapistList = new ArrayList<Integer>();
		List<Integer> nameList = new ArrayList<Integer>();
		int therapistid = 0;
		int nameid = 0;
		//Therapist therapist = new Therapist();
        Connection c = null;
    	
    	
        try {
            c = ConnectionHelper.getConnection();
            Statement statement = c.createStatement();
            logger.debug("Query:"+Queries.getQuery("select_therapistid"));
            String sql = Queries.getQuery("select_therapistid");
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                //therapist.setTherapistid(rs.getInt("therapistid"));
            	therapistid = rs.getInt("therapistid");
            	 nameid = rs.getInt("name_id");
            	therapistList.add(therapistid);
            	nameList.add(nameid);
            	
               
            }
            
            for(int i=0;i<therapistList.size();i++)
            {
            	therapistid = therapistList.get(i);
            	nameid = nameList.get(i);
            	
                String namesql = "select firstname,lastname,middlename from Name where nameid="+nameid;
                ResultSet rsName = statement.executeQuery(namesql);
                while(rsName.next()){
                	list.add(processName(rsName,therapistid));
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
	
	
	//Remove Therapist Method 
	public void removeTherapist(int nameid) {
		logger.debug("*****Therapistdao removeTherapist method called.********");
        Connection c = null;
        PreparedStatement ps;
        ResultSet rs;
        int therapistid=0;
        int addressid=0;
        int contactid=0;
        int officeid=0;
        try {
            c = ConnectionHelper.getConnection();
            c.setAutoCommit(false);
            logger.debug("Query:"+Queries.getQuery("selectForRemove_therapist"));
            ps = c.prepareStatement(Queries.getQuery("selectForRemove_therapist"));
            ps.setInt(1, nameid);
            rs = ps.executeQuery();
            while (rs.next()) {
               therapistid = rs.getInt("therapistid");
               addressid = rs.getInt("address_id");
               contactid = rs.getInt("contact_id");
               officeid  = rs.getInt("office_id");
            }
            logger.info("Therapist:Delete Therapist:"+ps);
            //Delete values from Name table
            logger.debug("Query:"+Queries.getQuery("delete_name"));
            ps = c.prepareStatement(Queries.getQuery("delete_name"));
            ps.setInt(1, nameid);
            ps.executeUpdate();
            logger.info("Therapist:Delete Name:"+ps);
          //Delete values from address table
            logger.debug("Query:"+Queries.getQuery("delete_address"));
            ps = c.prepareStatement(Queries.getQuery("delete_address"));
            ps.setInt(1, addressid);
            ps.executeUpdate();
            logger.info("Therapist:Delete Address:"+ps);
          //Delete values from contact table
            logger.debug("Query:"+Queries.getQuery("delete_contact"));
            ps = c.prepareStatement(Queries.getQuery("delete_contact"));
            ps.setInt(1, contactid);
            ps.executeUpdate();
            logger.info("Therapist:Delete Conact:"+ps);
          //Delete values from office table
            logger.debug("Query:"+Queries.getQuery("delete_office"));
            ps = c.prepareStatement(Queries.getQuery("delete_office"));
            ps.setInt(1, officeid);
            ps.executeUpdate();
            logger.info("Therapist:Delete Office:"+ps);
            //Delete values from Rate table
            logger.debug("Query:"+Queries.getQuery("delete_ratetable"));
            ps = c.prepareStatement(Queries.getQuery("delete_ratetable"));
            ps.setInt(1, therapistid);
            ps.executeUpdate();
            logger.info("Therapist:Delete Rate:"+ps);
            //Delete values from therapist table
            logger.debug("Query:"+Queries.getQuery("delete_therapist"));
            ps = c.prepareStatement(Queries.getQuery("delete_therapist"));
            ps.setInt(1, therapistid);
            int count = ps.executeUpdate();
            logger.info("Therapist:Delete Therapist:"+ps);
           c.commit();
           logger.info("Therapist:Delete Therapist Committed");
            
        } catch(SQLException e){
       	 try {
             c.rollback();
             e.printStackTrace();
        } catch(SQLException sqle){
        	sqle.printStackTrace();
        }
    }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }
	
	public Therapist getTherapist(int id)  {
		logger.debug("**********getTherapist Method is called in TherapistDao*****");
		Therapist therapist=new Therapist();
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement ps=null;
        
    	
			
        try {
            c = ConnectionHelper.getConnection();
            logger.debug("Query:"+Queries.getQuery("get_therapist"));
            String sql = Queries.getQuery("get_therapist");	
             ps = c.prepareStatement(sql);
            ps.setInt(1,  id);
            rs = ps.executeQuery();
            while (rs.next()) {
               //therapist = new Therapist();
               therapist.setTherapistid(rs.getInt("therapistid"));
    	       therapist.setAddressid(rs.getInt("address_id"));
    	       therapist.setContactid(rs.getInt("contact_id"));
    	       therapist.setOfficeid(rs.getInt("office_id"));
    	       therapist.setFlagid(rs.getInt("flagid"));
    	      
            }
            
          //Fetch data from name table using name_id
            logger.debug("Query:"+Queries.getQuery("get_name"));
            sql = Queries.getQuery("get_name");	
            ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
                logger.debug("Data fetched from Name table ");
                therapist.setFirstname(rs.getString("firstname"));
                therapist.setLastname(rs.getString("lastname"));
                therapist.setMiddlename(rs.getString("middlename"));
                
               
            }
            
            //Fetch data from addres table using address_id
            logger.debug("Query:"+Queries.getQuery("get_address"));
            sql = Queries.getQuery("get_address");	
            ps = c.prepareStatement(sql);
            ps.setInt(1, therapist.getAddressid());
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
                logger.debug("Data fetched from Address table ");
                therapist.setAddress1(rs.getString("address1"));
                therapist.setAddress2(rs.getString("address2"));
                therapist.setCity(rs.getString("city"));
                therapist.setState(rs.getString("state"));
                therapist.setZipcode(rs.getInt("zip"));
            }
           
            //Fetch data from contact table using contact_id
            logger.debug("Query:"+Queries.getQuery("get_contact"));
            sql = Queries.getQuery("get_contact");	
            ps = c.prepareStatement(sql);
            ps.setInt(1, therapist.getContactid());
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
                logger.debug("Data fetched from Address table ");
                therapist.setHomephone(rs.getString("homephone"));
                therapist.setWorkphone(rs.getString("workphone"));
                therapist.setMobile(rs.getString("mobile"));
                
            }
           
            //Fetch data from Office table using office_id
            logger.debug("Query:"+Queries.getQuery("get_office"));
            sql = Queries.getQuery("get_office");	
            ps = c.prepareStatement(sql);
            ps.setInt(1, therapist.getOfficeid());
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
                logger.debug("Data fetched from Office table ");
                therapist.setId(rs.getString("id"));
                therapist.setLicense(rs.getString("license"));
                therapist.setEin(rs.getString("ein"));
                
            }
           
            
          
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return therapist;
    }
	protected Therapist processRow(ResultSet rs) throws SQLException {
		   Therapist therapist = new Therapist();
		   therapist.setNameid(rs.getInt("nameid"));
	       therapist.setFirstname(rs.getString("firstname"));
	       therapist.setLastname(rs.getString("lastname"));
	       therapist.setMiddlename(rs.getString("middlename"));
	      
	        return therapist;
	    }
	protected Therapist processName(ResultSet rs, int therapistid) throws SQLException {
		   Therapist therapist = new Therapist();
		   therapist.setTherapistid(therapistid);
		   therapist.setFirstname(rs.getString("firstname"));
	       therapist.setMiddlename(rs.getString("middlename"));
	       therapist.setLastname(rs.getString("lastname"));
	      
	        return therapist;
	    }
	
	

}
