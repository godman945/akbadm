<#assign s=JspTaglibs["/struts-tags"]>

<#if Session.session_user_privilege?exists>
    <ul id="browser" class="filetree">
    <#assign menuList = Session.session_user_privilege>
    <#list menuList as menu>
        <li><span class="folder"><img src="html/img/ico_fo.gif" width="19" height="17" border="0" />${menu.displayName!}</span>
        <#if menu.childrenList?exists>
            <#assign childrenList = menu.childrenList>
            <#list childrenList as childMenu>
                <#if childMenu.checkedFlag?exists && childMenu.checkedFlag == "checked">
                    <ul><li><span class="file"><a href="${request.contextPath}/${childMenu.action!}" ><img src="${request.contextPath}/html/img/ico_fo_11.gif" border="0" />${childMenu.displayName!}</a></span></ul>
                </#if>
            </#list>
        </#if>
    </#list>
    </ul>
</#if>
