<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />修改廣告定義</h2>
<form action="doDefineAdUpdateAJAX.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">廣告來源序號<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramAdPoolSeq" name="paramAdPoolSeq" value="${admDefineAd.adPoolSeq!}" maxlength="20" readonly /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義序號<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdSeq" name="paramDefineAdSeq" value="${admDefineAd.defineAdSeq!}" maxlength="20" readonly /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義代碼<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdId" name="paramDefineAdId" value="${admDefineAd.defineAdId!}" maxlength="30" /> ( 限 30 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義名稱<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdName" name="paramDefineAdName" value="${admDefineAd.defineAdName!}" maxlength="50" /> ( 限 50 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告類型<font color="red">*</font></td>
        <td class="td02">
			<select id="paramDefineAdType" name="paramDefineAdType">
            <#list defineAdTypeList as adtype1>
                <#assign selectedFlag="">
                <#if adtype1.defineAdTypeId == admDefineAd.admDefineAdType.defineAdTypeId>
                    <#assign selectedFlag="selected">
                </#if>
                <option value="${adtype1.defineAdTypeId}" ${selectedFlag}>${adtype1.defineAdTypeName}</option>
            </#list>
            </select>
        </td>
    </tr>
     <tr>
        <td width="100" class="td01">廣告定義尺寸<font color="red">*</font></td>
        <td class="td02">
            <select id="updateParamDefineAdSize" style="width:155px;">
            <#list pfbxSizeList as pfbxSizeVO>
                <option>
                    ${pfbxSizeVO.size}
                </option>
            </#list>
            </select>
        ( 寬 x 高)
        </td>
    </tr>
    <!--
    <tr>
        <td width="100" class="td01">廣告定義寬度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdWidth" name="paramDefineAdWidth" value="${admDefineAd.defineAdWidth!}" maxlength="4" /> (0 ~ 3000)</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義高度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="paramDefineAdHeight" name="paramDefineAdHeight" value="${admDefineAd.defineAdHeight!}" maxlength="4" /> (0 ~ 2000)</td>
    </tr>
    -->
    <tr>
        <td width="100" class="td01">廣告定義內容<font color="red">*</font></td>
        <td class="td02"><textarea id="paramDefineAdContent" name="paramDefineAdContent" rows="4" cols="35">${admDefineAd.defineAdContent!}</textarea></td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="button" value="儲存" onclick="doUpdate()" />
            <input type="button" value="關閉視窗" onclick="javascript:window.close();" />
        </td>
    </tr>
</table>
</form>

<input type="hidden" id="messageId" value="${message!}">
<input type="hidden" id="defineAdWidth" value="${admDefineAd.defineAdWidth!}">
<input type="hidden" id="defineAdHeight" value="${admDefineAd.defineAdHeight!}">