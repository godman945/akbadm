<#assign s=JspTaglibs["/struts-tags"]>

<div id="dialog" class="dialog" ></div>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />系統公告</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增"></td>
    </tr>
</table>
<br>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="5%">流水號</td>
        <td class="td01" width="10%">公告類型</td>
        <td class="td01" width="10%">公告種類</td>
        <td class="td01" width="35%">公告內容</td>
        <td class="td01" width="15%">上線日期</td>
        <td class="td01" width="15%">下線日期</td>
        <td class="td01" width="10%">操作</td>
    </tr>
<#if boardList?exists>
    <#list boardList as boards>
    <tr>
        <td class="td03">${boards_index+1}</td>
        <td class="td03">
		<#list enumBoardType as types>
			<#if boards.boardType == types.type>
				${types.chName}
			</#if>
		</#list>
		</td>
        <td class="td03">
        <#list enumCategories as category>
            <#if boards.category == category.category>
                ${category.chName}
            </#if>
        </#list>
        </td>
        <td class="td03">${boards.content!}</td>
        <td class="td03">${boards.startDate!}</td>
        <td class="td03">${boards.endDate!}</td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="modifyBoard('${boards.boardId!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deleteBoard('${boards.boardId!}');">
        </td>
    </tr>
    </#list>
</#if>
</table>


<form action="" method="post">
    <input type="hidden" id="modelId" name="modelId" value="">
</form>