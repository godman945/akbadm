<#assign s=JspTaglibs["/struts-tags"]>

<#if (recognizeRecords?exists) && (recognizeRecords?size>0) >
<table class="table01" width="800">
    <tr>
        <td class="td01" width="100">儲值日期</td>
        <td class="td01" width="150">會員帳號</td>
        <td class="td01" width="150">帳戶名稱</td>
        <td class="td01" width="100">儲值類型</td>
        <td class="td01" width="100">儲值金額</td>
        <td class="td01" width="100">稅金</td>
        <td class="td01" width="100">總金額</td>
        <td class="td01" width="150">經銷歸屬</td>
        <td class="td01" width="150">業務歸屬</td>
        <td class="td01" width="100">付款方式</td>
    </tr>
    <#list recognizeRecords as record>
    <tr>
        <td class="td03">${record.saveDate!}</td>
        <td class="td02">${record.memberId!}</td>
        <td class="td02">${record.customerInfoTitle!}</td>
        <td class="td03">${record.orderType!}</td>
        <td class="td04">${record.orderPrice!}</td>
        <td class="td04">${record.tax!}</td>
        <td class="td04">${record.totalOrderPrice!}</td>
        <td class="td02">${record.pfdCustInfoTitle!}</td>
        <td class="td02">${record.pfdUserName!}</td>
        <td class="td03">${record.payType!}</td>
    </tr>
    </#list>
</table>

<#else>

    ${message!}

</#if>
