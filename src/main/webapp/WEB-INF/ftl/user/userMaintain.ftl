<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/javascript">

function showDetail(modelId) {
    window.open("privilegeModelDetail.html?modelId=" + modelId, "detail", "height=600,width=350,top=0,left=0,scrollbars=yes");
}

function addUser() {
    window.location = "userAdd.html";
}

function updateUser(userId) {
    document.getElementById("userId").value = userId;
    document.forms[1].action = "userUpdate.html";
    document.forms[1].submit();
}

function deleteUser(userId) {
    if (confirm("確定刪除帳號 [ " + userId + " ] ?")) {
        document.getElementById("userId").value = userId;
        document.forms[1].action = "doUserDelete.html";
        document.forms[1].submit();
    }
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳號維護</h2>
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
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addUser()"></td>
    </tr>
</table>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="200">帳號</td>
        <td class="td01" width="150">姓名</td>
        <td class="td01" width="200">權限模組</td>
        <td class="td01" width="100">狀態</td>
        <td class="td01" width="300">備註</td>
        <td class="td01" width="200">操作</td>
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
