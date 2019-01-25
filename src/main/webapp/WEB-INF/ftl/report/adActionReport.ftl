<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />總廣告成效</h2>

<form action="adActionReport.html" method="post">
<input type="hidden" name="adTypeText" id="adTypeText" value="" />
<input type="hidden" name="pfdCustomerInfoIdText" id="pfdCustomerInfoIdText" value="" />
<input type="hidden" name="payTypeText" id="payTypeText" value="" />

<table width="750">
    <tr>
        <td>廣告型式:
            <select id="adType" name="adType">
                <option value="">全部</option>
   	            <#list adTypeSelectOptionsMap?keys as skey>
		  		    <option value="${skey}" <#if adType?exists && adType = skey>selected="selected" </#if>>${adTypeSelectOptionsMap[skey]}</option>
		  	    </#list>
		    </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;帳戶序號:
            <input type="text" id="customerInfoId" name="customerInfoId" value="${customerInfoId!}">
        </td>
    </tr>
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly>
		</td>
	</tr>
    <tr>
        <td>經銷歸屬:
        	<select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部</option>
				<#if companyList?exists && (companyList?size>0)>
   	            	<#list companyList as company>
		  		    <option value="${company.customerInfoId}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId = company.customerInfoId>selected="selected" </#if>>${company.companyName}</option>
		  	    	</#list>
		  	    </#if>
		    </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;付款方式:
            <select id="payType" name="payType">
   	            <#list payTypeMap?keys as skey>
		  		    <option value="${skey}" <#if payType?exists && payType = skey>selected="selected" </#if>>${payTypeMap[skey]}</option>
		  	    </#list>
		    </select>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()">
            &nbsp;
            <input type="button" value="清除" onclick="doClear()">
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()">
        </td>
    </tr>
</table>
<!--alex測試-->
Daily Report報表測試
<input type="button" value="下載測試" onclick="downloadDailyReport();">


<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="800">
    <tr>
        <td class="td01" width="80">日期</td>
        <td class="td01" width="80">曝光數</td>
        <td class="td01" width="80">互動數</td>
        <td class="td01" width="80">互動率</td>
        <td class="td01" width="80">單次互動費用</td>
        <td class="td01" width="80">千次曝光費用</td>
        <td class="td01" width="80">費用</td>
        <td class="td01" width="80">超播金額</td>
        <td class="td01" width="80">查看攤提</td>
        <td class="td01" width="80">對帳</td>
    </tr>
    <#list dataList as data>
    <tr>
        <td class="td03"><a href="javascript:adActionDetailReport('${data.reportDate!}');">${data.reportDate!}</a></td>
        <td class="td04">${data.pvSum!}</td>
        <td class="td04">${data.clkSum!}</td>
        <td class="td04">${data.clkRate!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.pvPriceAvg!}</td>
        <td class="td04">${data.priceSum!}</td>
        <td class="td04">${data.overPriceSum!}</td>
        <td class="td03"><a href="javascript:everydayRecognizeReport('${data.reportDate!}', '${pfdCustomerInfoId}');">攤提明細</a></td>
        <td class="td03"><a href="javascript:checkBillReport('${data.searchDate!}');">對帳</a></td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
