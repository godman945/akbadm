<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告十日內即將下檔報表</h2>

<form action="adTemplateReport.html" method="post">

<table width="750">
    <tr>
        <td>經銷歸屬:
        	<select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部</option>
				<#if companyList?exists && (companyList?size>0)>
   	            	<#list companyList as company>
		  		    <option value="${company.customerInfoId}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId = company.customerInfoId>selected="selected" </#if>>${company.companyName}</option>
		  	    	</#list>
		  	    </#if>
		    </select>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="1000">
    <tr>
        <td class="td01" width="80">廣告開始</td>
        <td class="td01" width="80">廣告結束</td>
        <td class="td01" width="80">統一編號</td>
        <td class="td01" width="200">帳戶名稱</td>
        <td class="td01" width="200">廣告活動</td>
        <td class="td01" width="90">每日花費</td>
        <td class="td01" width="90">每日花費達成</td>
        <td class="td01" width="80">曝光數</td>
        <td class="td01" width="80">點選次數</td>
        <td class="td01" width="80">點選率</td>
        <td class="td01" width="90">平均點選出價</td>
        <td class="td01" width="80">費用</td>
    </tr>
    <#list dataList as data>
    <tr>
        <td class="td03">${data.startDate}</td>
        <td class="td03">${data.endDate}</td>
        <td class="td03">${data.taxId}</td>
        <td class="td02">${data.customerInfoName}</td>
        <td class="td02">${data.adActionName}</td>
        <td class="td04">${data.maxPrice}</td>
        <td class="td04">${data.arrivalRate!}</td>
        <td class="td04">${data.pvSum}</td>
        <td class="td04">${data.clkSum}</td>
        <td class="td04">${data.clkRate}</td>
        <td class="td04">${data.clkPriceAvg}</td>
        <td class="td04">${data.priceSum}</td>
    </tr>
    </#list>
</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
