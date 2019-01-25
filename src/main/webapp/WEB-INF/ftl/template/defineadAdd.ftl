<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增廣告定義</h2>
<form action="doDefineAdAddAJAX.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">廣告來源序號<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramAdPoolSeq" name="paramAdPoolSeq" value="${paramAdPoolSeq!}" maxlength="20" <#if paramAdPoolSeq?exists && (paramAdPoolSeq != "")>readonly</#if> /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義代碼<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdId" name="paramDefineAdId" value="${paramDefineAdId!}" maxlength="30" /> ( 限 30 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義名稱<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdName" name="paramDefineAdName" value="${paramDefineAdName!}" maxlength="50" /> ( 限 50 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告類型<font color="red">*</font></td>
        <td class="td02">
			<select id="paramDefineAdType" name="paramDefineAdType">
            <#list defineAdTypeList as adtype1>
                <option value="${adtype1.defineAdTypeId}">${adtype1.defineAdTypeName}</option>
            </#list>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義尺寸<font color="red">*</font></td>
        <td class="td02">
            <select id="paramDefineAdSize" style="width:155px;">
            <#list pfbxSizeList as pfbxSizeVO>
                <option>
                    ${pfbxSizeVO.size}
                </option>
            </#list>
            </select>
        ( 寬 x 高 )
        </td>
    </tr>
    <!--
    <tr>
        <td width="100" class="td01">廣告定義寬度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdWidth" name="paramDefineAdWidth" value="${paramDefineAdWidth!}" maxlength="4" /> (0 ~ 3000)</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義高度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdHeight" name="paramDefineAdHeight" value="${paramDefineAdHeight!}" maxlength="4" /> (0 ~ 2000)</td>
    </tr>
    -->
    <tr>
        <td width="100" class="td01">廣告定義內容<font color="red">*</font></td>
        <td class="td02"><textarea id="paramDefineAdContent" name="paramDefineAdContent" rows="4" cols="35">${paramDefineAdContent!}</textarea></td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="button" value="儲存" onclick="doAdd()">
            <input type="button" value="關閉視窗" onclick="javascript:window.close();">
        </td>
    </tr>
</table>
</form>

<input type="hidden" id="messageId" value="${message!}">