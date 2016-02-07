import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.ofbiz.party.party.PartyHelper;
import com.serp.SchoolMgmtHelperServices;
import java.math.BigDecimal;
import java.util.*;
import java.sql.Timestamp;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.base.util.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import net.sf.json.JSONArray;
import java.util.SortedMap;

import javolution.util.FastList;

JSONArray jsonData= new JSONArray();


punchDate = parameters.punchDate;
productId = parameters.productId;

dctx = dispatcher.getDispatchContext();
def sdf = new SimpleDateFormat("MMMM dd, yyyy");
try {
	if (parameters.punchDate) {
		context.punchDate = parameters.punchDate;
		punchDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(parameters.punchDate).getTime()));
	}else {
	   punchDate = UtilDateTime.nowTimestamp();
	}
	
} catch (Exception e) {
	Debug.logError(e, "Cannot parse date string: " + e, "");
	context.errorMessage = "Cannot parse date string: " + e;
	return;
}

if(productId){
	 result = SchoolMgmtHelperServices.getStudentList(dctx,UtilMisc.toMap("fromDate",punchDate,"productId",productId));
	 studentList = result.studentList;
	 i=0;
	studentList.each{student->
		 JSONObject stu = new JSONObject();
		 
		 id = "id_" + i++;
		 stu.put("id", id);
		 stu.put("partyId", student.partyId);
		 stu.put("rollNumber", student.rollNumber);
		 stu.put("name", student.name);
		 stu.put("punchDate", UtilDateTime.toDateString(punchDate, "yyyy-MM-dd"));
		 stu.put("inOut", "IN");
		 stu.put("check", true);
		 jsonData.add(stu);
		 
	 }
	
}

context.put("jsonData", jsonData.toString());