<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

function addDepartment() {
    window.location = "departmentAdd.html";
}

function updateDepartment(deptId) {
    document.getElementById("deptId").value = deptId;
    document.forms[0].action = "departmentUpdate.html";
    document.forms[0].submit();
}

function deleteDepartment(deptId, deptName) {
    if (confirm("確定刪除部門 [ " + deptName + " ] ?")) {
        document.getElementById("deptId").value = deptId;
        document.forms[0].action = "doDepartmentDelete.html";
        document.forms[0].submit();
    }
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />部門組織設定</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addDepartment()"></td>
    </tr>
</table>
<br>
<table class="table01" width="800">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="150">部門名稱</td>
        <td class="td01" width="50">排序</td>
        <td class="td01" width="400">小組</td>
        <td class="td01" width="100">操作</td>
    </tr>
<#if parentDeptList?exists>
    <#assign index=0>
    <#list parentDeptList as dept>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td03">${dept.deptName!}</td>
        <td class="td03">${dept.sort!}</td>
        <td class="td02">&nbsp; ${dept.childDept!}</td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="updateDepartment('${dept.deptId!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deleteDepartment('${dept.deptId!}', '${dept.deptName!}');">
        </td>
    </tr>
    </#list>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">

<form action="" method="post">
    <input type="hidden" id="deptId" name="deptId" value="">
</form>
