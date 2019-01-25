<#assign s=JspTaglibs["/struts-tags"]>

<#if (dataList?exists) && (dataList?size>0) >
<table class="table01" width="900">
    <tr>
        <td class="td01" width="80">交易日期</td>
        <td class="td01" width="120">帳戶名稱</td>
        <td class="td01" width="120">帳戶編號</td>
        <td class="td01" width="70">儲值金額</td>
        <td class="td01" width="70">營業稅</td>
        <td class="td01" width="70">廣告花費</td>
        <td class="td01" width="70">惡意點擊費用</td>
        <td class="td01" width="70">退款</td>
        <td class="td01" width="70">禮金贈送</td>
        <td class="td01" width="70">帳戶餘額</td>
        <td class="td01" width="100">經銷歸屬</td>
        <td class="td01" width="100">業務歸屬</td>
        <td class="td01" width="100">付款方式</td>
    </tr>
    <#list dataList as trans>
    <tr>
        <td class="td03">${trans.transDate!}</td>
        <td class="td02">${trans.custName!}</td>
        <td class="td02">${trans.custId!}</td>
        <td class="td04">${trans.add_display!}</td>
        <td class="td04">${trans.tax_display!}</td>
        <td class="td04">${trans.spend_display!}</td>
        <td class="td04">${trans.invalid_display!}</td>
        <td class="td04">${trans.refund_display!}</td>
        <td class="td04">${trans.gift_display!}</td>
        <td class="td04">${trans.remain_display!}</td>
        <td class="td03">${trans.pfdCustInfoName!}</td>
        <td class="td03">${trans.pfdUserName!}</td>
        <td class="td03">${trans.payType!}</td>
    </tr>
    </#list>
    <tr>
        <td class="td03">小計：</td>
        <td class="td03"></td>
        <td class="td03"></td>
        <td class="td04">${totalAddMoney!}</td>
        <td class="td04">${totalTax!}</td>
        <td class="td04">${totalSpend!}</td>
        <td class="td04">${totalInvalidCost!}</td>
        <td class="td04">${totalRefund!}</td>
        <td class="td04">${totalGift!}</td>
        <td class="td03"></td>
        <td class="td03"></td>
        <td class="td03"></td>
        <td class="td03"></td>
	</tr>
</table>

<#else>

    ${message!}

</#if>
