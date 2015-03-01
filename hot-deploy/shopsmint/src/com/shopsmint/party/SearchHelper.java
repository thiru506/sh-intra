package com.shopsmint.party;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.catalog.CatalogWorker;
import org.ofbiz.product.product.ProductSearchSession;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

public class SearchHelper {
	public static String module = SearchHelper.class.getName();
	
	public static Map<String,Object> getProductsByKeyWord(DispatchContext dctx,Map<String, ? extends Object> context){
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        String SEARCH_STRING = (String) context.get("SEARCH_STRING");
      //refer ProductSearchSession class for valid accepted params
        Locale locale = (Locale) context.get("locale");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
        //Map<String, Object> paramMap = FastMap.newInstance();
        Map searchResult = FastMap.newInstance();
        try {
        	
        	// note: this can be run multiple times in the same request without causing problems, will check to see on its own if it has run again
        	ProductSearchSession.processSearchParameters(paramMap, request);
        	String prodCatalogId = CatalogWorker.getCurrentCatalogId(request);
        	searchResult = ProductSearchSession.getProductSearchResult(request, delegator, prodCatalogId);

        	List<GenericValue> applicationTypes = delegator.findList("ProductFeatureApplType", null, null, UtilMisc.toList("description"), null, false);

        	EntityCondition expr = EntityCondition.makeCondition(EntityCondition.makeCondition("showInSelect", EntityOperator.EQUALS, null),
        	                                     EntityOperator.OR,
        	                                     EntityCondition.makeCondition("showInSelect", EntityOperator.NOT_EQUAL, "N"));
        	List<GenericValue> productCategories = delegator.findList("ProductCategory", expr, null, UtilMisc.toList("description"), null, false);

        	searchResult.put("applicationTypes", applicationTypes);
        	searchResult.put("productCategories", productCategories);
        	
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.log("searchResult========="+searchResult);
        result.put("searchResult", searchResult);
        return result;		
	}
	public static Map<String,Object> getProductStoresByGeo(DispatchContext dctx,Map<String, ? extends Object> context){
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        String SEARCH_STRING = (String) context.get("SEARCH_STRING");
      //refer ProductSearchSession class for valid accepted params
        Locale locale = (Locale) context.get("locale");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
        //Map<String, Object> paramMap = FastMap.newInstance();
        Map searchResult = FastMap.newInstance();
        try {
        	
        	// note: this can be run multiple times in the same request without causing problems, will check to see on its own if it has run again
        	ProductSearchSession.processSearchParameters(paramMap, request);
        	String prodCatalogId = CatalogWorker.getCurrentCatalogId(request);
        	searchResult = ProductSearchSession.getProductSearchResult(request, delegator, prodCatalogId);

        	List<GenericValue> applicationTypes = delegator.findList("ProductFeatureApplType", null, null, UtilMisc.toList("description"), null, false);

        	EntityCondition expr = EntityCondition.makeCondition(EntityCondition.makeCondition("showInSelect", EntityOperator.EQUALS, null),
        	                                     EntityOperator.OR,
        	                                     EntityCondition.makeCondition("showInSelect", EntityOperator.NOT_EQUAL, "N"));
        	List<GenericValue> productCategories = delegator.findList("ProductCategory", expr, null, UtilMisc.toList("description"), null, false);

        	searchResult.put("applicationTypes", applicationTypes);
        	searchResult.put("productCategories", productCategories);
        	List condList = FastList.newInstance();
        	//condList.add(EntityCondition.makeCondition("showInSelect", EntityOperator.EQUALS, Math.acos(Math.sin(Math.toRadians("latitude")))));
        
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.log("searchResult========="+searchResult);
        result.put("searchResult", searchResult);
        return result;		
	}
	
}