<#assign s=JspTaglibs["/struts-tags"]>

<div id="dialog" class="dialog" ></div>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />ARW 廣告權重調整</h2>

<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addView();"></td>
    </tr>
</table>

<br>

<table class="table01" width="95%">
    <tr>
        <td class="td01" width="15%">經銷商帳號</td>
        <td class="td01" width="15%">PFP 帳號</td>
        <td class="td01" width="15%">PFP 名稱</td>
        <td class="td01" width="7%">ＡＲＷ值（2~20）</td>
        <td class="td01" width="7%">走期狀態</td>
        <td class="td01" width="10%">開始日</td>
        <td class="td01" width="10%">到期日</td>
        <td class="td01" width="10%">修改/刪除</td>
    </tr>

	<#if dataList?exists>
		<#list dataList as list>
			<tr>
				<td class="td03">${list.pfdCustomerInfoId!}</td>
				<td class="td03">${list.customerInfoId!}</td>
				<td class="td03">${list.customerInfoTitle!}</td>
				<td class="td03">${list.arwValue!}</td>
				<td class="td03"><#if list.dateFlag == 0>無限期<#else>啟用</#if></td>
				<td class="td03"><#if list.dateFlag == 1>${list.startDate!}</#if></td>
				<td class="td03"><#if list.dateFlag == 1>${list.endDate!}</#if></td>
				<td class="td03">
					<input type="button" id="modifyBtn" value="修改" onclick="modifyView('${list.customerInfoId!}');">
					<input type="button" id="deleteBtn" value="刪除" onclick="deleteArwValue('${list.customerInfoId!}');">
                </td>
			</tr>
		</#list>
	</#if>
</table>