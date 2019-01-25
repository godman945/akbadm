<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >網站類型:${websiteCategoryName!}</div>
<div style="font-size:16px;font-weight: bold;" >裝置:${adDeviceMap[searchAdDevice]!}</div>
<br/>
<div style="font-size:16px;font-weight: bold;" >日期:${adPvclkDate!}</div>
<br/>
<#if voList?exists && (voList?size>0)>
<table id="tableDetalView" class="table01 tableDetalView tablesorter">
    <thead>
	    <tr>
	        <th class="td09" style="width:180px;" >PFP帳戶名稱</td>
	        <th class="td09" style="width:100px;">時段區間</td>
	        <th class="td09" style="width:100px;">曝光數</td>
	        <th class="td09" style="width:100px;">互動數</td>
	        <th class="td09" style="width:100px;">互動率</td>
	        <th class="td09" style="width:100px;">平均互動費用</td>
	        <th class="td09" style="width:100px;">千次曝光費用</td>
	        <th class="td09" style="width:100px;">廣告費用</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    <tr>
	        <td class="td09">${data.pfpCustomerInfoName}</td>
	        <td class="td09">${data.timeCode}</td>
	        <td class="td10">${data.adPvSum}</td>
	        <td class="td10">${data.adClkSum}</td>
	        <td class="td10">${data.adClkRate}</td>
	        <td class="td10">$ ${data.adClkAvgPrice}</td>
	        <td class="td10">$ ${data.adPvAvgPrice}</td>
	        <td class="td10">$ ${data.adClkPriceSum}</td>
	    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="2" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalVO.adPvSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkRate}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkAvgPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adPvAvgPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkPriceSum}</td>
    </tr>
</table>
</#if>
