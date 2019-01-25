<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<center><div class="titel_t">使用者登入</div></center>

<form name="loginForm" id="loginForm" action="<@s.url value='/loginCheck.html' />" method="post">
<center>
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">帳號(E-mail)</td>
        <td class="td02"> <input type="text" name="email" id="email" value="${email!}" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">密碼</td>
        <td class="td02"> <input type="password" name="password" id="password" /></td>
    </tr>
</table>
</center>
<br>
<center>
<input type="submit" value="登入">
</center>

</form>

<input type="hidden" id="messageId" value="${message!}">
