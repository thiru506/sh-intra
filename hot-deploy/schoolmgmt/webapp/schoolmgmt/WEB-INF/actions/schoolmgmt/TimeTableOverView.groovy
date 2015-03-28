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

workEffortId=parameters.workEffortId;

List timeTableEventList=FastList.newInstance();
if(UtilValidate.isNotEmpty(workEffortId)){
	timeTableEventList=delegator.findList("WorkEffortAndChild",EntityCondition.makeCondition("workEffortId",EntityOperator.EQUALS,workEffortId),null,null,null,false);
}

context.timeTableEventList =timeTableEventList;
Debug.log("timeTableEventList============"+timeTableEventList);