<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />總使用明細</h2>

<form action="adReport.html" method="post">

<table width="750">
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}" readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
            &nbsp;&nbsp;經銷歸屬:
        	<select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部</option>
				<#if companyList?exists && (companyList?size>0)>
   	            	<#list companyList as company>
		  		    <option value="${company.customerInfoId}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId = company.customerInfoId>selected="selected" </#if>>${company.companyName}</option>
		  	    	</#list>
		  	    </#if>
		    </select>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="600">
    <tr>
        <td class="td01" width="80">日期</td>
        <td class="td01" width="80">帳戶儲值</td>
        <td class="td01" width="80">營業稅</td>
        <td class="td01" width="80">廣告活動花費</td>
        <td class="td01" width="80">惡意點擊費用</td>
        <td class="td01" width="80">退款</td>
        <td class="td01" width="80">免費贈送</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03"><a href="javascript:totalTransDetail('${data.reportDate!}');">${data.reportDate!}</a></td>
        <td class="td04">${data.add?string('###,###,###.##')}</td>
        <td class="td04">${data.tax?string('###,###,###.##')}</td>
        <td class="td04">${data.spend?string('###,###,###.##')}</td>
        <td class="td04">${data.invalid?string('###,###,###.##')}</td>
        <td class="td04">${data.refund?string('###,###,###.##')}</td>
        <td class="td04">${data.free?string('###,###,###.##')}</td>
    </tr>
    </#list>

    <tr>
        <td class="td03">小計</td>
        <td class="td04">${add_sum?string('###,###,###.##')}</td>
        <td class="td04">${tax_sum?string('###,###,###.##')!}</td>
        <td class="td04">${spend_sum?string('###,###,###.##')}</td>
        <td class="td04">${invalid_sum?string('###,###,###.##')}</td>
        <td class="td04">${refund_sum?string('###,###,###.##')}</td>
        <td class="td04">${free_sum?string('###,###,###.##')}</td>
    </tr>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
