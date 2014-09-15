<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>shopsmint</title>

    </head>
    <script type="text/javascript">
    var host = document.domain;
	url = "https://"+host+":8443/ecommerce/control/additem";
	function submit(){
	document.forms[0].action = url;
	document.forms[0].submit();
	}
	</script>
    <body>
    	<h1>Products List<h1>
		<table height="150" width="150" border="1">
			<tr>
			<#list resultMap.products as product>
				<td>
				<form method="post" style="margin: 0;">
              		<input type="hidden" name="add_product_id" value="${product.productId}"/>
              		<input type="text" size="5" name="quantity" value="1"/>
              		<input type="hidden" name="clearSearch" value="N"/>
              		<input type="hidden" name="mainSubmitted" value="Y"/>
              		${product.productId}
              		<a href="#" onclick="javascript:submit()">Add To Cart </a>
            	</form>
				</td>
			</#list>
			</tr>
		<table>
		
	</dody>
</html>