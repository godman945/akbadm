<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />付費刊登-未來30天統計日報表</h2>

<form action="admAdSourceDayReport.html" method="post">

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
        <td colspan="5" align="center">
            <br>
            <input type="button" value="下載" onclick="doDownlaod()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table id="tableView" class="table01 tablesorter" style="min-width:600px">
    <thead>
	    <tr>
	    	<th class="td09" style="min-width:20%;" >日期</td>
	        <th class="td09" style="min-width:20%;" >廣告客戶數</td>
	        <th class="td09" style="min-width:20%;" >廣告活動數</td>
	        <th class="td09" style="min-width:20%;" >廣告明細數</td>
	        <th class="td09" style="min-width:20%;" >廣告花費</td>
	    </tr>
    </thead>
    <tbody>
	    <#list dataList as data>
		    <tr>
		        <td class="td09">${data.countDate}(${data.week})</td>
		        <td class="td10">${data.pfpCount}</td>
		        <td class="td10">${data.pfpAdActionCount}</td>
		        <td class="td10">${data.pfpAdCount}</td>
		        <td class="td10">$ ${data.pfpAdActionMaxPrice}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
        <td class="td09" style="background-color:#99FFFF;" >平均</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfpCount}</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfpAdActionCount}</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.pfpAdCount}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.pfpAdActionMaxPrice}</td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>