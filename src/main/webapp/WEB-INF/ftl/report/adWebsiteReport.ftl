<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />網站類型成效查詢</h2>

<form action="adWebsiteSearch.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		    <br/>
            <BR>查詢條件 : 
        	<select id="searchCategory" name="searchCategory">
                <option value="">全部類型</option>
                <#list websiteCategoryMap?keys as key>
		  			<option value="${key}" <#if searchCategory?exists && searchCategory = key> selected</#if> >${websiteCategoryMap[key]}</option>
		  	    </#list>
		    </select>
		    <input type="hidden" id="category" value="${searchCategory!}" />
            <select id="searchAdDevice" name="searchAdDevice">
		    	<#list adDeviceMap?keys as key>
		  			<option value="${key}" <#if searchAdDevice?exists && searchAdDevice = key> selected</#if> >${adDeviceMap[key]}</option>
		  	    </#list>
		    </select>
		    <input type="hidden" id="adDevice" value="${searchAdDevice!}" />
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
	        <th class="td09" style="width:90px;">日期</td>
	        <th class="td09" style="width:120px;">網站類型</td>
	        <th class="td09" style="width:120px;">廣告客戶數</td>
	        <th class="td09" style="width:60px;">裝置</td>
	        <th class="td09" style="width:120px;">廣告曝光數</td>
	        <th class="td09" style="width:100px;">廣告互動數</td>
	        <th class="td09" style="width:100px;">廣告互動率</td>
	        <th class="td09" style="width:120px;">平均互動費用</td>
	        <th class="td09" style="width:120px;">千次曝光費用</td>
	        <th class="td09" style="width:120px;">廣告費用</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
		    <tr>
		        <td class="td09">${data.adPvclkDate}</td>
		        <td class="td09">${data.websiteCategoryName}</td>
		        <td class="td09">
		        	<a style="cursor:pointer;" onclick="fundDetalView(<#if data.websiteCategoryCode != ''>'${data.websiteCategoryCode}'<#else>'A'</#if>,'${data.websiteCategoryName}','${data.adPvclkDate}');">${data.pfpCustomerInfoIdCount}</a>
		        </td>
		        <td class="td09">${data.adDevice}</td>
		        <td class="td10">${data.adPvSum}</td>
		        <td class="td10">${data.adClkSum}</td>
		        <td class="td10">${data.adClkRate}</td>
		        <td class="td10">$ ${data.adClkAvgPrice}</td>
		        <td class="td10">$ ${data.adPvAvgPrice}</td>
		        <td class="td10">$ ${data.adClkPriceSum}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="10" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    </tr>
    <#list sumList as data>
	    <tr>
	        <td class="td09" colspan="4">${data.websiteCategoryName}</td>
	        <td class="td10">${data.adPvSum}</td>
	        <td class="td10">${data.adClkSum}</td>
	        <td class="td10">${data.adClkRate}</td>
	        <td class="td10">$ ${data.adClkAvgPrice}</td>
	        <td class="td10">$ ${data.adPvAvgPrice}</td>
	        <td class="td10">$ ${data.adClkPriceSum}</td>
	    </tr>
    </#list>
    <tr>
    	<td class="td09" colspan="4" style="background-color:#99FFFF;" >共計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalVO.adPvSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkRate!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkAvgPrice!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adPvAvgPrice!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkPriceSum!}</td>
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
	<div id="adWebsiteDetalDiv">
	
	</div>
</div>