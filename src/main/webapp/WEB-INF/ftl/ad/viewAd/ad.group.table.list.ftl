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
		<th>分類名稱</th>
		<th>裝置</th>
		<th>分類狀態</th>
		<th>搜尋廣告出價</th>
		<th>聯播廣告出價</th>
		<th style="width:8%">曝光數</th>
		<th style="width:8%">點擊數</th>
		<th style="width:8%">點閱率(%)</th>
		<th style="width:8%">平均點選費用</th>
		<th style="width:8%">費用</th>
		<th>無效點擊數</th>
		<th>無效點擊費用</th>
		<th>廣告</th>
		<th>廣告狀態</th>
	</tr>
</thead>
<tbody>
	<#if adGroupViewVO?exists>
	    <#list adGroupViewVO as vo>
			<tr>
				<td class="td01">${vo.adGroupCreatTime!}</td>
				<td class="td02">${vo.memberId!}</td>
				<td class="td02">${vo.customerInfoTitle!}</td>
		        <td height="35" class="td02">
	        	<#if vo.adGroupStatus != 9 >
	        		${vo.adGroupName!}<br>
	        		<input type="button" id="btnAd" name="btnAd" value="明細" onclick="javascript:goAd('${vo.adGroupSeq!}','${adclkDevice!}');">
	        		<input type="button" id="btnKW" name="btnKW" value="關鍵字" onclick="javascript:goKW('${vo.adGroupSeq!}');">
	        		<!--<a href="adKeywordView.html?adGroupSeq=${vo.adGroupSeq!}">${vo.adGroupName!}</a>-->
				<#else>
			        ${vo.adGroupName!}
				</#if>		        	
		        </td>
				<td class="td01">${vo.adPvclkDevice}</td>		        
		        <td class="td02">${vo.adGroupStatusDesc!}</td>
		        <td class="td01">
		        <div class="notsbg">
		        <#if vo.adGroupSearchPriceType == adSearchPriceType[0].typeId >
			        <img src="<@s.url value="/" />html/img/icon_Q.gif" align="absmiddle" title="在您的廣告預算內，每個關鍵字，系統為您爭取較高的廣告排名。">
			        ${adSearchPriceType[0].desc!}
				<#else>
			        <img src="<@s.url value="/" />html/img/icon_Q.gif" align="absmiddle" title="系統為您的關鍵字出價均預設為最低出價 NT$3。&#13;如要爭取較高的廣告排名，可將出價設定變更為&#13;「系統建議出價 」、或個別變更關鍵字出價金額。">       	
			        ${adSearchPriceType[1].desc!}
				</#if>
				${vo.adGroupSearchPrice?string('#,###')!}
                </div>
				</td>
		        <td class="td01">${vo.adGroupChannelPrice?string('#,###')!}</td>
				<td class="td01">${vo.adPv?string('#,###')!}</td>
				<td class="td01">${vo.adClk?string('#,###')!}</td>
				<td class="td01">${vo.adClkRate?string('#.##')!}</td>
				<td class="td01">${vo.adClkPriceAvg?string('#.##')!}</td>
				<td class="td01">${vo.adClkPrice?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClk?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClkPrice?string('#,###')!}</td>
				<td class="td02">${vo.adActionName!}</td>
				<td class="td02">${vo.adActionStatusDesc!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="13">
			此廣告中尚未建立分類。請按「新增分類」按鈕，新增分類。
		</td>
	</tr>
	</#if>
</tbody>
	<tr class="tbg">
		<td colspan="8">總計：${totalSize!}筆</td>
		<td class="td01">${totalPv?string('#,###')!}</td>
		<td class="td01">${totalClk?string('#,###')!}</td>			
		<td class="td01">${totalClkRate?string('#.##')!}</td>
		<td class="td01">${totalAvgCost?string('#.##')!}</td>
		<td class="td01">${totalCost?string('#,###')!}</td>
		<td class="td01">${totalInvalidClk?string('#,###')!}</td>
		<td class="td01">${totalInvalidClkPrice?string('#,###')!}</td>
		<td class="td01" colspan="2"></td>
	</tr>
</table>
<input type="hidden" id="adGroupSeq" name="adGroupSeq" />
<input type="hidden" id="status" name="status" />
<input type="hidden" id="userPrice" name="userPrice" />
<input type="hidden" id="adclkDevice" name="adclkDevice" />
</form>
