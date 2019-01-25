<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />修改廣告樣板</h2>
<form action="doTemplateAdUpdate.html" method="post">
<table width="950" class="table01">
    <tr>
        <td width="100" class="td01">廣告樣板序號</td>
        <td class="td02"> ${paramTemplateAdSeq!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板名稱</td>
        <td class="td02"> ${paramTemplateAdName!}</td>
    </tr>
     
    <tr>
        <td width="100" class="td01">廣告定義尺寸<font color="red">*</font></td>
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
                <option  value=${pfbxPositionMenuVO.id!} 
                <#if '${paramTemplateAdType}' == '${pfbxPositionMenuVO.id!}'> selected </#if>>
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
                <option value="1" <#if '1' =='${paramAdType}'> selected </#if> >
                                                    搜尋廣告
                </option>
                <option value="2" <#if '2' =='${paramAdType}'> selected </#if>>
                                                    聯播廣告
                </option>
			</select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板屬性<font color="red">*</font></td>
		<td class="td02">
            <select  id="paramTemplateUpdateAdProperties"  onChange="defineTemplateAdPropertiesUpdateSelect();" style="width:155px;">
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
			<#if '${pfbxPositionMenuVO.pfbxMenuProperties!}' != '0'>
            	<#if '${pfbxPositionMenuVO.id!}' == '${paramTemplateAdProperties!}'>
					<option selected="selected"  class="updateTest_${pfbxPositionMenuVO.pfbxMenuProperties!}"  value="${pfbxPositionMenuVO.id!}">${pfbxPositionMenuVO.pfbxMenuName!}</option>
            		<#else>
					<option  class="updateTest_${pfbxPositionMenuVO.pfbxMenuProperties!}"  value="${pfbxPositionMenuVO.id!}">${pfbxPositionMenuVO.pfbxMenuName!}</option>
            	</#if>	
			</#if>
			</#list>
            </select>
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
            	<#if '${pfbxPositionMenuVO.id!}' == '${paramTemplateAdProperties!}'>
            		<input id="updateDefineTemplateType" type="hidden" value="${pfbxPositionMenuVO.pfbxMenuProperties!}">
				</#if>
			</#list>
		</td>
    </tr>
    <!--
    <tr>
        <td width="100" class="td01">廣告樣板寬度<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramTemplateAdWidth" value="${paramTemplateAdWidth!}" maxlength="4" /></td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板高度<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramTemplateAdHeight" value="${paramTemplateAdHeight!}" maxlength="4" /></td>
    </tr>
    -->
    <tr>
        <td width="100" class="td01">廣告樣板內容<font color="red">*</font></td>
        <td class="td02"> <textarea rows="20" cols="100" name="paramTemplateAdContent" wrap="off">${paramTemplateAdContent!}</textarea> </td>
    </tr>
</table>
<br>
<table width="550">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn"  value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='templateAdMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="templateAdSeq" name="templateAdSeq" value="${paramTemplateAdSeq!}">
<input type="hidden" id="defineTemplateAdHeight" name="paramTemplateAdHeight" value="${paramTemplateAdHeight!}">
<input type="hidden" id="defineTemplateAdWidth" name="paramTemplateAdWidth" value="${paramTemplateAdWidth!}">
<input type="hidden" id="defineTemplateAdType" name="paramTemplateAdType" value="${paramTemplateAdType}">
<input type="hidden" id="defineAdType" name="paramAdType" value="${paramAdType!}">
<input type="hidden" id="defineAdProperties" name="paramTemplateAdProperties" value="${paramTemplateAdProperties}">

</form>

<input type="hidden" id="messageId" value="${message!}">



			


