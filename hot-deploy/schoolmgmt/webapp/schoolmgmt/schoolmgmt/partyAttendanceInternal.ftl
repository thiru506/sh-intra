<style type="text/css">
		
	.dataTables_filter input {
  		border-style: groove;    
	}
	 #datatable1_filter,  #datatable1_length, #datatable1_info, #datatable1_paginate { 
	 }
	 #datatable2_filter,  #datatable2_length, #datatable2_info, #datatable2_paginate { 
	 }
</style>	


<script type="text/javascript" language="javascript" class="init">	

$(document).ready(function() {

	// attendance table 
	var attendanceTable = ${StringUtil.wrapString(punchListJSON!'[]')};	 
	$('#attendanceTable').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="datatable1"></table>' );

	var datatable1 = $('#datatable1').dataTable( {
		"data": attendanceTable,
		"columns": [
			{ "title": "In Time" },			
			{ "title": "Out Time" },
			{ "title": "Duration (HH:MM)"}],	
		"columnDefs": [{ type: 'date-eu', targets: [0,1] }],
       	"iDisplayLength" : 100
	} );
	datatable1.fnSort( [ [0,'desc'] ] );		 	
	
	
	// holidays table
	var holidaysTable = ${StringUtil.wrapString(holidaysListJSON!'[]')};	 
	$('#holidaysTable').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="datatable4"></table>' );

	var datatable4 = $('#datatable4').dataTable( {
		"data": holidaysTable,
		"columns": [
			{ "title": "Date" },
			{ "title": "Holiday Description" }],	
		"columnDefs": [{ type: 'date-eu', targets: [0] }],
       	"iDisplayLength" : 100
	} );				
	datatable4.fnSort( [ [0,'desc'] ] );	
	
	// missed table
	var missedTable = ${StringUtil.wrapString(missedListJSON!'[]')};	 
	$('#missedTable').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="datatable5"></table>' );

	var datatable5 = $('#datatable5').dataTable( {
		"data": missedTable,
		"columns": [
			{ "title": "Date" }],	
		"columnDefs": [{ type: 'date-eu', targets: [0] }],
       	"iDisplayLength" : 100
	} );				
	datatable5.fnSort( [ [0,'desc'] ] );		 	

} );

</script>

<div style="width:800px; margin:0 auto; padding:20px">
<h2>Party Attendance Details for ${partyId}: ${partyName} [${fromDate?date} - ${thruDate?date}]</h2>	
</div>

<div class="container">
<div class="lefthalf">		
<div class="screenlet">
	<div class="screenlet-title-bar">
      	<h3>Daily Punch </h3>	
     </div>
    <div class="screenlet-body">
    	<div id="attendanceTable"/>
    </div>
</div>
 <div class="screenlet">  
    	<div class="screenlet-title-bar">
      		<h3>Holidays</h3>	
     	</div>
	    <div class="screenlet-body">
    		<div id="holidaysTable"/>  
    	</div>  	
    </div>
</div> 
<div class="container">
<div class="righthalf">	   
    <div class="screenlet">  
    	<div class="screenlet-title-bar">
      		<h3>Absent Days</h3>	
     	</div>
	    <div class="screenlet-body">
    		<div id="missedTable"/>  
    	</div>  	
    </div>          
</div>
</div> 
</div> 
   
