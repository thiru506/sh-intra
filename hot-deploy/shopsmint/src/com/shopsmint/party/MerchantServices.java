package com.shopsmint.party;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

public class MerchantServices {
	
	public static Map<String,Object> getMerchants(DispatchContext dctx,Map<String, ? extends Object> context){
		Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        //String partyId = (String) context.get("partyId");
        //Locale locale = (Locale) context.get("locale");
        List<GenericValue> merchantsList = new ArrayList<GenericValue>();
        List<GenericValue> merchants = new ArrayList<GenericValue>();

        try {
        	
            //merchants = delegator.findByAnd("PartyRoleAndPartyDetail",UtilMisc.toMap("roleTypeId","MERCHANT"));
        	merchantsList = delegator.findByAnd("PartyClassification",UtilMisc.toMap("partyClassificationGroupId","RETAIL_FMCG"));
            
        	ListIterator<GenericValue> merchantsIte = merchantsList.listIterator();
        	while(merchantsIte.hasNext()){
        		GenericValue merchant = merchantsIte.next();
        		String partyID = (String)merchant.get("partyId");
        		merchants.addAll(delegator.findByAnd("ProductStore",UtilMisc.toMap("payToPartyId",partyID)));
        	}
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        if (merchants != null) {
            result.put("merchants", merchants);
        }
        return result;		
		
	}

}
