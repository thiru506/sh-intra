package org.ofbiz.humanres;


import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javolution.util.FastList;
import javolution.util.FastMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.fop.fo.properties.CondLengthProperty;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.security.Security;
import org.ofbiz.base.util.StringUtil;

import javolution.util.FastList;
import javolution.util.FastMap;



public class HumanResServices {

    public static final String module = HumanResServices.class.getName();
    /*
     * Helper that returns full employee profile.  This method expects the employee's EmploymentAndPerson
     * record as an input.
     */
   
    
	static void populateOrgEmployements(DispatchContext dctx, Map<String, ? extends Object> context, List employementList) {
    	Delegator delegator = dctx.getDelegator();
		LocalDispatcher dispatcher = dctx.getDispatcher();    	    	
        GenericValue userLogin = (GenericValue) context.get("userLogin");		
        GenericValue org = (GenericValue) context.get("org");
        Timestamp fromDate =  (Timestamp)context.get("fromDate");
        Timestamp thruDate =  (Timestamp)context.get("thruDate");
        Boolean isGroup = (Boolean)context.get("isGroup");
        if (org == null) {
        	return;
        }
        if(UtilValidate.isEmpty(fromDate)){
        	fromDate = UtilDateTime.nowTimestamp();
        }
        if(UtilValidate.isEmpty(thruDate)){
        	thruDate = UtilDateTime.getDayEnd(fromDate);
        }
        fromDate = UtilDateTime.getDayStart(fromDate);
        thruDate = UtilDateTime.getDayEnd(thruDate);
		List<GenericValue> internalOrgs = FastList.newInstance();
  		try{
  			List conditionList = FastList.newInstance();
  			if(UtilValidate.isNotEmpty(isGroup) && !isGroup){
  				// for single employee
  				conditionList.clear();
  	  			conditionList.add(EntityCondition.makeCondition("partyIdTo", EntityOperator.EQUALS, org.getString("partyId")));
  	  			conditionList.add(EntityCondition.makeCondition("roleTypeIdTo", EntityOperator.EQUALS,  "EMPLOYEE"));
  	  			conditionList.add(EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, thruDate));
  				conditionList.add(EntityCondition.makeCondition(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null), EntityOperator.OR, 
  						EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate)));
  				
