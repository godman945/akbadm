<script type="text/javascript">

function addChildDept() {

    var rowCount = document.getElementById("childDeptTab").rows.length;

    var Tr = document.getElementById("childDeptTab").insertRow(-1);

    var Td1 = Tr.insertCell(Tr.cells.length);
    Td1.innerHTML = '<input type="text" name="childDeptName" value="" size="20" maxlength="20" />';
    Td1.className = "td03";

    var Td2 = Tr.insertCell(Tr.cells.length);
    Td2.innerHTML = '<input type="text" name="childDeptSort" value="0" size="2" maxlength="2" />';
    Td2.className = "td03";

    var Td3 = Tr.insertCell(Tr.cells.length);
    Td3.innerHTML = '&nbsp;';
    Td3.className = "td03";
}

function removeChildDept() {
    var rowCount = document.getElementById("childDeptTab").rows.length;
    if (rowCount>2) {
        document.getElementById("childDeptTab").deleteRow(rowCount-1);
    }
}

</script>

<center><div class="titel_t">部門組織設定</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">新增部門</td>
    </tr>
</table>
<br>
<form action="doDepartmentAdd.html" method="post">
<table width="400">
    <tr><td>部門設定：</td></tr>
</table>
<table width="400" class="table01">
    <tr>
        <td width="100" class="td01">部門名稱 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="parentDeptName" value="${parentDeptName!}" size="20" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">排序 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="parentSort" value="${parentSort!}" size="2" maxlength="2" /> ( 限數字 )</td>
    </tr>
</table>
<br>
<table width="400">
    <tr>
        <td align="left">小組設定：</td>
        <td align="right">
            <input type="button" id="addBtn" value="增加欄位" onclick="addChildDept();">
            <input type="button" id="addBtn" value="減少欄位" onclick="removeChildDept();">
        </td>
    </tr>
</table>
<table width="400" class="table01" id="childDeptTab">
    <tr>
        <td class="td01" width="300">小組名稱</td>
        <td class="td01" width="50">排序</td>
        <td class="td01" width="50">操作</td>
    </tr>
<#assign index=0>
<#if childDeptList?exists>
    <#list childDeptList as dept>
    <#assign index = index+1>
    <tr>
        <td class="td03"><input type="text" name="childDeptName" value="${dept.depName!}" size="20" maxlength="20" /></td>
        <td class="td03"><input type="text" name="childDeptSort" value="${dept.sort!}" size="2" maxlength="2" /></td>
        <td class="td03">
            <input type="button" value="移除" onclick="removeChildDept('${index!}');">
        </td>
    </tr>
    </#list>
</#if>
</table>
<font color="red">※ 新增、修改、刪除皆儲存後才會生效！</font>
<br>
<table width="400">
    <tr>
        <td align="center">
            <input type="submit" id="saveBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='departmentMaintain.html'">
        </td>
    </tr>
</table>
</form>

<input type="hidden" id="messageId" value="${message!}">
