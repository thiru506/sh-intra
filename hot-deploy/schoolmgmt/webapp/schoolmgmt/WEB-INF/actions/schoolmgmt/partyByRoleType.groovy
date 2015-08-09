
import javolution.util.FastList;
import javolution.util.FastMap;
import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.humanres.HumanResServices;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;


dctx = dispatcher.getDispatchContext();
JSONArray partysJSON = new JSONArray();
Map partyInputMap = FastMap.newInstance();
partyInputMap.put("userLogin", userLogin);
partyInputMap.put("orgPartyId", "Company");
partyInputMap.put("fromDate", UtilDateTime.getDayStart(UtilDateTime.addDaysToTimestamp(UtilDateTime.nowTimestamp(),-60)));
partyInputMap.put("thruDate", UtilDateTime.getDayEnd(UtilDateTime.nowTimestamp()));
Map resultMap = HumanResServices.getActiveEmployements(dctx,partyInputMap);
List<GenericValue> employementList = (List<GenericValue>)resultMap.get("employementList");
employementList = EntityUtil.orderBy(employementList, UtilMisc.toList("firstName"));
JSONObject party = new JSONObject();
party.put("partyId","");
party.put("name","");
partysJSON.add(party);
for (int i = 0; i < employementList.size(); ++i) {
		GenericValue employment = employementList.get(i);
		partyId = employment.getString("partyIdTo");
		lastName="";
		if(employment.getString("lastName") !=null){
			lastName = employment.getString("lastName");
		}
		name = employment.getString("firstName") + " " + lastName;
		party.put("partyId", partyId);
		party.put("name", name);
		partysJSON.add(party);
	}
	

//Debug.logError("partysJSON="+partysJSON,"");
context.partysJSON = partysJSON.toString();;
