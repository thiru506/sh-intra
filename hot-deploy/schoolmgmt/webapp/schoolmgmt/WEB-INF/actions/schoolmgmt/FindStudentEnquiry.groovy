import org.ofbiz.base.util.*;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import java.util.*;
import java.lang.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import java.sql.*;
import javolution.util.FastList;
import javolution.util.FastMap;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ServiceUtil;
import java.text.SimpleDateFormat;
import java.text.ParseException;

custRequestId=parameters.custRequestId;
fromDateStr=parameters.fromDate;
thruDateStr=parameters.thruDate;
fromPartyId=parameters.partyId;
statusId=parameters.statusId;
custRequestName = parameters.custRequestName;
List conditionList=FastList.newInstance();
List custRequestList = FastList.newInstance(); 
List tempList=FastList.newInstance();
//For enquiry overview
Map enquiryOverViewMap = FastMap.newInstance();
if(UtilValidate.isNotEmpty(custRequestId)){
	conditionList.add(EntityCondition.makeCondition("custRequestId",EntityOperator.EQUALS,custRequestId));
}
if(UtilValidate.isNotEmpty(fromPartyId)){
	conditionList.add(EntityCondition.makeCondition("fromPartyId",EntityOperator.EQUALS,fromPartyId));
}
if(UtilValidate.isNotEmpty(statusId)){
	conditionList.add(EntityCondition.makeCondition("statusId",EntityOperator.EQUALS,statusId));
}

if(UtilValidate.isNotEmpty(custRequestName)){
	conditionList.add(EntityCondition.makeCondition("custRequestName",EntityOperator.EQUALS,custRequestName));
}
Timestamp fromDate = null;
Timestamp thruDate = null;
if(UtilValidate.isNotEmpty(fromDateStr)){
	
	def sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	fromDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(fromDateStr).getTime()));
	dayBegin=UtilDateTime.getDayStart(fromDate);
	dayEnd=UtilDateTime.getDayEnd(fromDate);
	if(UtilValidate.isNotEmpty(thruDateStr)){
		thruDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(thruDateStr).getTime()));
		dayEnd=UtilDateTime.getDayEnd(thruDate);
	}
	conditionList.add(EntityCondition.makeCondition([EntityCondition.makeCondition("custRequestDate",EntityOperator.GREATER_THAN_EQUAL_TO,dayBegin),
		                                            EntityCondition.makeCondition("custRequestDate",EntityOperator.LESS_THAN_EQUAL_TO,dayEnd)],EntityOperator.AND));
}

if(UtilValidate.isEmpty(fromDate)){
	fromDate = UtilDateTime.nowTimestamp();
	thruDate = UtilDateTime.nowTimestamp();
	dayBegin=UtilDateTime.getDayStart(fromDate);
	dayEnd=UtilDateTime.getDayEnd(thruDate);
	
	conditionList.add(EntityCondition.makeCondition([EntityCondition.makeCondition("custRequestDate",EntityOperator.GREATER_THAN_EQUAL_TO,dayBegin),
													EntityCondition.makeCondition("custRequestDate",EntityOperator.LESS_THAN_EQUAL_TO,dayEnd)],EntityOperator.AND));
}


conditionList.add(EntityCondition.makeCondition("custRequestTypeId",EntityOperator.EQUALS,"STUDENT_ENQUIRY"));
EntityCondition  condition=EntityCondition.makeCondition(conditionList,EntityOperator.AND);
custRequestList=delegator.findList("CustRequest",condition,UtilMisc.toSet("custRequestName","custRequestId","description","statusId","custRequestDate"),null,null,false);
if(UtilValidate.isNotEmpty(custRequestList)){
	custRequestList.each{custRequest->
		tempMap=[:];
		tempMap.custRequestId=custRequest.custRequestId;
		tempMap.statusId=custRequest.statusId;
		tempMap.custRequestDate=custRequest.custRequestDate;
		tempMap.custRequestName=custRequest.custRequestName;
		tempMap.description=custRequest.description;
		//partyTelephone= dispatcher.runSync("getPartyTelephone", [partyId: custRequest.fromPartyId, userLogin: userLogin]);
		phoneNumber = "";
		
		
		
		//For enquiry overview
		enquiryOverViewMap.putAll(tempMap);
		tempList.add(tempMap);
	}
}
//Debug.log("custRequestList======"+tempList);
//For enquiry overview
context.enquiryDetails=enquiryOverViewMap;
context.custRequestList=tempList;


