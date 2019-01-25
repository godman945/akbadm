<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增廣告定義</h2>
<form action="doAdPoolAdd.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">AdPool名稱<font color="red">*</font></td>
        <td class="td02"><input type="hidden" name="paramAdPoolName" value="${paramAdPoolName!}" />${paramAdPoolName!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">是否區分廠商<font color="red">*</font></td>
        <td class="td02">
        	<input type="hidden" name="paramDiffCompany" value="${paramDiffCompany!}" />
			<#if paramDiffCompany == "Y">
			是
			<#elseif paramDiffCompany == "N">
			否
			</#if>
		</td>
    </tr>
</table>
<br>
</form>
<iframe id="ifrmDad" name="ifrmDad" src="defineAdList.html?paramAdPoolSeq=${paramAdPoolSeq!}" width="900" height="300" marginwidth="0" marginheight="0" scrolling="yes" frameborder="0" align="center">
</iframe>

<input type="hidden" id="messageId" value="${message!}">
<input type="hidden" name="paramAdPoolSeq" value="${paramAdPoolSeq!}" />
