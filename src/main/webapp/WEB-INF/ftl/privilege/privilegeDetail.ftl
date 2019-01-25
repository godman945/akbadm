<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<table width="300">
    <tr>
        <td class="td05">範本明細</td>
    </tr>
</table>
<br>
<table width="300" class="table01">
    <tr>
        <td width="100" class="td01">範本名稱</td>
        <td class="td02">${privilegeModelVO.modelName!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">範本備註 </td>
        <td class="td02">${privilegeModelVO.note!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">權限設定</td>
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
                            <input type="checkbox" disabled="disabled" ${childMenu.checkedFlag!}> ${childMenu.displayName!}
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
<table width="300">
    <tr>
        <td align="center">
            <input type="button" value="關閉" onclick="javascript:window.close();">
        </td>
    </tr>
</table>

<input type="hidden" id="messageId" value="${message!}">
