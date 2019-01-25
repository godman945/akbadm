<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div style="font-size:16px;font-weight: bold;" >共${detailVoList?size!}筆記錄</div>
<br/>
<#if detailVoList?exists && (detailVoList?size>0)>
<table id="tableDetalView" class="table01 tableDetalView tablesorter">
    <thead>
	    <tr>
	        <th class="td09" style="width:180px;">版位編號</td>
	        <th class="td09" style="width:120px;">點擊區間</td>
	        <th class="td09" style="width:120px;">點擊數</td>
	        <th class="td09" style="width:120px;">點擊費用</td>
	    </tr>
    </thead>
    <tbody>
	    <#list detailVoList as data>
	    <tr>
	        <td class="td09">${data.pfbxPositionId}</td>
	        <td class="td09">${data.recordTime}</td>
	        <td class="td10">${data.adClk}</td>
	        <td class="td10">$ ${data.adPrice}</td>
	    </tr>
	    </#list>
    </tbody>
</table>
</#if>
