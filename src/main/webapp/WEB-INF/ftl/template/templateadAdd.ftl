<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">
function addAdPool() {
	location.href = "adPoolAdd.html";
}
</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增廣告樣板</h2>
<form action="doTemplateAdAdd.html" method="post">
<table width="550" class="table01">
    <tr>
        <td width="100" class="td01">廣告樣板名稱<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramTemplateAdName" value="${paramTemplateAdName!}" maxlength="20" /> ( 限 50 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板尺寸<font color="red">*</font></td>
        <td class="td02">
            <select  id="paramTemplateAdSize" onChange="changeTemplateSize();" style="width:155px;">
            <#list pfbxSizeList as pfbxSizeVO>
                <option>
                    ${pfbxSizeVO.width!} x ${pfbxSizeVO.height!}
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
        <td width="100" class="td01">廣告樣板內容<font color="red">*</font></td>
        <td class="td02">
			<textarea rows="20" cols="50" name="paramTemplateAdContent">${paramTemplateAdContent!}</textarea><br>
			廣告定義的位置，需改為{#tad_第幾則}<br>例如：第2則需換成 {#tad_2}
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">區分廠商<font color="red">*</font></td>
        <td class="td02">
			<input type="radio" name="diffCompany" value="N" checked>否
			<input type="radio" name="diffCompany" value="Y">是
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義數量<font color="red">*</font></td>
        <td class="td02">
			<select name="paramDefineAdCount" id="paramDefineAdCount">
                <option value="0">-- 請選擇廣告定義數量 --</option>
                <#assign x=10>
				<#list 1..x as i>
					<option value="${i}">${i}</option>
				</#list>
            </select>
        </td>
    </tr>
</table>
<br>
選擇資料來源<input type="button" id="btn_addAdPool" name="btn_addAdPool" value="新增資料來源" onclick="addAdPool();">
<table width="550" class="table01">
    <tr>
        <td width="100" class="td01">資料來源<font color="red">*</font></td>
        <td class="td02">
			<select id="adm_ad_pool" name="adm_ad_pool" size="8">
                <option value="0">-- 請選擇資料來源 --</option>
                <#if adPoolList?exists>
                <#list adPoolList as adpool1>
                    <option value="${adpool1.adPoolSeq}">${adpool1.adPoolName}</option>
                </#list>
                </#if>
			</select>
		</td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="下一步">
            <input type="button" value="返回" onclick="javascript:window.location='templateAdMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="defineTemplateAdWidth" name="paramTemplateAdWidth" value="">
<input type="hidden" id="defineTemplateAdHeight" name="paramTemplateAdHeight" value="">
<input type="hidden" id="defineTemplateAdType" name="paramTemplateAdType" value="">
<input type="hidden" id="defineAdType" name="paramAdType" value="">
<input type="hidden" id="defineAdProperties" name="paramTemplateAdProperties" value="">
</form>

<input type="hidden" id="messageId" value="${message!}">
<input type="hidden" id="outputHtmlId" value="${outputHtml!}">

