<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div>
	<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶會員管理</h2>
		
	<table>
	    <tr>
	    	<td>帳戶名稱：</td>
    		<td><input type="text" id="keyword" name="keyword" ></td>
	    	<td>會員帳號：</td>
    		<td><input type="text" id="userAccount" name="userAccount" ></td>
    	</tr>
    	<tr>
	    	<td>EMAIL：</td>
    		<td><input type="text" id="userEmail" name="userEmail" ></td>
	    	<td>帳戶使用狀態：</td>
    		<td>
                <select id="customerInfoStatus" name="customerInfoStatus">
                	<option value="">全部</option>
                	<#list accountStatus as status>
                		<option value="${status.status!}">${status.description!}</option>
                    </#list>
                </select>
    		</td>
    	</tr>
    	
    	
    	<tr>
	    	<td>經銷商歸屬：</td>
    		<td>
                <select id="pfdCustomerSelect" name="pfdCustomerSelect">
                	<option value="">全部</option>
                	<#if accountList?exists>
        				<#list accountList as account>
							<option value="${account.customerInfoId!}">${account.companyName!}</option>
						</#list>
					</#if>
                </select>
    		</td>
    	</tr>
    	<tr>
        	<td ="2"><input type="button" id="searchBtn" value="查詢" ></td>
    	</tr>
	</table>
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶列表</h2>
<div id="accountListTable"></div>

</div>