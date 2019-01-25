<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />關鍵字成效報表</h2>

<form action="keywordReport.html" method="post">

<table width="750">
    <tr>
        <td>廣告型式:
            <select id="keywordType" name="keywordType">
                <option value="">全部</option>
   	            <#list keywordTypeSelectOptionsMap?keys as skey>
		  		    <option value="${skey}" <#if keywordType?exists && keywordType = skey>selected="selected" </#if>>${keywordTypeSelectOptionsMap[skey]}</option>
		  	    </#list>
		    </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;排序方式:
            <select id="sortMode" name="sortMode">
   	            <#list sortModeMap?keys as skey>
		  		    <option value="${skey}" <#if sortMode?exists && sortMode = skey>selected="selected" </#if>>${sortModeMap[skey]}</option>
		  	    </#list>
		    </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;顯示:前
            <select id="displayCount" name="displayCount">
   	            <#list displayCountMap?keys as skey>
		  		    <option value="${skey}" <#if displayCount?exists && displayCount = skey>selected="selected" </#if>>${displayCountMap[skey]}</option>
		  	    </#list>
		    </select>名
            &nbsp;&nbsp;經銷歸屬:
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
    <tr><td></td></tr>
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="90%">
    <tr>
        <td class="td01" width="50">排名</td>
        <td class="td01" width="100">關鍵字</td>
        <td class="td01" width="100">曝光數</td>
        <td class="td01" width="100">點選次數</td>
        <td class="td01" width="100">點選率</td>
        <td class="td01" width="100">平均點選出價</td>
        <td class="td01" width="100">費用</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td02">&nbsp; <a href="javascript:kwDetail('${data.keyword!}');">${data.keyword!}</a></td>
        <td class="td04">${data.kwPvSum!}</td>
        <td class="td04">${data.kwClkSum!}</td>
        <td class="td04">${data.kwClkRate!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.kwPriceSum!}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
