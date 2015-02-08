<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<#assign nowTimestamp = Static["org.ofbiz.base.util.UtilDateTime"].nowTimestamp()>

  <br />
  <div class="footer">
      
      <br />
      <div class="tabletext">
        <a href="http://ofbiz.apache.org">${uiLabelMap.EcommerceAboutUs}</a>
        <div class="tabletext">Copyright (c) 2015-${nowTimestamp?string("yyyy")} The ShopsMint - <a href="http://www.shopsmint.com" class="tabletext">www.shopsmint.com</a></div>
        <div class="tabletext">Powered by <a href="http://www.shopsmint.com" class="tabletext">ShopsMint</a></div>
        
      </div>
      <br />
      <div class="tabletext"><a href="<@ofbizUrl>policies</@ofbizUrl>">${uiLabelMap.EcommerceSeeStorePoliciesHere}</a></div>
  </div>

