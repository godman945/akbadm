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
		<th>新增日期</th>
		<th>會員帳號</th>
		<th>帳戶名稱</th>
		<th>帳戶狀態</th>
		<th>裝置</th>
		<th>帳戶餘額</th>
		<th height="35">廣告</th>
		<th>狀態</th>
		<th>廣告走期</th>
		<th>每日花費</th>
		<th>調控金額</th>
		<th style="width:7%">曝光數</th>
		<th>點擊數</th>
		<th>點閱率(%)</th>
		<th>平均點選費用</th>
		<th>費用</th>
		<th>無效點擊數</th>
		<th>無效點擊費用</th>
	</tr>	
</thead>
<tbody>
	<#if adActionViewVO?exists>
	    <#list adActionViewVO as vo>
			<tr>
				<td class="td01">${vo.adActionCreatTime!}</td>
				<td class="td01">${vo.memberId!}</td>
				<td class="td01">${vo.customerInfoTitle!}</td>
				<td class="td01">${vo.customerInfoStatusDesc}</td>
				<td class="td01">${vo.adPvclkDevice}</td>
				<td class="td01">${vo.remain?string('#,###')!}</td>
		        <td height="35" class="td02">
	        	<#if vo.adActionStatus != 9>
			        <!--<a href="adGroupView.html?adActionSeq=${vo.adActionSeq!}">${vo.adActionName!}</a>-->
			        <a href="javascript:goAdGroup('${vo.adActionSeq!}','${vo.adPvclkDevice}');">${vo.adActionName!}</a>
				<#else>
			        ${vo.adActionName!}
				</#if>		        	
		        </td>
		        <td class="td02">${vo.adActionStatusDesc!}</td>
		        <#--
				<td class="td02">${vo.adType!}</td>
				-->
				<td class="td02">
				<#if vo.adEndDate == "3000-12-31">
					${vo.adStartDate!} ~ 永久
				<#else>
					${vo.adStartDate!} ~ ${vo.adEndDate!}
				</#if>
				</td>
				<td class="td01">${vo.adActionMax?string('#,###')!}</td>
				<td class="td01">${vo.adActionControlPrice?string('#,###')!}</td>
				<td class="td01">${vo.adPv?string('#,###')!}</td>
				<td class="td01">${vo.adClk?string('#,###')!}</td>
				<td class="td01">${vo.adClkRate?string('#.##')!}</td>
				<td class="td01">${vo.adClkPriceAvg?string('#.##')!}</td>
				<td class="td01">${vo.adClkPrice?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClk?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClkPrice?string('#,###')!}</td>
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
		<td colspan="11">總計：${totalSize!}筆</td>
		<td class="td01">${totalPv?string('#,###')!}</td>
		<td class="td01">${totalClk?string('#,###')!}</td>			
		<td class="td01">${totalClkRate?string('#.##')!}</td>
		<td class="td01">${totalAvgCost?string('#.##')!}</td>
		<td class="td01">${totalCost?string('#,###')!}</td>
		<td class="td01">${totalInvalidClk?string('#,###')!}</td>
		<td class="td01">${totalInvalidClkPrice?string('#,###')!}</td>
	</tr>
</table>
<input type="hidden" id="adActionSeq" name="adActionSeq" />
<input type="hidden" id="status" name="status" />
<input type="hidden" id="adActionMax" name="adActionMax" />
</form>
