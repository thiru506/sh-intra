	<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/css/jquery.dataTables.css</@ofbizContentUrl>">
	<link rel="stylesheet" type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/extensions/TableTools/css/dataTables.tableTools.css</@ofbizContentUrl>">

	<style type="text/css" class="init">

	</style>
<style type="text/css">
.dataTables_filter input {
  border-style: groove;    
}
</style>	
	<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/jquery.js</@ofbizContentUrl>"></script>
	<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/jquery.dataTables.js</@ofbizContentUrl>"></script>
	<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/extensions/TableTools/js/dataTables.tableTools.js</@ofbizContentUrl>"></script>
	<script type="text/javascript" language="javascript" src="<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/media/js/dataTables.plugins.js</@ofbizContentUrl>"></script>


	<script type="text/javascript" language="javascript" class="init">	

var dataSet = ${StringUtil.wrapString(facilityListJSON!'[]')};

$(document).ready(function() {
    
    $('#example tfoot th').each( function () {
        var title = $('#example thead th').eq( $(this).index() ).text();
        $(this).html( '<input type="text" placeholder="Search '+title+'" />' );
    } );
    
	var table = $('#example').DataTable( {
		"data": dataSet,
		"columns": [
			{ "title": "Dormitary Name" },
			{ "title": "Type" },
			{ "title": "Main Building" },
			{ "title": "Open Date" },
			{ "title": "Capacity" },
			{ "title": "Filled" },
			{ "title": "Warden Name" },			
			{ "title": "Warden MobileNumber" }],
			"columnDefs": [{ type: 'date-eu', targets: 4 }],			
            "sDom": 'lfTrtip',
            "tableTools": {
                "sSwfPath": "<@ofbizContentUrl>/images/jquery/plugins/datatables/1.10.0/extensions/TableTools/swf/copy_csv_xls_pdf.swf</@ofbizContentUrl>",
                "aButtons": [ 
                { 
                	"sExtends": "copy", 
                	"oSelectorOpts": { filter: 'applied', order: 'current' }
                },
                { 
                	"sExtends": "csv", 
                	"oSelectorOpts": { filter: 'applied', order: 'current' },
                	"sFileName": "DormitaryList.csv"                 	
                },
                { 
                	"sExtends": "pdf", 
                	"oSelectorOpts": { filter: 'applied', order: 'current' },
                	"sFileName": "DormitaryList.pdf"   
                } 
                ]
            },
		"iDisplayLength" : 20,		
     	"fnRowCallback": function(nRow, aData, iDisplayIndex ) {
     		<#if security.hasEntityPermission("SCHOOLMGMT", "_ADMIN", session)>
		    $('td:eq(0)', nRow).html('<a href="VehicleView?facilityId=' + aData[8] + '">' +
                aData[0] + '</a>');
            </#if> 
            return nRow;
			
		}
	} );	
	
	table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', table.column( colIdx ).footer() ).on( 'keyup change', function () {
            table
                .column( colIdx )
                .search( this.value )
                .draw();
        } );
    } );
	
} );


	</script>
		
		<#if security.hasEntityPermission("SCHOOLMGMT", "_ADMIN", session)>
		<div id="demo">
		<div align = "right">
        
        <br class="clear"/>
    	</div>
    	</#if>
		<table id="example" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Dormitary Name</th>
                <th>Type</th>
                <th>Main Building</th>
                 <th>Open Date</th>
                <th>Capacity</th>
                <th>Filled</th>
                <th>Warden Name</th>
                <th>Warden MobileNumber</th>
            </tr>
        </thead>
 		<tbody></tbody>
        <tfoot>
            <tr>
                <th>Dormitary Name</th>
                <th>Type</th>
                <th>Main Building</th>
                 <th>Open Date</th>
                <th>Capacity</th>
                <th>Filled</th>
                <th>Warden Name</th>
                <th>Warden MobileNumber</th>
            </tr>
        </tfoot>
		</table>
		</div>