
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

dctx = dispatcher.getDispatchContext();


vehicleList =delegator.findList("Vehicle", null, null, null, null, false );
JSONArray vehiclesJSON = new JSONArray();
Debug.logError("vehicleList====================="+vehicleList.size(),"");
vehicleList.each { vehicle ->
	JSONArray vehicleJSON = new JSONArray();
	if(vehicle.vehicleName){
		vehicleJSON.add(vehicle.vehicleName);
	}else{
		vehicleJSON.add("Vehicle Name");
	}
	if(vehicle.vehicleNumber){
		vehicleJSON.add(vehicle.vehicleNumber);
	}else{
		vehicleJSON.add("Vehicle Number");
	}
	if(vehicle.vehicleCapacity){
		vehicleJSON.add(vehicle.vehicleCapacity);
	}else{
		vehicleJSON.add(32);
	}
	
	vehicleJSON.add("filled");
	vehicleJSON.add("Driver Name");
	vehicleJSON.add("Driver MOBNO");
	vehicleJSON.add(vehicle.vehicleId);
	
	
	vehiclesJSON.add(vehicleJSON);
	
}
context.vehiclesJSON = vehiclesJSON.toString();
Debug.logError("vehiclesJSON===="+vehiclesJSON,"");