  				EntityCondition condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);  		
  				List<GenericValue> employments = delegator.findList("EmploymentAndPerson", condition, null, UtilMisc.toList("firstName","-thruDate"), null, false);
  				
  				employementList.addAll(employments);
  				return;
  				
  			}
  			conditionList.clear();
  			conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, org.getString("partyId")));
  			conditionList.add(EntityCondition.makeCondition("partyRelationshipTypeId", EntityOperator.EQUALS, "GROUP_ROLLUP"));
  			conditionList.add(EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, thruDate));
			conditionList.add(EntityCondition.makeCondition(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null), EntityOperator.OR, 
					EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate)));
			
			EntityCondition condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);  		
			internalOrgs = delegator.findList("PartyRelationshipAndDetail", condition, null, UtilMisc.toList("groupName"), null, false);
			
  			/*internalOrgs = EntityUtil.filterByDate(delegator.findByAnd("PartyRelationshipAndDetail", UtilMisc.toMap("partyIdFrom", org.getString("partyId"),
				"partyRelationshipTypeId", "GROUP_ROLLUP"),UtilMisc.toList("groupName")),fromDate);*/
  			for(GenericValue internalOrg : internalOrgs){
  				Map<String, Object> inputParamMap = FastMap.newInstance();
  				inputParamMap.put("userLogin", userLogin);			  				
  				inputParamMap.put("org", internalOrg);
  				inputParamMap.put("fromDate", fromDate);
  				inputParamMap.put("thruDate", thruDate);
  				populateOrgEmployements(dctx, inputParamMap, employementList);
  			}
  			conditionList.clear();
  			conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, org.getString("partyId")));
  			conditionList.add(EntityCondition.makeCondition("roleTypeIdTo", EntityOperator.EQUALS,  "EMPLOYEE"));
  			conditionList.add(EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, thruDate));
			conditionList.add(EntityCondition.makeCondition(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null), EntityOperator.OR, 
					EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate)));
			
			condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);  		
			List<GenericValue> employments = delegator.findList("EmploymentAndPerson", condition, null, UtilMisc.toList("firstName","-thruDate"), null, false);
			/*List<GenericValue> employments = EntityUtil.filterByDate(delegator.findByAnd("EmploymentAndPerson", UtilMisc.toMap("partyIdFrom", org.getString("partyId"), 
					"roleTypeIdTo", "EMPLOYEE"), UtilMisc.toList("firstName")),fromDate);*/
			
			employementList.addAll(employments);
			
  		}catch(GenericEntityException e){
  			Debug.logError("Error fetching employments " + e.getMessage(), module);
  		}
		catch (Exception e) {
  			Debug.logError("Error fetching employments " + e.getMessage(), module);
		}  		
	}
	
	 public static Map<String, Object> getActiveEmployements(DispatchContext dctx, Map<String, ? extends Object> context) {
	    	Delegator delegator = dctx.getDelegator();
			LocalDispatcher dispatcher = dctx.getDispatcher();    	
	        GenericValue userLogin = (GenericValue) context.get("userLogin");
	        String orgPartyId =  (String)context.get("orgPartyId");
	        Timestamp fromDate =  (Timestamp)context.get("fromDate");
	        Timestamp thruDate = (Timestamp)context.get("thruDate");
	        
	        Security security = dctx.getSecurity();
	            	
			List employementList = FastList.newInstance();        
			try {
				Map<String, Object> inputParamMap = FastMap.newInstance();
				inputParamMap.put("userLogin", userLogin);	
				GenericValue org = delegator.findByPrimaryKey("PartyAndGroup", UtilMisc.toMap("partyId", orgPartyId));
				if(UtilValidate.isEmpty(org)){
					org = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", orgPartyId));
					inputParamMap.put("isGroup", false);
				}
						
				inputParamMap.put("org", org);
				inputParamMap.put("fromDate", fromDate);
				inputParamMap.put("thruDate", thruDate);
				populateOrgEmployements(dctx, inputParamMap, employementList);	
			}catch(GenericEntityException e){
	  			Debug.logError("Error fetching employments " + e.getMessage(), module);
	  		}
			List employeeIds = EntityUtil.getFieldListFromEntityList(employementList, "partyIdTo", true);
			List tempEmployementList = FastList.newInstance();
			for(int i=0;i<employeeIds.size();i++){
				String employeeId = (String)employeeIds.get(i);
				GenericValue tempEmployement = EntityUtil.getFirst(EntityUtil.filterByAnd(employementList,UtilMisc.toMap("partyIdTo",employeeId)));
				tempEmployementList.add(tempEmployement);
			}
	    	Map result = FastMap.newInstance();  
	    	result.put("employementList", tempEmployementList);
	    //Debug.logInfo("result:" + result, module);		 
	    	return result;
	    } 
	 
	 public static Map emplPunch(DispatchContext dctx, Map context)

		{

			GenericDelegator delegator = (GenericDelegator) dctx.getDelegator();
			GenericValue userLogin = (GenericValue) context.get("userLogin");
			Map<String, Object> result = ServiceUtil.returnSuccess();
			LocalDispatcher dispatcher = dctx.getDispatcher();
			int PunchId = 0;
			String employeePunchId = (String) context.get("employeePunchId");
			String partyId = (String) context.get("partyId");
			String emplPunchId = (String) context.get("emplPunchId");
			//String oldPunchType = (String) context.get("oldPunchType");
			String oldInOut = (String) context.get("oldInOut");
			Date punchdate = (Date) context.get("punchdate");
			Time punchtime = (Time) context.get("punchtime");
			String PunchType = (String) context.get("PunchType");
			String inout = (String) context.get("InOut");
			String note = (String) context.get("Note");
			String isManual = (String) context.get("isManual");
			String consolidatedFlag = (String) context.get("consolidatedFlag");
			String flag = "out";
			GenericValue emplPunch = null;
			
			 if(UtilValidate.isNotEmpty(employeePunchId)){
				 try{
					 emplPunch = delegator.findOne("EmplPunch", UtilMisc.toMap("employeePunchId",employeePunchId), false);
			         punchdate = emplPunch.getDate("punchdate");
			         
			         if(UtilValidate.isEmpty(punchtime)){
			        	 punchtime = emplPunch.getTime("punchtime");
			         }
			         if(UtilValidate.isEmpty(PunchType)){
			        	 PunchType = emplPunch.getString("PunchType");
			         }
			         
			         
				 }catch(Exception e){
					 Debug.logError("Error In fetch employeePunch :"+e.toString(), module);
					 return ServiceUtil
								.returnError("Error In fetch employeePunch :"+e.toString());
				 }
	         	
	         	
	         }
				
			String date3 = punchdate.toString();
			String dateArr2[] = date3.split(Pattern.quote("-"));
			String contactMechId = null;
			Security security = dctx.getSecurity();
			int sec = 0, sec0 = 0, sec1 = 0;
			int min = 0, min0 = 0, min1 = 0;
			int hr = 0, hr0 = 0, hr1 = 0;
	        Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
	        /*if(UtilValidate.isEmpty(consolidatedFlag)){
	        	if (!(security.hasEntityPermission("EMP_PUNCH_OOD", "_UPDATE", userLogin)) && (UtilDateTime.getIntervalInDays(UtilDateTime.toTimestamp(punchdate),nowTimestamp) >0) && PunchType.equals("Ood")) {
	            	String errMsg = "you don't have permissoin to edit previous day punch";
	            	Debug.logError(errMsg, module);
	                return ServiceUtil.returnError(errMsg);
	            } 
	        }*/
			String punchout = punchtime.toString();
			String punchintime = null;
			String punchouttime = null;
			int dai = Integer.parseInt(dateArr2[2]);

			int res = dai % 7;
			if ((dai % 7) != 0) {
				PunchId = res;

			} else {
				PunchId = 7;

			}		
			//Debug.log("emplPunchId----" + emplPunchId, module);
			if (emplPunchId == null) {
				try {

					List conditionList = UtilMisc.toList(EntityExpr.makeCondition(
							"partyId", EntityOperator.EQUALS, partyId), EntityExpr
							.makeCondition("PunchType", EntityOperator.EQUALS,
									PunchType), EntityExpr.makeCondition(
							"punchdate", EntityOperator.EQUALS, punchdate),
							EntityExpr.makeCondition("InOut",
									EntityOperator.EQUALS, inout));
					List conditionList1 = UtilMisc.toList(EntityExpr.makeCondition(
							"partyId", EntityOperator.EQUALS, partyId), EntityExpr
							.makeCondition("PunchType", EntityOperator.EQUALS,
									PunchType), EntityExpr.makeCondition(
							"punchdate", EntityOperator.EQUALS, punchdate));

					Map enConList = UtilMisc.toMap("partyId", partyId, "PunchType",
							PunchType, "punchdate", punchdate);
					
					List tops1 = delegator.findByAnd("EmplPunch", enConList);
					Debug.logInfo("after findby..........." + tops1.size(), module);
					//commented out for bio-metric punch
					/*try {
				    	GenericValue punchInoutTenantConfiguration = delegator.findOne("TenantConfiguration", UtilMisc.toMap("propertyName", "PUNCHINOUT_CHECK","propertyTypeEnumId","HUMANRES"), false);
				    	 if(UtilValidate.isNotEmpty(punchInoutTenantConfiguration)&& ("Y".equals(punchInoutTenantConfiguration.get("propertyValue")))){
				    		 if (tops1.size() > 0) {
									if ((tops1.size() % 2 == 1) && inout.equals("IN"))
										return ServiceUtil
												.returnError("Please PUNCH-OUT Before PUNCH-IN Again....");
								}
				    	 }
			    	}catch(GenericEntityException e){
			    		 Debug.logError(e, module);    
			    	}*/
					

					List tops = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
							"partyId", partyId, "PunchType", PunchType,
							"punchdate", punchdate, "InOut", inout));				
					if (tops.size() == 0)
						emplPunchId = new Integer(PunchId).toString();

					else{
						int e = tops.size();
						PunchId = PunchId + 7 * e;
						emplPunchId = new Integer(PunchId).toString();
						
					}

				} catch (GenericEntityException e) {
					return ServiceUtil
							.returnError("Error In generating emplPunchId ");
				}

			}
			context.put("emplPunchId",emplPunchId);
			//Debug.logInfo("emplPunchId===" + emplPunchId, module);

			try {

				List conditionList2 = UtilMisc.toList(EntityExpr.makeCondition(
						"partyId", EntityOperator.EQUALS, partyId), EntityExpr
						.makeCondition("punchdate", EntityOperator.EQUALS,
								punchdate), EntityExpr.makeCondition("PunchType",
						EntityOperator.EQUALS, "Normal"));

				List south = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
						"partyId", partyId, "punchdate", punchdate, "PunchType",
						"Normal"));
				Debug.logInfo("===================[ " + punchdate
						+ " ]=================\n\n", module);

				int sub = south.size();

				if (sub % 2 == 0) {

					/*if (!PunchType.equals("Normal")) {

						return ServiceUtil.returnError("You can't punch for ["
								+ PunchType + "] without Normal Punch ");
					}*/
				}
			} catch (GenericEntityException e) {
				return ServiceUtil
						.returnError("Error in PunchType Please contact adminstrator ");
			}

			try {

				List conditionListb = UtilMisc.toList(EntityExpr.makeCondition(
						"partyId", EntityOperator.EQUALS, partyId), EntityExpr
						.makeCondition("punchdate", EntityOperator.EQUALS,
								punchdate), EntityExpr.makeCondition("PunchType",
						EntityOperator.EQUALS, "Break"));

				List brk = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
						"partyId", partyId, "punchdate", punchdate, "PunchType",
						"Break"));

				int subb = brk.size();

				if (subb % 2 != 0) {

					/*if (!PunchType.equals("Break")) {

						return ServiceUtil
								.returnError(" Please PunchOut for Break First ");
					}*/
				}
			} catch (GenericEntityException e) {
				return ServiceUtil
						.returnError("Error in PunchType[Break] Please contact adminstrator ");
			}

			try {

				List conditionListl = UtilMisc.toList(EntityExpr.makeCondition(
						"partyId", EntityOperator.EQUALS, partyId), EntityExpr
						.makeCondition("punchdate", EntityOperator.EQUALS,
								punchdate), EntityExpr.makeCondition("PunchType",
						EntityOperator.EQUALS, "Lunch"));

				List lun = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
						"partyId", partyId, "punchdate", punchdate, "PunchType",
						"Lunch"));

				int subl = lun.size();

				if (subl % 2 != 0) {

					if (!PunchType.equals("Lunch")) {

						return ServiceUtil
								.returnError(" Please PunchOut for Lunch First ");
					}
				}
			} catch (GenericEntityException e) {
				return ServiceUtil
						.returnError("Error in PunchType[Lunch] Please contact adminstrator ");
			}

			try {

			List check = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
						"partyId", partyId, "PunchType", PunchType, "punchdate",
						punchdate));
			/*try {
		    	GenericValue punchInoutTenantConfiguration = delegator.findOne("TenantConfiguration", UtilMisc.toMap("propertyName", "PUNCHINOUT_CHECK","propertyTypeEnumId","HUMANRES"), false);
		    	 if(UtilValidate.isNotEmpty(punchInoutTenantConfiguration)&& ("Y".equals(punchInoutTenantConfiguration.get("propertyValue")))){
		    		 if (check.size() == 0) {

		 				if (inout.equals("OUT")) {
		                      
		 					return ServiceUtil
		 							.returnError("You can't punch out without punchin");
		 				}
		 			}
		    	 }
	    	}catch(GenericEntityException e){
	    		 Debug.logError(e, module);    
	    	}*/
			
			    //commented out for bio-metric punch
				/*if (check.size() == 0) {

					if (inout.equals("OUT")) {
	                     
						return ServiceUtil
								.returnError("You can't punch out without punchin");
					}
				}*/
			} catch (GenericEntityException e) {
				return ServiceUtil.returnError("Error Please contact adminstrator");
			}

			// This is to send email to admin if employee punch in after specified
			// time
			try {

				List exprList = UtilMisc.toList(EntityCondition.makeCondition(
						"partyId", EntityOperator.EQUALS, partyId), EntityCondition
						.makeCondition("contactMechTypeId", EntityOperator.EQUALS,
								"EMAIL_ADDRESS"));
				EntityCondition condition = EntityCondition.makeCondition(exprList,
						EntityOperator.AND);
				List tasks = delegator.findList("ContactPersonDetails", condition,
						null, null, null, false);
				List requirements = FastList.newInstance();
				for (Iterator iter = tasks.iterator(); iter.hasNext();) {
					Map union = FastMap.newInstance();
					GenericValue requirement = (GenericValue) iter.next();
					contactMechId = requirement.getString("contactMechId");
				}

				
				List notiemail = delegator.findByAnd("EmplPunch", UtilMisc.toMap(
						"partyId", partyId, "punchdate", punchdate, "PunchType",
						"Normal"));

				if (notiemail.size() == 0) {

					String firstpunchtime = punchtime.toString();
					String subpunchtime[] = firstpunchtime.split(":");
					int hrpunchtime = Integer.parseInt(subpunchtime[0]);
					int minpunchtime = Integer.parseInt(subpunchtime[1]);
					int secpunchtime = Integer.parseInt(subpunchtime[2]);

					List var1 = new ArrayList(); // =delegator.findByAnd("Notification",null);
					Iterator t1 = var1.iterator();
					while (t1.hasNext()) {
						GenericValue gvar1 = (GenericValue) t1.next();
						String pruleid = gvar1.getString("inputParamEnumId");
						EntityExpr condition_punchin2 = EntityCondition
								.makeCondition("inputParamEnumId",
										EntityOperator.EQUALS, "LATE_PUNCH");
						List var2 = delegator.findByAnd("Notification",
								UtilMisc.toMap("inputParamEnumId", "LATE_PUNCH"));
						Iterator t2 = var2.iterator();
						while (t2.hasNext()) {

							GenericValue gvar2 = (GenericValue) t2.next();
							String operatorEnumId = gvar2
									.getString("operatorEnumId");
							String condValue = gvar2.getString("condValue");
							int lenofcondValue = condValue.length();
							int minvalue = 59;
							int hrvalue = 0;
							if (lenofcondValue > 2) {
								String scondValue[] = condValue.split(":");
								hrvalue = Integer.parseInt(scondValue[0]);
								minvalue = Integer.parseInt(scondValue[1]);
							}

							if (lenofcondValue <= 2) {
								hrvalue = Integer.parseInt(condValue);
							}
							if (operatorEnumId.equals("IS_AFT")) {

								if ((hrpunchtime > hrvalue)
										|| ((hrpunchtime == hrvalue) && (minpunchtime > minvalue))) {

									// String user=userLogin.toString();
									String tm = punchtime.toString();
									String content = "Employee Punched In Very late......Punched in at "
											+ tm;
									Map commEventMap = FastMap.newInstance();
									commEventMap.put("communicationEventTypeId",
											"AUTO_EMAIL_COMM");
									commEventMap.put("statusId", "COM_IN_PROGRESS");
									commEventMap.put("contactMechTypeId",
											"EMAIL_ADDRESS");
									commEventMap.put("partyIdFrom", partyId);
									commEventMap.put("partyIdTo", "admin");
									commEventMap.put("contactMechIdFrom",
											contactMechId);
									commEventMap.put("contactMechIdTo", "admin");
									commEventMap.put("datetimeStarted",
											UtilDateTime.nowTimestamp());
									commEventMap.put("datetimeEnded",
											UtilDateTime.nowTimestamp());
									commEventMap.put("subject", "Late Punch");
									commEventMap.put("content", content);
									commEventMap.put("userLogin", userLogin);
									commEventMap.put("contentMimeTypeId",
											"text/html");
									Map<String, Object> createResp = null;
									try {
										createResp = dispatcher.runSync(
												"createCommunicationEvent",
												commEventMap);
									} catch (GenericServiceException e) {
										Debug.logError(e, module);
										return ServiceUtil.returnError(e
												.getMessage());
									}
									if (ServiceUtil.isError(createResp)) {
										return ServiceUtil.returnError(ServiceUtil
												.getErrorMessage(createResp));
									}

								}

							}

						}
						break;
					}

				} 

			} catch (GenericEntityException e) {
				return ServiceUtil.returnError("Error Please contact adminstrator");
			}

			// This is to send mail if employee took more than 1hr for lunch
			try {
				List exprList = UtilMisc.toList(EntityCondition.makeCondition(
						"partyId", EntityOperator.EQUALS, partyId), EntityCondition
						.makeCondition("contactMechTypeId", EntityOperator.EQUALS,
								"EMAIL_ADDRESS"));
				EntityCondition condition = EntityCondition.makeCondition(exprList,
						EntityOperator.AND);
				List tasks = delegator.findList("ContactPersonDetails", condition,
						null, null, null, false);
				List requirements = FastList.newInstance();
				for (Iterator iter = tasks.iterator(); iter.hasNext();) {
					Map union = FastMap.newInstance();
					GenericValue requirement = (GenericValue) iter.next();
					contactMechId = requirement.getString("contactMechId");
				}
			} catch (GenericEntityException e) {
				return ServiceUtil.returnError("Error while finding contactmechId");
			}

			if (PunchType.equals("Lunch")) {
				if (inout.equals("OUT")) {

					try {

						List conditionList = UtilMisc.toList(EntityExpr
								.makeCondition("partyId", EntityOperator.EQUALS,
										partyId), EntityExpr.makeCondition(
								"PunchType", EntityOperator.EQUALS, "Lunch"),
								EntityExpr.makeCondition("punchdate",
										EntityOperator.EQUALS, punchdate),
								EntityExpr.makeCondition("InOut",
										EntityOperator.EQUALS, "IN"));
						List tasks = delegator.findByAnd("EmplPunch", UtilMisc
								.toMap("partyId", partyId, "PunchType", "Lunch",
										"punchdate", punchdate, "InOut", "IN"));

						if (tasks.size() > 0) {
							Iterator tr = tasks.iterator();
							while (tr.hasNext()) {
								GenericValue supr = (GenericValue) tr.next();
								punchintime = supr.getString("punchtime");

							}
						}
						if (tasks.size() > 0) {

							// String Intime = punchintime.toString();
							String dateArr[] = punchintime
									.split(Pattern.quote(":"));
							sec0 = Integer.parseInt(dateArr[2]);
							min0 = Integer.parseInt(dateArr[1]);
							hr0 = Integer.parseInt(dateArr[0]);

						}

					} catch (GenericEntityException e) {

						return ServiceUtil
								.returnError("Error in reading punchintime ");

					}

					
					punchouttime = punchout;
					String dateArr1[] = punchouttime.split(Pattern.quote(":"));
					sec0 = Integer.parseInt(dateArr1[2]);
					min1 = Integer.parseInt(dateArr1[1]);
					hr1 = Integer.parseInt(dateArr1[0]);


				}
			}
			// this is end for lunch hr notification
			try {
	            if(UtilValidate.isNotEmpty(employeePunchId)){
	            	emplPunch.set("emplPunchId", emplPunchId);
	            	
	            }else{
	            	
	            	emplPunch = delegator.makeValue("EmplPunch", UtilMisc
	    					.toMap("partyId", partyId, "emplPunchId", emplPunchId,
	    							"PunchType", PunchType, "punchdate", punchdate,
	    							"InOut", inout ,"isManual" ,isManual)); // create a generic value from id
	            	emplPunch.setNextSeqId();
	            }
				
													// we just got

	            emplPunch.setNonPKFields(context); // move non-primary key fields
													// from input parameters to
													// genericvalue
				
				Timestamp punchDateTime =
					    Timestamp.valueOf(
					        new SimpleDateFormat("yyyy-MM-dd ")
					        .format(punchdate) // get the current date as String
					        .concat(punchtime.toString())        // and append the time
					    );
				
				emplPunch.set("punchDateTime", punchDateTime);
				
				
				if(UtilValidate.isEmpty(consolidatedFlag)){
					emplPunch.set("createdDate", UtilDateTime.nowTimestamp());
					emplPunch.set("createdByUserLogin", userLogin.getString("userLoginId"));
					emplPunch.set("lastModifiedByUserLogin", userLogin.getString("userLoginId"));
					emplPunch.set("lastModifiedDate", UtilDateTime.nowTimestamp());
				}
				Debug.logInfo("emplPunch-==========="+emplPunch,module);
				
				delegator.createOrStore(emplPunch);
				
				Map resultMap = ServiceUtil.returnSuccess("Data added successfully");
				resultMap.put("employeePunchId",emplPunch.get("employeePunchId"));
				return resultMap;

			} catch (Exception e) {
	            Debug.logError("Unable to add punch, mismatch found! , Contact administrator"+e.toString(), punchouttime);
				return ServiceUtil
						.returnError("Unable to add punch, mismatch found! , Contact administrator :"+e.toString());

			}

		}
}
