<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/javascript">

function showDetail(modelId) {
    window.open("privilegeModelDetail.html?modelId=" + modelId, "detail", "height=600,width=350,top=0,left=0,scrollbars=yes");
}

function addPrivilegeModel() {
    window.location = "privilegeModelAdd.html";
}

function updatePrivilegeModel(modelId) {
    document.getElementById("modelId").value = modelId;
    document.forms[0].action = "privilegeModelUpdate.html";
    document.forms[0].submit();
}

function deletePrivilegeModel(modelId, modelName) {
    if (confirm("確定刪除範本 [ " + modelName + " ] ?")) {
        document.getElementById("modelId").value = modelId;
        document.forms[0].action = "doPrivileModelgeDelete.html";
        document.forms[0].submit();
    }
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />權限範本維護</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addPrivilegeModel()"></td>
    </tr>
</table>
<br>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="200">範本名稱</td>
        <td class="td01" width="400">範本備註</td>
        <td class="td01" width="150">建立時間</td>
        <td class="td01" width="150">更新時間</td>
        <td class="td01" width="150">操作</td>
    </tr>
<#if privilegeModelList?exists>
    <#assign index=0>
    <#list privilegeModelList as privilegeMode>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td03"><a href="javascript:showDetail('${privilegeMode.modelId!}');">${privilegeMode.modelName!}</a></td>
        <td class="td02">${privilegeMode.note!}</td>
        <td class="td03">${privilegeMode.createDate?string("yyyy-MM-dd")}</td>
        <td class="td03">${privilegeMode.updateDate?string("yyyy-MM-dd")}</td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="updatePrivilegeModel('${privilegeMode.modelId!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deletePrivilegeModel('${privilegeMode.modelId!}', '${privilegeMode.modelName!}');">
        </td>
    </tr>
    </#list>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">

<form action="" method="post">
    <input type="hidden" id="modelId" name="modelId" value="">
</form>
