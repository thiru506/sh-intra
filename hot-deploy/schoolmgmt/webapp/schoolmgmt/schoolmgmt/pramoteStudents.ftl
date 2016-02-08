
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/slick.grid.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.pager.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/css/smoothness/jquery-ui-1.8.5.custom.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/examples/examples.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/controls/slick.columnpicker.css</@ofbizContentUrl>" type="text/css" media="screen" charset="utf-8" />

<style type="text/css">
	.cell-title {
		font-weight: normal;
	}
	.cell-effort-driven {
		text-align: center;
	}
	
	.tooltip { /* tooltip style */
    background-color: #ffffbb;
    border: 0.1em solid #999999;
    color: #000000;
    font-style: arial;
    font-size: 80%;
    font-weight: normal;
    margin: 0.4em;
    padding: 0.1em;
}
.tooltipWarning { /* tooltipWarning style */
    background-color: #ffffff;
    border: 0.1em solid #FF0000;
    color: #FF0000;
    font-style: arial;
    font-size: 80%;
    font-weight: bold;
    margin: 0.4em;
    padding: 0.1em;
}	

.messageStr {
    background:#e5f7e3;
    background-position:7px 7px;
    border:4px solid #c5e1c8;
    font-weight:700;
    color:#005e20;    
    text-transform:uppercase;
}

</style>			
			
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/lib/firebugx.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/slickgrid/lib/jquery-1.4.3.min.js</@ofbizContentUrl>"></script>
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
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/validate/jquery.validate.js</@ofbizContentUrl>"></script>

<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/validate/jquery.validate.js</@ofbizContentUrl>"></script>
<script language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/plugins/qtip/jquery.qtip.js</@ofbizContentUrl>"></script>

<script type="application/javascript">
	function fetchStudents(){
		var custId = document.getElementById("customTimePeriodId").value;
		var fromProd = document.getElementById("fromProductId").value;
		var toProd = document.getElementById("toProductId").value;
		if(custId==null || custId == "" || custId==undefined){
				alert('Please Select CustomTimePeriodId');
				document.getElementById('customTimePeriodId').focus();
				return false;
		}
		if(fromProd==null || fromProd == "" || fromProd==undefined){
				alert('Please Select fromProductId');
				document.getElementById('fromProductId').focus();
				return false;
		}
		if(toProd==null || toProd=="" || toProd==undefined){
				alert('Please Select toProductId');
				document.getElementById('toProductId').focus();
				return false;
		}
		var form = document.getElementById("fatchStudentsForm");
		form.submit();
	
	}
</script>
<div class="screenlet">
	<div class="screenlet-title-bar">
      	<h3>Fetch Students</h3>	
     </div>
<div class="screenlet-body">
	<form name="fetchStudentsForm" id="fatchStudentsForm" action="">
		<table>
			<tr>
				<td>Acadamic Year</td>
				<td><select name="customTimePeriodId" id="customTimePeriodId" >
						<option value="">Select</option>
						<#if academicYearsList?has_content>
							<#list academicYearsList as year>
                                <#if customTimePeriodId?exists && customTimePeriodId == year.customTimePeriodId>
                                 <option value="${customTimePeriodId}" selected="selected">${year.periodName?if_exists} : ${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(year.fromDate, "dd-MM-yyyy")} To ${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(year.thruDate, "dd-MM-yyyy")}</option> 
                                <#else>
								<option value="${year.customTimePeriodId}">${year.periodName?if_exists} : ${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(year.fromDate, "dd-MM-yyyy")} To ${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(year.thruDate, "dd-MM-yyyy")}</option>
								</#if>
                            </#list>
                        </#if>
					</select><td>
			</tr>
			<tr><td>From Class</td>
				<td><select name="fromProductId" id="fromProductId"> 
          				<option value="">Select</option>
						<#if classList?has_content>
							<#list classList as class>
                                <#if fromProductId?exists && fromProductId==class.productId>
                                <option value="${fromProductId}" selected="selected">${class.productName?if_exists}</option>
                                <#else> 
								<option value="${class.productId}">${class.productName?if_exists}</option>
                                </#if>
                            </#list>
                        </#if>
					</select></td>
			</tr>
			<tr><td>To Class</td>
				<td><select name="toProductId" id="toProductId" >
						<option value="">Select</option>
						<#if classList?has_content>
							<#list classList as class>
								 <#if toProductId?exists && toProductId==class.productId>
                                <option value="${toProductId}" selected="selected">${class.productName?if_exists}</option>
                                <#else> 
								<option value="${class.productId}">${class.productName?if_exists}</option>
                                </#if>
                            </#list>
                        </#if>
					</select></td>
			</tr>
			<tr><td></td><td><input type="button" value="Fetch Students" onclick="javascript:fetchStudents();"  class="smallSubmit"/></td></tr>
		</table>
	</form>
</div>
