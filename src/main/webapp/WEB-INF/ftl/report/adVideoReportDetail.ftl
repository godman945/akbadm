<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<#if vo?exists>
	<div style="font-size:16px;font-weight: bold;">日期：${vo.adVideoDate?string("yyyy-MM-dd")}</div>
	<div style="font-size:16px;font-weight: bold;">廣告活動：${vo.adActionName!}</div>
	<div style="font-size:16px;font-weight: bold;">廣告分類：${vo.adGroupName!}</div>
	<br/>
	<table class="table01 tablesorter">
	    <thead>
		    <tr>
		        <th class="td09" style="width:120px;" rowspan="2">收視人數(不重複)</th>
		        <th class="td09" style="width:120px;" rowspan="2">重播次數</th>
		        <th class="td09" style="width:120px;" rowspan="2">聲音開啟次數</th>
		        <th class="td09" style="width:480px;" colspan="4">影片播放進度</th>
		        <th class="td09" style="width:120px;" rowspan="2">完整播放率</th>
		    </tr>
		    <tr>
		        <th class="td09" style="width:120px;">25%</th>
		        <th class="td09" style="width:120px;">50%</th>
		        <th class="td09" style="width:120px;">75%</th>
		        <th class="td09" style="width:120px;">100%</th>
		    </tr>
	    </thead>
	    <tbody>
		    <tr>
		    	<td class="td09">${vo.adVideoUniq?string('#,###')}</td>
		    	<td class="td09">${vo.adVideoReplay?string('#,###')}</td>
		        <td class="td09">${vo.adVideoMusic?string('#,###')}</td>
		        <td class="td09">${vo.adVideoProcess25?string('#,###')}</td>
		        <td class="td09">${vo.adVideoProcess50?string('#,###')}</td>
		        <td class="td09">${vo.adVideoProcess75?string('#,###')}</td>
		        <td class="td09">${vo.adVideoProcess100?string('#,###')}</td>
		        <td class="td09">${vo.adVideoProcessRate?string('#,##0.00')}%</td>
		    </tr>
	    </tbody>
	</table>
<#else>
	${message!}
</#if>
