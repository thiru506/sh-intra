import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ServiceUtil;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

dctx = dispatcher.getDispatchContext();

studentRoles = delegator.findList("PartyRole", EntityCondition.makeCondition("roleTypeId",EntityOperator.EQUALS,"STUDENT"), null, null, null, false );
studentList = delegator.findList("PartyAndPerson", EntityCondition.makeCondition("partyId",EntityOperator.IN,studentRoles.partyId), null, null, null, false );
JSONArray studentsJSON = new JSONArray();
studentList.each { student ->
	JSONArray studentJSON = new JSONArray();
	studentJSON.add(student.firstName+" "+student.lastName);
	studentJSON.add(student.partyId);
	studentJSON.add("");
	studentJSON.add(student.bloodGroup);
	if(student.birthDate){
		studentJSON.add(student.birthDate.toString());
	}else{
	studentJSON.add("");
	}
	try {
		Map<String, Object> getTelParams = FastMap.newInstance();
		getTelParams.put("partyId", student.partyId);
		getTelParams.put("userLogin", userLogin);
		tmpResult = dispatcher.runSync("getPartyTelephone", getTelParams);
	} catch (GenericServiceException e) {
		Debug.logError(e, module);
		return ServiceUtil.returnError(e.getMessage());
	}
	if (ServiceUtil.isError(tmpResult)) {
		return ServiceUtil.returnError(ServiceUtil.getErrorMessage(tmpResult));
	}
	if(tmpResult.get("contactNumber")){
		contactNumber = (String) tmpResult.get("countryCode") + (String) tmpResult.get("contactNumber");
		studentJSON.add(contactNumber);
	}else{
		studentJSON.add("");
	}
	
	studentsJSON.add(studentJSON);
	
}
context.studentsJSON = studentsJSON.toString();

