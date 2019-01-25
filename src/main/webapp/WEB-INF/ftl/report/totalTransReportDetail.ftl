<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />總廣告成效明細表</h2>

<#if dataList?exists && (dataList?size>0)>

<form action="downloadTotalTransDetail.html" method="post">
    <input type="hidden" name="reportDate" value="${reportDate!}">
    <input type="submit" value="列印">
</form>

<table class="table01" width="800">
    <tr>
        <td class="td01" width="80">日期</td>
        <td class="td01" width="80">會員帳號</td>
        <td class="td01" width="80">帳戶名稱</td>
        <td class="td01" width="80">帳戶儲值</td>
        <td class="td01" width="80">營業稅</td>
        <td class="td01" width="80">廣告活動花費</td>
        <td class="td01" width="80">惡意點擊費用</td>
        <td class="td01" width="80">退款</td>
        <td class="td01" width="80">免費贈送</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03">${data.reportDate!}</td>
        <td class="td03">${data.customerInfoId!}</td>
        <td class="td02">${data.customerInfoName!}</td>
        <td class="td04">${data.add?string('###,###,###.##')}</td>
        <td class="td04">${data.tax?string('###,###,###.##')}</td>
        <td class="td04">${data.spend?string('###,###,###.##')}</td>
        <td class="td04">${data.invalid?string('###,###,###.##')}</td>
        <td class="td04">${data.refund?string('###,###,###.##')}</td>
        <td class="td04">${data.free?string('###,###,###.##')}</td>
    </tr>
    </#list>

    <tr>
        <td class="td03" colspan="3">小計</td>
        <td class="td04">${add_sum?string('###,###,###.##')}</td>
        <td class="td04">${tax_sum?string('###,###,###.##')!}</td>
        <td class="td04">${spend_sum?string('###,###,###.##')}</td>
        <td class="td04">${invalid_sum?string('###,###,###.##')}</td>
        <td class="td04">${refund_sum?string('###,###,###.##')}</td>
        <td class="td04">${free_sum?string('###,###,###.##')}</td>
    </tr>

</table>

</#if>
