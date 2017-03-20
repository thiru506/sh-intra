package com.serp;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilDateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilNumber;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.security.Security;


public class SchoolMgmtServices {
	public static final String module = SchoolMgmtServices.class.getName();
    
	public static Map<String, Object> createStudent(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String productId = (String) context.get("productId");
        String productFeatureId = (String) context.get("productFeatureId");
        String customTimePeriodId = (String) context.get("customTimePeriodId");
        String roleTypeId = (String) context.get("roleTypeId");
        String firstName = (String) context.get("firstName");
        String middleName = (String) context.get("middleName");
        String lastName = (String) context.get("lastName");
        String externalId = (String) context.get("externalId");
        String gender = (String) context.get("gender");
        String bloodGroup = (String) context.get("bloodGroup");
        Date birthDate = (Date) context.get("birthDate");
        String fatherName = (String) context.get("fatherName");
        String motherName = (String) context.get("motherName");
        String placeOfBirth = (String) context.get("placeOfBirth");
        String religion = (String) context.get("religion");
        String caste = (String) context.get("caste");
        String nationality = (String) context.get("nationality");
        String email = (String) context.get("emailAddress");
        String mobileNumber = (String) context.get("mobileNumber");
        String occupation = (String) context.get("occupation");
        String contactNumber =(String)context.get("residentialNumber");  
        String presentAddress1 = (String) context.get("presentAddress1");
        String presentAddress2 = (String) context.get("presentAddress2");
        String presentCity = (String) context.get("presentCity");
        String presentPostalCode = (String) context.get("presentPostalCode");
        String presentState = (String) context.get("presentState");
        String presentCuntry = (String) context.get("presentCuntry");
        String address1 = (String) context.get("address1");
		String address2 = (String) context.get("address2");
		String city =(String)context.get("city");
		String stateProvinceGeoId = (String) context.get("stateProvinceGeoId");
		String postalCode = (String) context.get("postalCode");
		String countryGeoId = (String) context.get("countryGeoId");
		String partyId =null;
		Map<String, Object> input = FastMap.newInstance();
		try {
			input.put("userLogin", userLogin);
            input.put("firstName",firstName);
            input.put("middleName",middleName);
            input.put("lastName",lastName);
            input.put("gender",gender);
            input.put("birthDate",birthDate);
            input.put("fatherName",fatherName);
            input.put("motherName",motherName);
            input.put("bloodGroup", bloodGroup);
            input.put("placeOfBirth",placeOfBirth);
            input.put("occupation",occupation);
            input.put("religion",religion);
            input.put("nationality",nationality);
            Map resultctx = dispatcher.runSync("createPerson", input);
            if(ServiceUtil.isError(resultctx)){
           	 	Debug.logError("Error while create person:"+ServiceUtil.getErrorMessage(resultctx), module);
           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(resultctx));
            }
            partyId = (String)resultctx.get("partyId");
            
            Timestamp fromDate=null;
            GenericValue customTimePeriod = delegator.findOne("CustomTimePeriod", UtilMisc.toMap("customTimePeriodId",customTimePeriodId), false);
			if(UtilValidate.isNotEmpty(customTimePeriod)){
				fromDate = UtilDateTime.toTimestamp(customTimePeriod.getDate("fromDate"));
			}
            
            GenericValue subScription = delegator.makeValue("Subscription");
            subScription.set("partyId", partyId);
            subScription.set("productId", productId);
            subScription.set("productFeatureId", productFeatureId);
            subScription.set("customTimePeriodId",customTimePeriodId);
            subScription.set("fromDate",fromDate);
            delegator.createSetNextSeqId(subScription);
            
            GenericValue partyRole = delegator.makeValue("PartyRole");
            partyRole.set("partyId", partyId);
            partyRole.set("roleTypeId",roleTypeId);
            delegator.create(partyRole);
            if(UtilValidate.isNotEmpty(caste)){
	            GenericValue partyCaste = delegator.makeValue("PartyClassification");
	            partyCaste.set("partyId", partyId);
	            partyCaste.set("partyClassificationGroupId", caste);
	            partyCaste.set("fromDate", UtilDateTime.nowTimestamp());
	            delegator.create(partyCaste);
            }
            
            String postalContactId=null;
            if (UtilValidate.isNotEmpty(address1)){
            	input.clear();
				input = UtilMisc.toMap("userLogin", userLogin, "partyId",partyId, "address1",address1, "address2", address2, "city", city, "stateProvinceGeoId", stateProvinceGeoId, "postalCode", postalCode, "countryGeoId",countryGeoId);
				Map	resultMap =  dispatcher.runSync("createPartyPostalAddress", input);
				if (ServiceUtil.isError(resultMap)) {
					Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	                return resultMap;
	            }
				postalContactId = (String) resultMap.get("contactMechId");
				input.clear();
				input = UtilMisc.toMap("userLogin", userLogin, "contactMechId", postalContactId, "partyId",partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION");
				resultMap =  dispatcher.runSync("createPartyContactMechPurpose", input);
				if (ServiceUtil.isError(resultMap)) {
				    Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	                return resultMap;
	            }
            }
            String presentPostalContactId=null;
            if (UtilValidate.isNotEmpty(presentAddress1)){
            	input.clear();
				input = UtilMisc.toMap("userLogin", userLogin, "partyId",partyId, "address1",presentAddress1, "address2", presentAddress2, "city", presentCity, "stateProvinceGeoId", presentState, "postalCode", presentPostalCode, "countryGeoId",presentCuntry);
				Map	resultMap =  dispatcher.runSync("createPartyPostalAddress", input);
				if (ServiceUtil.isError(resultMap)) {
					Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	                return resultMap;
	            }
				presentPostalContactId = (String) resultMap.get("contactMechId");	
				input.clear();
				input = UtilMisc.toMap("userLogin", userLogin, "contactMechId", presentPostalContactId, "partyId",partyId, "contactMechPurposeTypeId", "GENERAL_LOCATION");
				resultMap =  dispatcher.runSync("createPartyContactMechPurpose", input);
				if (ServiceUtil.isError(resultMap)) {
				    Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	                return resultMap;
	            }
            }
            String countryCode=null;
         // create phone number
 			if (UtilValidate.isNotEmpty(mobileNumber)){
 				if (UtilValidate.isEmpty(countryCode)){
 					countryCode	="91";
 				}
 	            input.clear();
 	            input.put("userLogin", userLogin);
 	            input.put("contactNumber",mobileNumber);
 	            input.put("contactMechPurposeTypeId","PRIMARY_PHONE");
 	            input.put("countryCode",countryCode);	
 	            input.put("partyId", partyId);
 	            Map outMap = dispatcher.runSync("createPartyTelecomNumber", input);
 	            if(ServiceUtil.isError(outMap)){
 	           	 	Debug.logError("failed service create party contact telecom number:"+ServiceUtil.getErrorMessage(outMap), module);
 	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
 	            }
 			}
             // create landLine number
 			if (UtilValidate.isNotEmpty(contactNumber)){
 	            input.clear();
 	            input.put("userLogin", userLogin);
 	            input.put("contactNumber",contactNumber);
 	            input.put("contactMechPurposeTypeId","PHONE_HOME");
 	            input.put("partyId", partyId);
 	            Map outMap = dispatcher.runSync("createPartyTelecomNumber", input);
 	            if(ServiceUtil.isError(outMap)){
 	           	 	Debug.logError("failed service create party contact telecom number:"+ServiceUtil.getErrorMessage(outMap), module);
 	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
 	            }
 			}
 		// Create Party Email
			if (UtilValidate.isNotEmpty(email)){
	            input.clear();
	            input.put("userLogin", userLogin);
	            input.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
	            input.put("emailAddress", email);
	            input.put("partyId", partyId);
	            input.put("verified", "Y");
	            input.put("fromDate", UtilDateTime.nowTimestamp());
	            Map outMap = dispatcher.runSync("createPartyEmailAddress", input);
	            if(ServiceUtil.isError(outMap)){
	           	 	Debug.logError("faild service create party Email:"+ServiceUtil.getErrorMessage(outMap), module);
	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	            }
			}
        
		} catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Student Successfully added..!");
		return result;
	}
	public static Map<String, Object> addStudentToHostel(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String roleTypeId = (String) context.get("roleTypeId");
        String partyId = (String) context.get("partyId");
        Date fromDate = (Date) context.get("fromDate");
        Date thruDate = (Date) context.get("thruDate");
        Timestamp fromDateTime = UtilDateTime.toTimestamp(fromDate);
        Timestamp thruDateTime = UtilDateTime.toTimestamp(thruDate);
        String facilityId = (String) context.get("facilityId");
        
        try {
	        	List conList= FastList.newInstance();
	        	conList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS ,partyId));
	        	conList.add(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null));
	        	EntityCondition cond=EntityCondition.makeCondition(conList,EntityOperator.AND);
	        	List<GenericValue> facilityPartys = delegator.findList("FacilityParty", cond, null,null, null, false);
	        	if(UtilValidate.isNotEmpty(facilityPartys)){    
	        		return ServiceUtil.returnError("This Student Already Existed..!");
	        	}else{	
	        		GenericValue facilityParty = delegator.makeValue("FacilityParty");
	        		facilityParty.set("partyId",partyId);
	        		facilityParty.set("facilityId",facilityId);
	        		facilityParty.set("roleTypeId",roleTypeId);
	        		facilityParty.set("fromDate",UtilDateTime.getDayStart(fromDateTime));
	        		if(UtilValidate.isNotEmpty(thruDateTime)){
	        			facilityParty.set("thruDate", UtilDateTime.getDayEnd(thruDateTime));
	        		}
	        		delegator.create(facilityParty);
	        	}
        }catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Student Added Succefully..!");
		return result;
	}
	public static Map<String, Object> updateStudentToHostel(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String roleTypeId = (String) context.get("roleTypeId");
        String partyId = (String) context.get("partyId");
        Date thruDate = (Date) context.get("thruDate");
        Timestamp fromDateTime = (Timestamp) context.get("fromDate");
        Timestamp thruDateTime = UtilDateTime.toTimestamp(thruDate);
        String facilityId = (String) context.get("facilityId");
        try {
        	List conList= FastList.newInstance();
        	conList.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS ,roleTypeId));
        	conList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS ,partyId));
        	conList.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS ,facilityId));
        	conList.add(EntityCondition.makeCondition("fromDate", EntityOperator.EQUALS ,fromDateTime));
        	EntityCondition cond=EntityCondition.makeCondition(conList,EntityOperator.AND);
        	List<GenericValue> facilityPartys = delegator.findList("FacilityParty", cond, null,null, null, false);
        	
        	if(UtilValidate.isNotEmpty(facilityPartys)){
        		GenericValue facilityParty = EntityUtil.getFirst(facilityPartys);
        		if(UtilValidate.isNotEmpty(thruDateTime)){
        			facilityParty.set("thruDate", UtilDateTime.getDayEnd(thruDateTime));
        		}
        		facilityParty.store();
        	}
        	
        }catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Student Updated Succefully..!");
		return result;
	}
	public static Map<String, Object> createVehicleRole(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String roleTypeId = (String) context.get("roleTypeId");
        String vehicleId = (String) context.get("vehicleId");
        String partyId = (String) context.get("partyId");
        Date fromDate = (Date) context.get("fromDate");
        Date thruDate = (Date) context.get("thruDate");
        Timestamp fromDateTime = UtilDateTime.toTimestamp(fromDate);
        Timestamp thruDateTime = UtilDateTime.toTimestamp(thruDate);
        String facilityId = (String) context.get("facilityId");
        
        try {
	        	List conList= FastList.newInstance();
	        	conList.add(EntityCondition.makeCondition("vehicleId", EntityOperator.EQUALS ,vehicleId));
	        	conList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS ,partyId));
	        	conList.add(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null));
	        	EntityCondition cond=EntityCondition.makeCondition(conList,EntityOperator.AND);
	        	List<GenericValue> facilityPartys = delegator.findList("VehicleRole", cond, null,null, null, false);
	        	GenericValue facilityParty = EntityUtil.getFirst(facilityPartys);
	        	if(UtilValidate.isNotEmpty(facilityParty) && (fromDateTime.compareTo(facilityParty.getTimestamp("fromDate"))) !=0){    
	        		return ServiceUtil.returnError("This Party Already Existed..!");
	        	}else{	
	        		GenericValue vechileRole = delegator.makeValue("VehicleRole");
	        		vechileRole.set("vehicleId",vehicleId);
	        		vechileRole.set("partyId",partyId);
	        		vechileRole.set("facilityId",facilityId);
	        		vechileRole.set("roleTypeId",roleTypeId);
	        		vechileRole.set("fromDate",UtilDateTime.getDayStart(fromDateTime));
	        		if(UtilValidate.isNotEmpty(thruDateTime)){
	        			vechileRole.set("thruDate", UtilDateTime.getDayEnd(thruDateTime));
	        		}
	        		delegator.createOrStore(vechileRole);
	        	}
        }catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Party Added Succefully..!");
		return result;
	}
	
	public static Map<String, Object> CreateStudentEnquiry(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        try{
        String firstName = (String) context.get("firstName");
        String description = (String) context.get("description");
        Timestamp time = (Timestamp)context.get("custRequestDate");
			GenericValue custRequest=delegator.makeValue("CustRequest");
			custRequest.set("custRequestTypeId","STUDENT_ENQUIRY");
			custRequest.set("statusId","ENQ_CREATED");
			custRequest.set("custRequestDate",time);
			custRequest.set("custRequestName", firstName);
			custRequest.set("description", description);
			delegator.createSetNextSeqId(custRequest);
			
        
		} catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Created Successfully.!");
		return result;
	}	
	public static String pramoteStudentsToNextClass(HttpServletRequest request,HttpServletResponse response){
		  Delegator delegator = (Delegator) request.getAttribute("delegator");
		  LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		  Locale locale = UtilHttp.getLocale(request);
		  HttpSession session = request.getSession();
		  GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		  Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
		  int rowCount = UtilHttp.getMultiFormRowCount(paramMap);
		  
		  if (rowCount < 1) {
				Debug.logError("No rows to process, as rowCount = " + rowCount,module);
				request.setAttribute("_ERROR_MESSAGE_", "No rows to process");
				return "error";
			}
		  	Map inputMap = FastMap.newInstance();
	        String fromProductId = (String)paramMap.get("fromProductId");
	        String toProductId = (String)paramMap.get("toProductId");
	        String customTimePeriodId = (String)paramMap.get("customTimePeriodId");
	        Timestamp fromDate =null;
	        Timestamp thruDate=null;
	        Timestamp prevDay=null;
		try {
			GenericValue customTimePeriod = delegator.findOne("CustomTimePeriod", UtilMisc.toMap("customTimePeriodId",customTimePeriodId), false);
			if(UtilValidate.isNotEmpty(customTimePeriod)){
				fromDate = UtilDateTime.toTimestamp(customTimePeriod.getDate("fromDate"));
				prevDay = UtilDateTime.addDaysToTimestamp(fromDate, -1);
			}
			List orderBy = UtilMisc.toList("-fromDate");
			List condList = FastList.newInstance();
			condList.add(EntityCondition.makeCondition("fromDate",EntityOperator.LESS_THAN,customTimePeriod.getDate("fromDate")));
			condList.add(EntityCondition.makeCondition("periodTypeId",EntityOperator.EQUALS,"ACADEMIC_YEAR"));
			EntityCondition cond = EntityCondition.makeCondition(condList,EntityOperator.AND);
			List<GenericValue> customTimePeriodList = delegator.findList("CustomTimePeriod",cond , null, orderBy, null, false);
			if(UtilValidate.isNotEmpty(customTimePeriodList)){
				thruDate = UtilDateTime.toTimestamp(EntityUtil.getFirst(customTimePeriodList).getDate("thruDate"));
			}else{
				thruDate = prevDay;
			}
			for (int i = 0; i < rowCount; i++) {
				String thisSuffix = UtilHttp.MULTI_ROW_DELIMITER + i;
				String partyId="";
				String subscriptionId="";
				if (paramMap.containsKey("partyId" + thisSuffix)) {
					partyId = (String) paramMap.get("partyId"+ thisSuffix);
				}
				if (paramMap.containsKey("subscriptionId" + thisSuffix)) {
					subscriptionId = (String) paramMap.get("subscriptionId"+ thisSuffix);
				}
				//papulating thruDate
				/*inputMap.clear();
				inputMap.put("subscriptionId", subscriptionId);
				inputMap.put("thruDate", thruDate);
				inputMap.put("userLogin", userLogin);
				Map updateRsltMap = dispatcher.runSync("updateSubscription", inputMap);
				if(ServiceUtil.isError(updateRsltMap)){
					Debug.logError("Error While Updating Subscription for "+partyId, module);
					request.setAttribute("_ERROR_MESSAGE_", "Error While Updating Subscription for "+partyId);
					return "error";
				}*/
				GenericValue subscription = delegator.findOne("Subscription", UtilMisc.toMap("subscriptionId",subscriptionId), false);
				subscription.set("thruDate", thruDate);
				subscription.store();
				
				inputMap.clear();
				inputMap.put("userLogin", userLogin);
				inputMap.put("partyId", partyId);
				inputMap.put("productId", toProductId);
				inputMap.put("fromDate", fromDate);
				inputMap.put("customTimePeriodId", customTimePeriodId);
				Map createRstlMap = dispatcher.runSync("createSubscription", inputMap);
				if(ServiceUtil.isError(createRstlMap)){
					Debug.logError("Error While Creating Subscription for "+partyId, module);
					request.setAttribute("_ERROR_MESSAGE_", "Error While Creating Subscription for "+partyId);
					return "error";
				}
		  }
		} catch (Exception e) {
            request.setAttribute("_ERROR_MESSAGE_", ServiceUtil.returnError(e.getMessage()));
			return "error";
        }
		return "success";
	}
	public static String processStudentsMarks(HttpServletRequest request,HttpServletResponse response){
		  Delegator delegator = (Delegator) request.getAttribute("delegator");
		  LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		  Locale locale = UtilHttp.getLocale(request);
		  HttpSession session = request.getSession();
		  GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		  Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
		  int rowCount = UtilHttp.getMultiFormRowCount(paramMap);
		  
		  if (rowCount < 1) {
				Debug.logError("No rows to process, as rowCount = " + rowCount,module);
				request.setAttribute("_ERROR_MESSAGE_", "No rows to process");
				return "error";
			}
		  	Map inputMap = FastMap.newInstance();
	        String classId = (String)paramMap.get("fromProductId");
	        String examTypeId = (String)paramMap.get("examTypeId");
	        String customTimePeriodId = (String)paramMap.get("customTimePeriodId");
		try {
			
			for (int i = 0; i < rowCount; i++) {
				String thisSuffix = UtilHttp.MULTI_ROW_DELIMITER + i;
				String partyId="";
				String subjectId="";
				String marksStr="";
				String attempted="";
				BigDecimal marks = BigDecimal.ZERO;
				if (paramMap.containsKey("partyId" + thisSuffix)) {
					partyId = (String) paramMap.get("partyId"+ thisSuffix);
				}
				if (paramMap.containsKey("subjectId" + thisSuffix)) {
					subjectId = (String) paramMap.get("subjectId"+ thisSuffix);
				}
				if (paramMap.containsKey("marks" + thisSuffix)) {
					marksStr = (String) paramMap.get("marks"+ thisSuffix);
					marks = new BigDecimal(marksStr);
				}
				if (paramMap.containsKey("attempted" + thisSuffix)) {
					attempted = (String) paramMap.get("attempted"+ thisSuffix);
				}
				
				inputMap.clear();
				inputMap.put("partyId", partyId);
				inputMap.put("subjectId", subjectId);
				inputMap.put("customTimePeriodId", customTimePeriodId);
				inputMap.put("classId", classId);
				inputMap.put("examTypeId", examTypeId);
				inputMap.put("userLogin", userLogin);
				inputMap.put("marks", marks);
				inputMap.put("isAttempted", attempted);
				
				Map resultMap = dispatcher.runSync("createOrUpdateExamMarkDetails", inputMap);
				if(ServiceUtil.isError(resultMap)){
					Debug.logError("Error While Processing for "+partyId, module);
					request.setAttribute("_ERROR_MESSAGE_", "Error While Processing for "+partyId);
					return "error";
				}
				
		  }
		} catch (Exception e) {
          request.setAttribute("_ERROR_MESSAGE_", ServiceUtil.returnError(e.getMessage()));
			return "error";
      }
		return "success";
	}
	public static Map<String, Object> createOrUpdateExamMarkDetails(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String partyId = (String) context.get("partyId");
        String subjectId = (String) context.get("subjectId");
        String classId = (String) context.get("classId");
        String customTimePeriodId = (String) context.get("customTimePeriodId");
        String examTypeId = (String) context.get("examTypeId");
        String isAttempted = "";
        if(UtilValidate.isNotEmpty(context.get("isAttempted"))){
        	isAttempted = (String) context.get("isAttempted");
        }
        BigDecimal marks = BigDecimal.ZERO;
        if(UtilValidate.isNotEmpty(context.get("marks"))){
        	marks = (BigDecimal) context.get("marks");
        }
		try {
			Map input = FastMap.newInstance();
			input.put("partyId", partyId);
			input.put("subjectId", subjectId);
			input.put("classId", classId);
			input.put("customTimePeriodId", customTimePeriodId);
			input.put("examTypeId", examTypeId);
			GenericValue examMarksDetails = delegator.findOne("ExamMarksDetails", input, false);
			if(UtilValidate.isNotEmpty(examMarksDetails)){
				examMarksDetails.set("marks",marks);
				if(UtilValidate.isNotEmpty(isAttempted)){
					examMarksDetails.set("isAttempted",isAttempted);
				}
				examMarksDetails.set("lastModifiedByUserLogin",userLogin.getString("userLoginId"));
				examMarksDetails.set("lastModifiedDate",UtilDateTime.nowTimestamp());
				examMarksDetails.store();
			}else{
				examMarksDetails = delegator.makeValue("ExamMarksDetails");
				examMarksDetails.set("partyId", partyId);
				examMarksDetails.set("subjectId", subjectId);
				examMarksDetails.set("classId", classId);
				examMarksDetails.set("customTimePeriodId", customTimePeriodId);
				examMarksDetails.set("examTypeId", examTypeId);
				examMarksDetails.set("marks",marks);
				if(UtilValidate.isNotEmpty(isAttempted)){
					examMarksDetails.set("isAttempted",isAttempted);
				}
				examMarksDetails.set("createdByUserLogin",userLogin.getString("userLoginId"));
				examMarksDetails.set("createdDate",UtilDateTime.nowTimestamp());
				delegator.create(examMarksDetails);
			}			
        
		} catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Created Successfully.! StudentId :"+partyId);
		return result;
	}
	public static Map<String, Object> deleteCustRequestNote(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String custRequestId = (String) context.get("custRequestId");
        String noteId = (String) context.get("noteId");
		try {
			GenericValue custRequestNote = delegator.findOne("CustRequestNote", UtilMisc.toMap("custRequestId",custRequestId,"noteId",noteId), false);
			if(UtilValidate.isEmpty(custRequestNote)){
				return ServiceUtil.returnError("CustRequestNote Not Found.!");
			}
			GenericValue noteData = delegator.findOne("NoteData", UtilMisc.toMap("noteId",noteId), false);
			delegator.removeValue(custRequestNote);
			delegator.removeValue(noteData);
        
		} catch (Exception e) {
            return ServiceUtil.returnError("Error While Deleting CustRequestNote.!"+e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Deleted Successfully.!");
		return result;
	}
}
