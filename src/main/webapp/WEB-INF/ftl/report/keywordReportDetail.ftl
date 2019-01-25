<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />關鍵字明細表</h2>

<#if dataList?exists && (dataList?size>0)>

<form action="downloadKeywordDetail.html" method="post">
    <input type="hidden" name="keywordType" value="${keywordType!}">
    <input type="hidden" name="sortMode" value="${sortMode!}">
    <input type="hidden" name="displayCount" value="${displayCount!}">
    <input type="hidden" name="startDate" value="${startDate!}">
    <input type="hidden" name="endDate" value="${endDate!}">
    <input type="hidden" name="keyword" value="${keyword!}">
    <input type="submit" value="列印">
</form>

<table class="table01" width="820">
    <tr>
        <td class="td01" width="30">排名</td>
        <td class="td01" width="150">會員帳號</td>
        <td class="td01" width="80">帳戶名稱</td>
        <td class="td01" width="80">廣告群組</td>
        <td class="td01" width="80">廣告活動</td>
        <td class="td01" width="80">關鍵字</td>
        <td class="td01" width="60">曝光數</td>
        <td class="td01" width="60">點選次數</td>
        <td class="td01" width="60">點選率</td>
        <td class="td01" width="80">平均點選出價</td>
        <td class="td01" width="60">費用</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td03">${data.customerId!}</td>
        <td class="td02">${data.customerName!}</td>
        <td class="td02">${data.adGroup!}</td>
        <td class="td02">${data.adAction!}</td>
        <td class="td02">${data.keyword!}</td>
        <td class="td04">${data.kwPvSum!}</td>
        <td class="td04">${data.kwClkSum!}</td>
        <td class="td04">${data.kwClkRate!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.kwPriceSum!}</td>
    </tr>
    </#list>

</table>

</#if>
