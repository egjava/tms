package com.tms.service;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.tms.bean.ActivityType;
import com.tms.bean.ReportGeneration;
import com.tms.bean.Client;
import com.tms.bean.Diagnosis;
import com.tms.bean.Flag;
import com.tms.bean.Report;
import com.tms.bean.Therapist;
import com.tms.bean.Client;
import com.tms.bean.TherapistRate;
import com.tms.dao.ActivityTypedao;

import com.tms.dao.Clientdao;
import com.tms.dao.Diagnosisdao;
import com.tms.dao.Flagdao;
import com.tms.dao.ReportGenerationdao;
import com.tms.dao.Reportdao;
import com.tms.dao.TherapistRatedao;
import com.tms.dao.Therapistdao;
import com.tms.exception.FlagException;


 
@Path("resource")
public class TMSService {
	
	Flagdao flagdao = new Flagdao();
	Therapistdao therapistdao = new Therapistdao();
	ActivityTypedao acttypdao = new ActivityTypedao();
	Diagnosisdao diagdao = new Diagnosisdao();
	TherapistRatedao therapistratedao = new TherapistRatedao();
	Clientdao clientdao = new Clientdao();
	Reportdao reportdao = new Reportdao();
	ReportGenerationdao binsdao = new ReportGenerationdao();
	
	final static Logger logger = Logger.getLogger(TMSService.class);
	
	// To Add New Flag
	
