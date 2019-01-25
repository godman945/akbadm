<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="clear:both;height:50%"></div>
			
<form method="post" id="tableForm" name="tableForm" >
<table id="tableView" class="tablesorter" width="80%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th>帳戶編號</th>
		<th>優惠活動</th>
		<th>記錄日期</th>
		<th>建立日期</th>
	</tr>	
</thead>
<tbody>
	<#if admFreeRecords?exists>
	    <#list admFreeRecords as admFreeRecord>
			<tr>
				<td class="td01">${admFreeRecord.customerInfoId!}</td>
				<td class="td01">${admFreeRecord.admFreeAction.actionName!}</td>
				<td class="td01">${admFreeRecord.recordDate!}</td>
				<td class="td01">${admFreeRecord.updateDate!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="17">
			沒有符合搜尋條件的內容。
		</td>
	</tr>
	</#if>
</tbody>
	<tr class="tbg">
		<td colspan="4">總計：${totalCount!}筆</td>
	</tr>
</table>
<input type="hidden" id="adActionSeq" name="adActionSeq" />
<input type="hidden" id="status" name="status" />
<input type="hidden" id="adActionMax" name="adActionMax" />
</form>
