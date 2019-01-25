<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />PFD系統公告</h2>
<table width="95%">
    <tr>
        <td><input type="button" value="新增" onclick="addBoard()"></td>
    </tr>
</table>
<br>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="20">NO.</td>
        <td class="td01" width="50">公告種類</td>
        <td class="td01" width="100">經銷商帳戶</td>
        <td class="td01" width="100">經銷商帳號</td>
        <td class="td01" width="200">公告內容</td>
        <td class="td01" width="60">上線日期</td>
        <td class="td01" width="60">下線日期</td>
        <td class="td01" width="50">操作</td>
    </tr>
<#if boardList?exists>
    <#list boardList as board>
    <tr>
        <td class="td03">${board_index+1}</td>
        <td class="td03">
		<#list enumBoardType as types>
			<#if board.boardType == types.type>
				${types.chName}
			</#if>
		</#list>
		</td>
        <td class="td03">
            <#if board.pfdCustomerInfoId?exists && board.pfdCustomerInfoId!="">
                ${pfdCustomerInfoMap[board.pfdCustomerInfoId]}
            </#if>
        </td>
        <td class="td03">
            <#if board.pfdUserId?exists && board.pfdUserId!="">
                ${pfdUserMap[board.pfdUserId]}
            </#if>
        </td>
        <td class="td02">${board.boardContent}</td>
        <td class="td03">${board.startDate?string("yyyy-MM-dd")}</td>
        <td class="td03">${board.endDate?string("yyyy-MM-dd")}</td>
        <td class="td03">
            <input type="button" value="修改" onclick="modifyBoard('${board.boardId}')">
            <input type="button" value="刪除" onclick="deleteBoard('${board.boardId}')">
        </td>
    </tr>
    </#list>
</#if>
</table>
