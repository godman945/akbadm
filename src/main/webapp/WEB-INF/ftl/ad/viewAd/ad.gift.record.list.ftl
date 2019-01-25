<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2>
	<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告禮金帳戶開通明細
</h2>
<table width="100%" >
	<tr>
		<td colspan="2">
			<span style="font-size:16px;"><b>廣告活動編號：${actionId!}</b></span>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="height:20px;">
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span style="font-size:16px;"><b>實際領用總金額：NT$ ${totalFreeUseMoney!}</b></span>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="height:30px;";></td>
	</tr>
	<tr>
		<td style="text-align:left;width:80%">
			
		</td>
		<td style="text-align:center;width:20%">
			<input type="button" value="下載" onclick="doDownlaod()" >
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="返回" onclick="returnView()" >
		</td>
	</tr>
</table>
<input type="hidden" id="actionId" name="actionId" value="${actionId!}" />
<div style="clear:both;height:50%"></div>
			
<form method="post" id="tableForm" name="tableForm" >
<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th>開通筆數</th>
		<th>帳戶編號</th>
		<th>訂單編號</th>
		<th>序號開啟日期</th>
	</tr>	
</thead>
<tbody>
	<#if recordList?exists>
	    <#list recordList as vo>
			<tr>
				<td class="td01">${vo.numberId!}</td>
		        <td class="td01">${vo.customerInfoId!}</td>
		        <td class="td01">${vo.orderId!}</td>
				<td class="td01">${vo.recordDate!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="4">
			沒有符合搜尋條件的內容。
		</td>
	</tr>
	</#if>
</tbody>
</table>
</form>
