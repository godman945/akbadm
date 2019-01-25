<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告客戶成效查詢</h2>

<form action="adTimeReport.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		    <br/>
            <BR>查詢條件 : 
            <select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部</option>
                <#list pfdCustomerInfoIdMap?keys as skey>
		    		<option value="${skey}" <#if searchPfdCustomerInfoId?exists && searchPfdCustomerInfoId = skey>selected="selected" </#if>>${pfdCustomerInfoIdMap[skey]}</option>
		    	</#list>
		    </select>
		    <input type="hidden" id="searchPfdCustomerInfoId" value="${pfdCustomerInfoId!}" />
        	<select id="searchOption" name="searchOption">
                <option value="">全部</option>
                <option value="customerInfoId">帳戶編號</option>
                <option value="pfpCustomerInfoTitle">廣告客戶</option>
		    </select>
		    <input type="hidden" id="option" value="${searchOption!}" />
		    
		    <input type="text" id="searchText" name="searchText" value="${searchText!}" />
		    <br/>
		    <BR>裝置查詢 : 
		    <select id="searchAdDevice" name="searchAdDevice">
		    	<#list adDeviceMap?keys as key>
		  			<option value="${key}" <#if searchAdDevice?exists && searchAdDevice = key> selected</#if> >${adDeviceMap[key]}</option>
		  	    </#list>
		    </select>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="doDownlaod()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if voList?exists && (voList?size>0)>

<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>

<table id="tableView" class="table01 tablesorter">
    <thead>
	    <tr>
	    	<th class="td09">期間</td>
	        <th class="td09">帳戶編號</td>
	        <th class="td09">經銷商</td>
	        <th class="td09">廣告客戶</td>
	        <th class="td09">投放網站數</td>
	        <th class="td09">廣告曝光數</td>
	        <th class="td09">廣告互動數</td>
	        <th class="td09">廣告互動率</td>
	        <th class="td09">平均互動費用</td>
	        <th class="td09">千次曝光費用</td>
	        <th class="td09">廣告費用</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
		    <tr>
		        <td class="td09">${data.adPvclkDate}</td>
		        <td class="td09">${data.pfpCustomerInfoId}</td>
		        <td class="td09">${data.pfdCustomerInfoName}</td>
		        <td class="td09">${data.pfpCustomerInfoName}</td>
		        <td class="td09"><a onclick="fundDetalView('${data.pfpCustomerInfoId}','${data.adPvclkDate}','${data.pfpCustomerInfoName}');">${data.pfbCustomerInfoId}</a></td>
		        <td class="td10">${data.adPvSum}</td>
		        <td class="td10">${data.adClkSum}</td>
		        <td class="td10">${data.adClkRate}</td>
		        <td class="td10">$ ${data.adClkAvgPrice}</td>
		        <td class="td10">$ ${data.adPvRate}</td>
		        <td class="td10">$ ${data.adClkPriceSum}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="5" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalPv?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClk?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClkRate?string('0.00')!}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkAvgPrice?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdPvRate?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkPriceSum?string('#,###')!}</td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>
<div style="display:none;">
	<div id="adClientDetalDiv">
	
	</div>
</div>