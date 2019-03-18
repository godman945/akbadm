<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />轉換追蹤</h2>

<form id="queryFrom" action="convertReportDetail.html" method="post">
	<input type="hidden" id="contentPath" name="contentPath" value="<@s.url value="/html/img/"/>">
	
	<table width="100%">
		<tr>
			<td>經銷商名稱:
				<select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
					<option value="">全部經銷商</option>
	                <#list pfdCustomerInfoIdMap?keys as skey>
			    		<option value="${skey}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId == skey>selected="selected"</#if>>${pfdCustomerInfoIdMap[skey]}</option>
			    	</#list>
				</select>
			</td>
			
			<td>帳戶編號:
				<input type="text" id="pfpCustomerInfoId" name="pfpCustomerInfoId" value="${pfpCustomerInfoId!}" />
			</td>
			
			<td>帳戶名稱:
				<input type="text" id="pfpCustomerInfoName" name="pfpCustomerInfoName" value="${pfpCustomerInfoName!}" />
			</td>
			
			<td>狀態:
				<select id="status" name="status">
					<option value="0">已關閉</option>
					<option value="1" <#if status?exists && status == "1">selected="selected"</#if>>已啟用</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="查詢" onclick="query()"/>
			</td>
		</tr>
	</table>
	
	<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
	
	<#if dataList?exists && (dataList?size > 0)>
	
		<span class="pages">
			共 ${totalCount!} 筆
			<input type="hidden" id="pageNo" name="pageNo" value="${pageNo!}" />
			<input type="hidden" id="pageCount" name="pageCount" value="${pageCount!}" />
			
			<img style="vertical-align:middle" id="fpage" src="<@s.url value="/html/img/"/>page_first.gif" />
			<img style="vertical-align:middle" id="ppage" src="<@s.url value="/html/img/"/>page_pre.gif" />
			${pageNo}/${pageCount}
			<img style="vertical-align:middle" id="npage" src="<@s.url value="/html/img/"/>page_next.gif" />
			<img style="vertical-align:middle" id="epage" src="<@s.url value="/html/img/"/>page_end.gif" />
	
			&nbsp&nbsp
	
			顯示
			
			<input type="hidden" id="hiddenPageSize" name="hiddenPageSize" value="${pageSize}">
			<select id="pageSizeSelect" name="pageSize" style="vertical-align:middle" >
				<#list pageSizeList as ps>
			        <option>${ps}</option>
			    </#list> 
			</select>
			行
			
		</span>
		<br><br>
		

		<table class="table01" width="100%">
			<tr>
				<td class="td01" width="10%">經銷商</td>
				<td class="td01" width="11%">廣告客戶</td>
				<td class="td01" width="16%">名稱</td>
				<td class="td01" width="5%">狀態</td>
				<td class="td01" width="5%">認證狀態</td>
				<td class="td01" width="8%">代碼類型</td>
				<td class="td01" width="8%">轉換類型</td>
				<td class="td01" width="5%">點擊後轉換追溯</td>
				<td class="td01" width="5%">瀏覽後轉換追溯</td>
				<td class="td01" width="8%">轉換價值</td>
				<td class="td01" width="5%">點擊後轉換數</td>
				<td class="td01" width="5%">瀏覽後轉換數</td>
				<td class="td01" width="5%">所有轉換</td>
			</tr>
		
			<#list dataList as tableDatalist>
			<tr>
				<td class="td03">${tableDatalist.pfdCustomerInfoName!}</td>
				<td class="td03">${tableDatalist.pfpCustomerInfoId!} <br> ${tableDatalist.pfpCustomerInfoName!}</td>
				<td class="td03">${tableDatalist.convertName!} <br> ID:${tableDatalist.convertSeq!}</td>
				<td class="td03">${tableDatalist.convertStatusDesc!}</td>
				<td class="td03">${tableDatalist.verifyStatus!}</td>
				<td class="td03">${tableDatalist.convertTypeDesc!}</td>
				<td class="td03">${tableDatalist.convertClassDesc!}</td>
				<td class="td03">${tableDatalist.clickRangeDate!}</td>
				<td class="td03">${tableDatalist.impRangeDate!}</td>
				<td class="td03">${tableDatalist.transConvertPrice!}</td>
				<td class="td03">${tableDatalist.transCKConvertCount!}</td>
				<td class="td03">${tableDatalist.transPVConvertCount!}</td>
				<td class="td03">${tableDatalist.transAllConvertCount!}</td>
			</tr>
			</#list>
		</table>

	<#else>
		<#if message?exists && message != "">
			${message!}
		</#if>
	</#if>
	
</form>