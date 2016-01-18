
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

	var data1;
	function setUpQtySnfItemList(jsonData) {
			var grid;
			data1=jsonData
			var columns = [
				{id:"shed", name:"Shed", field:"shedName", width:50, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"unitCode", name:"unit Code", field:"unitCode", width:60, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"centerCode", name:"center Code", field:"centerCode", width:60, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"orderDate", name:"proc Date", field:"orderDate", width:100, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"supplyTypeEnumId", name:"Time", field:"purchaseTime", width:40, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"productName", name:"Milk", field:"productName", width:70, minWidth:100, cssClass:"cell-title", sortable:false},
				{id:"quantity", name:"quantity", field:"quantity", width:80, minWidth:100, sortable:false , editor:FloatCellEditor},
				{id:"fat", name:"fat", field:"fat", width:40, minWidth:40, sortable:false , editor:FloatCellEditor},
				<#if (tenantConfigCondition?has_content) && (tenantConfigCondition.enableLR?has_content)&& tenantConfigCondition.enableLR != 'N'>
					{id:"lactoReading", name:"lactoReading", field:"lactoReading", width:40, minWidth:40, sortable:false , editor:FloatCellEditor}
				<#else>
					{id:"snf", name:"snf", field:"snf", width:40, minWidth:40, sortable:false , editor:FloatCellEditor}
				</#if>,
				{id:"sQuantity", name:"sQuantity", field:"sQuantity", width:80, minWidth:100, sortable:false , editor:FloatCellEditor},
				{id:"sFat", name:"sFat", field:"sFat", width:40, minWidth:40, sortable:false , editor:FloatCellEditor},
				{id:"cQuantity", name:"cQuantity", field:"cQuantity", width:80, minWidth:100, sortable:false , editor:FloatCellEditor},
				{id:"ptcQuantity", name:"ptcQuantity", field:"ptcQuantity", width:80, minWidth:100, sortable:false , editor:FloatCellEditor},
				{id:"statusId", name:"status", field:"statusId", width:80, minWidth:100, sortable:false}								
			];
	
			columns.push({id:"button", name:"Approve", field:"button", width:70, minWidth:70, cssClass:"cell-title",
			 			formatter: function (row, cell, id, def, datactx) { 
			 				if (dataView4.getItem(row)["statusId"] != "Approved") {
        						return '<a href="#" class="button" onclick="approveClickQtySnfValidationHandler('+row+')">Approve</a>'; 
        					}
        					else {
        					return '';
        					}
        	 			}
 					   });
 			columns.push({id:"button", name:"Edit", field:"button", width:70, minWidth:70, cssClass:"cell-title",
			 			formatter: function (row, cell, id, def, datactx) { 
			 				if (dataView4.getItem(row)["statusId"] != "Approved") {
        						return '<a href="#" class="button" onclick="editClickQtySnfValidationHandler('+row+')">Edit</a>'; 
        					}
        					else {
        					return '';
        					}
        	 			}
 					   });	
 		    columns.push({id:"check", name:"Check", field:"check", width:100, minWidth:100, cssClass:"cell-title", formatter:BoolCellFormatter, editor:YesNoCheckboxCellEditor, sortable:false});		   		   	   
		
	
			var options = {
				editable: true,		
				forceFitColumns: false,
				enableCellNavigation: true,
				autoEdit: true,
				asyncEditorLoading: false,			
	            secondaryHeaderRowHeight: 25
			};
			
	        var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
			dataView4 = new Slick.Data.DataView({
	        	groupItemMetadataProvider: groupItemMetadataProvider
	        });
			grid = new Slick.Grid("#itemGrid3", dataView4, columns, options);
	        grid.setSelectionModel(new Slick.CellSelectionModel());
			grid.onBeforeEditCell.subscribe(function(e, args) { 
				
			});
			var columnpicker = new Slick.Controls.ColumnPicker(columns, grid, options);
			
			// wire up model events to drive the grid
			dataView4.onRowCountChanged.subscribe(function(e,args) {
				grid.updateRowCount();
	            grid.render();
			});
			dataView4.onRowsChanged.subscribe(function(e,args) {
			    
				grid.invalidateRows(args.rows);
				grid.render();
			});
			
	        grid.onKeyDown.subscribe(function(e){
	        	var cell = grid.getCellFromEvent(e);
				if (e.which == $.ui.keyCode.ENTER) {
				     if(cell && cell.cell == 6){
				          $(grid.getCellNode((cell.row),7)).click();
				          grid.getEditController().commitCurrentEdit();
				     
				     }else{
					      editClickHandler(cell.row);
					      approveClickHandler(cell.row);
					      $(grid.getCellNode(cell.row +1, 6)).click();
					      grid.getEditController().commitCurrentEdit();
				     }
				     
				}
				
			});
	          
	         
			// initialize the model after all the events have been hooked up
			dataView4.beginUpdate();
			dataView4.setItems(data1);
			dataView4.endUpdate();	
	
	}	
	
	$(document).ready(function() {
		
		//populate Last change on load
		var dataJson;
		<#if qtySnfItemsJson?has_content>
		 dataJson = ${StringUtil.wrapString(qtySnfItemsJson)!'[]'}
		 setUpQtySnfItemList(dataJson);
		</#if>  
		  	
	});	
	var statusValue;
