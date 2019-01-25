<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶每日儲值</h2>

<form id="orderForm" method="post">
<table width="750">
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
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
        <td>付款方式:
            <select id="payType" name="payType">
   	            <#list payTypeMap?keys as skey>
		  		    <option value="${skey}">${payTypeMap[skey]}</option>
		  	    </#list>
		    </select>
		</td>
	</tr>

    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" id="search" value="查詢" />
            &nbsp;
            <input type="button" id="download" value="下載" />
        </td>
    </tr>
</table>
</form>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<div id="orderTable"></div>


