<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<@t.insertAttribute name="page" />
</br>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="100">日期</td>
        <td class="td01" width="100">會員帳號</td>
        <td class="td01" width="50">帳戶名稱</td>
        <td class="td01" width="50">項目說明</td>
        <td class="td01" width="50">總額</td>
        <td class="td01" width="70">營業稅</td>
        <td class="td01" width="70">廣告費用</td>
        <td class="td01" width="70">餘額</td>
    </tr>
<#if transDetails?exists>
	
	<#list transDetails as list>
		<tr>
	        <td class="td03" width="100">${list.transDate!}</td>
	        <td class="td03" width="100">${list.pfpCustomerInfo.memberId!}</td>
	        <td class="td03" width="50">${list.pfpCustomerInfo.customerInfoTitle!}</td>
	        <td class="td03" width="50">${list.transContent!}</td>
	        <td class="td03" width="50">${list.orderPrice!}</td>
	        <td class="td03" width="70">${list.tax!}</td>
	        <td class="td03" width="70">${list.expense!}</td>
	        <td class="td03" width="70">${list.remain!}</td>
    	</tr>
	</#list>
</#if>

</table>