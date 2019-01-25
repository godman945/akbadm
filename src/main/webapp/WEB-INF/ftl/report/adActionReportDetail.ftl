<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />總廣告成效明細表</h2>

<#if dataList?exists && (dataList?size>0)>

<form action="downloadAdActionDetail.html" method="post">
    <input type="hidden" name="reportDate" value="${reportDate!}">
    <input type="hidden" name="adType" value="${adType!}">
    <input type="hidden" name="customerInfoId" value="${customerInfoId!}">
    <input type="hidden" name="pfdCustomerInfoId" value="${pfdCustomerInfoId!}">
    <input type="hidden" name="payType" value="${payType!}">
    <input type="submit" value="列印">
</form>

<table class="table01" width="100%">
    <tr>
        <td class="td01" width="60">日期</td>
        <td class="td01" width="100">經銷商</td>
        <td class="td01" width="100">業務</td>
        <td class="td01" width="100">會員帳戶</td>
        <td class="td01" width="100">廣告活動</td>
        <td class="td01" width="50">曝光數</td>
        <td class="td01" width="50">互動數</td>
        <td class="td01" width="50">互動率</td>
        <td class="td01" width="60">單次互動費用</td>
        <td class="td01" width="60">千次曝光費用</td>
        <td class="td01" width="50">費用</td>
        <td class="td01" width="50">超播金額</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03">${reportDate!}</td>
        <td class="td02">${data.pfdCustomerInfoTitle!}<br>${data.pfdCustomerInfoId!}</td>
        <td class="td02">${data.pfdUserName!}<br>${data.pfdUserId!}</td>
        <td class="td02">${data.customerInfoName!}<br>${data.customerInfoId!}</td>
        <td class="td02">${data.adActionName!}</td>
        <td class="td04">${data.pvSum!}</td>
        <td class="td04">${data.clkSum!}</td>
        <td class="td04">${data.clkRate!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.pvPriceAvg!}</td>
        <td class="td04">${data.priceSum!}</td>
        <td class="td04">${data.overPriceSum!}</td>
    </tr>
    </#list>

</table>

</#if>
