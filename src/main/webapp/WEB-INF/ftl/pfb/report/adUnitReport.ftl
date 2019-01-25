<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />單元成效查詢</h2>

<form action="adTimeReport.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
            <BR>查詢條件 :
        	<select id="searchOption" name="searchOption">
                <option value="">&nbsp;</option>
                <option value="customerInfoId">會員編號</option>
                <option value="websiteChineseName">公司名稱</option>
                <option value="unit">單元</option>
		    </select>
		    
		    <input type="text" id="searchText" name="searchText" />
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if voList?exists && (voList?size>0)>

<table class="table01" width="950">
    <tr>
        <td class="td01">會員編號</td>
        <td class="td01">網站名稱</td>
    	<td class="td01">單元</td>
        <td class="td01">曝光數</td>
        <td class="td01">點擊數</td>
        <td class="td01">點擊率</td>
        <td class="td01">千次曝光出價</td>
        <td class="td01">平均點選出價</td>
        <td class="td01">總廣告花費</td>
    </tr>
    <#list voList as data>
    <tr>
        <td class="td03">${data.customerInfoId}</td>
        <td class="td03">${data.websiteChineseName}</td>
    	<td class="td03">${data.adPvclkUnit}</td>
        <td class="td02">${data.adPvSum}</td>
        <td class="td02">${data.adClkSum}</td>
        <td class="td02">${data.adClkRate}</td>
        <td class="td02">${data.adPvRate}</td>
        <td class="td02">${data.adClkAvgPrice}</td>
        <td class="td02">${data.adClkPriceSum}</td>
    </tr>
    </#list>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<#--
	<input type="hidden" id="fstartDate" name="startDate" value="${startDate}">
	<input type="hidden" id="fendDate" name="endDate" value="${endDate}">
	<input type="hidden" id="fperiod" name="period" value="${period}">
-->
</form>
