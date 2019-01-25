<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />禁用字詞管理</h2>

<form action="illegalKeywordList.html" method="post">
<input type="hidden" id="targetSeq" name="targetSeq" value="">
<input type="hidden" id="modifyContent" name="modifyContent" value="">

<table width="100%">
    <tr>
        <td>搜尋禁用字詞:
        	<input type="text" name="queryString" value="${queryString!}" />
            <input type="submit" value="查詢" />
		</td>
	</tr>
</table>

<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />禁用字設定</h2>
<table width="95%">
    <tr>
        <td>
            <input type="text" name="addContent" value="" size="40" maxlength="50" />
            <input type="button" id="addBtn" value="新增" onclick="addIllegalKeyword()"> ( 限 50 字元 )
        </td>
    </tr>
</table>

<#if illegalKeywordList?exists>
<@t.insertAttribute name="page" />
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="20">NO.</td>
        <td class="td01" width="200">禁用字詞</td>
        <td class="td01" width="60">操作</td>
        <td class="td01" width="50">建立時間</td>
        <td class="td01" width="50">修改時間</td>
    </tr>

    <#assign index=(pageNo-1)*pageSize>
    <#list illegalKeywordList as ill_kw>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index}</td>
        <td class="td02">
            <input type="text" id="content_${index}" value="${ill_kw.content!}" size="40" maxlength="50" />
        </td>
        <td class="td02">
            <input type="button" value="修改" onclick="updateIllegalKeyword('${ill_kw.seq!}', 'content_${index!}')">
            <input type="button" value="刪除" onclick="deleteIllegalKeyword('${ill_kw.seq!}', '${ill_kw.content!}')">
        </td>
        <td class="td03">${ill_kw.createDate?string("yyyy-MM-dd")}</td>
        <td class="td03">${ill_kw.updateDate?string("yyyy-MM-dd")}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
