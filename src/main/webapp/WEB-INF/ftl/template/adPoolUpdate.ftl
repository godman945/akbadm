<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />修改 AdPool</h2>
<form action="doAdPoolUpdate.html" method="post">
<input type="hidden" name="paramAdPoolSeq" value="${admAdPool.adPoolSeq!}" />
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">AdPool名稱<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramAdPoolName" value="${admAdPool.adPoolName!}" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">是否區分廠商<font color="red">*</font></td>
        <td class="td02">
        	<input type="radio" id="paramDiffCompany" name="paramDiffCompany" value="Y" <#if admAdPool.diffCompany == "Y">checked="checked"</#if>>是
        	<input type="radio" id="paramDiffCompany" name="paramDiffCompany" value="N" <#if admAdPool.diffCompany == "N">checked="checked"</#if>>否
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
