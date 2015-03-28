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

timeTableList = delegator.findList("WorkEffortAndPartyAssignAndType", EntityCondition.makeCondition("parentTypeId",EntityOperator.EQUALS,"TIME_TABLE"), null, null, null, false );
JSONArray timeTablesJSON = new JSONArray();
timeTableList.each { timeTable ->
	JSONArray timeTableJSON = new JSONArray();
	timeTableJSON.add(timeTable.workEffortName);
	timeTableJSON.add(timeTable.workEffortPurposeTypeId);
	timeTableJSON.add(timeTable.currentStatusId);
	timeTableJSON.add(timeTable.workEffortId);
	timeTablesJSON.add(timeTableJSON);
	
}
Debug.log("timeTablesJSON===="+timeTablesJSON);
context.timeTablesJSON = timeTablesJSON.toString();
