<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >帳戶編號:${customerInfoId!}</div>
<br/>
<div style="font-size:16px;font-weight: bold;" >日期:${adPvclkDate!}</div>
<br/>
<#if voList?exists && (voList?size>0)>
<table id="tableDetalView" class="table01 tableDetalView tablesorter">
    <thead>
	    <tr>
	        <th class="td09" style="width:200px;" >聯播網址</td>
	        <th class="td09" style="width:120px;">曝光數</td>
	        <th class="td09" style="width:100px;">點擊數</td>
	        <th class="td09" style="width:80px;">點擊率</td>
	        <th class="td09" style="width:120px;">單次點擊收益</td>
	        <th class="td09" style="width:120px;">千次曝光收益</td>
	        <th class="td09" style="width:120px;">預估收益</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    <tr>
	        <td class="td09">${data.adPvclkUrl}</td>
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
    	<td class="td09" style="background-color:#99FFFF;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;" >${totalPv?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClk?string('#,###')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalClkRate?string('0.00')!}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdClkAvgPrice?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalAdPvRate?string('#,##0.00')!}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalBonus?string('#,##0.00')!}</td>
    </tr>
    
</table>
</#if>
