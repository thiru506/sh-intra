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

custRequestId=parameters.custRequestId;
custRequestDate=parameters.custRequestDate;
fromPartyId=parameters.partyId;
statusId=parameters.statusId;
List conditionList=FastList.newInstance();
List custRequestList = FastList.newInstance(); 
List tempList=FastList.newInstance();
if(UtilValidate.isNotEmpty(custRequestId)){
	conditionList.add(EntityCondition.makeCondition("custRequestId",EntityOperator.EQUALS,custRequestId));
}
if(UtilValidate.isNotEmpty(fromPartyId)){
	conditionList.add(EntityCondition.makeCondition("fromPartyId",EntityOperator.EQUALS,fromPartyId));
}
if(UtilValidate.isNotEmpty(statusId)){
	conditionList.add(EntityCondition.makeCondition("statusId",EntityOperator.EQUALS,statusId));
}
if(UtilValidate.isNotEmpty(custRequestDate)){
	dayBegin=UtilDateTime.getDayStart(custRequestDate);
	dayEnd=UtilDateTime.getDayEnd(custRequestDate);
	conditionList.add(EntityCondition.makeCondition([EntityCondition.makeCondition("custRequestDate",EntityOperator.GREATER_THAN_EQUAL_TO,dayBegin),
		                                            EntityCondition.makeCondition("custRequestDate",EntityOperator.LESS_THAN_EQUAL_TO,dayEnd)],EntityOperator.AND));
}
conditionList.add(EntityCondition.makeCondition("custRequestTypeId",EntityOperator.EQUALS,"STUDENT_ENQUIRY"));
EntityCondition  condition=EntityCondition.makeCondition(conditionList,EntityOperator.AND);
custRequestList=delegator.findList("CustRequest",condition,UtilMisc.toSet("custRequestName","custRequestId","fromPartyId","statusId","custRequestDate"),null,null,false);
if(UtilValidate.isNotEmpty(custRequestList)){
	custRequestList.each{custRequest->
		tempMap=[:];
		tempMap.custRequestId=custRequest.custRequestId;
		tempMap.fromPartyId=custRequest.fromPartyId;
		tempMap.statusId=custRequest.statusId;
		tempMap.custRequestDate=custRequest.custRequestDate;
		tempMap.custRequestName=custRequest.custRequestName;
		partyTelephone= dispatcher.runSync("getPartyTelephone", [partyId: custRequest.fromPartyId, userLogin: userLogin]);
		phoneNumber = "";
		if (partyTelephone != null && partyTelephone.contactNumber != null) {
			phoneNumber = partyTelephone.contactNumber;
		}
		partyEmail= dispatcher.runSync("getPartyEmail", [partyId: custRequest.fromPartyId, userLogin: userLogin]);
		emailAddress="";
		emailAddress=partyEmail.emailAddress;
		personDetails=delegator.findOne("Person",[partyId:custRequest.fromPartyId],false);
		name="";
		if(UtilValidate.isNotEmpty(personDetails)){
			name=personDetails.firstName+" "+personDetails.lastName; 
		}
		tempMap.name=name;
		tempMap.emailAddress=emailAddress;
		tempMap.phoneNumber=phoneNumber;
		tempList.add(tempMap);
	}
}
//Debug.log("custRequestList======"+tempList);
context.custRequestList=tempList;


