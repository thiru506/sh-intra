package com.serp;

import java.math.BigDecimal;
import java.util.Map;

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
        String customTimePeriodId = (String) context.get("customTimePeriodId");
        String roleTypeId = (String) context.get("roleTypeId");
        String firstName = (String) context.get("firstName");
        String middleName = (String) context.get("middleName");
        String lastName = (String) context.get("lastName");
        String externalId = (String) context.get("externalId");
        String gender = (String) context.get("gender");
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
            
            GenericValue subScription = delegator.makeValue("Subscription");
            subScription.set("partyId", partyId);
            subScription.set("productId", productId);
            subScription.set("customTimePeriodId",customTimePeriodId);
            delegator.createSetNextSeqId(subScription);
            
            GenericValue partyRole = delegator.makeValue("PartyRole");
            partyRole.set("partyId", partyId);
            partyRole.set("roleTypeId",roleTypeId);
            delegator.create(partyRole);
            
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
}
