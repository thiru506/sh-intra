
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
	function setUpAttendanceGrid(jsonData) {
			var grid;
			data1=jsonData
			var columns = [
				{id:"rollNumber", name:"Roll Number", field:"rollNumber", width:100, minWidth:100, cssClass:"cell-title", sortable:true},
				{id:"name", name:"Name", field:"name", width:200, minWidth:200, cssClass:"cell-title", sortable:true},
				{id:"subjectName", name:"Subject Name", field:"subjectName", width:200, minWidth:200, cssClass:"cell-title", sortable:true},
				{id:"marks", name:"Marks", field:"marks", width:200, minWidth:200, cssClass:"cell-title",editor:FloatCellEditor },
			];
	       columns.push({id:"check", name:"Attempted", field:"check", width:100, minWidth:100, cssClass:"cell-title", formatter:BoolCellFormatter, editor:YesNoSelectCellEditor, sortable:false});
	
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
				     if(cell && cell.cell == 3){
				          $(grid.getCellNode((cell.row+1),3)).click();
				          grid.getEditController().commitCurrentEdit();
				     
				     }else{
					      editClickHandler(cell.row);
					      approveClickHandler(cell.row);
					      $(grid.getCellNode(cell.row +1, 3)).click();
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
		<#if jsonData?has_content>
		 dataJson = ${StringUtil.wrapString(jsonData)!'[]'}
		 setUpAttendanceGrid(dataJson);
		</#if>  
	    $('#ui-datepicker-div').css('clip', 'auto');
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


   var dataString;
	function processMarksEntry() {
		if (Slick.GlobalEditorLock.isActive() && !Slick.GlobalEditorLock.commitCurrentEdit()) {
			return false;		
		}
      		var customTimePeriodId = "${customTimePeriodId}";
			var fromProductId = "${fromProductId}";
			var examTypeId = "${examTypeId}";

		for (var rowCount=0; rowCount < data1.length; ++rowCount)
		{
		
			var partyId = data1[rowCount]["partyId"];
			var check = data1[rowCount]["check"];
			var subjectId = data1[rowCount]["subjectId"];
			var marks = data1[rowCount]["marks"];
			var attempted;
			if(check){
				attempted ="Y";
			}else{
				attempted ="N";
			}
				var inputPartyId = jQuery("<input>").attr("type", "hidden").attr("name", "partyId_o_" + rowCount).val(partyId);
               var inputSubjectId = jQuery("<input>").attr("type", "hidden").attr("name", "subjectId_o_" + rowCount).val(subjectId);
               var inputMarks = jQuery("<input>").attr("type", "hidden").attr("name", "marks_o_" + rowCount).val(marks);
               var inputAttempt = jQuery("<input>").attr("type", "hidden").attr("name", "attempted_o_" + rowCount).val(attempted);
				jQuery("#pramoteStudentsForm").append(jQuery(inputPartyId));
				jQuery("#pramoteStudentsForm").append(jQuery(inputSubjectId));
				jQuery("#pramoteStudentsForm").append(jQuery(inputMarks));
				jQuery("#pramoteStudentsForm").append(jQuery(inputAttempt));
			
		}
		var action ="processStudentsMarks";
        var inputCustTimePeriodId = jQuery("<input>").attr("type", "hidden").attr("name", "customTimePeriodId").val(customTimePeriodId);
		var inputFromProdId = jQuery("<input>").attr("type", "hidden").attr("name", "fromProductId").val(fromProductId);
		var inputExamTypeId = jQuery("<input>").attr("type", "hidden").attr("name", "examTypeId").val(examTypeId);
        jQuery("#pramoteStudentsForm").append(jQuery(inputCustTimePeriodId));
		jQuery("#pramoteStudentsForm").append(jQuery(inputFromProdId));
		jQuery("#pramoteStudentsForm").append(jQuery(inputExamTypeId));
		dataString = jQuery("#pramoteStudentsForm").serializeArray();
		// lets make the ajaxform submit
		$.ajax({
             type: "POST",
             url: action,
             async:false,
             data: dataString,
             dataType: 'json',
             success: function(result) { 
             	//$(formId+' input').remove()            	
	               if(result["_ERROR_MESSAGE_"] || result["_ERROR_MESSAGE_LIST_"]){               	   
	            	    $('div#updateEntryMsg').html();
	            	    $('div#updateEntryMsg').removeClass("messageStr");            	 
	            	    $('div#updateEntryMsg').addClass("errorMessage");
	            	    $('div#updateEntryMsg').html('<label>'+ result["_ERROR_MESSAGE_"]+result["_ERROR_MESSAGE_LIST_"]+'</label>');
	            	    $('div#updateEntryMsg').delay(6000).fadeOut('slow');  
	               }else{
	               		
	               		$("div#updateEntryMsg").fadeIn();               	         	   
	            	    $('div#updateEntryMsg').html(); 
	            	    $('div#updateEntryMsg').removeClass("errorMessage");           	 
	            	    $('div#updateEntryMsg').addClass("messageStr");
	            	    $('div#updateEntryMsg').html('<label>Marks succesfully updated.</label>'); 
	            	    $('div#updateEntryMsg').delay(5000).fadeOut('slow');  
	            	   cleanUpGrid(); 
	               }
               
               },
             error: function() {
            	 	//alert("record not updated");
            	 }
           });
			
	}
    
    function cleanUpGrid(){
		
		var emptyJson= [];
		setUpAttendanceGrid(emptyJson) ;
	}
   
   
</script>

 <div name ="updateEntryMsg" id="updateEntryMsg">      
    </div>
 <div>
 	<form name="pramoteStudentsForm" id="pramoteStudentsForm"/>
 </div>
<div class="screenlet">   
<div id="div2" style="float: left;width: 100%;align:left; border: #F97103 solid 0.1em;">
  <#if jsonData?has_content>
    <div>    	
 		<div class="grid-header" style="width:100%">
			<label>Student Marks Entry</label>
		</div>    
		<div id="itemGrid3" style="width:100%;height:350px;">
			
		</div>
		<div align="center">
		 <table width="50%" border="0" cellspacing="0" cellpadding="0" align="left">  
		    <tr><td></td><td></td></tr>
		    <tr><td></td><td></td></tr>
		    <tr><td>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" style="padding:.3em" name="changeSave" id="changeSave" <#if flag=="promoteStudents">value="Submint"<#else>value="Save"</#if> onclick="javascript:processMarksEntry();" /></td>
		    <td><input type="button" style="padding:.3em" id="changeCancel" value="Cancel" onclick="javascript:cleanUpGrid();"/>  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;</td></tr>
		 <tr><td></td><td></td></tr>
		 </table>
	</div>
				
    </div>
   </#if> 
</div>
</div>
</div>

