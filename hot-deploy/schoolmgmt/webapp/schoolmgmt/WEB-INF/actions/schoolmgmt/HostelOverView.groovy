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

facilityId=parameters.facilityId;
List parentList=FastList.newInstance();
List facilityPartyList = FastList.newInstance();
List facilityList=FastList.newInstance();
List facilityIds=FastList.newInstance();
List facilityPartiesList=FastList.newInstance();
parentMap=[:];
parentFacilityId=null;
if(UtilValidate.isNotEmpty(facilityId)){
	facilityDetails=delegator.findOne("Facility",[facilityId:facilityId],true);
	if(UtilValidate.isNotEmpty(facilityDetails.parentFacilityId)){
		parentFacilityId=facilityDetails.parentFacilityId;
	}else{
	  parentFacilityId=facilityId;
	  facilityList=delegator.findList("Facility",EntityCondition.makeCondition("parentFacilityId",EntityOperator.EQUALS,facilityId),UtilMisc.toSet("facilityId"),null,null,false);
	  facilityIds=EntityUtil.getFieldListFromEntityList(facilityList,"facilityId",true);
	}
datetime=UtilDateTime.nowTimestamp();	
dayBegin=UtilDateTime.getDayStart(datetime);
filledBeds=null;
if(UtilValidate.isNotEmpty(facilityIds)){
	facilityPartiesList=EntityUtil.filterByDate(delegator.findList("FacilityParty",EntityCondition.makeCondition("facilityId",EntityOperator.IN,facilityIds),UtilMisc.toSet("partyId","facilityId","roleTypeId","fromDate","thruDate"),null,null,false),dayBegin);
}

facilityPartyList=EntityUtil.filterByDate(delegator.findList("FacilityParty",EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,facilityId),UtilMisc.toSet("partyId","facilityId","roleTypeId","fromDate","thruDate"),null,null,false),dayBegin);

if(UtilValidate.isNotEmpty(facilityPartyList)){
	context.facilityPartyList=facilityPartyList;
	filledBeds=facilityPartyList.size();
}	
if(UtilValidate.isNotEmpty(facilityPartiesList)){
	filledBeds=facilityPartiesList.size();
}
facilitySize=facilityDetails.facilitySize;

phoneNumber=null;
address1=null;
address2=null;
city=null;
postalCode=null;
countryGeoId=null
stateProvinceGeoId=null;	
 condition=EntityCondition.makeCondition([EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,parentFacilityId),
	                                      EntityCondition.makeCondition("thruDate",EntityOperator.EQUALS,null)],EntityOperator.AND);
 facilityContactMechPurpose=delegator.findList("FacilityContactMechPurpose",condition,null,null,null,false);
 if(UtilValidate.isNotEmpty(facilityContactMechPurpose)){
	 contactNumbers=EntityUtil.filterByCondition(facilityContactMechPurpose,EntityCondition.makeCondition("contactMechPurposeTypeId",EntityOperator.EQUALS,"PRIMARY_PHONE"));
	 if(UtilValidate.isNotEmpty(contactNumbers)){
		 contactNumber=EntityUtil.getFirst(contactNumbers);
		 phoneContactMechId=contactNumber.contactMechId;
		 phoneNumbers=delegator.findOne("ContactMechDetail",[contactMechId:phoneContactMechId],true);
		 if(UtilValidate.isNotEmpty(phoneNumbers.tnContactNumber)){
			 phoneNumber=phoneNumbers.tnCountryCode+" "+phoneNumbers.tnContactNumber; 
		 }
	 }
 } 	
 
 if(UtilValidate.isNotEmpty(facilityContactMechPurpose)){
	 contactAddresses=EntityUtil.filterByCondition(facilityContactMechPurpose,EntityCondition.makeCondition("contactMechPurposeTypeId",EntityOperator.EQUALS,"PRIMARY_LOCATION"));
	 if(UtilValidate.isNotEmpty(contactAddresses)){
		 contactAddress=EntityUtil.getFirst(contactAddresses);
		 addressContactMechId=contactAddress.contactMechId;
		 primaryLocation=delegator.findOne("ContactMechDetail",[contactMechId:addressContactMechId],true);
		 if(UtilValidate.isNotEmpty(primaryLocation.paAddress1)){
			 address1=primaryLocation.paAddress1;
		 }	
		 if(UtilValidate.isNotEmpty(primaryLocation.paAddress2)){
			 address2=primaryLocation.paAddress2;
		 }
		 if(UtilValidate.isNotEmpty(primaryLocation.paCity)){
			 city=primaryLocation.paCity;
		 }
		 if(UtilValidate.isNotEmpty(primaryLocation.paPostalCode)){
			 postalCode=primaryLocation.paPostalCode;
		 }
		 if(UtilValidate.isNotEmpty(primaryLocation.paCountryGeoId)){
			 countryGeoId=primaryLocation.paCountryGeoId;
		 }
		 if(UtilValidate.isNotEmpty(primaryLocation.paStateProvinceGeoId)){
			 stateProvinceGeoId=primaryLocation.paStateProvinceGeoId;
		 }
	 }
 }
 parentMap.phoneNumber=phoneNumber;
 parentMap.address1=address1;
 parentMap.address2=address2;
 parentMap.city=city;
 parentMap.countryGeoId=countryGeoId;
 parentMap.stateProvinceGeoId=stateProvinceGeoId;
 							  	
}
parentMap.parentFacilityId=parentFacilityId;
parentMap.facilitySize=facilitySize;
parentMap.filledBeds=filledBeds;
parentList.add(parentMap);
context.parentList=parentList;