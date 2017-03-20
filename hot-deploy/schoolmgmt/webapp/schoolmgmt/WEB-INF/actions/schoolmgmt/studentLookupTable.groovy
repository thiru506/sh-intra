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

studentList = delegator.findList("Contacts",null, null, null, null, false );
JSONArray studentsJSON = new JSONArray();
studentList.each { student ->
	JSONArray studentJSON = new JSONArray();
	studentJSON.add(student.employeeName);
	studentJSON.add(student.location);
	studentJSON.add(student.department);
	studentJSON.add(student.email);
	studentJSON.add(student.phone);
	
	
	
	studentsJSON.add(studentJSON);
	
}
context.studentsJSON = studentsJSON.toString();

