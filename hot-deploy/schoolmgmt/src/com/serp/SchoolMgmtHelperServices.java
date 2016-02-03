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
import java.util.Set;
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


public class SchoolMgmtHelperServices {
	public static final String module = SchoolMgmtHelperServices.class.getName();
    
	public static Map<String, Object> getStudentList(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        String partyId = (String) context.get("partyId");
        Timestamp fromDate = (Timestamp)context.get("fromDate");
        List studentList = FastList.newInstance();
        if(UtilValidate.isEmpty(fromDate)){
        	fromDate = UtilDateTime.nowTimestamp();
        }
        fromDate = UtilDateTime.getDayStart(fromDate); 
        
        
        List condList =FastList.newInstance();
        if(UtilValidate.isNotEmpty(productId)){
        	 condList.add(EntityCondition.makeCondition("productId",EntityOperator.EQUALS,productId));
        }
        if(UtilValidate.isNotEmpty(partyId)){
       	 condList.add(EntityCondition.makeCondition("partyId",EntityOperator.EQUALS,partyId));
       }
        condList.add(EntityCondition.makeCondition("fromDate",EntityOperator.LESS_THAN_EQUAL_TO,fromDate));
        condList.add(EntityCondition.makeCondition(EntityCondition.makeCondition("thruDate",EntityOperator.GREATER_THAN_EQUAL_TO,fromDate),EntityOperator.OR,EntityCondition.makeCondition("thruDate",EntityOperator.EQUALS,null)));
        
        EntityCondition cond = EntityCondition.makeCondition(condList,EntityOperator.AND);
        try{
        	Set fieldToSelect = UtilMisc.toSet("externalId","partyId","productId","lastName","firstName");
        	fieldToSelect.add("birthDate");
        	fieldToSelect.add("fatherName");
        	fieldToSelect.add("subscriptionId");
        	fieldToSelect.add("customTimePeriodId");
        	
        	 List<GenericValue> studentListItr = delegator.findList("SubscriptionPartyAndPerson", cond, fieldToSelect, null, null, false);
        	 if(UtilValidate.isNotEmpty(studentListItr)){
        		 for(GenericValue student : studentListItr){
        			Map tempStudentMap = FastMap.newInstance();
        			tempStudentMap.put("partyId", student.getString("partyId"));
        			tempStudentMap.put("rollNumber", student.getString("partyId"));
        			if(UtilValidate.isNotEmpty(student.getString("externalId"))){
        				tempStudentMap.put("rollNumber", student.getString("externalId"));
        			}
        			
        			tempStudentMap.put("name", student.getString("firstName")+" "+student.getString("lastName"));
        			tempStudentMap.put("productId", student.getString("productId"));
        			tempStudentMap.put("birthDate", student.getString("birthDate"));
        			tempStudentMap.put("fatherName", student.getString("fatherName"));
        			tempStudentMap.put("subscriptionId", student.getString("subscriptionId"));
        			tempStudentMap.put("customTimePeriodId", student.getString("customTimePeriodId"));
        			studentList.add(tempStudentMap);
        		 }
        		 
        	 }
        	 
        }catch(Exception e){
        	Debug.logError("Error while fetching student details", module);
        	return ServiceUtil.returnError("Error while fetching student details");
        }
       result.put("studentList", studentList);
       return result; 
	}
	
	
	
	
	
}	