<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >廣告明細:${adTitle!}</div>
<br/>
<div style="font-size:16px;font-weight: bold;" >日期:${startDate!}到${endDate!}</div>
<br/>
<#if voList?exists && (voList?size>0)>
<table id="tableDetalView" class="table01 tableDetalView tablesorter">
    <thead>
	    <tr>
	        <th class="td09" style="width:100px;">日期</td>
	        <th class="td09" style="width:100px;">曝光數</td>
	        <th class="td09" style="width:100px;">互動數</td>
	        <th class="td09" style="width:100px;">互動率</td>
	        <th class="td09" style="width:100px;">平均互動出價</td>
	        <th class="td09" style="width:100px;">千次曝光出價</td>
	        <th class="td09" style="width:100px;">廣告費用</td>
	        <th class="td09" style="width:100px;">轉換次數</td>
	        <th class="td09" style="width:100px;">轉換率</td>
	        <th class="td09" style="width:100px;">總轉換價值</td>
	        <th class="td09" style="width:100px;">平均轉換成本</td>
	        <th class="td09" style="width:100px;">廣告投資報酬率</td>
	        
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    <tr>
	        <td class="td09">${data.adPvclkDate}</td>
	        <td class="td10">${data.kwPvSum!}</td>
	        <td class="td10">${data.kwClkSum!}</td>
	        <td class="td10">${data.kwClkRate!}</td>
	        <td class="td10">$ ${data.clkPriceAvg!}</td>
	        <td class="td10">$ ${data.pvPriceAvg!}</td>
	        <td class="td10">$ ${data.kwPriceSum!}</td>
	        <td class="td10">${data.convertCountSum!}</td>
	        <td class="td10">${data.convertCVR!}</td>
	        <td class="td10">${data.convertPriceCountSum!}</td>
	        <td class="td10">${data.convertCost!}</td>
	        <td class="td10">${data.convertInvestmentCost!}</td>
	        
	    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalVO.kwPvSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.kwClkSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.kwClkRate!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.clkPriceAvg!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.pvPriceAvg!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.kwPriceSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.convertCountSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.convertCVR!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.convertPriceCountSum!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.convertCost!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.convertInvestmentCost!}</td>
        
    </tr>
    
</table>
</#if>
