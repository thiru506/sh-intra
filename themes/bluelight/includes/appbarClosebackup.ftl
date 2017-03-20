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
<style>
#commonAppBarDiv {
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: white;
  font-weight: bold;
  white-space:nowrap;
  background-color: #efe4b0; 
  
}
#commonAppBarSpan {
  display: inline-block;
  vertical-align: middle;
  line-height: normal;   
  font-size: 18px;
  padding-right: 10px;   
}
#simple-menu2 {
	display: inline-block;
	vertical-align: middle;
	line-height: 50px;
}
</style>

<div id="commonAppBarDiv" >
<span id="commonAppBarSpan"></span>
<a id="simple-menu2" class="menu-button" href="#sidr2">Toggle menu</a>
</div>

<script type="text/javascript">
jQuery("#sidr2").hide()
$("#simple-menu2").hide();

    jQuery(document).ready(function ($) {
      $('#simple-menu2').sidr({
        name: 'sidr2',
        side: 'right',      
        timing: 'ease-in-out',
        speed: 500
      });

      $("#sidr2").show();
      
      var menuCount = $("#sidr2 ul li ul li").length;
	  if (menuCount > 1) {
		  $("#simple-menu2").show();
	  }
      
      var text_val1 = $('#sidr2 > h2').text();
      var text_val2 = $('#content-main-section > .page-title > span').text();
      var text_val = text_val2 ? (text_val1 + " > " + text_val2) : text_val1;
      $("#commonAppBarSpan").text(text_val);
      
    });

    jQuery( window ).resize(function ($) {
      $.sidr('close', 'sidr2');
    });
</script>

