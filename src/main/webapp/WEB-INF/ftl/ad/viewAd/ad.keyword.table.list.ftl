<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>	

<div style="clear:both;height:50%"></div>

<form method="post" id="tableForm" name="tableForm" >
<div style="overflow-x:auto;width:100%">
<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th height="35" style="min-width:60px" rowspan="2">新增日期</th>
		<th height="35" style="min-width:60px" rowspan="2">會員帳號</th>
		<th height="35" style="min-width:60px" rowspan="2">帳戶名稱</th>
		<th height="35" style="min-width:60px" rowspan="2">廣告</th>
		<th height="35" style="min-width:60px" rowspan="2">分類</th>
		<th height="35" style="min-width:100px" rowspan="2">關鍵字</th>
		<th height="35" style="min-width:40px" rowspan="2">狀態</th>
		<th height="35" style="min-width:55px" rowspan="2">裝置</th>
		<th colspan="3">比對方式</th>
		<th colspan="3">搜尋廣告出價</th>
		<th class="pvDataTh" style="min-width:100px" colspan="1" >曝光數 <input class="pvDataButton" type="button" onclick="toggleTd('pvData')" value="展開" /></th>
		<th class="clkDataTh" style="min-width:110px" colspan="1">點選次數 <input class="clkDataButton" type="button" onclick="toggleTd('clkData')" value="展開" /></th>
		<th class="clkRateDataTh" style="min-width:100px" colspan="1">點選率 <input class="clkRateDataButton" type="button" onclick="toggleTd('clkRateData')" value="展開" /></th>
		<th class="invClickDataTh" style="min-width:135px" colspan="1">無效點選次數 <input class="invClickDataButton" type="button" onclick="toggleTd('invClickData')" value="展開" /></th>
		<th class="clkPriceAvgDataTh" style="min-width:135px" colspan="1">平均點選費用 <input class="clkPriceAvgDataButton" type="button" onclick="toggleTd('clkPriceAvgData')" value="展開" /></th>
		<th class="clkPriceDataTh" style="min-width:90px" colspan="1">費用 <input class="clkPriceDataButton" type="button" onclick="toggleTd('clkPriceData')" value="展開" /></th>
		<th class="invClickPriceDataTh" style="min-width:135px" colspan="1">無效點選費用 <input class="invClickPriceDataButton" type="button" onclick="toggleTd('invClickPriceData')" value="展開" /></th>
		<th colspan="3">平均廣告排名</th>
	</tr>
	<tr>
		<th style="min-width:40px">廣泛<br/>比對</th>
		<th style="min-width:40px">詞組<br/>比對</th>
		<th style="min-width:40px">精準<br/>比對</th>
		<th style="min-width:40px">廣泛<br/>比對</th>
		<th style="min-width:40px">詞組<br/>比對</th>
		<th style="min-width:40px">精準<br/>比對</th>
		<th class="pvData" style="min-width:60px;display:none;">廣泛<br/>比對</th>
		<th class="pvData" style="min-width:60px;display:none;">詞組<br/>比對</th>
		<th class="pvData" style="min-width:60px;display:none;">精準<br/>比對</th>
		<th style="min-width:60px;">總計</th>
		<th class="clkData" style="min-width:50px;display:none;">廣泛<br/>比對</th>
		<th class="clkData" style="min-width:50px;display:none;">詞組<br/>比對</th>
		<th class="clkData" style="min-width:50px;display:none;">精準<br/>比對</th>
		<th style="min-width:50px;">總計</th>
		<th class="clkRateData" style="min-width:40px;display:none;">廣泛<br/>比對</th>
		<th class="clkRateData" style="min-width:40px;display:none;">詞組<br/>比對</th>
		<th class="clkRateData" style="min-width:40px;display:none;">精準<br/>比對</th>
		<th style="min-width:40px;">總計</th>
		<th class="invClickData" style="min-width:50px;display:none;">廣泛<br/>比對</th>
		<th class="invClickData" style="min-width:50px;display:none;">詞組<br/>比對</th>
		<th class="invClickData" style="min-width:50px;display:none;">精準<br/>比對</th>
		<th style="min-width:50px;">總計</th>
		<th class="clkPriceAvgData" style="min-width:60px;display:none;">廣泛<br/>比對</th>
		<th class="clkPriceAvgData" style="min-width:60px;display:none;">詞組<br/>比對</th>
		<th class="clkPriceAvgData" style="min-width:60px;display:none;">精準<br/>比對</th>
		<th style="min-width:60px;">總計</th>
		<th class="clkPriceData" style="min-width:70px;display:none;">廣泛<br/>比對</th>
		<th class="clkPriceData" style="min-width:70px;display:none;">詞組<br/>比對</th>
		<th class="clkPriceData" style="min-width:70px;display:none;">精準<br/>比對</th>
		<th style="min-width:80px;">總計</th>
		<th class="invClickPriceData" style="min-width:50px;display:none;">廣泛<br/>比對</th>
		<th class="invClickPriceData" style="min-width:50px;display:none;">詞組<br/>比對</th>
		<th class="invClickPriceData" style="min-width:50px;display:none;">精準<br/>比對</th>
		<th style="min-width:50px;">總計</th>
		<th style="min-width:40px;">廣泛<br/>比對</th>
		<th style="min-width:40px;">詞組<br/>比對</th>
		<th style="min-width:40px;">精準<br/>比對</th>
	</tr>
