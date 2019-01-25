<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />未達每日花費上限報表</h2>

<form action="adSpendReport.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly>
            <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;顯示:前
            <select id="displayCount" name="displayCount">
   	            <#list displayCountMap?keys as skey>
		  		    <option value="${skey}" <#if displayCount?exists && displayCount = skey>selected="selected" </#if>>${displayCountMap[skey]}</option>
		  	    </#list>
		    </select>名 -->
		</td>
	</tr>
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
<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>
<table id="tableView" class="table01 tablesorter" style="min-width:950px">
    <thead>
	    <tr>
	        <th class="td09" style="min-width:120px">會員帳號</th>
	        <th class="td09" style="min-width:150px">帳戶名稱</th>
	        <th class="td09" style="min-width:150px">廣告活動</th>
	        <th class="td09" style="min-width:60px">每日花費</th>
	        <th class="td09" style="min-width:60px">調控金額</th>
	        <th class="td09" style="min-width:60px">曝光數</th>
	        <th class="td09" style="min-width:60px">點選次數</th>
	        <th class="td09" style="min-width:60px">點選率</th>
	        <th class="td09" style="min-width:90px">平均點選出價</th>
	        <th class="td09" style="min-width:60px">費用</th>
	        <th class="td09" style="min-width:60px">未達費用</th>
	    </tr>
    </thead>
    <tbody>
    <#list dataList as data>
	    <tr>
	        <td class="td09">${data.customerInfoId!}</td>
	        <td class="td09">${data.customerInfoName!}</td>
	        <td class="td09">${data.adActionName!}</td>
	        <td class="td10">$ ${data.maxPrice!}</td>
	        <td class="td10">$ ${data.adActionControlPrice!}</td>
	        <td class="td10">${data.pvSum!}</td>
	        <td class="td10">${data.clkSum!}</td>
	        <td class="td10">${data.clkRate!}%</td>
	        <td class="td10">$ ${data.clkPriceAvg!}</td>
	        <td class="td10">$ ${data.priceSum!}</td>
	        <td class="td10">$ ${data.unReachPrice!}</td>
	    </tr>
	    </#list>
	</tbody>
	<tr>
		<td class="td09" colspan="5" style="background-color:#99FFFF;" >總計：${totalCount!}筆</td>
		<td class="td10" style="background-color:#99FFFF;" >${totalVO.pvSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.clkSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.clkRate!}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.clkPriceAvg!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.priceSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.unReachPrice!}</td>
	</tr>
</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
