<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">
function addTPRO(itemno) {
	if(document.getElementById("adm_template_product").value != 0) {
		var selectedIndex = document.getElementById("adm_template_product").selectedIndex;
		document.getElementsByName("sel_template_ad")[itemno - 1].value = document.getElementById("adm_template_product").options[selectedIndex].text;
		document.getElementsByName("sel_template_ad_seq")[itemno - 1].value = document.getElementById("adm_template_product").value;
		//alert(document.getElementById("adm_template_ad").options[selectedIndex].text);
	}
}

function addTAD(itemno) {
	if(document.getElementById("adm_template_ad").value != 0) {
		var selectedIndex = document.getElementById("adm_template_ad").selectedIndex;
		document.getElementsByName("sel_template_ad")[itemno - 1].value = document.getElementById("adm_template_ad").options[selectedIndex].text;
		document.getElementsByName("sel_template_ad_seq")[itemno - 1].value = document.getElementById("adm_template_ad").value;
		//alert(document.getElementById("adm_template_ad").options[selectedIndex].text);
	}
}

function removeTAD(itemno) {
	document.getElementsByName("sel_template_ad")[itemno - 1].value = "";
	document.getElementsByName("sel_template_ad_id")[itemno - 1].value = "";
}
</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增商品樣板</h2>
<form action="doTemplateProductRelate.html" method="post">
<table width="550" class="table01">
    <tr>
        <td width="130" class="td01">商品樣板名稱<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateProductName" name="paramTemplateProductName" value="${paramTemplateProductName!}" />${paramTemplateProductName!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板高度<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateProductWidth" name="paramTemplateProductHeight" value="${paramTemplateProductHeight!}" />${paramTemplateProductHeight!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板寬度<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateProductHeight" name="paramTemplateProductWidth" value="${paramTemplateProductWidth!}" />${paramTemplateProductWidth!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">廣告商品類型<font color="red">*</font></td>
        <td class="td02"> 
             <#list pfbxPositionMenuList as pfbxPositionMenuVO>
             <#if '${pfbxPositionMenuVO.id!}' == '${paramTemplateAdType!}'>
                    ${pfbxPositionMenuVO.pfbxMenuName}
             </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td width="130" class="td01">廣告商品屬性<font color="red">*</font></td>
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
    <tr>
        <td width="130" class="td01">廣告類型<font color="red">*</font></td>
        <td class="td02">
            <input type="hidden" id="paramAdType" name="paramAdType" value="${paramAdType!}" />
              <#if '${paramAdType!}' == '1'>
                                                        搜尋廣告
                    <#else>
                                                        聯播廣告
                </#if>
        </td>
    </tr>
    
    <tr>
        <td width="130" class="td01">商品樣板內容<font color="red">*</font></td>
        <td class="td02"> <textarea rows="20" cols="50" id="paramTemplateProductContent" name="paramTemplateProductContent" readonly>${paramTemplateProductContent!}</textarea> </td>
    </tr>
    <tr>
        <td width="130" class="td01">廣告樣板數量<font color="red">*</font></td>
        <td class="td02"> <input type="hidden" id="paramTemplateAdCount" name="paramTemplateAdCount" value="${paramTemplateAdCount!}" />${paramTemplateAdCount!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">自動廣告則數<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="auto" name="auto" value="${auto!}" />${auto!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板每頁筆數<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="pageTotalNum" name="pageTotalNum" value="${pageTotalNum!}" />${pageTotalNum!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板開始筆數<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="startNum" name="startNum" value="${startNum!}" />${startNum!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板是否為 iframe<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="iframe" name="iframe" value="${iframe!}" />${iframe!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板 iframe 高度<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="height" name="height" value="${height!}" />${height!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">商品樣板 iframe 寬度<font color="red">*</font></td>
        <td class="td02"><input type="hidden" id="weight" name="weight" value="${weight!}" />${weight!}</td>
    </tr>
    <tr>
        <td width="130" class="td01">是否顯示於選擇清單<font color="red">*</font></td>
        <td class="td02">
        	<input type="radio" id="show" name="show" value="Y" checked>是
        	<input type="radio" id="show" name="show" value="N">否
        </td>
    </tr>
</table>
<br>
<table width="630" class="table01">
    <tr>
        <td width="150" class="td02">
        	商品樣板<br>
			<select id="adm_template_product" name="adm_template_product" size="8">
                <option value="0">-- 請選擇商品樣板 --</option>
                <#if templateProductList?exists>
                <#list templateProductList as templateproduct1>
                    <option value="${templateproduct1.templateProductSeq}">${templateproduct1.templateProductName}</option>
                </#list>
                </#if>
			</select>
		</td>
        <td width="310" class="td02">
			<table class="table01">
    <#assign x="${paramTemplateAdCount}">
	<#list 1..x?number as i>
				<tr>
					<td class="td02">
						<input type="button" id="btn_add_p_${i}" name="btn_add_p_${i}" value="加入->" onclick="addTPRO(${i});">
						<input type="text" id="sel_template_ad" name="sel_template_ad" value="" size="17" readonly>
						<input type="hidden" id="sel_template_ad_seq" name="sel_template_ad_seq" value="">
						<input type="button" id="btn_add_t_${i}" name="btn_add_t_${i}" value="<-加入" onclick="addTAD(${i});">
						<input type="button" id="btn_del_${i}" name="btn_del_${i}" value="移除" onclick="removeTAD(${i});"><br />
                        廣告則數<input type="text" name="adNums" size="2" maxlength="2" />
					</td>
				</tr>
	</#list>
			</table>
		</td>
        <td width="150" class="td02">
        	廣告樣板<br>
			<select id="adm_template_ad" name="adm_template_ad" size="8">
                <option value="0">-- 請選擇廣告樣板 --</option>
                <#if templateAdList?exists>
                <#list templateAdList as templatead1>
                    <option value="${templatead1.templateAdSeq}">${templatead1.templateAdName}</option>
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
            <input type="submit" id="addBtn" value="儲存，並新增/選擇資料來源型態">
            <input type="button" value="返回" onclick="javascript:window.location='templateProductMaintain.html'">
        </td>
    </tr>
</table>
<input type="hidden" id="defineAdProperties" name="paramTemplateAdProperties" value="">
<input type="hidden" id="defineTemplateAdType" name="paramTemplateAdType" value=${paramTemplateAdType!}>
</form>
<input type="hidden" id="messageId" value="${message!}">

<input type="hidden" id="outputHtmlId" value="${outputHtml!}">
