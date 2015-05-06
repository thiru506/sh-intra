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


partyId=parameters.partyId;
studentMap=[:];
personDetails=delegator.findOne("Person",[partyId:partyId],false);
if(personDetails){
	studentMap.putAll(personDetails);
}

ecl=EntityCondition.makeCondition([EntityCondition.makeCondition("partyId",EntityOperator.EQUALS,partyId),
	                               EntityCondition.makeCondition("fromDate",EntityOperator.LESS_THAN_EQUAL_TO, UtilDateTime.nowTimestamp()),   
	                               EntityCondition.makeCondition([EntityCondition.makeCondition("thruDate",EntityOperator.EQUALS,null),
									                              EntityCondition.makeCondition("thruDate",EntityOperator.GREATER_THAN_EQUAL_TO,UtilDateTime.nowTimestamp())],EntityOperator.OR)],EntityOperator.AND);
							   
partyClassification=delegator.findList("PartyClassification",ecl,null,null,null,false);
partyCaste=EntityUtil.getFirst(partyClassification);
if(partyCaste){
	studentMap.putAll(partyCaste);
}

partyTelephone= dispatcher.runSync("getPartyTelephone", [partyId: partyId, userLogin: userLogin]);
phoneNumber = "";
if (partyTelephone != null && partyTelephone.contactNumber != null) {
	phoneNumber = partyTelephone.contactNumber;
}
studentMap.put("mobileNumber", phoneNumber);
partyLandNumber= dispatcher.runSync("getPartyTelephone", [partyId: partyId,contactMechPurposeTypeId:"PHONE_HOME", userLogin: userLogin]);
landNumber="";
if (partyLandNumber != null && partyLandNumber.contactNumber != null) {
	landNumber = partyLandNumber.contactNumber;
}
studentMap.put("residentialNumber", landNumber);
partyEmail= dispatcher.runSync("getPartyEmail", [partyId: partyId, userLogin: userLogin]);
emailAddress="";
emailAddress=partyEmail.emailAddress;
studentMap.put("emailAddress", emailAddress);
partyPostalAddress= dispatcher.runSync("getPartyPostalAddress", [partyId: partyId,contactMechPurposeTypeId:"PRIMARY_LOCATION",userLogin: userLogin]);
if (partyPostalAddress != null) {
	if(partyPostalAddress.address1==null){partyPostalAddress.address1="";}
	if(partyPostalAddress.address2==null){partyPostalAddress.address2="";}
	if(partyPostalAddress.city==null){partyPostalAddress.city="";}
	if(partyPostalAddress.postalCode==null){partyPostalAddress.postalCode="";}
	studentMap.put("address1", partyPostalAddress.address1);
	studentMap.put("address2", partyPostalAddress.address2);
	studentMap.put("city", partyPostalAddress.city);
	studentMap.put("postalCode", partyPostalAddress.postalCode);
	
}

partyPresentAddress= dispatcher.runSync("getPartyPostalAddress", [partyId: partyId,contactMechPurposeTypeId:"GENERAL_LOCATION",userLogin: userLogin]);
if (partyPresentAddress != null) {
	if(partyPresentAddress.address1==null){partyPresentAddress.address1="";}
	if(partyPresentAddress.address2==null){partyPresentAddress.address2="";}
	if(partyPresentAddress.city==null){partyPresentAddress.city="";}
	if(partyPresentAddress.postalCode==null){partyPresentAddress.postalCode="";}
	studentMap.put("presentAddress1", partyPostalAddress.address1);
	studentMap.put("presentAddress2", partyPostalAddress.address2);
	studentMap.put("presentCity", partyPostalAddress.city);
	studentMap.put("presentPostalCode", partyPostalAddress.postalCode);
}
context.studentMap=studentMap;						   
							   
							   
							   
							   