<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>

<div style="clear:both;height:50%"></div>
			
<form method="post" id="tableForm" name="tableForm" >
<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th>禮金動作</th>
		<th>禮金序號輸入頁面</th>
		<th>活動日期</th>
		<th>活動名稱</th>
		<th>說明</th>
		<th>共用序號</th>
		<th>贈送禮金</th>
		<th>禮金發放總金額</th>
		<th>實際領用總金額</th>
		<th>禮金失效日期</th>
		<th>開通帳號</th>
		<th>序號明細</th>
		<th>編輯</th>
	</tr>	
</thead>
<tbody>
	<#if dataList?exists>
	    <#list dataList as vo>
			<tr>
				<td class="td01">${vo.payment!}</td>
				<td class="td01">${vo.giftStyle!}</td>
		        <td class="td01">${vo.actionStartDate!}~${vo.actionEndDate!}</td>
				<td class="td01">${vo.actionName!}</td>
		        <td class="td01">${vo.note!}</td>
		        <td class="td01">${vo.sharedNote!}</td>
				<td class="td04">${vo.giftMoney!}</td>
				<td class="td04"><#if vo.shared == 'N' >${vo.totalGiftMoney!}</#if></td>
				<td class="td04">${vo.totalRecordMoney!}</td>
		        <td class="td01">${vo.inviledDate!}</td>
		        <td class="td01"><a onclick="openRecordView('${vo.actionId!}')">開通帳號</a></td>
				<td class="td01"><a onclick="openDetalView('${vo.actionId!}','${vo.actionEndDate!}','${vo.shared!}')">明細</a></td>
				<td class="td01"><a onclick="updateFreeAction('${vo.actionId!}')">修改</a></td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="13">
			沒有符合搜尋條件的內容。
		</td>
	</tr>
	</#if>
</tbody>
	<tr class="tbg">
		<td colspan="13">總計：${totalSize!}筆</td>
	</tr>
</table>
<input type="hidden" id="adActionSeq" name="adActionSeq" />
<input type="hidden" id="status" name="status" />
<input type="hidden" id="adActionMax" name="adActionMax" />
</form>
