<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >帳戶編號:${pfbCustomerInfoId!}</div>
<div style="font-size:16px;font-weight: bold;" >帳戶名稱:${pfbCustomerInfoName!}</div>
<div style="font-size:16px;font-weight: bold;" >網站名稱:${allowUrlName}</div>
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
	        <th class="td09" style="width:100px;">點擊數</td>
	        <th class="td09" style="width:100px;">點擊率</td>
	        <th class="td09" style="width:100px;">平均點擊費用</td>
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
	        <td class="td10">$ ${data.adClkPriceSum}</td>
	    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="2" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalPv?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClk?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClkRate?string('0.00')!}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkAvgPrice?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkPriceSum?string('#,###')!}</td>
    </tr>
    
</table>
</#if>
