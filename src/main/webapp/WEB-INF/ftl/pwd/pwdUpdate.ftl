<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />密碼變更</h2>
<form action="doPwdUpdate.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">帳號(E-mail)</td>
        <td class="td02"> ${userId!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">密碼</td>
        <td class="td02"> <input type="text" name="paramPassword" value="${paramPassword!}" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">確認密碼</td>
        <td class="td02"> <input type="text" name="paramRePassword" value="${paramRePassword!}" /> ( 限 20 字元 )</td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="儲存">
        </td>
    </tr>
</table>
<input type="hidden" name="userId" value="${userId!}" />
</form>

<input type="hidden" id="messageId" value="${message!}">
