<center><div class="titel_t">權限範本設定</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">權限範本修改</td>
    </tr>
</table>
<br>
<form action="doPrivilegeModelUpdate.html" method="post">
<table width="500" class="table01">
    <tr>
        <td width="100" class="td01">範本名稱 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="modelName" value="${modelName!}" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">範本備註 </td>
        <td class="td02">
            <textarea name="note" cols=25 rows=2>${note!}</textarea> ( 限 50 字元 )
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">權限設定 <font color="red">*</font></td>
        <td class="td02">
            <table border="0">
                <#list allMenuList as menu>
                <tr>
                    <td><b>${menu.displayName!}</b><td>
                </tr>
                <#if menu.childrenList?exists>
                    <#list menu.childrenList as childMenu>
                    <tr>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="checkbox" name="newMenuIds" value="${childMenu.menuId!}" ${childMenu.checkedFlag!}> ${childMenu.displayName!}
                        <td>
                    </tr>
                    </#list>
                </#if>
                </#list>
            </table>
        </td>
    </tr>
</table>
<br>
<table width="500">
    <tr>
        <td align="center">
            <input type="submit" id="updateBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='privilegeModelMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="modelId" name="modelId" value="${modelId!}">
</form>

<input type="hidden" id="messageId" value="${message!}">
