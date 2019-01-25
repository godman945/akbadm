<#assign s=JspTaglibs["/struts-tags"]>

<div class="cont">
	<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />經銷商帳務管理</h2>
	
	<div  class="grtba">
		
<table width="95%" class="srchtb">
    <tr>
    	<td align="right">開始月份</td>
		<td> 
        	<input type="text" id="invoiceStrDate" name="invoiceStrDate" value="${invoiceStrDate!}" readonly="readonly" />      
		</td>
    	<td align="right">結束月份</td>
		<td align="left">
        	<input type="text" id="invoiceEndDate" name="invoiceEndDate" value="${invoiceEndDate!}" readonly="readonly"/>
		</td>
	</tr>
	
	<tr>
		<td align="right">經銷商</td>
		<td align="left">
            <select id="pfdCustomerInfoId" name="pfdCustomerInfoId" >
            	<option value="" selected="selected">全部</option>
            	<#list pfdCustomerInfo as customerInfo>
		    		<option value="${customerInfo.customerInfoId}" >${customerInfo.companyName}</option>
		    	</#list>
		    </select>
		</td>
	</tr>
	
    <tr>
    	<td colspan="4" align="center"></td>
	</tr>
	
	<tr>
		<td colspan="4" align="center">
		    <input type="button" id="search" value="查詢發票" />		    
		</td>
	</tr>
</table>
</from>
</div>

<br>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />發票列表</h2>
	<div  class="grtba">
	<input type="button" id="balanceBtn" name="balanceBtn" value="沖帳" />
	<input type="button" id="balanceReportBtn" name="balanceReportBtn" value="沖帳報表" onclick="balanceReport()"/>
		<div id="invoiceTable"></div>
	</div>
</div>

