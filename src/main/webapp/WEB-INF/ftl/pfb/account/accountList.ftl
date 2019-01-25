

<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

function updateAccount(targetId) {
    document.getElementById("targetId").value = targetId;
    document.forms[0].action = "pfbAccountUpdate.html";
    document.forms[0].submit();
}

</script>
<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />聯播網帳戶資料查詢</h2>

<form action="pfbAccountList.html" method="post">
<input type="hidden" id="targetId" name="targetId" value="">

<table>
    <tr>
        <td>申請日期：</td>
        <td><input type="text" id="startDate" name="queryStartDate" value="${queryStartDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="queryEndDate" value="${queryEndDate!}"  readonly/>
        </td>
    </tr>
    <tr>
        <td>查詢條件：</td>
        <td>
            <select id="queryType" name="queryType" style="width:80px;">
                <#list queryTypeOptionsMap?keys as key>
                <option value="${key}"<#if queryType?exists && queryType = key> selected</#if>>${queryTypeOptionsMap[key]}</option>
                </#list> 
            </select>
            &nbsp;&nbsp;
            <input type="text" id="queryText" name="queryText" value="${queryText!}" />
        </td>
    </tr>
    <tr>
    	<td>帳戶類別：</td>
    	<td>
    		<select id="queryCategory" name="queryCategory" style="width:80px;">
                <#list queryCategoryOptionsMap?keys as key>
                <option value="${key}"<#if queryCategory?exists && queryCategory = key> selected</#if>>${queryCategoryOptionsMap[key]}</option>
                </#list> 
            </select>
    	</td>
    </tr>
    <tr>
        <td>帳戶狀態：</td>
        <td>
            <select id="queryStatus" name="queryStatus" style="width:80px;">
                <#list queryStatusOptionsMap?keys as key>
                <option value="${key}"<#if queryStatus?exists && queryStatus = key> selected</#if>>${queryStatusOptionsMap[key]}</option>
                </#list>
            </select>
        </td>
    </tr>
    <tr>
    	<td align="center" colspan="2">
    		<input type="submit" value="查詢" />&nbsp;&nbsp;
    		<input type="button" value="清除" onclick="doClear()" />
    	</td>
    </tr>
</table>

</form>

<br>

<table id="tableView" class="table01 tablesorter" style="word-wrap:break-word;word-break:break-all" >
    <thead>
	    <tr>
	        <th class="td01" style="width:70px;background-color:#CDE5FF;" >申請日期</th>
	        <th class="td01" style="width:120px;background-color:#CDE5FF;" >帳戶編號</th>
	        <th class="td01" style="width:120px;background-color:#CDE5FF;" >會員帳號</th>
	        <th class="td01" style="width:60px;background-color:#CDE5FF;" >帳戶類別</th>
	        <th class="td01" style="width:150px;background-color:#CDE5FF;" >公司名稱</th>
	        <th class="td01" style="width:70px;background-color:#CDE5FF;" >統一編號</th>
	        <th class="td01" style="width:150px;background-color:#CDE5FF;" >聯絡人</th>
	        <th class="td01" style="width:60px;background-color:#CDE5FF;" >狀態</th>
	        <th class="td01" style="background-color:#CDE5FF;" >網址</td>
	        <th class="td01" style="width:70px;background-color:#CDE5FF;" >狀態日期</th>
	        <th class="td01" style="width:80px;background-color:#CDE5FF;" >撥放管理</th>
	    </tr>
    </thead>
<#if accountList?exists>
	<tbody>
    <#list accountList as account>
	    <tr>
	        <td class="td03">${account.createDate?string("yyyy-MM-dd")}</td>
	        <td class="td03"><a href="javascript:updateAccount('${account.customerInfoId!}');">${account.customerInfoId}</a></td>
	        <td class="td03">${account.memberId}</td>
	        <td class="td03">${queryCategoryOptionsMap[account.category]}</td>
	        <td class="td03">
	            <#if account.category == "1">
	                ${account.contactName}
	            <#else>
	                ${account.companyName}
	            </#if>
	        </td>
	        <td class="td03">${account.taxId}</td>
	        <td class="td03">${account.contactName}</td>
	        <td class="td03">${queryStatusOptionsMap[account.status]}</td>
	        <td class="td02">${account.websiteDisplayUrl}</td>
	        <td class="td03">
	            <#if account.activateDate?exists
	                && account.status != '0' >${account.updateDate?string("yyyy-MM-dd")}</#if></td>
	        <td class="td03"><#if '${account.adType!}' == '2' >內容廣告
	                        <#elseif '${account.adType!}' == '1'>搜尋廣告
	                        <#elseif '${account.adType!}' == '3'>搜尋廣告<br/>內容廣告
	                        <#elseif '${account.adType!}' == '4'>封鎖權限</#if></td>
	    </tr>
    </#list>
    </tbody>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">
