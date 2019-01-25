<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />修改商品樣板</h2>
<form action="doTemplateProductUpdate.html" method="post">
<table width="550" class="table01">
    <tr>
        <td width="100" class="td01">商品樣板序號</td>
        <td class="td02"> ${paramTemplateProductSeq!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板名稱</td>
        <td class="td02">
        <input type="hidden" id="defaultHeight" value=${paramTemplateProductHeight!}>
        <input type="hidden" id="defaultWidth" value=${paramTemplateProductWidth!}>
       
        ${paramTemplateProductName!}</td>
        
        
    </tr>
     <tr>
        <td width="100" class="td01">商品樣板尺寸<font color="red">*</font></td>
        <td class="td02">
            <select id="paramTemplateAdSize" onChange="changeTemplateSize();" style="width:155px;">
            <#list pfbxSizeList as pfbxSizeVO>
                <option>
                    ${pfbxSizeVO.width} x ${pfbxSizeVO.height}
                </option>
            </#list>
            </select>
        </td>
    </tr>
    <!--
    <tr>
        <td width="100" class="td01">商品樣板寬度<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramTemplateProductWidth" value="${paramTemplateProductWidth!}" maxlength="4" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板高度<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramTemplateProductHeight" value="${paramTemplateProductHeight!}" maxlength="4" /></td>
    </tr>
    -->
    <tr>
        <td width="100" class="td01">商品樣板內容<font color="red">*</font></td>
        <td class="td02"> <textarea rows="20" cols="50" name="paramTemplateProductContent">${paramTemplateProductContent!}</textarea> </td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板每頁筆數<font color="red">*</font></td>
        <td class="td02"><input type="text" id="pageTotalNum" name="pageTotalNum" value="${pageTotalNum!}" maxlength="4" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板開始筆數<font color="red">*</font></td>
        <td class="td02"><input type="text" id="startNum" name="startNum" value="${startNum!}" maxlength="4" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板是否為 iframe<font color="red">*</font></td>
        <td class="td02">
            <select name="iframe" id="iframe">
                <option value="Y" <#if iframe?exists && iframe == "Y">selected="selected"</#if>>是</option>
                <option value="N" <#if iframe?exists && iframe == "N">selected="selected"</#if>>否</option>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板iframe尺寸<font color="red">*</font></td>
        <td class="td02">
            <select id="iframeAdSize" onChange="changeIframeSize();" style="width:155px;">
            <#list pfbxSizeList as pfbxSizeVO>
                <option>
                    ${pfbxSizeVO.width} x ${pfbxSizeVO.height}
                </option>
            </#list>
            </select>
        </td>
    </tr>
    <!--
    <tr>
        <td width="100" class="td01">商品樣板 iframe 高度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="height" name="height" value="${height!}" maxlength="4" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板 iframe 寬度<font color="red">*</font></td>
        <td class="td02"><input type="text" id="weight" name="weight" value="${weight!}" maxlength="4" /></td>
    </tr>
    -->
</table>
<br>
<table width="550">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="儲存" >
            <input type="button" value="返回" onclick="javascript:window.location='templateProductMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="templateProductSeq" name="templateProductSeq" value="${paramTemplateProductSeq!}">
<input type="hidden" id="defineTemplateAdWidth" name="paramTemplateProductWidth" value="${paramTemplateProductWidth!}">
<input type="hidden" id="defineTemplateAdHeight" name="paramTemplateProductHeight" value="${paramTemplateProductHeight!}">
<input type="hidden" id="weight" name="weight" value=${weight!}>
<input type="hidden" id="height" name="height" value=${height!}>
</form>

<input type="hidden" id="messageId" value="${message!}">