function editClickQtySnfValidationHandler(row) {
		statusValue="Created";
		updateQtySnfProcValidations('qtySnfProcValidations', '<@ofbizUrl>updateProcurementEntryAjax</@ofbizUrl>', row,statusValue);
	}	
function approveClickQtySnfValidationHandler(row) {
		statusValue="Approved"
		updateQtySnfProcValidations('qtySnfProcValidations', '<@ofbizUrl>approveValidationEntryAjax</@ofbizUrl>', row,statusValue);
		
	}	


function updateQtySnfProcValidations(formName, action, row,statusValue) {
		if (Slick.GlobalEditorLock.isActive() && !Slick.GlobalEditorLock.commitCurrentEdit()) {
			return false;		
		} 	
		var formId = "#" + formName;
									
		var changeItem = dataView4.getItem(row);			
		var rowCount = 0;
		for (key in changeItem){			
			var value = changeItem[key];	
	 		if (key != "id") {	 			 		
				var inputParam = jQuery("<input>").attr("type", "hidden").attr("name", key).val(value);										
				jQuery(formId).append(jQuery(inputParam));				
				 				
   			}
		}		
		var dataString = $(formId).serialize();	
			
		$.ajax({
             type: "POST",
             url: action,
             data: dataString,
             dataType: 'json',
             success: function(result) { 
             	$(formId+' input').remove()            	
               if(result["_ERROR_MESSAGE_"] || result["_ERROR_MESSAGE_LIST_"]){               	   
            	  alert(result["_ERROR_MESSAGE_"]);
            	     
               }else{
               		alert("Successfully Updated");               		          				
   					data1[row]["statusId"]=statusValue;
					setUpQtySnfItemList(data1);									
               }
               
             } ,
             error: function() {
            	 	populateError(result["_ERROR_MESSAGE_"]);
            	 }
               });
		
	}//end 
	
	
    $(function() {
	 	 	 	
        $( "#accordion" ).accordion({ collapsible: true , active : true});
        $( "#accordion" ).accordion({ icons: { "header": "ui-icon-plus", "headerSelected": "ui-icon-minus" } }); 
        
        
              
   });
   
   function processQtySnfEntry(formName, action) {
   
   
		jQuery("#changeSave").attr( "disabled", "disabled");
		processQtySnfInternal(formName, action);
		
	}
	function processQtySnfInternal(formName, action) {
	
	
		if (Slick.GlobalEditorLock.isActive() && !Slick.GlobalEditorLock.commitCurrentEdit()) {
			return false;		
		}
		var formId = "#" + formName;
		var inputRowSubmit = jQuery("<input>").attr("type", "hidden").attr("name", "_useRowSubmit").val("Y");
		jQuery(formId).append(jQuery(inputRowSubmit));	
		for (var rowCount=0; rowCount < data2.length; ++rowCount)
		{
		
			
			var validationTypeId = data2[rowCount]["validationTypeId"];
			var customTimePeriodId = data2[rowCount]["customTimePeriodId"];
			
			var supplyTypeEnumId = data2[rowCount]["supplyTypeEnumId"];
			var productName = data2[rowCount]["productName"];
			var quantity = data2[rowCount]["quantity"];
			var fat = data2[rowCount]["fat"];
			var lactoReading = data2[rowCount]["lactoReading"];
			var snf = data2[rowCount]["snf"];
			
			var sQuantity = data2[rowCount]["sQuantity"];
			var sFat = data2[rowCount]["sFat"];
			var cQuantity = data2[rowCount]["cQuantity"];
			var ptcQuantity = data2[rowCount]["ptcQuantity"];
			var statusId = data2[rowCount]["statusId"];
			var sequenceNum = data2[rowCount]["sequenceNum"];
			var unitId = data2[rowCount]["unitId"];
			
			var check = data2[rowCount]["check"];
			
			
			if(check != "undefined"){
				if(check){
					var inputSupplyTypeEnumId = jQuery("<input>").attr("type", "hidden").attr("name", "supplyTypeEnumId_o_" + rowCount).val(supplyTypeEnumId);
					var inputProductName = jQuery("<input>").attr("type", "hidden").attr("name", "productName_o_" + rowCount).val(productName);
					var inputQuantity = jQuery("<input>").attr("type", "hidden").attr("name", "quantity_o_" + rowCount).val(quantity);
					var inputFat = jQuery("<input>").attr("type", "hidden").attr("name", "fat_o_" + rowCount).val(fat);
					var inputLactoReading = jQuery("<input>").attr("type", "hidden").attr("name", "lactoReading_o_" + rowCount).val(lactoReading);
					var inputSnf = jQuery("<input>").attr("type", "hidden").attr("name", "snf_o_" + rowCount).val(snf);
					var inputSQuantity = jQuery("<input>").attr("type", "hidden").attr("name", "sQuantity_o_" + rowCount).val(sQuantity);
					var inputSFat = jQuery("<input>").attr("type", "hidden").attr("name", "sFat_o_" + rowCount).val(sFat);
					var inputCQuantity = jQuery("<input>").attr("type", "hidden").attr("name", "cQuantity_o_" + rowCount).val(cQuantity);
					var inputPtcQuantity = jQuery("<input>").attr("type", "hidden").attr("name", "ptcQuantity_o_" + rowCount).val(ptcQuantity);
					var inputStatusId = jQuery("<input>").attr("type", "hidden").attr("name", "statusId_o_" + rowCount).val(statusId);
					var inputValidationTypeId = jQuery("<input>").attr("type", "hidden").attr("name", "validationTypeId_o_" + rowCount).val(validationTypeId);
					var inputCustomTimePeriodId = jQuery("<input>").attr("type", "hidden").attr("name", "customTimePeriodId_o_" + rowCount).val(customTimePeriodId);
					var inputSequenceNum = jQuery("<input>").attr("type", "hidden").attr("name", "sequenceNum_o_" + rowCount).val(sequenceNum);
					var inputUnitId = jQuery("<input>").attr("type", "hidden").attr("name", "unitId_o_" + rowCount).val(unitId);
					
					
					jQuery(formId).append(jQuery(inputSupplyTypeEnumId));
					jQuery(formId).append(jQuery(inputProductName));
					jQuery(formId).append(jQuery(inputQuantity));
					jQuery(formId).append(jQuery(inputFat));
					jQuery(formId).append(jQuery(inputSnf));
					jQuery(formId).append(jQuery(inputSQuantity));
					jQuery(formId).append(jQuery(inputSFat));
					jQuery(formId).append(jQuery(inputCQuantity));
					jQuery(formId).append(jQuery(inputPtcQuantity));
					jQuery(formId).append(jQuery(inputStatusId));
					jQuery(formId).append(jQuery(inputValidationTypeId));
					jQuery(formId).append(jQuery(inputCustomTimePeriodId));
					jQuery(formId).append(jQuery(inputSequenceNum));
					jQuery(formId).append(jQuery(inputUnitId));
				}
			}
				
				    
		}
		
		// lets make the ajaxform submit
		var dataString = $(formId).serializeArray();	
		$.ajax({
             type: "POST",
             url: action,
             data: dataString,
             dataType: 'json',
             success: function(result) { 
             	//$(formId+' input').remove()            	
	               if(result["_ERROR_MESSAGE_"] || result["_ERROR_MESSAGE_LIST_"]){               	   
	            	    $('div#updateEntryMsg').html();
	            	    $('div#updateEntryMsg').removeClass("messageStr");            	 
	            	    $('div#updateEntryMsg').addClass("errorMessage");
	            	    $('div#updateEntryMsg').html('<label>'+ result["_ERROR_MESSAGE_"]+result["_ERROR_MESSAGE_LIST_"]+'</label>');
	            	     
	               }else{
	               		
	               		$("div#updateEntryMsg").fadeIn();               	         	   
	            	    $('div#updateEntryMsg').html(); 
	            	    $('div#updateEntryMsg').removeClass("errorMessage");           	 
	            	    $('div#updateEntryMsg').addClass("messageStr");
	            	    $('div#updateEntryMsg').html('<label>succesfully updated.</label>'); 
	            	    $('div#updateEntryMsg').delay(5000).fadeOut('slow');  
	            	    cleanUpGrid3(); 
	               }
               
               },
             error: function() {
            	 	//alert("record not updated");
            	 }
           });
			
	}
    
    function cleanUpGrid3(value){
		$('[name=qtySnfProcValidations]').val(value);
		var emptyJson= [];
		setUpQtySnfItemList("cleanGrid") ;
	}
   
   $( "#punchDate" ).datepicker({
			dateFormat:'MM d, yy',
			changeMonth: true,
			numberOfMonths: 1,
			onSelect: function( selectedDate ) {
					
				date = $(this).datepicker('getDate');
	        	var maxDate = new Date(date.getTime());
	        	maxDate.setDate(maxDate.getDate() + 600);
				$( "#punchDate" ).datepicker('setDate', date);
			}
		});
   
   $('#ui-datepicker-div').css('clip', 'auto');

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
				<td align="right" width="10%"><span class='h3'>Date : </span></td>
            	<td width="20%"><input class='h2' type="text" id="punchDate" name="punchDate"/></td>
				<td><input type="submit" value="Submit" id="getCharts" class="smallSubmit" /></td>
			</tr>
    	</table> 
    					 		
    	
	</form>
	</div>
