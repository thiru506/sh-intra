<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>shopsmint</title>

    </head>
    <body>
    	<h1>Products List<h1>
		<table height="150" width="150" border="1">
			<tr>
			<#list resultMap.products as product>
				<td>
				<form method="post" action="/ecommerce/control/additem" style="margin: 0;">
              		<input type="hidden" name="add_product_id" value="${product.productId}"/>
              		<input type="text" size="5" name="quantity" value="1"/>
              		<input type="hidden" name="clearSearch" value="N"/>
              		<input type="hidden" name="mainSubmitted" value="Y"/>
              		<input type="submit">${product.productId} </input>
            	</form>
				</td>
			</#list>
			</tr>
		<table>
		
	</dody>
</html>