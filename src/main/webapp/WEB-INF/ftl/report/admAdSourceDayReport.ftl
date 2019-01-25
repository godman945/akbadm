<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />付費刊登-聯播網網站-統計日報表</h2>

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

<table id="tableView" class="table01 tablesorter" style="min-width:1080px">
    <thead>
	    <tr>
	    	<th class="td09" style="min-width:70px;" >日期</td>
	    	<th class="td09" style="min-width:110px;" >聯播網帳戶名稱</td>
	        <th class="td09" style="min-width:110px;" >廣告聯播網網站</td>
	        <th class="td09" style="min-width:60px;" >廣告客戶數</td>
	        <th class="td09" style="min-width:100px;" >廣告點擊費用</td>
	        <th class="td09" style="min-width:100px;" >廣告曝光數</td>
	        <th class="td09" style="min-width:80px;" >廣告點擊數</td>
	        <th class="td09" style="min-width:70px;" >點擊率</td>
	        <th class="td09" style="min-width:100px;" >單次點擊收益</td>
	        <th class="td09" style="min-width:110px;" >每千次曝光收益</td>
	        <th class="td09" style="min-width:100px;" >惡意點擊收益</td>
	        <th class="td09" style="min-width:100px;" >預估收益</td>
	    </tr>
    </thead>
    <tbody>
	    <#list dataList as data>
		    <tr>
		        <td class="td09">${data.adPvclkDate}</td>
		        <td class="td09">${data.pfbCustomerInfoName}</td>
		        <td class="td08">${data.pfbWebsiteChineseName}</td>
		        <td class="td10">${data.pfpCustomerInfoId}</td>
		        <td class="td10">$ ${data.adClkPriceSum}</td>
		        <td class="td10">${data.adPvSum}</td>
		        <td class="td10">${data.adClkSum}</td>
		        <td class="td10">${data.adClkRate}</td>
		        <td class="td10">$ ${data.adClkAvgPrice}</td>
		        <td class="td10">$ ${data.adPvRate}</td>
		        <td class="td10">$ ${data.invalidBonus}</td>
		        <td class="td10">$ ${data.totalBonus}</td>
		    </tr>
	    </#list>
    </tbody>
    <thead>
	    <tr>
	    	<th class="td09" colspan="3">小計</td>
	        <th class="td09">廣告客戶數</td>
	        <th class="td09">廣告點擊費用</td>
	        <th class="td09">廣告曝光數</td>
	        <th class="td09">廣告點擊數</td>
	        <th class="td09">點擊率</td>
	        <th class="td09">單次點擊收益</td>
	        <th class="td09">每千次曝光收益</td>
	        <th class="td09">惡意點擊收益</td>
	        <th class="td09">預估收益</td>
	    </tr>
    </thead>
    <#list sumList as sumData>
	    <tr>
	        <td class="td08" colspan="3">${sumData.pfbCustomerInfoName}</td>
	        <td class="td10"></td>
	        <td class="td10">$ ${sumData.adClkPriceSum}</td>
	        <td class="td10">${sumData.adPvSum}</td>
	        <td class="td10">${sumData.adClkSum}</td>
	        <td class="td10">${sumData.adClkRate}</td>
	        <td class="td10">$ ${sumData.adClkAvgPrice}</td>
	        <td class="td10">$ ${sumData.adPvRate}</td>
	        <td class="td10">$ ${sumData.invalidBonus}</td>
	        <td class="td10">$ ${sumData.totalBonus}</td>
	    </tr>
	</#list>
	<tr>
        <td class="td08" colspan="3" style="background-color:#99FFFF;" >加總</td>
        <td class="td10" style="background-color:#99FFFF;" ></td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkPriceSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adPvSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${totalVO.adClkRate}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adClkAvgPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.adPvRate}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.invalidBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${totalVO.totalBonus}</td>
    </tr>
    <tr>
        <td class="td08" colspan="3" style="background-color:#99FFFF;" >平均</td>
        <td class="td10" style="background-color:#99FFFF;" ></td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adClkPriceSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adPvSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adClkSum}</td>
        <td class="td10" style="background-color:#99FFFF;" >${avgVO.adClkRate}%</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adClkAvgPrice}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.adPvRate}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.invalidBonus}</td>
        <td class="td10" style="background-color:#99FFFF;" >$ ${avgVO.totalBonus}</td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>