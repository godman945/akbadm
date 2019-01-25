<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2>
	<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告禮金序號明細
</h2>
<form method="post" id="addFreeGiftForm" action="addFreeGift.html" >
<table width="100%" >
	<tr>
		<td colspan="2">
			<span style="font-size:16px;"><b>廣告活動編號：${actionId!}</b></span>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="height:30px;";></td>
	</tr>
	<tr>
		<td style="text-align:left;width:80%">
			<#if shared == 'N' >
				<span style="font-size:14px;"><b>序號開頭前兩碼</b></span>&nbsp;
				<input type="text" id="giftSnoHead" name="giftSnoHead" value="${giftSnoHead!}" style="width:30px" maxlength="2" >
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span style="font-size:14px;"><b>新增序號組數</b></span>&nbsp;
				<input type="text" id="snoCount" name="snoCount" value="${snoCount!}" style="width:50px" maxlength="4" >
				&nbsp;&nbsp;
				<input type="button" value="新增" onclick="addFreeGift()" >
			</#if>
		</td>
		<td style="text-align:center;width:20%">
			<input type="button" value="下載" onclick="doDownlaod()" >
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="返回" onclick="returnView()" >
		</td>
	</tr>
</table>
<input type="hidden" id="actionId" name="actionId" value="${actionId!}" />
<input type="hidden" id="actionEndDate" name="actionEndDate" value="${actionEndDate!}" />
</from>
<div style="clear:both;height:50%"></div>
			
<form method="post" id="tableForm" name="tableForm" >
<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th>禮金筆數</th>
		<th>禮金序號</th>
		<th>帳戶編號</th>
		<th>序號開啟日期</th>
		<th>訂單編號</th>
		<th>序號狀態</th>
	</tr>	
</thead>
<tbody>
	<#if giftList?exists>
	    <#list giftList as vo>
			<tr>
				<td class="td01">${vo.numberId!}</td>
				<td class="td01">${vo.giftSno!}</td>
		        <td class="td01">${vo.customerInfoId!}</td>
				<td class="td01">${vo.openDate!}</td>
		        <td class="td01">${vo.orderId!}</td>
		        <td class="td01">${vo.giftSnoStatus!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="6">
			沒有符合搜尋條件的內容。
		</td>
	</tr>
	</#if>
</tbody>
</table>
</form>
