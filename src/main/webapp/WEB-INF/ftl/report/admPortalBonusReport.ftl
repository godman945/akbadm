<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />付費刊登-PORTAL收益報表</h2>

<form action="admPortalBonusReport.html" method="post">

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

<table id="tableView" class="table01 tablesorter" style="min-width:1320px">
    <thead>
	    <tr>
	    	<th class="td09" rowspan="2" style="min-width:70px;" >日期</td>
	        <th class="td09" rowspan="2" style="min-width:30px;" >星期</td>
	        <th class="td09" colspan="3">PChome直客管理經銷商</td>
	        <th class="td09" colspan="3">DD-測試經銷商</td>
	        <th class="td09" colspan="3">PORTAL業務用</td>
	        <th class="td09" colspan="3">PChome收入</td>
	    </tr>
	    <tr>
	    	<th class="td09" style="min-width:100px;" >廣告執行</td>
	    	<th class="td09" style="min-width:100px;" >廣告客戶數</td>
	    	<th class="td09" style="min-width:100px;" >經銷商獎金</td>
	    	<th class="td09" style="min-width:100px;" >廣告執行</td>
	    	<th class="td09" style="min-width:100px;" >廣告客戶數</td>
	    	<th class="td09" style="min-width:100px;" >經銷商獎金</td>
	    	<th class="td09" style="min-width:100px;" >廣告執行</td>
	    	<th class="td09" style="min-width:100px;" >廣告客戶數</td>
	    	<th class="td09" style="min-width:100px;" >經銷商獎金</td>
	    	<th class="td09" style="min-width:100px;" >PChome網站總分潤</td>
	    	<th class="td09" style="min-width:100px;" >營運收入</td>
	    	<th class="td09" style="min-width:120px;" >收入總計</td>
	    </tr>
    </thead>
    <tbody>
	    <#list dataList as data>
		    <tr>
		        <td class="td09">${data.adPvclkDate}</td>
		        <td class="td09">${data.week}</td>
		        <td class="td10">$ ${data.portalValidPrice}</td>
		        <td class="td10">${data.portalPfpClientCount}</td>
		        <td class="td10">$ ${data.portalPfdBonus}</td>
		        <td class="td10">$ ${data.ddTestValidPrice}</td>
		        <td class="td10">${data.ddTestPfpClientCount}</td>
		        <td class="td10">$ ${data.ddTestPfdBonus}</td>
		        <td class="td10">$ ${data.salesValidPrice}</td>
		        <td class="td10">${data.salesPfpClientCount}</td>
		        <td class="td10">$ ${data.salesPfdBonus}</td>
		        <td class="td10">$ ${data.portalPfbBonus}</td>
		        <td class="td10">$ ${data.portalOperatingIncome}</td>
		        <td class="td10">$ ${data.incomeSum}</td>
		    </tr>
	    </#list>
    </tbody>
    <tr>
    	<td class="td09" colspan="2" style="background-color:#99FFFF;" >總計</td>
    	<td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.portalValidPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" ></td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.portalPfdBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.ddTestValidPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" ></td>
        <td class="td10" style="background-color:#99FFFF;" ">$ ${totalVO.ddTestPfdBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.salesValidPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" ></td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.salesPfdBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.portalPfbBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.portalOperatingIncome}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.incomeSum}</td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>