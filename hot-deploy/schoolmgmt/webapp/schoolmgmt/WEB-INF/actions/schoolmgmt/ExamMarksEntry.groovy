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
JSONObject partySubscription = new JSONObject();
productId="";
fromDate=null;

if(UtilValidate.isNotEmpty(parameters.fromProductId)){
	productId = parameters.fromProductId;
	context.fromProductId=parameters.fromProductId;
}
if(UtilValidate.isNotEmpty(parameters.examTypeId)){
	examTypeId = parameters.examTypeId;
	context.examTypeId=parameters.examTypeId;
}
if(UtilValidate.isNotEmpty(parameters.customTimePeriodId)){
	context.customTimePeriodId=parameters.customTimePeriodId;
	GenericValue customTimePeriod = delegator.findOne("CustomTimePeriod", UtilMisc.toMap("customTimePeriodId",parameters.customTimePeriodId), false);
	if(UtilValidate.isNotEmpty(customTimePeriod)){
		fromDate = UtilDateTime.toTimestamp(customTimePeriod.getDate("fromDate"));
	}
}
if(UtilValidate.isNotEmpty(parameters.toProductId)){
	context.toProductId=parameters.toProductId;
}
dctx = dispatcher.getDispatchContext();
List subjectIds = FastList.newInstance();
if(productId){
	 result = SchoolMgmtHelperServices.getStudentList(dctx,UtilMisc.toMap("fromDate",fromDate,"productId",productId));
	 ecl = EntityCondition.makeCondition([EntityCondition.makeCondition("productIdTo",EntityOperator.EQUALS,productId),
		                                  EntityCondition.makeCondition("productAssocTypeId",EntityOperator.EQUALS,"PRODUCT_COMPONENT")],EntityOperator.AND);
	 List subjectsList = delegator.findList("ProductAssoc",ecl,null,null,null,false);
	 subjectsList = EntityUtil.filterByDate(subjectsList,fromDate);
	 subjectIds =EntityUtil.getFieldListFromEntityList(subjectsList,"productId",true);
	 List prodDetailsList = delegator.findList("Product",EntityCondition.makeCondition("productId",EntityOperator.IN,subjectIds),null,null,null,false);
	 studentList = result.studentList;
	 eMDCondition = EntityCondition.makeCondition([EntityCondition.makeCondition("classId",EntityOperator.EQUALS,productId),
		                                           EntityCondition.makeCondition("customTimePeriodId",EntityOperator.EQUALS,parameters.customTimePeriodId),
												   EntityCondition.makeCondition("examTypeId",EntityOperator.EQUALS,examTypeId)],EntityOperator.AND);
	 List examMarksDetailsList = delegator.findList("ExamMarksDetails",eMDCondition,null,null,null,false);
	 i=0;
	studentList.each{student->
		 JSONObject stu = new JSONObject();
		 subjectIds.each{subjectId->
		 prodDetl=EntityUtil.filterByCondition(prodDetailsList,EntityCondition.makeCondition("productId",EntityOperator.EQUALS,subjectId));
		 GenericValue examMarksDetail=null;
		 BigDecimal marks = BigDecimal.ZERO;
		 String isAttempted="Y";
		 if(UtilValidate.isNotEmpty(examMarksDetailsList)){
			 examMDCond = EntityCondition.makeCondition([EntityCondition.makeCondition("partyId",EntityOperator.EQUALS,student.partyId),
		                                           EntityCondition.makeCondition("subjectId",EntityOperator.EQUALS,subjectId)],EntityOperator.AND);
			 List examMarksDetails = EntityUtil.filterByCondition(examMarksDetailsList,examMDCond);
			 if(UtilValidate.isNotEmpty(examMarksDetails)){
				 examMarksDetail = EntityUtil.getFirst(examMarksDetails);
				 if(UtilValidate.isNotEmpty(examMarksDetail.marks)){
					 marks = examMarksDetail.marks;
				 }
				 stu.put("marks", marks);
				 if(UtilValidate.isNotEmpty(examMarksDetail.isAttempted)){
					 isAttempted = examMarksDetail.isAttempted;
				 }
			 }
		 }
		 id = "id_" + i++;
		 stu.put("id", id);
		 stu.put("partyId", student.partyId);
		 stu.put("rollNumber", student.rollNumber);
		 stu.put("name", student.name);
		 stu.put("subjectId", subjectId);
		 stu.put("subjectName", prodDetl.productName);
		 if(isAttempted=="N"){
			 stu.put("check", false);
		 }else{
		 	stu.put("check", true);
		 }
		 jsonData.add(stu);
		 partySubscription.put(student.partyId, student.subscriptionId);
		 }
	 }
	
}
context.partySubscription=partySubscription;
context.put("jsonData", jsonData.toString());
context.subjectIds=subjectIds;
