<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告族群成效查詢</h2>

<form action="adAgesexSearch.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		    <br/>
            <BR>查詢條件 : 
            <select id="searchAgesex" name="searchAgesex">
                <option value="A">年齡</option>
                <option value="S">性別</option>
		    </select>
		    <input type="hidden" id="agesex" value="${searchAgesex!}" />
		    &nbsp;&nbsp;
        	<select id="searchDevice" name="searchDevice">
                <option value="">裝置</option>
                <option value="PC">電腦</option>
                <option value="mobile">行動裝置</option>
		    </select>
		    <input type="hidden" id="device" value="${searchDevice!}" />
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
	    	<th class="td09">日期</td>
	    	<#if searchAgesex == 'S'>
	        	<th class="td09">性別</td>
	        <#else>
	        	<th class="td09">年齡</td>
	        </#if>
	        <th class="td09">裝置</td>
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
		        <td class="td09">${data.reportDate}</td>
		        <#if searchAgesex == 'S'>
		        	<td class="td09">${data.sex}</td>
		        <#else>
		        	<td class="td09">${data.age}</td>
		        </#if>
		        <td class="td09">${data.adDevice}</td>
		        <td class="td10">${data.adPvSum}</td>
		        <td class="td10">${data.adClkSum}</td>
		        <td class="td10">${data.adClkRate}</td>
		        <td class="td10">$ ${data.adClkAvgPrice}</td>
		        <td class="td10">$ ${data.adPvAvgPrice}</td>
		        <td class="td10">$ ${data.adPriceSum}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="9" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    </tr>
    <#list totalList as totalVO>	
	    <tr>
	    	<td class="td09" ></td>
	        <#if searchAgesex == 'S'>
	        	<td class="td09" >${totalVO.sex}</td>
	        <#else>
	        	<td class="td09" >${totalVO.age}</td>
	        </#if>
	        <td class="td09" >${totalVO.adDevice}</td>
	        <td class="td10" >${totalVO.adPvSum}</td>
	        <td class="td10" >${totalVO.adClkSum}</td>
	        <td class="td10" >${totalVO.adClkRate}</td>
	        <td class="td10" >$ ${totalVO.adClkAvgPrice}</td>
	        <td class="td10" >$ ${totalVO.adPvAvgPrice}</td>
	        <td class="td10" >$ ${totalVO.adPriceSum}</td>
	    </tr>
    </#list>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>
