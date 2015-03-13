
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.ofbiz.party.party.*;
dctx = dispatcher.getDispatchContext();


facilityList =delegator.findList("Facility", EntityCondition.makeCondition("facilityTypeId",EntityOperator.IN,UtilMisc.toList("ROOM","BUILDING")), null, null, null, false );
JSONArray facilityListJSON = new JSONArray();
facilityList.each { facility ->
	JSONArray facilityJSON = new JSONArray();
	if(facility.facilityName){
		facilityJSON.add(facility.facilityName);
	}else{
		facilityJSON.add(" ");
	}
	if(facility.facilityTypeId){
		GenericValue facilityType = delegator.findOne("FacilityType" ,UtilMisc.toMap("facilityTypeId",facility.facilityTypeId),false);
		facilityJSON.add(facilityType.description);
	}else{
		facilityJSON.add("");
	}
	if(facility.parentFacilityId){
		GenericValue parentFacility = delegator.findOne("Facility" ,UtilMisc.toMap("facilityId",facility.parentFacilityId),false);
		facilityJSON.add(parentFacility.description);
	}else{
		facilityJSON.add("");
	}
	if(facility.openedDate){
		facilityJSON.add(UtilDateTime.toDateString(facility.openedDate ,"dd MMMM, yyyy"));
		//facilityJSON.add(facility.openedDate);
	}else{
		facilityJSON.add("");
	}
	if(facility.facilitySize){
		facilityJSON.add(facility.facilitySize);
	}else{
		facilityJSON.add(20);
	}
	facilityJSON.add("filled");
	//get warden details here
	condList=[];
	condList.add(EntityCondition.makeCondition("roleTypeId",EntityOperator.EQUALS,"HOSTEL_WARDEN"));
	condList.add(EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,facility.facilityId));
	cond = EntityCondition.makeCondition(condList,EntityOperator.AND);
	facilityPartyList =EntityUtil.filterByDate(delegator.findList("FacilityParty", cond, null, null, null, false ));
	facilityParty = EntityUtil.getFirst(facilityPartyList);
	if(facilityParty){
		facilityJSON.add(PartyHelper.getPartyName(delegator,facilityParty.partyId,true));
		facilityJSON.add("warden MOBNO");
	}else{
		facilityJSON.add("Warden Name");
		facilityJSON.add("warden MOBNO");
	}
	
	facilityJSON.add(facility.facilityId);
	
	
	facilityListJSON.add(facilityJSON);
	
}
context.facilityListJSON = facilityListJSON.toString();

