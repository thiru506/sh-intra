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
import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.ofbiz.entity.util.EntityUtil;

uiLabelMap = UtilProperties.getResourceBundleMap("AccountingUiLabels", locale);
if(UtilValidate.isEmpty(parameters.noConditionFind)){
return "";
}
condList = [];

condList.clear();
parentTypeId=parameters.parentTypeId;
actionName="createVoucherPayment";

//AP PaymentTypes
condList.add(EntityCondition.makeCondition("parentTypeId", EntityOperator.IN, UtilMisc.toList("DISBURSEMENT")));
cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
apPaymentTypes = delegator.findList("PaymentType", cond, null, ["description"], null, false);


//AR PaymentTypes
condList.clear();

condList.add(EntityCondition.makeCondition("parentTypeId", EntityOperator.IN, UtilMisc.toList("RECEIPT")));
cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
arPaymentTypes = delegator.findList("PaymentType", cond, null, ["description"], null, false);
paymentTypes=[];
paymentMethodLike="";
if("SALES_INVOICE"==parentTypeId){
	context.paymentTypes=arPaymentTypes;
	paymentMethodLike="%_PAYIN%";
	actionName="createArVoucherPayment";
}else if("PURCHASE_INVOICE"==parentTypeId){
context.paymentTypes=apPaymentTypes;
paymentMethodLike="%_PAYOUT%";
actionName="createApVoucherPayment";
}else{
context.paymentTypes=paymentTypes;
}
if(UtilValidate.isNotEmpty(parameters.paymentMethodSearchFlag)){
actionName="createOtherVoucherPayment";
}
context.parentTypeId=parentTypeId;
context.actionName=actionName;



//Debug.log("====paymentMethodSearchFlag=HECKKKKK="+parameters.paymentMethodSearchFlag);
JSONObject voucherPaymentMethodJSON = new JSONObject();
JSONArray cashMethodItemsJSON = new JSONArray();
JSONArray bankMethodItemsJSON = new JSONArray();
JSONArray allMethodItemsJSON = new JSONArray();
bankPaymentMethodList=[];
cashPaymentMethodList=[];
condList.clear();
if(UtilValidate.isNotEmpty(parameters.paymentMethodSearchFlag)){
	condList.add(EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.NOT_LIKE, "%_PAYOUT%"));
	cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
	bankPaymentMethodList = delegator.findList("PaymentMethodType",cond, null, null, null, false);
}else{
condList.add(EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.LIKE, paymentMethodLike));
cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
bankPaymentMethodList = delegator.findList("PaymentMethodType",cond, null, null, null, false);
condList.clear();
//CASH_PAYIN
if("SALES_INVOICE"==parentTypeId){
//         
	condList.add(EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.LIKE, "%_PAYIN%"));
	
}else{
	//condList.add(EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.LIKE, paymentMethodLike));
}
//condList.add(EntityCondition.makeCondition("parentTypeId", EntityOperator.EQUALS,"CASH"));
cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
cashPaymentMethodList = delegator.findList("PaymentMethodType", cond, null, null, null, false);
}

bankPaymentMethodIdsList=EntityUtil.getFieldListFromEntityList(bankPaymentMethodList, "paymentMethodTypeId", false);
cashPaymentMethodIdsList=EntityUtil.getFieldListFromEntityList(cashPaymentMethodList, "paymentMethodTypeId", false);
if("SALES_INVOICE"==parentTypeId){
	cashPaymentMethodList.each{ methodTypeEach->
		JSONObject newPMethodObj = new JSONObject();
		newPMethodObj.put("value",methodTypeEach.paymentMethodTypeId);
		newPMethodObj.put("text", methodTypeEach.description);
		cashMethodItemsJSON.add(newPMethodObj);
		allMethodItemsJSON.add(newPMethodObj);
	}
	bankPaymentMethodList.each{ methodTypeEach->
		JSONObject newPMethodObj = new JSONObject();
		newPMethodObj.put("value",methodTypeEach.paymentMethodTypeId);
		newPMethodObj.put("text", methodTypeEach.description);
		bankMethodItemsJSON.add(newPMethodObj);
		allMethodItemsJSON.add(newPMethodObj);
	}
}else if("PURCHASE_INVOICE"==parentTypeId){
		bankPaymentMethodList = delegator.findList("PaymentMethod", EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.IN,bankPaymentMethodIdsList), null, null, null, false);
		cashPaymentMethodList = delegator.findList("PaymentMethod", EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.IN,cashPaymentMethodIdsList), null, null, null, false);
		cashPaymentMethodList.each{ methodTypeEach->
			JSONObject newPMethodObj = new JSONObject();
			newPMethodObj.put("value",methodTypeEach.paymentMethodId);
			newPMethodObj.put("text", methodTypeEach.description);
			cashMethodItemsJSON.add(newPMethodObj);
			allMethodItemsJSON.add(newPMethodObj);
		}
		// for payment method id in ap payments
		condList.clear();
		condList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, "Company"));
		condList.add(EntityCondition.makeCondition("paymentMethodId", EntityOperator.NOT_EQUAL, "PAYMENTMETHOD2"));
		cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
		bankPaymentMethodList = delegator.findList("PaymentMethod", cond, null, ["description"], null, false);
		bankPaymentMethodList.each{ methodTypeEach->
			JSONObject newPMethodObj = new JSONObject();
			newPMethodObj.put("value",methodTypeEach.paymentMethodId);
			newPMethodObj.put("text", methodTypeEach.description);
			bankMethodItemsJSON.add(newPMethodObj);
			allMethodItemsJSON.add(newPMethodObj);
		}
}
	voucherPaymentMethodJSON.put("CASH",cashMethodItemsJSON);
	voucherPaymentMethodJSON.put("BANK",bankMethodItemsJSON);
	voucherPaymentMethodJSON.put("ALL",allMethodItemsJSON);
	
Debug.log("allMethodItemsJSON=======>"+allMethodItemsJSON);
context.voucherPaymentMethodJSON=voucherPaymentMethodJSON.toString();

voucherType=parameters.prefPaymentMethodTypeId;


invoiceCreateScreenTitle="";
if(UtilValidate.isEmpty(voucherType)){
	invoiceCreateScreenTitle = uiLabelMap.AccountingCreateNewPurchaseInvoice;
}else if(voucherType=="CASH"){
invoiceCreateScreenTitle = uiLabelMap.AccountingCreateNewCashPurchaseInvoice;
}else if(voucherType=="BANK"){
invoiceCreateScreenTitle = uiLabelMap.AccountingCreateNewBankPurchaseInvoice;
}
context.invoiceCreateScreenTitle=invoiceCreateScreenTitle;
context.voucherType = voucherType;


prefPaymentMethodTypeId=voucherType;
context.invoiceCreateScreenTitle=invoiceCreateScreenTitle;

arScreenTitle="";
arVoucherType=parameters.arVoucherType;
if(UtilValidate.isEmpty(arVoucherType)){
	arScreenTitle = uiLabelMap.AccountingCreateNewSalesInvoice;
}else if(arVoucherType=="CASH"){
arScreenTitle = uiLabelMap.AccountingCreateNewCashSalesInvoice;
}else if(arVoucherType=="BANK"){
arScreenTitle = uiLabelMap.AccountingCreateNewBankSalesInvoice;
}
prefPaymentMethodTypeId=arVoucherType;
context.arScreenTitle=arScreenTitle;
