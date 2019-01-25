<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />行動裝置成效</h2>

<form action="adActionReport.html" method="post">

<table width="750">
    <tr>
        <td>作業系統:
            <select id="adMobileOS" name="adMobileOS">
   	            <#list adOsMap?keys as skey>
		  		    <option value="${skey}" <#if adMobileOS?exists && adMobileOS = skey>selected="selected" </#if>>${adOsMap[skey]}</option>
		  	    </#list>
		    </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;帳戶序號:
            <input type="text" id="customerInfoId" name="customerInfoId" value="${customerInfoId!}">
        </td>
    </tr>
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()">
            &nbsp;
            <input type="button" value="清除" onclick="doClear()">
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()">
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="800">
    <tr>
        <td class="td01" width="50">排名</td>
        <td class="td01" width="110">帳戶名稱</td>
        <td class="td01" width="80">裝置系統</td>
        <td class="td01" width="80">曝光數</td>
        <td class="td01" width="80">點選次數</td>
        <td class="td01" width="80">點選率</td>
        <td class="td01" width="80">無效點擊次數</td>
        <td class="td01" width="80">平均點選出價</td>
        <td class="td01" width="80">費用</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <tr>
    	<#if (data.rowspan > 0)>
	    <#assign index = index+1>
        <td class="td03" rowspan="${data.rowspan!}">${index!}</td>
        <td class="td02" rowspan="${data.rowspan!}">${data.customerInfoName!}<br>(${data.customerInfoId!})</td>
    	</#if>
        <td class="td03"><a href="javascript:getDetail('${data.adOs!}','${data.customerInfoId!}');"><#if data.adOs = "">其他<#else>${data.adOs!}</#if></a></td>
        <td class="td04">${data.pvSum!}</td>
        <td class="td04">${data.clkSum!}</td>
        <td class="td04">${data.clkRate!}</td>
        <td class="td04">${data.invalidClkSum!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.priceSum!}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
