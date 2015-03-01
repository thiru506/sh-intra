package com.shopsmint.party;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityFunction;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityTypeUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;


public class MerchantServices {
	
	public static final String module = MerchantServices.class.getName();
	
	public static Map<String,Object> getMerchants(DispatchContext dctx,Map<String, ? extends Object> context){
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        //String partyId = (String) context.get("partyId");
        //Locale locale = (Locale) context.get("locale");
        List<GenericValue> merchantsList = new ArrayList<GenericValue>();
        List<GenericValue> merchants = new ArrayList<GenericValue>();

        try {
        	
            //merchants = delegator.findByAnd("PartyRoleAndPartyDetail",UtilMisc.toMap("roleTypeId","MERCHANT"));
        	merchantsList = delegator.findByAnd("PartyClassification",UtilMisc.toMap("partyClassificationGroupId","RETAIL_FMCG"));
            
        	ListIterator<GenericValue> merchantsIte = merchantsList.listIterator();
        	while(merchantsIte.hasNext()){
        		GenericValue merchant = merchantsIte.next();
        		String partyID = (String)merchant.get("partyId");
        		merchants.addAll(delegator.findByAnd("ProductStore",UtilMisc.toMap("payToPartyId",partyID)));
        	}
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        if (merchants != null) {
            result.put("merchants", merchants);
        }
        return result;		
		
	}
	public static Map<String, Object> createMerchant(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Timestamp now = UtilDateTime.nowTimestamp();
		
		String groupName = (String) context.get("groupName");
		String address1 = (String) context.get("address1");
		String address2 = (String) context.get("address2");
		String email = (String) context.get("emailId");
		String mobileNumber = (String) context.get("mobileNumber");
		String contactNumber =(String)context.get("landNumber");     
		String city =(String)context.get("city");
		String longitude =(String)context.get("longitude");
		String latitude =(String)context.get("latitude");
		String natureOfBussiness =(String)context.get("natureOfBussiness");
		String radius =(String)context.get("radius");
		String tinOrTanNo =(String)context.get("tinOrTanNo");
		String domainName =(String)context.get("domainName");
		String contactMechId = null;
		String partyId =null;
		Map<String, Object> input = FastMap.newInstance();
		try {
			input.put("userLogin", userLogin);
            input.put("groupName",groupName);
            Map resultctx = dispatcher.runSync("createPartyGroup", input);
            if(ServiceUtil.isError(resultctx)){
           	 	Debug.logError("Error while create party group:"+ServiceUtil.getErrorMessage(resultctx), module);
           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(resultctx));
            }
            partyId = (String)resultctx.get("partyId");
            if (UtilValidate.isNotEmpty(address1)){
            	input.clear();
				input = UtilMisc.toMap("userLogin", userLogin, "partyId",partyId, "address1",address1, "address2", address2, "city", city, "stateProvinceGeoId", (String)context.get("stateProvinceGeoId"), "postalCode", (String)context.get("postalCode"), "contactMechId", contactMechId);
				Map	resultMap =  dispatcher.runSync("createPartyPostalAddress", input);
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
            if(UtilValidate.isNotEmpty(longitude) && UtilValidate.isNotEmpty(longitude)){
            	input.clear();
	            input.put("userLogin", userLogin);
	            input.put("latitude", latitude);
	            input.put("longitude", longitude);
	            input.put("partyId", partyId);
            	Map outMap = dispatcher.runSync("createOrUpdateGeoPoint", input);
            	if(ServiceUtil.isError(outMap)){
	           	 	Debug.logError("faild service create Geo Point:"+ServiceUtil.getErrorMessage(outMap), module);
	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	            }
            }
			
		} catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
		result = ServiceUtil.returnSuccess("Marchant Successfully Created..! MarchantId:"+partyId);
		return result;
	}
	public static Map<String, Object> createOrUpdateGeoPoint(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) context.get("partyId");
        String latitude = (String) context.get("latitude");
        String longitude = (String) context.get("longitude");
        String dataSourceId = "GEOPT_GOOGLE";
        try{
        	GenericValue geoPoint = delegator.findByPrimaryKey("GeoPoint", UtilMisc.toMap("geoPointId", partyId));
        	if(UtilValidate.isEmpty(geoPoint)){
        		GenericValue newGeoPoint = delegator.makeValue("GeoPoint");
        		newGeoPoint.set("geoPointId",partyId);
        		newGeoPoint.set("latitude",latitude);
        		newGeoPoint.set("longitude",longitude);
        		newGeoPoint.set("dataSourceId",dataSourceId);
        		newGeoPoint.create();
        	}else{
        		geoPoint.set("latitude",latitude);
        		geoPoint.set("longitude",longitude);
        		geoPoint.store();
        	}
        } catch (Exception e) {
        	Debug.logError(e, module);
        }
        return result;
	}
}
