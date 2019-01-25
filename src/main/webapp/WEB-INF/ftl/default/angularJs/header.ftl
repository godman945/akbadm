<#assign s=JspTaglibs["/struts-tags"]>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="2">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="500">
                        <a href="<@s.url value="/" />"><img src="html/img/logo_pchome.gif" border="0" /></a>
                        <a href="<@s.url value="/" />"><img src="html/img/logo_index.gif" border="0" /></a>
                        <a href="<@s.url value="/" />"><img src="html/img/logo2.gif" border="0" /></a>
                    </td>
                    <td align="right">
                        <div class="t13">
                        <#if Session.session_user_id?exists && Session.session_user_id != "">使用者：<br />
                            <strong><img src="html/img/webicon15.gif" width="16" height="15" align="absmiddle" /> ${Session.session_user_id} </strong>(<a href="<@s.url value="/" />logout.html">登出</a>)
                        <#else>
                            <a href="<@s.url value="/" />login.html">登入</a>&nbsp;
                        </#if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td height="3" colspan="2" bgcolor="#3D7CD3"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
