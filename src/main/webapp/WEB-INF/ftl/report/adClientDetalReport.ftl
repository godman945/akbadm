<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >帳戶編號:${pfpCustomerInfoId!}</div>
<div style="font-size:16px;font-weight: bold;" >帳戶名稱:${pfpCustomerInfoName!}</div>
<br/>
<div style="font-size:16px;font-weight: bold;" >日期:${adPvclkDate!}</div>
<br/>
<#if voList?exists && (voList?size>0)>
<table id="tableDetalView" class="table01 tableDetalView tablesorter">
    <thead>
	    <tr>
	        <th class="td09" style="width:120px;" >帳戶名稱</td>
	        <th class="td09" style="width:120px;" >主網站名稱</td>
	        <th class="td09" style="width:120px;" >網站名稱</td>
	        <th class="td09" style="width:120px;" >時段區間</td>
	        <th class="td09" style="width:120px;">曝光數</td>
	        <th class="td09" style="width:100px;">互動數</td>
	        <th class="td09" style="width:80px;">互動率</td>
	        <th class="td09" style="width:120px;">單次互動收益</td>
	        <th class="td09" style="width:120px;">千次曝光收益</td>
	        <th class="td09" style="width:120px;">預估收益</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    <tr>
	    	<td class="td09">${data.pfbCustomerInfoName}</td>
	    	<td class="td09">${data.pfbDefaultWebsiteChineseName}</td>
	        <td class="td09">${data.pfbWebsiteChineseName}</td>
	        <td class="td09">${data.timeCode}</td>
	        <td class="td10">${data.adPvSum}</td>
	        <td class="td10">${data.adClkSum}</td>
	        <td class="td10">${data.adClkRate}</td>
	        <td class="td10">$ ${data.adClkAvgPrice}</td>
	        <td class="td10">$ ${data.adPvRate}</td>
	        <td class="td10">$ ${data.totalBonus}</td>
	    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="4" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalPv?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClk?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClkRate?string('0.00')!}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkAvgPrice?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdPvRate?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalBonus?string('#,##0.00')!}</td>
    </tr>
    
</table>
</#if>
