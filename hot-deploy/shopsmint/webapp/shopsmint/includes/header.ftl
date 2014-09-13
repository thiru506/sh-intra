<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>shopsmint</title>

    </head>
    <body>
    <h1>shopsmint<h1>

<table height="150" width="150" border="1">
<tr>
<#list resultMap.merchants as merchant>
<td>
<a href="/shopsmint/control/products?productStoreId=${merchant.productStoreId}">${merchant.payToPartyId} </a>
</td>
</#list>
</tr>
<table>

</dody>
</html>