</div>
	
<div id="wrapper" style="width: 95%; height:100%">

 <form method="post" name="qtySnfProcValidations" id="qtySnfProcValidations"> 
 	
 </form>
</div>
 <div name ="qtySnfValidationMsg" id="qtySnfValidationMsg">      
    </div>
<div id="div2" style="float: left;width: 100%;align:right; border: #F97103 solid 0.1em;">
  <#if qtySnfItemsJson?has_content>
    <div>    	
 		<div class="grid-header" style="width:100%">
			<label>Qty SNF FAT Validation Entries</label>
		</div>    
		<div id="itemGrid3" style="width:100%;height:350px;">
			
		</div>
		<div align="center">
		 <table width="60%" border="0" cellspacing="0" cellpadding="0">  
		    <tr><td></td><td></td></tr>
		    <tr><td></td><td></td></tr>
		    <tr><td>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" style="padding:.3em" name="changeSave" id="changeSave" value="Save" onclick="javascript:processQtySnfEntry('qtySnfProcValidations','<@ofbizUrl>updateQtySnf</@ofbizUrl>');" /></td>
		    <td><input type="button" style="padding:.3em" id="changeCancel" value="Cancel" onclick="javascript:processIndentEntry('qtySnfProcValidations','<@ofbizUrl>FindProcurementBillingValidation</@ofbizUrl>');"/>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;</td></tr>
		 </table>
	</div>
				
    </div>
   </#if> 
    <#if checkCodeItemsJson?has_content>
     <div>    	
 		<div class="grid-header" style="width:100%">
			<label>CheckCode Entries</label>
		</div>    
		<div id="itemGrid2" style="width:100%;height:300px;">
			
		</div>
		<div align="center">
		 <table width="60%" border="0" cellspacing="0" cellpadding="0">  
		    <tr><td></td><td></td></tr>
		    <tr><td></td><td></td></tr>
		    <tr><td>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" style="padding:.3em" name="changeSave" id="changeSave" value="Save" onclick="javascript:processIndentEntry('updateProcurementEntry','<@ofbizUrl>updateQtySnf</@ofbizUrl>');" /></td>
		    <td><input type="button" style="padding:.3em" id="changeCancel" value="Cancel" onclick="javascript:processIndentEntry('updateProcurementEntry','<@ofbizUrl>FindProcurementBillingValidation</@ofbizUrl>');"/>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;</td></tr>
		 </table>
	</div>
				
    </div>
    </#if>
    <#if outLierItemsJson?has_content>
      <div>    	
 		<div class="grid-header" style="width:100%">
			<label>OutLier  Entries</label>
		</div>  
		
		<#--<div>
		 <table width="60%" border="0" cellspacing="0" cellpadding="0"> 
		 	<tr> 
			<td align="right">Select Top 4 <input type="checkbox" id="chkSelectAll" name="chkSelectAll" onclick="javascript:checkTopFourIds();"/></td>
			</tr>
		</table>
		</div>-->
		  
		<div id="itemGrid5" style="width:100%;height:350px;">
			
		</div>	
		<div align="center">
		 <table width="60%" border="0" cellspacing="0" cellpadding="0">  
		    <tr><td></td><td></td></tr>
		    <tr><td></td><td></td></tr>
		    <tr><td>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" style="padding:.3em" name="changeSave" id="changeSave" value="Save" onclick="javascript:processOutLierEntry('OutLierQtyEntry','<@ofbizUrl>updateOutLierSnf</@ofbizUrl>');" /></td>
		    <td><input type="button" style="padding:.3em" id="changeCancel" value="Cancel" onclick="javascript:processIndentEntry('OutLierQtyEntry','<@ofbizUrl>FindProcurementBillingValidation</@ofbizUrl>');"/>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;</td></tr>
		 </table>
	</div>			
    </div>
   </#if>
</div>
</div>
