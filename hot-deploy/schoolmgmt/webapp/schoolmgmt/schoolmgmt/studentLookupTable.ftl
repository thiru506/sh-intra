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

var dataSet = ${StringUtil.wrapString(studentsJSON!'[]')};

$(document).ready(function() {
    
    $('#example tfoot th').each( function () {
        var title = $('#example thead th').eq( $(this).index() ).text();
        $(this).html( '<input type="text" placeholder="Search '+title+'" />' );
    } );
    
	var table = $('#example').DataTable( {
		"data": dataSet,
		"columns": [
			{ "title": "Student Name" },
			{ "title": "Ro.Number" },
			{ "title": "Father Name" },
			{ "title": "Blood Group" },
			{ "title": "BirthDate" },			
			{ "title": "MobileNumber" }],
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
                	"sFileName": "VehilecList.csv"                 	
                },
                { 
                	"sExtends": "pdf", 
                	"oSelectorOpts": { filter: 'applied', order: 'current' },
                	"sFileName": "VehilecList.pdf"   
                } 
                ]
            },
		"iDisplayLength" : 25,		
     	"fnRowCallback": function(nRow, aData, iDisplayIndex ) {
     		<#if security.hasEntityPermission("SCHOOLMGMT", "_ADMIN", session)>
		    $('td:eq(0)', nRow).html('<a href="PartyView?partyId=' + aData[1] + '">' +
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
                <th>Student Name</th>
                <th>Ro.Number</th>
                <th>Father Name</th>
                <th>Blood Group</th>
                <th>Birth Date</th>
                <th>MobileNumber</th>
            </tr>
        </thead>
 		<tbody></tbody>
        <tfoot>
             <tr>
                <th>Student Name</th>
                <th>Ro.Number</th>
                <th>Father Name</th>
                <th>Blood Group</th>
                <th>Birth Date</th>
                <th>MobileNumber</th>
            </tr>
        </tfoot>
		</table>
</div>