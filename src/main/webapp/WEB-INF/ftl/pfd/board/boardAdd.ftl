<#assign s=JspTaglibs["/struts-tags"]>

<center><div class="titel_t">PFD系統公告</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">新增系統公告</td>
    </tr>
</table>
<br>
<form action="doPfdBoardAdd.html" method="post">
<table width="500" class="table01">
    <tr>
        <td width="100" class="td01">公告種類</td>
        <td class="td02">
            <select id="boardType" name="boardType">
                <#list enumBoardType as types>
                    <option value="${types.type}">${types.chName}</option>
                </#list>			
            </select>
		</td>
    </tr>
    <tr>
        <td class="td01">公告內容</td>
        <td class="td02"><input type="text" id="content" name="content" size="50" maxlength="100"></td>
    </tr>
    <tr>
        <td class="td01">經銷商帳戶</td>
        <td class="td02">
            <select id="pfdCustomerInfoId" name="pfdCustomerInfoId" disabled>
                <option value="">-- 請選擇 --</option>
                <#list pfdCustomerInfoMap?keys as key>
                    <option value="${key}">${pfdCustomerInfoMap[key]}</option>
                </#list>
            </select>
        </td>
    </tr>
    <tr>
        <td class="td01">經銷商帳號</td>
        <td class="td02">
            <select id="pfdUserId" name="pfdUserId" disabled>
            </select>
    </tr>
    <tr>
        <td class="td01">公告連結網址</td>
        <td class="td02"><input type="text" id="urlAddress" name="urlAddress" size="50" maxlength="200"></td>
    </tr>
    <tr>
        <td class="td01">上線日期</td>
        <td class="td02"><input type="text" id="startDate" name="startDate" value="${startDate!}" readonly></td>
    </tr>
    <tr>
        <td class="td01">下線日期</td>
        <td class="td02"><input type="text" id="endDate" name="endDate" value="${endDate!}" readonly></td>
    </tr>
</table>
<br>
<table width="500">
    <tr>
        <td align="center">
            <input type="button" value="儲存" onclick="doAdd()">
            <input type="button" value="返回" onclick="javascript:window.location='pfdBoardMaintain.html'">
        </td>
    </tr>
</table>
</form>
