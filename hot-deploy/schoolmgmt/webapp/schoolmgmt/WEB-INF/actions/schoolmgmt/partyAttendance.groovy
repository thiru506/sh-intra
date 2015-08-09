import java.sql.Timestamp;

import org.ofbiz.base.util.UtilValidate;

import java.sql.Timestamp;
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.party.party.PartyHelper;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.ofbiz.humanres.HumanResServices;
import java.util.concurrent.TimeUnit;

dctx = dispatcher.getDispatchContext();
SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
def dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

def sdf = new SimpleDateFormat("dd/MM/yyyy");
Timestamp fromDate = null;
Timestamp thruDate = null;
partyId = parameters.partyId;

try {
	if (parameters.fromDate) {
		context.fromDate = parameters.fromDate;
		fromDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(parameters.fromDate).getTime()));
	}
	if (parameters.thruDate) {
		context.thruDate = parameters.thruDate;
		thruDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(parameters.thruDate).getTime()));
	}
} catch (ParseException e) {
	Debug.logError(e, "Cannot parse date string: " + e, "");
}
if (thruDate == null) {
	thruDate = UtilDateTime.getDayStart(UtilDateTime.nowTimestamp());
}
if (fromDate == null) {
	fromDate = UtilDateTime.getDayStart(UtilDateTime.addDaysToTimestamp(thruDate, -45));
}

attendanceDaysSet = [:] as HashSet;
//String punchDateStr = UtilDateTime.toDateString(punchDate, "dd/MM/yyyy");			
String partyName = PartyHelper.getPartyName(delegator, partyId, false);

Timestamp timePeriodStart = UtilDateTime.getDayStart(fromDate);
Timestamp timePeriodEnd = UtilDateTime.getDayEnd(thruDate);
JSONArray punchListJSON = new JSONArray();
JSONArray oodPunchListJSON = new JSONArray();
conditionList=[];
conditionList.add(EntityCondition.makeCondition("punchdate", EntityOperator.GREATER_THAN_EQUAL_TO , UtilDateTime.toSqlDate(timePeriodStart)));
conditionList.add(EntityCondition.makeCondition("punchdate", EntityOperator.LESS_THAN_EQUAL_TO , UtilDateTime.toSqlDate(timePeriodEnd)));
conditionList.add(EntityCondition.makeCondition("partyId", partyId));

condition = EntityCondition.makeCondition(conditionList,EntityOperator.AND);
orderBy = UtilMisc.toList("-punchdate","-punchtime");
punchList = delegator.findList("EmplPunch", condition, null, orderBy, null, false);

normalPunchList=  EntityUtil.filterByCondition(punchList, EntityCondition.makeCondition("PunchType",EntityOperator.EQUALS , "Normal"));
def outTimestamp = null;
normalPunchList.each { punch->
	String partyId= punch.getString("partyId");
	String punchTime = timeFormat.format(punch.get("punchtime"));
	String inOut = "";
	if (punch.getString("InOut")) {
		inOut = punch.getString("InOut");
	}
	if (inOut.equals("OUT")) {
		outTimestamp = UtilDateTime.toDateString(punch.get("punchdate"), "dd/MM/yyyy") + " " + punchTime;
		return;
	}
	if (inOut.equals("IN")) {
		inTimestamp = UtilDateTime.toDateString(punch.get("punchdate"), "dd/MM/yyyy") + " " + punchTime;
		JSONArray punchJSON = new JSONArray();	
		punchJSON.add(inTimestamp);
		if (outTimestamp == null) {
			punchJSON.add("");
			punchJSON.add("");
		}
		else {
			punchJSON.add(outTimestamp);
			elapsedHours = (int)(UtilDateTime.getInterval(new java.sql.Timestamp(dateTimeFormat.parse(inTimestamp).getTime()), 
								new java.sql.Timestamp(dateTimeFormat.parse(outTimestamp).getTime()))/(1000*60*60));
							
			elapsedMinutes = (int)((UtilDateTime.getInterval(new java.sql.Timestamp(dateTimeFormat.parse(inTimestamp).getTime()),
								new java.sql.Timestamp(dateTimeFormat.parse(outTimestamp).getTime()))%(1000*60*60))/(1000*60));
			elapsedMinutes = String.format( "%02d", elapsedMinutes );
			punchJSON.add(elapsedHours+":"+elapsedMinutes);				
		}
		punchListJSON.add(punchJSON);
		attendanceDaysSet.add(UtilDateTime.toDateString(punch.get("punchdate"), "dd/MM/yyyy"));
		outTimestamp = null;
	}
}


// get companyBus details and weekly off days
JSONArray holidaysListJSON = new JSONArray();

inputMap = [userLogin:userLogin, fromDate:fromDate, thruDate:thruDate];
resultMap =[:];
//resultMap = HumanResServices.getGeneralHoliDays(dctx, inputMap);
if (resultMap) {
	generalHolidaysList = resultMap.get("holiDayList");
	generalHolidaysList.each { generalHoliday->
		JSONArray ghJSON = new JSONArray();
		ghDate = UtilDateTime.toDateString(generalHoliday.get("holiDayDate"), "dd/MM/yyyy");
		ghDescription = generalHoliday.get("description");
		ghJSON.add(ghDate);
		ghJSON.add(ghDescription);
		holidaysListJSON.add(ghJSON);
		attendanceDaysSet.add(ghDate);
	}
}

// Check for missing days (no punch, no leaves, no holidays)
JSONArray missingListJSON = new JSONArray();
Calendar c1=Calendar.getInstance();
c1.setTime(UtilDateTime.toSqlDate(fromDate));
Calendar c2=Calendar.getInstance();
c2.setTime(UtilDateTime.toSqlDate(thruDate));
while(c2.after(c1)){
	Timestamp cTime = new Timestamp(c1.getTimeInMillis());
	curDate = UtilDateTime.toDateString(cTime, "dd/MM/yyyy");
	if (!attendanceDaysSet.contains(curDate)) {
		JSONArray missingJSON = new JSONArray();
		missingJSON.add(curDate);
		missingListJSON.add(missingJSON);
	}
	c1.add(Calendar.DATE,1);
}


context.partyId = partyId;
context.partyName = partyName;
context.fromDate = fromDate;
context.thruDate = thruDate;
context.punchListJSON = punchListJSON.toString();
context.holidaysListJSON = holidaysListJSON.toString();
context.missedListJSON = missingListJSON.toString();