</thead>
<tbody>
	<#if adKeywordViewVO?exists>
	    <#list adKeywordViewVO as vo>
			<tr>
				<td class="td01">${vo.adKeywordCreateTime!}</td>
				<td class="td01">${vo.memberId!}</td>
				<td class="td01">${vo.customerInfoTitle!}</td>
				<td class="td01">${vo.adActionName!}</td>
				<td class="td01">${vo.adGroupName!}<BR><input type="button" id="btnAd" name="btnAd" value="明細" onclick="javascript:goAd('${vo.adGroupSeq!}');"></td>
		        <td height="35" class="td02">${vo.adKeyword!}</td >		        
		        <td class="td01">${vo.adKeywordStatusDesc!}</td>
				<td class="td01">${vo.adKeywordPvclkDevice}</td>
				<td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
		        <td style="display:none;"></td>
				<td class="td01">${vo.adKeywordOpen!}</td>
	        	<td class="td01">${vo.adKeywordPhrOpen!}</td>
	        	<td class="td01">${vo.adKeywordPreOpen!}</td>
	        	<td class="td04">$ ${vo.adKeywordSearchPrice!}</td>
	        	<td class="td04">$ ${vo.adKeywordSearchPhrPrice!}</td>
	        	<td class="td04">$ ${vo.adKeywordSearchPrePrice!}</td>
	        	<td class="td04 pvData" style="display:none;" >${vo.adKeywordPv?string('#,###')!}</td>
	        	<td class="td04 pvData" style="display:none;" >${vo.adKeywordPhrPv?string('#,###')!}</td>
	        	<td class="td04 pvData" style="display:none;" >${vo.adKeywordPrePv?string('#,###')!}</td>
	        	<td class="td04">${vo.adKeywordPvSum?string('#,###')!}</td>
				<td class="td04 clkData" style="display:none;" >${vo.adKeywordClk?string('#,###')!}</td>
	        	<td class="td04 clkData" style="display:none;" >${vo.adKeywordPhrClk?string('#,###')!}</td>
	        	<td class="td04 clkData" style="display:none;" >${vo.adKeywordPreClk?string('#,###')!}</td>
	        	<td class="td04">${vo.adKeywordClkSum?string('#,###')!}</td>
				<td class="td04 clkRateData" style="display:none;" >${vo.adKeywordClkRate?string('0.00')!}%</td>
	        	<td class="td04 clkRateData" style="display:none;" >${vo.adKeywordPhrClkRate?string('0.00')!}%</td>
	        	<td class="td04 clkRateData" style="display:none;" >${vo.adKeywordPreClkRate?string('0.00')!}%</td>
	        	<td class="td04">${vo.adKeywordClkRateSum?string('0.00')!}%</td>
	        	<td class="td04 invClickData" style="display:none;" >${vo.adKeywordInvalidClk?string('#,###')!}</td>
	        	<td class="td04 invClickData" style="display:none;" >${vo.adKeywordPhrInvalidClk?string('#,###')!}</td>
	        	<td class="td04 invClickData" style="display:none;" >${vo.adKeywordPreInvalidClk?string('#,###')!}</td>
	        	<td class="td04">${vo.adKeywordInvalidClkSum?string('#,###')!}</td>
				<td class="td04 clkPriceAvgData" style="display:none;" >$ ${vo.adKeywordClkPriceAvg?string('0.00')!}</td>
	        	<td class="td04 clkPriceAvgData" style="display:none;" >$ ${vo.adKeywordPhrClkPriceAvg?string('0.00')!}</td>
	        	<td class="td04 clkPriceAvgData" style="display:none;" >$ ${vo.adKeywordPreClkPriceAvg?string('0.00')!}</td>
	        	<td class="td04">NT$ ${vo.adKeywordClkPriceAvgSum?string('0.00')!}</td>
				<td class="td04 clkPriceData" style="display:none;" >$ ${vo.adKeywordClkPrice?string('#,###')!}</td>
	        	<td class="td04 clkPriceData" style="display:none;" >$ ${vo.adKeywordPhrClkPrice?string('#,###')!}</td>
	        	<td class="td04 clkPriceData" style="display:none;" >$ ${vo.adKeywordPreClkPrice?string('#,###')!}</td>
	        	<td class="td04">NT$ ${vo.adKeywordClkPriceSum?string('#,###')!}</td>
				<td class="td04 invClickPriceData" style="display:none;" >$ ${vo.adKeywordInvalidClkPrice?string('#,###')!}</td>
				<td class="td04 invClickPriceData" style="display:none;" >$ ${vo.adKeywordPhrInvalidClkPrice?string('#,###')!}</td>
				<td class="td04 invClickPriceData" style="display:none;" >$ ${vo.adKeywordPreInvalidClkPrice?string('#,###')!}</td>
				<td class="td04">NT$ ${vo.adKeywordInvalidClkPriceSum?string('#,###')!}</td>
				<td class="td01">${vo.adKeywordRankAvg?string('0.00')!}</td>
				<td class="td01">${vo.adKeywordPhrRankAvg?string('0.00')!}</td>
				<td class="td01">${vo.adKeywordPreRankAvg?string('0.00')!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="18">
			此分類中尚未建立關鍵字。請按「新增關鍵字」按鈕，新增關鍵字。
		</td>
	</tr>
	</#if>
</tbody>
	<tr class="tbg">
		<td class="td01" colspan="14">總計：${totalVO.dataSize!}筆</td>
		<td class="td04 pvData" style="display:none;" >${totalVO.adKeywordPv?string('#,###')!}</td>
    	<td class="td04 pvData" style="display:none;" >${totalVO.adKeywordPhrPv?string('#,###')!}</td>
    	<td class="td04 pvData" style="display:none;" >${totalVO.adKeywordPrePv?string('#,###')!}</td>
    	<td class="td04">${totalVO.adKeywordPvSum?string('#,###')!}</td>
		<td class="td04 clkData" style="display:none;" >${totalVO.adKeywordClk?string('#,###')!}</td>
    	<td class="td04 clkData" style="display:none;" >${totalVO.adKeywordPhrClk?string('#,###')!}</td>
    	<td class="td04 clkData" style="display:none;" >${totalVO.adKeywordPreClk?string('#,###')!}</td>
    	<td class="td04">${totalVO.adKeywordClkSum?string('#,###')!}</td>
		<td class="td04 clkRateData" style="display:none;" >${totalVO.adKeywordClkRate?string('0.00')!}%</td>
    	<td class="td04 clkRateData" style="display:none;" >${totalVO.adKeywordPhrClkRate?string('0.00')!}%</td>
    	<td class="td04 clkRateData" style="display:none;" >${totalVO.adKeywordPreClkRate?string('0.00')!}%</td>
    	<td class="td04">${totalVO.adKeywordClkRateSum?string('0.00')!}%</td>
    	<td class="td04 invClickData" style="display:none;" >${totalVO.adKeywordInvalidClk?string('#,###')!}</td>
    	<td class="td04 invClickData" style="display:none;" >${totalVO.adKeywordPhrInvalidClk?string('#,###')!}</td>
    	<td class="td04 invClickData" style="display:none;" >${totalVO.adKeywordPreInvalidClk?string('#,###')!}</td>
    	<td class="td04">${totalVO.adKeywordInvalidClkSum?string('#,###')!}</td>
		<td class="td04 clkPriceAvgData" style="display:none;" >$ ${totalVO.adKeywordClkPriceAvg?string('0.00')!}</td>
    	<td class="td04 clkPriceAvgData" style="display:none;" >$ ${totalVO.adKeywordPhrClkPriceAvg?string('0.00')!}</td>
    	<td class="td04 clkPriceAvgData" style="display:none;" >$ ${totalVO.adKeywordPreClkPriceAvg?string('0.00')!}</td>
    	<td class="td04">NT$ ${totalVO.adKeywordClkPriceAvgSum?string('0.00')!}</td>
		<td class="td04 clkPriceData" style="display:none;" >$ ${totalVO.adKeywordClkPrice?string('#,###')!}</td>
    	<td class="td04 clkPriceData" style="display:none;" >$ ${totalVO.adKeywordPhrClkPrice?string('#,###')!}</td>
    	<td class="td04 clkPriceData" style="display:none;" >$ ${totalVO.adKeywordPreClkPrice?string('#,###')!}</td>
    	<td class="td04">NT$ ${totalVO.adKeywordClkPriceSum?string('#,###')!}</td>
		<td class="td04 invClickPriceData" style="display:none;" >$ ${totalVO.adKeywordInvalidClkPrice?string('#,###')!}</td>
		<td class="td04 invClickPriceData" style="display:none;" >$ ${totalVO.adKeywordPhrInvalidClkPrice?string('#,###')!}</td>
		<td class="td04 invClickPriceData" style="display:none;" >$ ${totalVO.adKeywordPreInvalidClkPrice?string('#,###')!}</td>
		<td class="td04">NT$ ${totalVO.adKeywordInvalidClkPriceSum?string('#,###')!}</td>
		<td class="td01" colspan="3"></td>
	</tr>
</table>
</div>
<input type="hidden" id="adKeywordSeq" name="adKeywordSeq" />
<input type="hidden" id="status" name="status" />


<#if adGroupSeq != "">
<div style="padding:10px;float:right;">
	<a href="#" onClick="adExcludeKeyword('${adGroupSeq!}')" >排除關鍵字</a>
</div>
</#if>
</form>
