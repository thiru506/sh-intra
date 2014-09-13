package com.shopsmint.party;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.catalog.CatalogWorker;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

public class Product {

	
	public static Map<String,Object> getAllProductsOfMerchant(DispatchContext dctx,Map<String, ? extends Object> context){
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        //Locale locale = (Locale) context.get("locale");
        String originalProductStoreId = (String)context.get("productStoreId");
        System.out.println("***************************************************");
        System.out.println(originalProductStoreId);
        System.out.println("***************************************************");
        List<GenericValue> products = new ArrayList<GenericValue>();
        List<GenericValue> productStoreCategories = new ArrayList<GenericValue>();
        List<GenericValue> storeCatelog = null;
        String productStoreId = "9001";

        try {
        	
            //merchants = delegator.findByAnd("PartyRoleAndPartyDetail",UtilMisc.toMap("roleTypeId","MERCHANT"));
        	//products = delegator.findByAnd("ProductCategoryMemberAndPrice",UtilMisc.toMap("productId","SM-1000"));
        	storeCatelog = CatalogWorker.getStoreCatalogs(delegator,productStoreId);//EntityUtil.filterByDate(delegator.findByAndCache("ProductStoreCatalog", UtilMisc.toMap("productStoreId", productStoreId), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);
        	ListIterator<GenericValue> li = storeCatelog.listIterator();
        	while(li.hasNext()){
        		GenericValue val = li.next();
        		String prodCatalogId = (String)val.get("prodCatalogId");
        		productStoreCategories.addAll(CatalogWorker.getProdCatalogCategories(delegator,prodCatalogId,"PCCT_BROWSE_ROOT"));
        	}
        	 
        	ListIterator<GenericValue> catlist = productStoreCategories.listIterator();
        	while(catlist.hasNext()){
        		GenericValue val = catlist.next();
        		String productCategoryId = (String)val.get("productCategoryId");
        		products.addAll(delegator.findByAnd("ProductCategoryAndMember",UtilMisc.toMap("productCategoryId",productCategoryId)));
        	}
        	
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        if (products != null) {
            result.put("products", products);
        }
        return result;		
		
	}

}
