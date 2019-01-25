<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告客戶查詢</h2>
<form action="userQuery.html" method="post">
<table width="100%">
    <tr>
        <td>帳號:
        	<input type="text" name="queryUserId" value="${queryUserId!}" />        
            &nbsp;&nbsp;姓名:
            <input type="text" name="queryUserName" value="${queryUserName!}" />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="查詢" />
		</td>
	</tr>
</table>
</form>
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="50">開發狀態</td>
        <td class="td01" width="200">歸屬</td>
        <td class="td01" width="150">帳戶狀態</td>
        <td class="td01" width="200">帳戶編號</td>
        <td class="td01" width="100">帳戶名稱</td>
        <td class="td01" width="300">統一編號</td>
        <td class="td01" width="200">公司名稱</td>
    </tr>
<#if userList?exists>
    <#assign index=0>
    <#list userList as user>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td02">${user.userEmail!}</td>
        <td class="td03">${user.userName!}</td>
        <td class="td03"><a href="javascript:showDetail('${user.modelId!}');">${privilegeModelMap[user.modelId]!}</a></td>
        <td class="td03">${userStatusMap[user.status]!}</td>
        <td class="td02">${user.note!}</td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="updateUser('${user.userEmail!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deleteUser('${user.userEmail!}');">
        </td>
    </tr>
    </#list>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">

<form action="" method="post">
    <input type="hidden" id="userId" name="userId" value="">
</form>
