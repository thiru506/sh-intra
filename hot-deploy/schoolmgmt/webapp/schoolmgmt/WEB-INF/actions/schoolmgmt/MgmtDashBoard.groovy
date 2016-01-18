/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 
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
 
 fromDate = parameters.fromDate;
 thruDate = parameters.thruDate;
 organizationPartyId = "Company";
 context.organizationPartyId = organizationPartyId;
 context.apInvoiceListSize =0;
 dctx = dispatcher.getDispatchContext();
 def sdf = new SimpleDateFormat("MMMM dd, yyyy");
 try {
	 if (parameters.fromDate) {
		 context.fromDate = parameters.fromDate;
		 fromDate = UtilDateTime.getDayStart(new java.sql.Timestamp(sdf.parse(parameters.fromDate).getTime()));
	 }else {
		 fromDate = UtilDateTime.getDayStart(UtilDateTime.addDaysToTimestamp(UtilDateTime.nowTimestamp(),-60));
		 context.fromDate = fromDate
		 fromDate = fromDate;
	 }
	 if (parameters.thruDate) {
		 context.thruDate = parameters.thruDate;
		 thruDate = UtilDateTime.getDayEnd(new java.sql.Timestamp(sdf.parse(parameters.thruDate).getTime()));
	 }else {
		 context.thruDate = UtilDateTime.nowDate();
		 thruDate = UtilDateTime.getDayEnd(UtilDateTime.nowTimestamp());
	 }
 } catch (ParseException e) {
	 Debug.logError(e, "Cannot parse date string: " + e, "");
	 context.errorMessage = "Cannot parse date string: " + e;
	 return;
 }
 
 dctx = dispatcher.getDispatchContext();
 conditionList = [];
 conditionList.clear();
 itemDescMap = [:];
 if(UtilValidate.isNotEmpty(fromDate) && UtilValidate.isNotEmpty(thruDate)){
	 conditionList.add(EntityCondition.makeCondition("invoiceDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate));
	 conditionList.add(EntityCondition.makeCondition("invoiceDate", EntityOperator.LESS_THAN_EQUAL_TO, thruDate));
 }
 
 conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_IN, UtilMisc.toList("INVOICE_CANCELLED","INVOICE_PAID")));
 
 condition = EntityCondition.makeCondition(conditionList ,EntityOperator.AND);
 invoiceList = delegator.findList("InvoiceAndType", condition, null, null, null, false);
 SortedMap apDataMap = new TreeMap();
 SortedMap arDataMap = new TreeMap();
 totalApAmount =BigDecimal.ZERO;
 totalArAmount =BigDecimal.ZERO;
 List invoiceTypeIdList = FastList.newInstance();
 if(invoiceList){
	 apInvoiceList = EntityUtil.filterByAnd(invoiceList, UtilMisc.toMap("parentTypeId","PURCHASE_INVOICE"));
	 arInvoiceList = EntityUtil.filterByAnd(invoiceList, UtilMisc.toMap("parentTypeId","SALES_INVOICE"));
	 Set invoiceTypeIds = new HashSet(apInvoiceList.invoiceTypeId);
	 invoiceTypeIds.addAll(new HashSet(arInvoiceList.invoiceTypeId));
	 
	 invoiceTypeIdList.addAll(invoiceTypeIds);
	 List<GenericValue> invoiceTypeList = delegator.findList("InvoiceType", null, null, null, null, false);
	 
	 invoiceTypeList.each{ invoiceType ->
		 itemDescMap[invoiceType.invoiceTypeId] = invoiceType.description;
		 
	 }
	 invoiceTypeList.each{ invoiceType ->
		     invoiceTypeId = invoiceType.invoiceTypeId;
			 parentTypeId = invoiceType.parentTypeId;
			 if(parentTypeId.equals("SALES_INVOICE")){
				 arDataMap.put(invoiceTypeId, 0);
			 }else{
			 	apDataMap.put(invoiceTypeId, 0);
			 }
			 
			 
			 apInvoiceByStatusList = EntityUtil.filterByAnd(apInvoiceList, UtilMisc.toMap("invoiceTypeId", invoiceTypeId));
			 arInvoiceByStatusList = EntityUtil.filterByAnd(arInvoiceList , UtilMisc.toMap("invoiceTypeId", invoiceTypeId));
			 apStatusAmount = dispatcher.runSync("getInvoiceRunningTotal", [invoiceIds: apInvoiceByStatusList.invoiceId, organizationPartyId: organizationPartyId, userLogin: userLogin, currency:false]).get("invoiceRunningTotalValue");
			 arStatusAmount = dispatcher.runSync("getInvoiceRunningTotal", [invoiceIds: arInvoiceByStatusList.invoiceId, organizationPartyId: organizationPartyId, userLogin: userLogin,currency:false]).get("invoiceRunningTotalValue");
			 if (apStatusAmount) {
				// apStatusAmount = apStatusAmount.replace("," ,"");
				 apStatusAmount = (new BigDecimal(apStatusAmount)).setScale(0,0);
				 apDataMap.put(invoiceTypeId, apStatusAmount);
				 totalApAmount += apStatusAmount;
			 }
			 if (arStatusAmount) {
				 arStatusAmount = (new BigDecimal(arStatusAmount)).setScale(0,0);
				 arDataMap.put(invoiceTypeId, arStatusAmount);
				 totalArAmount += arStatusAmount;
			 }
			 
		 }
	 apInvoiceReportList = [];
	 for(Map.Entry entry : apDataMap.entrySet()){
		 tempMap = [:];
		 invoiceTypeId = entry.getKey();
		 totalAmount = entry.getValue();
		
		 tempMap.putAt("invoiceTypeId", itemDescMap[invoiceTypeId]);
		 tempMap.putAt("totalAmount", totalAmount);
		 if(totalAmount >0){
			 apInvoiceReportList.add(tempMap);
		 }
		 
	 }
	 arInvoiceReportList = [];
	 for(Map.Entry entry : arDataMap.entrySet()){
		 tempMap = [:];
		 invoiceTypeId = entry.getKey();
		 totalAmount = entry.getValue();
		 /*if(categoryTypeEnumMap[categoryTypeEnum]){
			 categoryTypeEnum = categoryTypeEnumMap[categoryTypeEnum];
		 }*/
		 tempMap.putAt("invoiceTypeId",  itemDescMap[invoiceTypeId]);
		 tempMap.putAt("totalAmount", totalAmount);
		 if(totalAmount >0){
			 arInvoiceReportList.add(tempMap);
		 }
		 
	 }
	 context.apInvoiceReportList = apInvoiceReportList;
	 context.arInvoiceReportList = arInvoiceReportList;
	 context.apInvoiceListSize = apInvoiceReportList.size();
	 context.arInvoiceListSize = arInvoiceReportList.size();
 }
 context.totalApAmount = totalApAmount;
 context.totalArAmount = totalArAmount;
 Debug.log("apInvoiceListSize==========="+context.apInvoiceListSize);
 Debug.log("arInvoiceListSize==========="+context.arInvoiceListSize);
 
 
 
 