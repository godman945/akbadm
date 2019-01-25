<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />當日攤提明細表</h2>

<table class="table01" width="500">

    <tr>
        <td class="td01"></td>
        <td class="td01">筆數</td>
        <td class="td01">總金額(未稅)</td>
        <td class="td01">總花費(未稅)</td>
        <td class="td01">總剩餘(未稅)</td>
        <td class="td01">總退額金額(未稅)</td>
        
        <td class="td01">總金額(含稅)</td>
        <td class="td01">總花費(含稅)</td>
        <td class="td01">總剩餘(含稅)</td>
        <td class="td01">總退額金額(含稅)</td>
    </tr>
    <#list vos as vo>
		<tr>
	        <td class="td01">${vo.typeChName!}</td>
	        <td class="td03">${vo.amount!}</td>
	        <td class="td04">${vo.totalSave?string('#,###')!}</td>
	        <td class="td04">${vo.totalSpend?string('#,###')!}</td>
	        <td class="td04">${vo.totalRemain?string('#,###')!}</td>
	        <td class="td04">${vo.totalRefundPrice?string('#,###')!}</td>
	        
	        <td class="td04">${vo.totalTaxSave?string('#,###')!}</td>
	        <td class="td04">${vo.totalTaxSpend?string('#,###')!}</td>
	        <td class="td04">${vo.totalTaxRemain?string('#,###')!}</td>
	        <td class="td04">${vo.totalRefundPriceTax?string('#,###')!}</td>
	    </tr>
	</#list>
    <tr>
        <td class="td01">加總</td>
        <td class="td03">${totalAmount!}</td>
        <td class="td04">${totalSave?string('#,###')!}</td>
        <td class="td04">${totalSpend?string('#,###')!}</td>
        <td class="td04">${totalRemain?string('#,###')!}</td>
        <td class="td04">${totalRefundPrice?string('#,###')!}</td>
        
        <td class="td04">${totalTaxSave?string('#,###')!}</td>
        <td class="td04">${totalTaxSpend?string('#,###')!}</td>
        <td class="td04">${totalTaxRemain?string('#,###')!}</td>
         <td class="td04">${totalRefundPriceTax?string('#,###')!}</td>
    </tr>

</table>

<br>

<table class="table01" width="800">
    <tr>
        <td class="td01">攤提日期</td>
        <td class="td01">帳戶編號</td>
        <td class="td01">訂單編號</td>
        <td class="td01">訂單類型</td>
        <td class="td01">訂單金額(未稅)</td>
        <td class="td01">廣告費用(不含惡意點擊費用)(未稅)</td>
        <td class="td01">退款金額</td>
        <td class="td01">剩餘金額(未稅)</td>
        <td class="td01">訂單金額(含稅)</td>
        <td class="td01">廣告費用(不含惡意點擊費用)(含稅)</td>
        <td class="td01">剩餘金額(含稅)</td>
        <td class="td01">備註說明</td>
    </tr>
    <#list detailVos as vo>
    	<tr>
	        <td class="td03">${vo.spendDate!}</td>
	        <td class="td03">${vo.pfpId!}</td>
	        <td class="td03">${vo.orderId!}</td>
	        <td class="td03">${vo.typChName!}</td>
	        <td class="td04">${vo.orderPrice?string('#,###')!}</td>
	        <td class="td04">${vo.spendCost?string('#,###')!}</td>
	        <td class="td04">${vo.refund?string('#,###')!}</td>
	        <td class="td04">${vo.remain?string('#,###')!}</td>	        
	        <td class="td04">${vo.orderTaxPrice?string('#,###')!}</td>
	        <td class="td04">${vo.spendTaxCost?string('#,###')!}</td>
	        <td class="td04">${vo.taxRemain?string('#,###')!}</td>
	        <td class="td02">${vo.note!}</td>
	    </tr>
    </#list>
</table>
