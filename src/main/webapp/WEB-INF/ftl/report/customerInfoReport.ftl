<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新帳戶開通數</h2>

<form id="customerInfoForm" method="post">
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
		<td>
			儲值類型:
<#-- 強哥加的，service 也沒用到，不知用意
            <select id="transType" name="transType" style="display:none">
   	            <#list transTypeMap?keys as skey>
		  		    <option value="${skey}" <#if transType?exists && transType = skey>selected="selected" </#if>>${transTypeMap[skey]}</option>
		  	    </#list>
		    </select>
-->
            <select id="orderType" name="orderType">
   	            <#list orderTypeMap?keys as skey>
		  		    <option value="${skey}">${orderTypeMap[skey]}</option>
		  	    </#list>
		    </select>
		    &nbsp;&nbsp;付款方式:
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

<div id="customerInfoTable"></div>
