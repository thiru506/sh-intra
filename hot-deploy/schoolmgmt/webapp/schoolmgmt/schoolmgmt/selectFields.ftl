<link type="text/css" href="<@ofbizContentUrl>/images/jquery/ui/css/ui-lightness/jquery-ui-1.8.13.custom.css</@ofbizContentUrl>" rel="Stylesheet" />	

<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->

<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/flot/jquery.flot.js</@ofbizContentUrl>"></script>
<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/flot/jquery.flot.axislabels.js"</@ofbizContentUrl>></script>

<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/flot/jquery.flot.pie.js"</@ofbizContentUrl>></script>
<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/flot/jquery.flot.tooltip.js"</@ofbizContentUrl>></script>
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.grid.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.pager.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/examples/examples.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.columnpicker.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />


<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/lib/firebugx.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/lib/jquery-ui-1.8.5.custom.min.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/lib/jquery.event.drag-2.0.min.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.core.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.editors.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/plugins/slick.cellrangedecorator.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/plugins/slick.cellrangeselector.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/plugins/slick.cellselectionmodel.js</@ofbizContentUrl>"></script>		
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.grid.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.groupitemmetadataprovider.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.dataview.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.pager.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.columnpicker.js</@ofbizContentUrl>"></script>

<script type="text/javascript">

$(document).ready(function(){

// enter event handle
	$("input").keypress(function(e){
		if (e.which == 13 && e.target.name =="facilityId") {
				$("#getCharts").click();
		}
	});
	


	$( "#fromDate" ).datepicker({
			dateFormat:'MM d, yy',
			changeMonth: true,
			numberOfMonths: 1,
			onSelect: function( selectedDate ) {
					
				date = $(this).datepicker('getDate');
	        	var maxDate = new Date(date.getTime());
	        	maxDate.setDate(maxDate.getDate() + 600);
				$( "#thruDate" ).datepicker( "option", {minDate: selectedDate, maxDate: maxDate}).datepicker('setDate', date);
			}
		});
		$( "#thruDate" ).datepicker({
			dateFormat:'MM d, yy',
			changeMonth: true,
			numberOfMonths: 1,
			onSelect: function( selectedDate ) {
				$( "#fromDate" ).datepicker( "option", "maxDate", selectedDate );
			}
		});
		$('#ui-datepicker-div').css('clip', 'auto');

	// fetch the charts for today
	  	    $('#loader').show();
     		$('#result').hide(); 
	        $.get(  
            "${ajaxUrl}",  
            { fromDate: $("#fromDate").val()},  
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
        
	// also set the click handler
  	$("#getCharts").click(function(){  
  	    $('#loader').show();
     	$('#result').hide(); 
  	    
        $.get(  
            "${ajaxUrl}",  
            { fromDate: $("#fromDate").val() ,thruDate: $("#thruDate").val() ,
              facilityId : $("input[name=facilityId]").val(), productId : $("input[name=productId]").val(),partyId : $("input[name=partyId]").val(),
              productCategoryId : $("select[name=productCategoryId]").val(), subscriptionTypeId : $("select[name=subscriptionTypeId]").val(),categoryTypeEnum : $("select[name=categoryTypeEnum]").val()},  
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
});

</script>

<div class="screenlet">
	<div class="screenlet-title-bar">
      	<h3>Select Period</h3>	
     </div>
    <div class="screenlet-body">
	  <form name="revenueAnalysis">
		<table class="basic-table" cellspacing="0">
			<tr>
        		<td width="10%"><span class='h3'>Class: </span></td>
				<td align="left" width="15%"><@htmlTemplate.lookupField value="${productId?if_exists}" formName="revenueAnalysis" name="productId" id="productId" fieldFormName="LookupProduct"/></td>
				<td align="right" width="10%"><span class='h3'>From: </span></td>
            	<td width="20%"><input class='h2' type="text" id="fromDate" name="fromDate"/></td>
				<td width="2%"><span class='h3'>To: </span></td>
				<td width="20%"><input class='h2' type="text" id="thruDate" name="thruDate"/></td>
				<td><input type="submit" value="Submit" id="getCharts" class="smallSubmit" /></td>
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