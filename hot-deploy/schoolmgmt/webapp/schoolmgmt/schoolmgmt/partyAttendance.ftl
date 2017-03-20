<link type="text/css" href="<@ofbizContentUrl>/images/jquery/ui/css/ui-lightness/jquery-ui-1.8.13.custom.css</@ofbizContentUrl>" rel="Stylesheet" />	
<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/glDatePicker-2.0/styles/glDatePicker.default.css</@ofbizContentUrl>">
<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/jquery.flexselect-0.5.3/flexselect.css</@ofbizContentUrl>">

<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/css/jquery.dataTables.css</@ofbizContentUrl>">
<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/extensions/TableTools/css/dataTables.tableTools.css</@ofbizContentUrl>">

<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->


<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/jquery.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/jquery.dataTables.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/extensions/TableTools/js/dataTables.tableTools.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/dataTables.plugins.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/glDatePicker-2.0/glDatePicker.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/jquery.flexselect-0.5.3/liquidmetal.js</@ofbizContentUrl>"></script>
<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/jquery.flexselect-0.5.3/jquery.flexselect.js</@ofbizContentUrl>"></script>

<style>
.mycalendar {
	width:200px;
 	display:inline;
	margin:5px;
}
.flexselect {
	width:200px;
}
</style>

<script type="text/javascript">

$(document).ready(function(){

  var partys = ${StringUtil.wrapString(partysJSON!'[]')};
  //alert("partys="+partys);
//$("#partyId").append('<option value=""></option>');
 /* $.each(partys, function(key, val){
    $("#partyId").append('<option value="' + val.partyId + '">' + val.name + " [" + val.partyId + "]" + '</option>');
  });
  $("#partyId").flexselect({
  								preSelection: false,
  								hideDropdownOnEmptyInput: true});	*/

thruDate = new Date();
$("#thruDate").val(thruDate.getDate() + "/" + (thruDate.getMonth() + 1) + "/" + thruDate.getFullYear());

fromDate = new Date(thruDate);
fromDate.setDate(thruDate.getDate() - 45);
$("#fromDate").val(fromDate.getDate() + "/" + (fromDate.getMonth() + 1) + "/" + fromDate.getFullYear());

$("#thruDate").glDatePicker(
{
	selectedDate: thruDate,
    onClick: function(target, cell, date, data) {
        target.val(date.getDate() + "/" + (date.getMonth() + 1) + "/" +date.getFullYear());

        if(data != null) {
            alert(data.message + '\n' + date);
        }
    }
});	

$("#fromDate").glDatePicker(
{
	selectedDate: fromDate,
    onClick: function(target, cell, date, data) {
        target.val(date.getDate() + "/" + (date.getMonth() + 1) + "/" +date.getFullYear());

        if(data != null) {
            alert(data.message + '\n' + date);
        }
    }
});		

// enter event handle
	$("input").keypress(function(e){
		if (e.which == 13) {
				$("#getPartyAttendance").click();
		}
	});
	
	// also set the click handler
  	$("#getPartyAttendance").click(function(){  
  	    $('#loader').show();
     	$('#result').hide(); 
  	    
        $.get(  
            "${ajaxUrl}",  
            { partyId: $("#partyId").val(),
              fromDate: $("#fromDate").val(),
              thruDate: $("#thruDate").val()},                
            function(responseText){  
                $("#result").html(responseText); 
				var reponse = jQuery(responseText);
       			var reponseScript = reponse.filter("script");
       			// flot does not work well with hidden elements, so we unhide here itself     			
       			jQuery.each(reponseScript, function(idx, val) { eval(val.text); } );       
       			$('#loader').hide();
     			$('#result').show();            
            },  
            "html"  
        );  
        return false;
    });
    
    $('#loader').hide();
    
/*
    $('#loader').show();
	$('#result').hide(); 
    $.get(  
    "${ajaxUrl}",  
    { partyId: $("#partyId").val(),
      fromDate: $("#fromDate").val(),
      thruDate: $("#thruDate").val()},  
    function(responseText){  
        $("#result").html(responseText); 
		var reponse = jQuery(responseText);
		var reponseScript = reponse.filter("script");
		// flot does not work well with hidden elements, so we unhide here itself       			
		jQuery.each(reponseScript, function(idx, val) { eval(val.text); } );  
		$('#loader').hide();
		$('#result').show();               
    },  
    "html");  
    
*/

});

</script>

<div class="screenlet">
	<div class="screenlet-title-bar">
      	<h3>Select Date</h3>	
     </div>
    <div class="screenlet-body">
	  <form name="attendance">
		<table class="basic-table" cellspacing="0">
        	<tr>
        		<td align="right" width="10%"><span class='h2'>Party: </span></td>
        		<#if security.hasEntityPermission("MYPORTAL", "_HREMPLVIEW", session) || security.hasEntityPermission("MYPORTAL", "_EMPLEAVE_AP", session)>
        			<#if security.hasEntityPermission("HUMANRES", "_ADMIN", session) || security.hasEntityPermission("HR_ATTENDANCE", "_VIEW", session)>
        				<td align="left" width="15%"><@htmlTemplate.lookupField value="${partyId?if_exists}" formName="attendance" name="partyId" id="partyId" fieldFormName="LookupPartyName"/></td>
        			<#else>
        				<#assign partyName= Static["org.ofbiz.party.party.PartyHelper"].getPartyName(delegator, userLogin.partyId, false)?if_exists/>
                		<td width="20%" class = "h3"><input type="hidden" name="employeeId" id="employeeId" value="${userLogin.partyId?if_exists}"/>
                		${partyName}[${userLogin.partyId?if_exists}]</td>
        			</#if>
                <#else>
                	<td valign="middle">
			            <div>
			              <@htmlTemplate.lookupField value="${partyId?if_exists}" formName="attendance" name="partyId" id="partyId" fieldFormName="LookupPartyName"/>
			            </div>
			        </td>
                </#if>        	
        		<td align="right" width="10%"><span class='h3'>From Date: </span></td>
            	<td width="20%"><input class="mycalendar" type="text" id="fromDate" name="fromDate"/></td>        	
        		<td align="right" width="10%"><span class='h3'>Thru Date: </span></td>
            	<td width="20%"><input class="mycalendar" type="text" id="thruDate" name="thruDate"/></td>
				<td><input type="submit" value="Submit" id="getPartyAttendance" class="smallSubmit" /></td>
			</tr>
    	</table>    	
	</form>
	</div>
</div>

<div id="loader" > 
      <p align="center" style="font-size: large;">
        <img src="<@ofbizContentUrl>/images/ajax-loader64.gif</@ofbizContentUrl>">
      </p>
</div>

<div id="result"/>