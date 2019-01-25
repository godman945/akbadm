<#assign s=JspTaglibs["/struts-tags"]>

<center><div class="titel_t">系統公告</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">新增系統公告</td>
    </tr>
</table>

<br>
<form action="boardSave.html" method="post">
<table width="500" class="table01">
    <tr>
        <td width="100" class="td01">公告屬性</td>
        <td class="td02">
		<select id="boardType" name="boardType">
			<#-- <#list enumBoardType[3..5] as types> -->
			<#list enumBoardType[1..6] as types>
				<option value="${types.type}">${types.chName}</option>
			</#list>			
		</select>
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">公告種類</td>
        <td class="td02">
        <select id="category" name="category">
            <#list enumCategories as category>
                <option value="${category.category}">${category.chName}</option>
            </#list>            
        </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">上線日期</td>
        <td class="td02"><input type="text" id="startDate" name="startDate" value="${startDate!}" readonly></td>
    </tr>
    <tr>
        <td width="100" class="td01">下線日期</td>
        <td class="td02"><input type="text" id="endDate" name="endDate" value="${endDate!}" readonly></td>
    </tr>
    <tr>
        <td width="100" class="td01">公告內容 </td>
        <td class="td02">
            <input type="text" id="content" name="content" size="50">
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">公告連結網址</td>
        <td class="td02"><input type="text" id="urlAddress" name="urlAddress" ></td>
    </tr>    
    <tr>
        <td width="100" class="td01">建立者</td>
        <td class="td02"><input type="text" name="author" value="${Session.session_user_id}" readonly></td>
    </tr>
</table>
<br>
<table width="500">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='board.html'">
        </td>
    </tr>
</table>
</form>