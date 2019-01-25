<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增 AdPool</h2>
<form action="doAdPoolAdd.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">AdPool名稱<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramAdPoolName" value="${paramAdPoolName!}" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">是否區分廠商<font color="red">*</font></td>
        <td class="td02">
        	<input type="radio" id="paramDiffCompany" name="paramDiffCompany" value="Y">是
        	<input type="radio" id="paramDiffCompany" name="paramDiffCompany" value="N">否
		</td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='defineAdMaintain.html'">
        </td>
    </tr>
</table>
</form>

<input type="hidden" id="messageId" value="${message!}">
