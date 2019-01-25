<#assign s=JspTaglibs["/struts-tags"]>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增商品樣板</h2>
<form id="form1" name="form1"  action="doTemplateProductAdd.html" method="post">
<table width="550" class="table01">
    <tr>
        <td width="100" class="td01">商品樣板名稱<font color="red">*</font></td>
        <td class="td02"> <input type="text" id="paramTemplateProductName"  maxlength="20" /> ( 限 50 字元 )</td>
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
    <tr>
        <td width="100" class="td01">廣告樣板類型<font color="red">*</font></td>
        <td class="td02">
            <select  id="paramTemplateAdType"  onChange="defineTemplateAdTypeSelect();" style="width:155px;">
           <#list pfbxPositionMenuList as pfbxPositionMenuVO>
                <#if '${pfbxPositionMenuVO.pfbxMenuProperties!}' == '0'>
                <option  value=${pfbxPositionMenuVO.id!}>
                    ${pfbxPositionMenuVO.pfbxMenuName!}
                </option>
                </#if>
            </#list>
            </select>
        </td>
    </tr>
     <tr>
        <td width="100" class="td01">廣告類型<font color="red">*</font></td>
        <td class="td02">
            <select id="paramAdType" onChange="defineAdTypeSelect();" style="width:155px;">
                <option value="1">
                                                    搜尋廣告
                </option>
                <option value="2">
                                                    聯播廣告
                </option>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">商品樣板內容<font color="red">*</font></td>
        <td class="td02">
			<textarea rows="20" cols="50" id="paramTemplateProductContent" name="paramTemplateProductContent">{#tpro_1}</textarea><br>
			廣告樣板的位置，需改為{#tpro_第幾則}<br>例如：第2則需換成 {#tpro_2}
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板數量<font color="red">*</font></td>
        <td class="td02">
			<select name="paramTemplateAdCount" id="paramTemplateAdCount">
                <option value="0">-- 請選擇廣告樣板數量 --</option>
                <#assign x=10>
				<#list 1..x as i>
					<option value="${i}" <#if paramTemplateAdCount?exists && paramTemplateAdCount == i?string>selected="selected"</#if>>${i}</option>
				</#list>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">自動廣告則數<font color="red">*</font></td>
        <td class="td02">
            <select name="auto" id="auto">
                <option value="Y" <#if auto?exists && auto == "Y">selected="selected"</#if>>是</option>
                <option value="N" <#if auto?exists && auto == "N">selected="selected"</#if>>否</option>
            </select>
        </td>
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
            <input type="button" id="addBtn" name="addBtn" value="下一步" onClick="chkCount();">
            <input type="button" value="返回" onclick="javascript:window.location='templateProductMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="defineTemplateAdWidth" name="paramTemplateProductWidth" value="">
<input type="hidden" id="defineTemplateAdHeight" name="paramTemplateProductHeight" value="">
<input type="hidden" id="defineTemplateAdType" name="paramTemplateAdType" value="">
<input type="hidden" id="defineAdType" name="paramAdType" value="">
<input type="hidden" id="weight" name="weight" value="">
<input type="hidden" id="height" name="height" value="">
<input type="hidden" id="defineTemplateAdProductName" name="paramTemplateProductName" value="">
<input type="hidden" id="defineAdProperties" name="paramTemplateAdProperties" value="">
</form>

<input type="hidden" id="messageId" value="${message!}">

<input type="hidden" id="outputHtmlId" value="${outputHtml!}">
