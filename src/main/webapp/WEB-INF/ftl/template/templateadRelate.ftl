<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

function addTemplateAd() {
    window.location = "templateAdAdd.html";
}
 
function addDAD(itemno) {
	if(document.getElementById("adm_define_ad").value != 0) {
		var selectedIndex = document.getElementById("adm_define_ad").selectedIndex;
		document.getElementsByName("sel_define_ad")[itemno - 1].value = document.getElementById("adm_define_ad").options[selectedIndex].text;
		document.getElementsByName("sel_define_ad_seq")[itemno - 1].value = document.getElementById("adm_define_ad").value;
		//alert(document.getElementById("adm_define_ad").options[selectedIndex].text);
	}
}

function removeDAD(itemno) {
	document.getElementsByName("sel_define_ad")[itemno - 1].value = "";
	document.getElementsByName("sel_define_ad_id")[itemno - 1].value = "";
}

function viewTemplateAD() {
	alert(document.getElementsByName("sel_define_ad").length);
	var len = document.getElementsByName("sel_define_ad").length;
	for(var  i = 0; i < len; i++) {
		if(document.getElementsByName("sel_define_ad")[i].value == "") {
			alert("您尚未完整選取");
			break;
			return;
		}
	}
    //document.getElementById("templateAdSeq").value = templateAdSeq;
    //document.forms[1].action = "doTemplateAdDelete.html";
    //document.forms[1].submit();
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增廣告樣板</h2>
<form action="doTemplateAdRelate.html" method="post">
<table width="550" class="table01">
    <tr>
        <td width="100" class="td01">廣告樣板名稱<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateAdName" name="paramTemplateAdName" value="${paramTemplateAdName!}" />${paramTemplateAdName!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板尺寸<font color="red">*</font></td>
        <td class="td02">
            ${paramTemplateAdWidth!} x ${paramTemplateAdHeight!}  
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告類型<font color="red">*</font></td>
        <td class="td02">
                <#if '${paramAdType!}' == '1'>
                                                        搜尋廣告
                    </option>
                    <#else>
                                                        聯播廣告
                    </option>
                </#if>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告內容類型<font color="red">*</font></td>
        <td class="td02">
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
             <#if '${pfbxPositionMenuVO.id!}' == '${paramTemplateAdType!}'>
                    ${pfbxPositionMenuVO.pfbxMenuName}
             </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板屬性<font color="red">*</font></td>
        <td class="td02">
            <select  id="paramTemplateAdProperties"  onChange="defineTemplateAdPropertiesSelect();" style="width:155px;">
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
            	 <#if '${paramAdType!}' == '1' && '${pfbxPositionMenuVO.pfbxMenuProperties!}' == '1'>
	  				<option  value=${pfbxPositionMenuVO.id!}>${pfbxPositionMenuVO.pfbxMenuName!}</option>
	  				<#elseif '${paramAdType!}' == '2' && '${pfbxPositionMenuVO.pfbxMenuProperties!}' == '2'>
	  				<option  value=${pfbxPositionMenuVO.id!}>${pfbxPositionMenuVO.pfbxMenuName!}</option>
	  			</#if>
            </#list>
            </select>
        </td>
    </tr>
   
    <!--
    <tr>
        <td width="100" class="td01">廣告樣板寬度<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateAdWidth" name="paramTemplateAdWidth" value="${paramTemplateAdWidth!}" />${paramTemplateAdWidth!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告樣板高度<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateAdHeight" name="paramTemplateAdHeight" value="${paramTemplateAdHeight!}" />${paramTemplateAdHeight!}</td>
    </tr>
    -->
    <tr>
        <td width="100" class="td01">廣告樣板內容<font color="red">*</font></td>
        <td class="td02">
			<textarea rows="20" cols="50" id="paramTemplateAdContent" name="paramTemplateAdContent" readonly>${paramTemplateAdContent!}</textarea>
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">區分廠商<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="diffCompany" name="diffCompany" value="${diffCompany!}" />${diffCompany!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告定義數量<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramDefineAdCount" name="paramDefineAdCount" value="${paramDefineAdCount!}" />${paramDefineAdCount!}</td>
    </tr>
</table>
<br>
<table width="550" class="table01">
    <tr>
        <td width="250" class="td02">
			<table width="250" class="table01">
    <#assign x="${paramDefineAdCount}">
	<#list 1..x?number as i>
				<tr>
					<td width="250" class="td02">
						<input type="text" id="sel_define_ad" name="sel_define_ad" value="" readonly>
						<input type="hidden" id="sel_define_ad_seq" name="sel_define_ad_seq" value="">
						<input type="button" id="btn_add_${i}" name="btn_add_${i}" value="加入" onclick="addDAD(${i});">
						<input type="button" id="btn_del_${i}" name="btn_del_${i}" value="移除" onclick="removeDAD(${i});">
					</td>
				</tr>
	</#list>
			</table>
		</td>
        <td width="200" class="td02">
        	<input type="hidden" id="adm_ad_pool" name="adm_ad_pool" value="${adm_ad_pool}">
			<select id="adm_define_ad" name="adm_define_ad" size="8">
                <option value="0">-- 請選擇廣告定義 --</option>
                <#if defineAdList?exists>
                <#list defineAdList as definead1>
                    <option value="${definead1.defineAdSeq}">${definead1.defineAdName}</option>
                </#list>
                </#if>
			</select>
		</td>
    </tr>
</table>
<br>
<table width="550">
    <tr>
        <td align="center">
            <input type="button" id="preStep" value="上一步" onclick="history.back()">
            <input type="submit" id="addBtn" value="存檔">
            <input type="button" id="preview" name="preview" value="預覽" onclick="viewTemplateAD()";>
            <input type="button" value="返回" onclick="javascript:window.location='templateAdMaintain.html'">
        </td>
    </tr>
</table>

<input type="hidden" id="defineTemplateAdWidth" name="paramTemplateAdWidth" value=${paramTemplateAdWidth!}>
<input type="hidden" id="defineTemplateAdHeight" name="paramTemplateAdHeight" value=${paramTemplateAdHeight!}>
<input type="hidden" id="defineTemplateAdType" name="paramTemplateAdType" value=${paramTemplateAdType!}>
<input type="hidden" id="defineAdType" name="paramAdType" value=${paramAdType!}>
<input type="hidden" id="defineAdProperties" name="paramTemplateAdProperties" value="">
</form>
<form id="previewForm" method="post">

</form>

<input type="hidden" id="messageId" value="${message!}">

<input type="hidden" id="outputHtmlId" value="${outputHtml!}">





