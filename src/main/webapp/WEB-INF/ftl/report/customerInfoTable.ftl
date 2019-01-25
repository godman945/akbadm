<#assign s=JspTaglibs["/struts-tags"]>

<#if (customerInfoReportVOs?exists) && (customerInfoReportVOs?size>0) >
<table class="table01" width="800">
    <tr>
        <td class="td01" width="100">開通日期</td>
        <td class="td01" width="150">會員帳號</td>
        <td class="td01" width="150">帳戶編號</td>
        <td class="td01" width="150">帳戶名稱</td>
        <td class="td01" width="100">帳戶類別</td>
        <td class="td01" width="100">儲值類型</td>
        <td class="td01" width="100">儲值金額</td>
        <td class="td01" width="100">稅金</td>
        <td class="td01" width="100">總金額</td>
        <td class="td01" width="100">經銷歸屬</td>
        <td class="td01" width="100">業務歸屬</td>
        <td class="td01" width="100">付款方式</td>
    </tr>
    <#list customerInfoReportVOs as info>
    <tr>
        <td class="td03">${info.activateDate!}</td>
        <td class="td02">${info.memberId!}</td>
        <td class="td03">${info.customerInfoId!}</td>
        <td class="td02">${info.customerInfoName!}</td>
        <td class="td03">${info.customerInfoType!}</td>
        <td class="td03">${info.transContent!}</td>
        <td class="td04">${info.addMoney?string("###,###,###,###")!}</td>
        <td class="td04">${info.tax?string("###,###,###,###")!}</td>
        <td class="td04">${(info.addMoney+info.tax)?string("###,###,###,###")!}</td>
        <td class="td03">${info.pfdCustInfoName!}</td>
        <td class="td03">${info.pfdUserName!}</td>
        <td class="td03">${info.payType!}</td>
    </tr>
    </#list>
</table>

<#else>

    ${message!}

</#if>
