<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />付費刊登-聯播網客戶-統計日報表</h2>

<form action="admClientCountDayRepor.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		    <br/>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="doDownlaod()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>

<table id="tableView" class="table01 tablesorter" style="min-width:2060px">
    <thead>
	    <tr>
	    	<th class="td09" rowspan="2" style="min-width:70px;" >日期</th>
	        <th class="td09" rowspan="2" style="min-width:30px;" >星期</th>
	        <th class="td09" rowspan="2" style="min-width:100px;">廣告曝光數</th>
	        <th class="td09" rowspan="2" style="min-width:80px;" >廣告互動數</th>
	        <th class="td09" rowspan="2" style="min-width:60px;" >互動率</td>
	        <th class="td09" rowspan="2" style="min-width:80px;" >單次互動<br/>費用</th>
	        <th class="td09" rowspan="2" style="min-width:80px;" >每千次曝光<br/>費用</th>
 			<th class="td09" colspan="4" style="min-width:80px;" >廣告客戶數</th>
 			<th class="td09" colspan="4" style="min-width:80px;" >互動費用</th>
			<th class="td09" colspan="4" style="min-width:80px;" >每日預算</th>
			<th class="td09" colspan="4" style="min-width:80px;" >預算達成率</th>
	        <!-- 線上廣告數 
	        <th class="td09" colspan="2">線上廣告數</th>
	       	-->
	        <th class="td09" rowspan="2" style="min-width:100px;" >營運費用(超播)</th>
	        <th class="td09" rowspan="2" style="min-width:80px;" >惡意點擊數</th>
	        <th class="td09" rowspan="2" style="min-width:90px;" >惡意點擊費用</th>
	        <th class="td09" colspan="2">預付儲值廣告金</th>
	        <th class="td09" colspan="3">PChome 營運費用</th>
	        <th class="td09" colspan="3">PFB 分潤</th>
	        <th class="td09" colspan="3">PFD 佣金</th>
	    </tr>
	    <tr>
	    	<th class="td09" style="min-width:60px;" >直客</td>
	    	<th class="td09" style="min-width:60px;" >業務</td>
	    	<th class="td09" style="min-width:60px;" >經銷商</td>
	    	<th class="td09" style="min-width:60px;" >總計</td>
	    	<th class="td09" style="min-width:80px;" >直客</td>
	    	<th class="td09" style="min-width:80px;" >業務</td>
	    	<th class="td09" style="min-width:80px;" >經銷商</td>
	    	<th class="td09" style="min-width:80px;" >總計</td>
	    	<th class="td09" style="min-width:80px;" >直客</td>
	    	<th class="td09" style="min-width:80px;" >業務</td>
	    	<th class="td09" style="min-width:80px;" >經銷商</td>
	    	<th class="td09" style="min-width:80px;" >總計</td>
	    	<th class="td09" style="min-width:80px;" >直客</td>
	    	<th class="td09" style="min-width:80px;" >業務</td>
	    	<th class="td09" style="min-width:80px;" >經銷商</td>
	    	<th class="td09" style="min-width:80px;" >總計</td>
	    	<!-- 線上廣告數 
	    	<th class="td09" style="min-width:60px;" >直客</td>
	    	<th class="td09" style="min-width:60px;" >經銷商</td>
	    	-->
	    	
	    	<th class="td09" style="min-width:60px;" >儲值筆數</td>
	    	<th class="td09" style="min-width:60px;" >儲值金額</td>
	    	<th class="td09" style="min-width:80px;" >儲值金</td>
	    	<th class="td09" style="min-width:80px;" >贈送金</td>
	    	<th class="td09" style="min-width:80px;" >後付費</td>
	    	<th class="td09" style="min-width:80px;" >儲值金</td>
	    	<th class="td09" style="min-width:80px;" >贈送金</td>
	    	<th class="td09" style="min-width:80px;" >後付費</td>
	    	<th class="td09" style="min-width:80px;" >儲值金</td>
	    	<th class="td09" style="min-width:80px;" >贈送金</td>
	    	<th class="td09" style="min-width:80px;" >後付費</td>
	    </tr>
    </thead>
    <tbody>
	    <#list dataList as data>
		    <tr> 
		        <!-- 日期 -->
		        <td class="td09">${data.countDate}</td>
		        <!-- 星期-->
		        <td class="td09">${data.week}</td>
		        <!-- 廣告曝光數 -->
		        <td class="td10">${data.adPv}</td>
		        <!-- 廣告互動數 -->
		        <td class="td10">${data.adClk}</td>
		        <!-- 互動率 -->
		        <td class="td10">${data.clkRate}%</td>
		        <!-- 單次互動費用 -->
		        <td class="td10">$ ${data.adClkPriceAvg}</td>
		        <!-- 每千次曝光費用 -->
		        <td class="td10">$ ${data.adPvPrice}</td>
		        <!-- 廣告客戶數-直客-->
		        <td class="td10">${data.pfpClientCount}</td>
		        <!-- 廣告客戶數-業務-->
		        <td class="td10">${data.salesClientCount}</td>
		        <!-- 廣告客戶數-經銷商-->
		        <td class="td10">${data.pfdClientCount}</td>
		        <!-- 廣告客戶數-總計-->
		        <td class="td10">${data.totalClientCount}</td>
		        <!-- 互動費用-直客-->
		        <td class="td10">$ ${data.pfpAdClkPrice}</td>
		        <!-- 互動費用-業務-->
		        <td class="td10">$ ${data.salesAdClkPrice}</td>
		        <!-- 互動費用-經銷商-->
		        <td class="td10">$ ${data.pfdAdClkPrice}</td>
		        <!-- 互動費用-總計-->
		        <td class="td10">$ ${data.totalAdClkPriceCount}</td>
		        <!-- 每日預算-直客-->
		        <td class="td10">$ ${data.pfpAdActionMaxPrice}</td>
		        <!-- 每日預算-業務-->
		        <td class="td10">$ ${data.salesAdActionMaxPrice}</td>
		        <!-- 每日預算-經銷商-->
		        <td class="td10">$ ${data.pfdAdActionMaxPrice}</td>
		        <!-- 每日預算-總計-->
		        <td class="td10">$ ${data.totalAdActionMaxPrice}</td>
		        <!-- 預算達成率-直客-->
		        <td class="td10">${data.pfpSpendRate}%</td>
		        <!-- 預算達成率-業務-->
		        <td class="td10">${data.salesSpendRate}%</td>
		        <!-- 預算達成率-經銷商-->
		        <td class="td10">${data.pfdSpendRate}%</td>
		        <!-- 預算達成率-總計-->
		        <td class="td10">${data.totalSpendRate}%</td>
		        <!-- 線上廣告數-直客
		        <td class="td10">${data.pfpAdCount}</td>
		        -->
		        <!-- 線上廣告數-經銷商
		        <td class="td10">${data.pfdAdCount}</td>
		        -->
		        <!-- 營運費用(超播) -->
		        <td class="td10">$ ${data.lossCost}</td>
		        <!-- 惡意點擊數 -->
		        <td class="td10">${data.adInvalidClk}</td>
		        <!-- 惡意點擊費用 -->
		        <td class="td10">$ ${data.adInvalidClkPrice}</td>
		        <!-- 儲值筆數 -->
		        <td class="td10">${data.totalSaveCount}</td>
		        <!-- 儲值金額 -->
		        <td class="td10">$ ${data.totalSavePrice}</td>
		        <!-- PChome 營運費用-儲值金 -->
		        <td class="td10">$ ${data.pfpSave}</td>
		        <!-- PChome 營運費用-贈送金 -->
		        <td class="td10">$ ${data.pfpFree}</td>
		        <!-- PChome 營運費用-後付費 -->
		        <td class="td10">$ ${data.pfpPostpaid}</td>
		        <!-- PFB 分潤-儲值金 -->
		        <td class="td10">$ ${data.pfbSave}</td>
		        <!-- PFB 分潤-贈送金 -->
		        <td class="td10">$ ${data.pfbFree}</td>
		        <!-- PFB 分潤-後付費 -->
		        <td class="td10">$ ${data.pfbPostpaid}</td>
		        <!-- PFD 佣金-儲值金 -->
		        <td class="td10">$ ${data.pfdSave}</td>
		        <!-- PFD 佣金-贈送金 -->
		        <td class="td10">$ ${data.pfdFree}</td>
		        <!-- PFD 佣金-後付費 -->
		        <td class="td10">$ ${data.pfdPostpaid}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="2" style="background-color:#99FFFF;" >加總</td>
    	<!-- 廣告曝光數 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adPv}</td>
        <!-- 廣告互動數 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClk}</td>
        <!-- 互動率 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.clkRate}%</td>
        <!-- 單次互動費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkPriceAvg}</td>
        <!-- 每千次曝光費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adPvPrice}</td>
    	<!-- 廣告客戶數-直客-->
    	<td class="td10" style="background-color:#99FFFF;" >${totalVO.pfpClientCount}</td>
    	<!-- 廣告客戶數-業務-->
    	<td class="td10" style="background-color:#99FFFF;" >${totalVO.salesClientCount}</td>
	    <!-- 廣告客戶數-經銷商-->
	    <td class="td10" style="background-color:#99FFFF;" >${totalVO.pfdClientCount}</td>
	    <!-- 廣告客戶數-總計-->
	    <td class="td10" style="background-color:#99FFFF;" >${totalVO.totalClientCount}</td>
	    <!-- 互動費用-直客 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfpAdClkPrice}</td>
	    <!-- 互動費用-業務 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.salesAdClkPrice}</td>
        <!-- 互動費用-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfdAdClkPrice}</td>
        <!-- 互動費用-總計-->
	    <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.totalAdClkPriceCount}</td>
       	<!-- 每日預算-直客-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfpAdActionMaxPrice}</td>
        <!-- 每日預算-業務-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.salesAdActionMaxPrice}</td>
        <!-- 每日預算-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfdAdActionMaxPrice}</td>
        <!-- 每日預算-總計-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.totalAdActionMaxPrice}</td>
        <!-- 預算達成率-直客-->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.pfpSpendRate}%</td>
        <!-- 預算達成率-業務-->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.salesSpendRate}%</td>
        <!-- 預算達成率-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.pfdSpendRate}%</td>
        <!-- 預算達成率-總計-->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.totalSpendRate}%</td>
        <!-- 營運費用(超播) -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.lossCost}</td>
        <!-- 惡意點擊數 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adInvalidClk}</td>
        <!-- 惡意點擊費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adInvalidClkPrice}</td>
        <!-- 預付儲值廣告金-儲值筆數 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.totalSaveCount}</td>
        <!-- 預付儲值廣告金-儲值金額 -->
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.totalSavePrice}</td>
        <!-- PChome 營運費用-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfpSave}</td>
        <!-- PChome 營運費用-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfpFree}</td>
        <!-- PChome 營運費用-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfpPostpaid}</td>
        <!-- PFB 分潤-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfbSave}</td>
        <!-- PFB 分潤-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfbFree}</td>
        <!-- PFB 分潤-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfbPostpaid}</td>
        <!-- PFD 佣金-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfdSave}</td>
        <!-- PFD 佣金-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfdFree}</td>
        <!-- PFD 佣金-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pfdPostpaid}</td>
    </tr>
    <tr>
    	<td class="td09" colspan="2" style="background-color:#99FFFF;" >平均</td>
    	<!-- 廣告曝光數 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adPv}</td>
        <!-- 廣告互動數 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adClk}</td>
        <!-- 互動率 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.clkRate}%</td>
        <!-- 單次互動費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adClkPriceAvg}</td>
        <!-- 每千次曝光費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adPvPrice}</td>
    	<!-- 廣告客戶數-直客-->
    	<td class="td10" style="background-color:#99FFFF;" >${avgVO.pfpClientCount}</td>
    	<!-- 廣告客戶數-業務-->
    	<td class="td10" style="background-color:#99FFFF;" >${avgVO.salesClientCount}</td>
	    <!-- 廣告客戶數-經銷商-->
	    <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfdClientCount}</td>
	    <!-- 廣告客戶數-總計-->
	    <td class="td10" style="background-color:#99FFFF;" >${avgVO.totalClientCount}</td>
	    <!-- 互動費用-直客 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpAdClkPrice}</td>
	    <!-- 互動費用-業務 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.salesAdClkPrice}</td>
        <!-- 互動費用-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfdAdClkPrice}</td>
        <!-- 互動費用-總計-->
	    <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.totalAdClkPriceCount}</td>
       	<!-- 每日預算-直客-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpAdActionMaxPrice}</td>
        <!-- 每日預算-業務-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.salesAdActionMaxPrice}</td>
        <!-- 每日預算-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfdAdActionMaxPrice}</td>
        <!-- 每日預算-總計-->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.totalAdActionMaxPrice}</td>
        <!-- 預算達成率-直客-->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfpSpendRate}%</td>
        <!-- 預算達成率-業務-->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.salesSpendRate}%</td>
        <!-- 預算達成率-經銷商-->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfdSpendRate}%</td>
        <!-- 預算達成率-總計-->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.totalSpendRate}%</td>
        <!-- 營運費用(超播) -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.lossCost}</td>
        <!-- 惡意點擊數 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adInvalidClk}</td>
        <!-- 惡意點擊費用 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adInvalidClkPrice}</td>
        <!-- 預付儲值廣告金-儲值筆數 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.totalSaveCount}</td>
        <!-- 預付儲值廣告金-儲值金額 -->
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.totalSavePrice}</td>
        <!-- PChome 營運費用-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpSave}</td>
        <!-- PChome 營運費用-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpFree}</td>
        <!-- PChome 營運費用-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpPostpaid}</td>
        <!-- PFB 分潤-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfbSave}</td>
        <!-- PFB 分潤-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfbFree}</td>
        <!-- PFB 分潤-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfbPostpaid}</td>
        <!-- PFD 佣金-儲值金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfdSave}</td>
        <!-- PFD 佣金-贈送金 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfdFree}</td>
        <!-- PFD 佣金-後付費 -->
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfdPostpaid}</td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>