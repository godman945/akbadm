<#assign s=JspTaglibs["/struts-tags"]>

<table class="table01" width="950">
    <tr>
        <td class="td02">訂單筆數：${pfpOrderDetail?size!}筆</td>
        <td class="td02">付款成功：${B3Count!}筆</td>
        <td class="td02">未付款：${B1Count!}筆</td>
        <td class="td02">付款中：${B2Count!}筆</td>
        <td class="td02">付款失敗：${B4Count!}筆</td>
        <td class="td02">退款成功：${R3Count!}筆</td>
    </tr>
    
    <tr>
        <td class="td02">訂單總金額：${total!}元</td>
        <td class="td02">付款成功金額：${B3Total!}元</td>
        <td class="td02">未付款金額：${B1Total!}元</td>
        <td class="td02">付款中：${B2Total!}元</td>
        <td class="td02">付款失敗金額：${B4Total!}元</td>
        <td class="td02">退款成功金額：${R3Total!}元</td>
    </tr>
</table>
<br>
<table class="table01" width="950">
    <tr>
        <td class="td01">訂單日期</td>
        <td class="td01">訂單編號</td>
        <td class="td01">金流編號</td>
        <td class="td01">帳戶編號</td>
        <td class="td01">帳戶名稱</td>
        <td class="td01">金額</td>
        <td class="td01">稅金</td>
        <td class="td01">總金額</td>
        <td class="td01">訂單狀態</td>
        <td class="td01">付款時間</td>
        <td class="td01">訂單明細</td>
    </tr>
	<#list pfpOrderDetail as list>
    <tr>
        <td class="td03">${list.createDate?string("yyyy-MM-dd")!}</td>
        <td class="td03">${list.orderId!}</td>
        <td class="td03">${list.billingId!}</td>
        <td class="td03">${list.pfpCustomerInfo.customerInfoId!}</td>
        <td class="td03">${list.pfpCustomerInfo.customerInfoTitle!}</td>
        <td class="td03">${list.orderPrice!}</td>
        <td class="td03">${list.tax!}</td>
        <td class="td03">${list.orderPrice+list.tax!}</td>
        <td class="td03">
        <#if list.status[0..1] =="B1">
        	未付款
        <#elseif list.status[0..1] =="B2">
        	付款中
        <#elseif list.status[0..1] =="B3">
        	付款成功
        <#elseif list.status[0..1] =="B4" ||  list.status[0..1] =="B5">
        	付款失敗
        <#elseif list.status[0..1] =="R1">
        	退款審核中
        <#elseif list.status[0..1] =="R2">
        	退款中
        <#elseif list.status[0..1] =="R3">
        	退款成功
       	<#elseif list.status[0..1] =="R4">
        	退款失敗
        </#if>
        </td>
        <td class="td03">${list.updateDate?string("yyyy-MM-dd HH:mm:ss" )!}</td>
        <td class="td03"><input type="button" onclick="billingTransDetail('${list.billingId!}');" value="交易明細" target="_blank" /></td>
    </tr>
    </#list>
</table>

