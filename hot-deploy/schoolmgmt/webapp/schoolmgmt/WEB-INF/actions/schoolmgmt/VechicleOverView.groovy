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

vehicleId=parameters.vehicleId;
tempMap=[:];
vehicleDetails=delegator.findOne("Vehicle",[vehicleId:vehicleId],true);
tempMap.putAll(vehicleDetails);

vehicleRoleDetails=delegator.findList("VehicleRole",EntityCondition.makeCondition("vehicleId",EntityOperator.EQUALS,vehicleId),null,null,null,false);

List driverDetails=FastList.newInstance();
List studentList=FastList.newInstance();
ecl=EntityCondition.makeCondition([EntityCondition.makeCondition("vehicleId",EntityOperator.EQUALS,vehicleId),
                                   EntityCondition.makeCondition("roleTypeId",EntityOperator.EQUALS,"DRIVER"),
                                   EntityCondition.makeCondition("thruDate",EntityOperator.EQUALS,null)],EntityOperator.AND)
driverDetails=EntityUtil.filterByCondition(vehicleRoleDetails,ecl);
context.driverDetails=driverDetails;
stCondition=EntityCondition.makeCondition([EntityCondition.makeCondition("vehicleId",EntityOperator.EQUALS,vehicleId),
                                   EntityCondition.makeCondition("roleTypeId",EntityOperator.EQUALS,"STUDENT"),
                                   EntityCondition.makeCondition("thruDate",EntityOperator.EQUALS,null)],EntityOperator.AND)

studentList=EntityUtil.filterByCondition(vehicleRoleDetails,stCondition);
context.studentList=studentList;
studentListSize=studentList.size();
available=vehicleDetails.vehicleCapacity-studentListSize;
tempMap.putAt("available", available);
context.vehicleDetails=tempMap;