	@GET @Path("/addFlag/{flag}") 
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	public Response getFlag(@PathParam("flag") String flagname) {
		logger.debug("*******Create Flag Method Called*****") ;
		Flag newFlag = new Flag();
		try{
				newFlag.setFlagname(flagname);
				logger.debug("******* DAO Create Flag Method Called*****") ;
				flagdao.createFlag(newFlag);
		}
		catch(Exception e)
		{
			try {
				logger.debug("Exception occured");
				throw new FlagException();
			} catch (FlagException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return Response.ok(newFlag).build();
	}
	
	//Search for already existing Flag to modify
	
	@GET @Path("searchFlag/{flagval}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Flag> findByFlag(@PathParam("flagval") String flagValue) {
		logger.debug("findByFlag: " + flagValue);
		return flagdao.searchFlag(flagValue);
	}
	
	//Search for already existing Therapist to modify
	
		@GET @Path("searchTherapist/{therafirstname}/{theralastname}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<Therapist> findByTherapist(@PathParam("therafirstname") String firstname,
											@PathParam("theralastname") String lastname) {
			logger.debug("findByTherapist: " + firstname +","+lastname);
			return therapistdao.searchTherapist(firstname,lastname);
		}
		
		//Retrieve the client rate based on activity and therapistid
		
		@GET @Path("clientNewActivityRateGet/{therapistid}/{acttypeid}/{nameid}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Client findClientActivityRate(@PathParam("therapistid") int therapistid,
											@PathParam("nameid") int nameid,
											@PathParam("acttypeid") int acttypeid) {
			logger.debug("findClientActivityRate: " + therapistid +","+nameid+","+acttypeid);
			return clientdao.findClientActivityRate(therapistid,nameid,acttypeid);
		}
		
		//Retrieve the client rate based on activity and therapistid
		
				
				@POST
				@Path("/clientAddNewActivity")
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public Client addNewActivityForClient(Client client) {
					logger.debug("addNewActivityForClient: ");
					return clientdao.addNewActivityForClient(client);
				}
				
				//Update a NewActivity
				
				@POST
				@Path("/clientUpdateNewActivity")
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public Client updateNewActivityForClient(Client client) {
					logger.debug("updateNewActivityForClient: ");
					return clientdao.updateNewActivityForClient(client);
				}
				
				//Add a new payment
		
				
				@POST
				@Path("/addPayment")
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public Client addPayment(Client client) {
					logger.debug("addPayment for client: ");
					return clientdao.addPayment(client);
				}
		
		
		
		//Search for already existing client to modify
		
			@GET @Path("searchClient/{clientfirstname}/{clientlastname}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public List<Client> findByClient(@PathParam("clientfirstname") String firstname,
												@PathParam("clientlastname") String lastname) {
				logger.debug("findByClient: " + firstname +","+lastname);
				return clientdao.searchClient(firstname,lastname);
			}
	//Delete the selected Flag
	
	@DELETE @Path("deleteFlag/{flagid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("flagid") int flagid) {
		logger.debug("****** Remove Flag method called****");
		flagdao.removeFlag(flagid);
	}
	
	//Add a new Client
	
		@POST
		@Path("/client")
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Client createClient(Client client) {
			logger.debug("************Createing Client Method Called********");
			return clientdao.createClient(client);
		}
		
		//Fetch Client list from DB
		
		@POST
		@Path("/fetchClient")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<Client> fetchClient() {
			logger.debug("************Fetch Client Method Called********");
			return clientdao.fetchClient();
		}
		
		//Fetch Report from DB
		
				@POST
				@Path("/fetchreport")	
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public List<Report> fetchReport(Report report) {
					logger.debug("************Fetch Report Method Called********");
					
					if(report.getTherapistid() == 0)
						return reportdao.fetchReportWC(report);
					else
						return reportdao.fetchReportWTC(report);
					
					
				}
				
		
		//Update Client
		
			@POST
			@Path("/clientUpdate/{clientid}")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Client updateClient(Client client,@PathParam("clientid") int clientid) {
				logger.debug("************Update Client Method Called********");
				return clientdao.updateClient(client,clientid);
			}
		
		//Fetch Client Details
		
			
		@POST
		@Path("/fetchClientDetails/{cnameid}")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Client fetchClientDetails(@PathParam("cnameid") int cnameid) {
			logger.debug("************Fetch Client Details Method Called********");
			return clientdao.fetchClientDetails(cnameid);
		}
		
		//Add any overridden rate
		
			@POST
			@Path("/addWaiver")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Client addWaiver(Client client) {
				logger.debug("************add Waiver Method Called********");
				return clientdao.addWaiver(client);
			}
			//Update overridden rate
			
			@POST
			@Path("/updateWaiver")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Client updateWaiver(Client client) {
				logger.debug("************Update Waiver Method Called********");
				return clientdao.updateWaiver(client);
			}
	
	//Add a new Therapist
	
	@POST
	@Path("/therapist")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Therapist create(Therapist doc) {
		logger.debug("************Createing Therapist Method Called********");
		return therapistdao.createTherapist(doc);
	}
	
	//Update Therapist
	
		@POST
		@Path("/therapistUpdate/{id}")
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Therapist updateTherapist(Therapist doc,@PathParam("id") int nameid) {
			logger.debug("************Updating Therapist Method Called********");
			return therapistdao.updateTherapist(doc,nameid);
		}
		
		//Fetch TherapistList from DB
		
		@POST
		@Path("/fetchTherapist")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<Therapist> fetchTherapist() {
			logger.debug("************Fetch Therapist Method Called********");
			return therapistdao.fetchTherapist();
		}
		
		//Delete the selected Therapist
		
		@DELETE @Path("deleteTherapist/{nameid}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public void removeTherapist(@PathParam("nameid") int nameid) {
			logger.debug("****** Remove Therapist method called****");
			therapistdao.removeTherapist(nameid);
		}
	//Delete the selected Client
		
		@DELETE @Path("deleteClient/{clientid}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public void removeClient(@PathParam("clientid") int clientid) {
			logger.debug("****** Remove Client method called****"+clientid);
			clientdao.removeClient(clientid);
		}
		
		//Fetch TherapistRate
		
			@GET
			@Path("/therapistRateGet/{id}")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public TherapistRate getTherapistRate(@PathParam("id") int therapistid) {
				logger.debug("************Updating Therapist Rate Method Called********");
				return therapistratedao.getTherapistRate(therapistid);
			}
			
			//Fetch ClientRate
			
			@GET
			@Path("/clientRateGet/{therapistid}/{clientid}")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Client getTherapistRate(@PathParam("therapistid") int therapistid,
													@PathParam("clientid") int clientid) {
				logger.debug("************Updating Client Rate Method Called********");
				return clientdao.getClientRate(therapistid,clientid);
			}
	
	//Add a Therapist Rate
	
		@POST
		@Path("/therapistrate")
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public TherapistRate create(TherapistRate docRate) {
			logger.debug("************Creating Therapist Method Called********");
			return therapistratedao.createTherapistRate(docRate);
		}
		
		//Update a Therapist Rate
		
			@POST
			@Path("/therapistrateUpdate/{id}")
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public TherapistRate updateTherapistRate(TherapistRate docRate, @PathParam("id") int therapistid) {
				logger.debug("************Updatin Therapist Rate Method Called********");
				return therapistratedao.updateTherapistRate(docRate,therapistid);
			}
		
		// Fetch Therapist by Name id
		
		@GET @Path("therapistName/{id}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Therapist findByNameId(@PathParam("id") int id) {
			logger.debug("findByNameid: " + id);
			return therapistdao.getTherapist(id);
		}
	
	//Fetch Flag list from DB
	
	@POST
	@Path("/fetch")	
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Flag> fetchFlag() {
		logger.debug("************Fetch Flag Method Called********");
		return flagdao.fetchFlag();
	}
	
	//Update Flag
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Flag update(Flag flag) {
		logger.debug("************Updating Flag Method Called********");
		return flagdao.updateFlag(flag);
	}
	
	//Search for already existing New Activity to modify
	
	
	@POST
	@Path("/searchNewActivity")	
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Client> findByNewActivityTC(Client client) {
		logger.debug("findNewActivity by Therapist and Client: ");
			
		return clientdao.searchNewActivityTC(client);
	}
	
	//Delete the New Activity for a client
	
	@DELETE @Path("/deleteNewActivity")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void removeNewActivity(Client client) {
		logger.debug("****** Remove Activity method called****");
		clientdao.removeNewActivity(client);
	}
	
	//Find the Activity Type by id
	@POST
	@Path("/findNewActivity")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Client findByNewActivityById(Client client) {
		
		return clientdao.getNewActivityDetails(client);
	}
	
	
	
	//To add Diagnosis Code
	
		@POST
		@Path("/addDiagnosisCode")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public Diagnosis createDiagnosisCode(Diagnosis dcode) {
			logger.debug("************Creating Diagnosis Code Method Called********");
			return diagdao.createDiagnosisCode(dcode);
		}
		
		//Search for already existing Diagnosis Code to modify
		
			@GET @Path("searchDiagnosisCode/{diagcode}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public List<Diagnosis> findByDiagnosisCode(@PathParam("diagcode") String diagcode) {
				logger.debug("findByDiagnosisCode: " + diagcode);
				return diagdao.searchDiagnosisCode(diagcode);
			}
			//Find the Diagnosis Code by id
			
			@GET @Path("DiagnosisCode/{id}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Diagnosis findByDiagnosisCodeId(@PathParam("id") int id) {
				logger.debug("findByDiagnosisCodeid: " + id);
				return diagdao.getDiagnosisId(id);
			}
			
			//Update the Diagnosis Code
			@POST
			@Path("/updateDiagnosisCode/{id}")	
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Diagnosis updateDiagnosisCode(Diagnosis diagcode,@PathParam("id") int id) {
				logger.debug("************Creating Diagnosis Code Method Called********");
				return diagdao.updateDiagnosisCode(diagcode,id);
			}
			
			//Delete the Diagnosis Code
			
			@DELETE @Path("deleteDiagnosisCode/{id}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public void removeDiagnosisCode(@PathParam("id") int diagnosisid) {
				logger.debug("****** Remove Diagnosis Code method called****");
				diagdao.removeDiagnosisCode(diagnosisid);
			}
			
			//Fetch Diagnsis Code list from DB
			
			@POST
			@Path("/fetchDiagnosisCode")	
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public List<Diagnosis> fetchDiagnosisCode() {
				logger.debug("************Fetch ActivityType Method Called********");
				return diagdao.fetchDiagnosisCode();
			}
	
			//To add Referral Source
			
			@POST
			@Path("/addReferralSource")	
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Flag create(Flag refsource) {
				logger.debug("************Creating Referral Source Method Called********");
				return flagdao.createReferralSource(refsource);
			}
			
			//Update the ReferralSource
			@POST
			@Path("/updateReferralSource/{refid}")	
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Flag updateReferralSource(Flag refsource,@PathParam("refid") int refid ) {
				logger.debug("************Updatin ReferralSource Method Called********");
				return flagdao.updateReferralSource(refsource, refid);
			}
			
			
			//Search for already existing ReferralSource to modify
			
			@GET @Path("searchReferralSource/{refsource}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public List<Flag> findByReferralSource(@PathParam("refsource") String refsource) {
				logger.debug("findByReferralSource: " + refsource);
				return flagdao.searchReferralSource(refsource);
			}
			
			@GET @Path("ReferralSource/{id}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public Flag findByReferralId(@PathParam("id") int refid) {
				logger.debug("findByReferralid: " + refid);
				return flagdao.getReferralId(refid);
			}
			
			//Delete the selected Referral Source
			
			@DELETE @Path("deleteReferral/{refid}")
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public void removeReferralSource(@PathParam("refid") int refid) {
				logger.debug("****** Remove Referral Source method called****");
				flagdao.removeReferralSource(refid);
			}
			
			//Fetch ReferralSource from DB
			
			@POST
			@Path("/fetchReferralSource")	
			@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
			@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
			public List<Flag> fetchReferralSource() {
				logger.debug("************Fetch Referral Source Method Called********");
				return flagdao.fetchReferralSource();
			}
	//To add Activity Type
	
	@POST
	@Path("/addActivityType")	
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ActivityType create(ActivityType actType) {
		logger.debug("************Creating Activty Type Method Called********");
		return acttypdao.createActivitytype(actType);
	}
	
	//Search for already existing Activity Type to modify
	
		@GET @Path("searchActivityType/{actType}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<ActivityType> findByActivityType(@PathParam("actType") String actType) {
			logger.debug("findByActivityType: " + actType);
			return acttypdao.searchActivitytype(actType);
		}
		
		@GET @Path("activitytype/{id}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public ActivityType findByTypeId(@PathParam("id") int id) {
			logger.debug("findByTypeid: " + id);
			return acttypdao.getActivityId(id);
		}
		
		//Update the Activity Type
		@POST
		@Path("/updateActivityType")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public ActivityType update(ActivityType actType) {
			logger.debug("************Creating Activty Type Method Called********");
			return acttypdao.updateActivitytype(actType);
		}
		
		//Delete the selected Activity Type
		
		@DELETE @Path("deleteActivityType/{actTypeid}")
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public void removeActivityType(@PathParam("actTypeid") int actTypeid) {
			logger.debug("****** Remove Activity Type method called****");
			acttypdao.removeActivityType(actTypeid);
		}
		
		//Fetch ActivityType list from DB
		
		@POST
		@Path("/fetchActivityType")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<ActivityType> fetchActivityType() {
			logger.debug("************Fetch ActivityType Method Called********");
			return acttypdao.fetchActivityType();
		}
		
	//Generate Report for Bill-Insurance
		
		@POST
		@Path("/generateReportBI")	
		@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
		@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
		public List<ReportGeneration > generateReportBI(ReportGeneration bins) {
			logger.debug("************Generate Report For Bill Summary Method Called********");
			return binsdao.generateReportBI(bins);
			}
		
		//Generate Report for Bill-Summary		
				@POST
				@Path("/generateReportBS")	
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public List<ReportGeneration > generateReportBS(ReportGeneration bins) {
					logger.debug("************Generate Report For Bill Summary Method Called********");
					return binsdao.generateReportBS(bins);
					}
				
				//Generate Report for Therapist Money		
				@POST
				@Path("/generateReportTM")	
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public List<ReportGeneration > generateReportTM(ReportGeneration bins) {
					logger.info("************Generate Report For Therapist Money Method Called********");
					return binsdao.generateReportTM(bins);
					}
				
				//Generate Report for Client List		
				@POST
				@Path("/generateReportCL")	
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public List<ReportGeneration > generateReportCL(ReportGeneration bins) {
					logger.debug("************Generate Report For Client List Method Called********");
					return binsdao.generateReportCL(bins);
					}
				
				//Generate Report for Client List		
				@POST
				@Path("/generateReportTOT")	
				@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
				@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
				public List<ReportGeneration > generateReportTOT(ReportGeneration bins) {
					logger.debug("************Generate Report For Client List Method Called********");
					return binsdao.generateReportTOT(bins);
					}
		
		
	
	}
