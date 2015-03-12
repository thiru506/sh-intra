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

<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/jquery-ui-1.11.4/external/jquery/jquery.js</@ofbizContentUrl>"></script>
<script  language="javascript" type="text/javascript" src="<@ofbizContentUrl>/images/jquery/jquery-ui-1.11.4/jquery-ui.js</@ofbizContentUrl>"></script>
<script type="text/javascript">
  $(function() {
    var dialog, form,
 
      emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
      name = $( "#name" ),
      email = $( "#email" ),
      password = $( "#password" ),
      allFields = $( [] ).add( name ).add( email ).add( password ),
      tips = $( ".validateTips" );
 
    function updateTips( t ) {
      tips
        .text( t )
        .addClass( "ui-state-highlight" );
      setTimeout(function() {
        tips.removeClass( "ui-state-highlight", 1500 );
      }, 500 );
    }
 
    function checkLength( o, n, min, max ) {
      if ( o.val().length > max || o.val().length < min ) {
        o.addClass( "ui-state-error" );
        updateTips( "Length of " + n + " must be between " +
          min + " and " + max + "." );
        return false;
      } else {
        return true;
      }
    }
 
    function checkRegexp( o, regexp, n ) {
      if ( !( regexp.test( o.val() ) ) ) {
        o.addClass( "ui-state-error" );
        updateTips( n );
        return false;
      } else {
        return true;
      }
    }
 
    function createVehicleRoute() {
      var valid = true;
      allFields.removeClass( "ui-state-error" );
     
     /* 
      valid = valid && checkLength( email, "email", 6, 80 );
       valid = valid && checkRegexp( email, emailRegex, "eg. ui@jquery.com" );
     valid = valid && checkLength( name, "username", 3, 16 );
      
      valid = valid && checkLength( password, "password", 5, 16 );
 
      valid = valid && checkRegexp( name, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter." );
     
      valid = valid && checkRegexp( password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" ); */
 
      if ( valid ) {
        console.log("==========="+jQuery("#routeName").val());
        //here add ajax submit if need be
        jQuery("#createVehicleRoute").submit();
        dialog.dialog( "close" );
      }
      return valid;
    }
 
    dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 300,
      width: 350,
      modal: true,
      buttons: {
        "Create an account": createVehicleRoute,
        Cancel: function() {
          dialog.dialog( "close" );
        }
      },
      close: function() {
        form[ 0 ].reset();
        allFields.removeClass( "ui-state-error" );
      }
    });
 
    form = dialog.find( "form" ).on( "submit", function( event ) {
     //if ajax submit un-comment prevent default
     // event.preventDefault();
      
    });
 
    $( "#vehicleRoute" ).button().on( "click", function() {
      dialog.dialog( "open" );
    });
  });
</script>
<div id="dialog-form" title="create Vehicle Route">
	<form name="createVehicleRoute" id="createVehicleRoute" method="post" action="createVehicleRoute">
	    <table class="basic-table hover-bar" cellspacing="0">
	       <tr>
	         <td><b>Route Name :</b></td>  <td><input type="text" name="routeName" id="routeName" value=""></td>
	          <td><b>Vehicle Number :</b></td>  <td><input type="text" name="vehicleNumber" id="vehicleNumber" value=""></td>
	       </tr>
	       <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
	    </table>
	</form>
</div>
<button id="vehicleRoute">create Vehicle Route</button>

