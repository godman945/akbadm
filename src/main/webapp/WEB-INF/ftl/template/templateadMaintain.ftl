<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/javascript">

function addTemplateAd() {
    window.location = "templateAdAdd.html";
}

function updateTemplateAd(templateAdSeq) {
    document.getElementById("templateAdSeq").value = templateAdSeq;
    document.forms[1].action = "templateAdUpdate.html";
    document.forms[1].submit();
}


</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告樣板維護</h2>
<form action="templateAdQuery.html" method="post">
<table width="100%">
    <tr>
        <td>廣告樣板序號:
        	<input type="text" name="queryTemplateAdSeq" value="${queryTemplateAdSeq!}" style="width:179px;" />        
            &nbsp;&nbsp;廣告樣板名稱:
            <input type="text" name="queryTemplateAdName" value="${queryTemplateAdName!}" style="width:179px;" />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="查詢" />
		</td>
	</tr>
	<tr>
        <td>廣告樣板類型:
            <select  id="queryTemplateAdType" name="queryTemplateAdType" style="width:183px;">
            	<option  value="">請選擇</option>
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
            	<#if '${pfbxPositionMenuVO.pfbxMenuProperties!}' != '0'>
                <option  value=${pfbxPositionMenuVO.pfbxAdTemplateProductXType!} 
                	<#if '${pfbxPositionMenuVO.pfbxAdTemplateProductXType!}' == '${queryTemplateAdType!}'>selected</#if>>
                    ${pfbxPositionMenuVO.pfbxAdTemplateProductXType!}_${pfbxPositionMenuVO.pfbxMenuName!}
                </option>
                </#if>
            </#list>
            </select>
        	&nbsp;&nbsp;廣告樣板尺寸:
            <select  id="queryTemplateAdSize" name="queryTemplateAdSize" style="width:183px;">
            	<option  value="">請選擇</option>
            <#list pfbxSizeList as pfbxSizeVO>
                <option value='${pfbxSizeVO.width!}x${pfbxSizeVO.height!}'
                	<#if '${pfbxSizeVO.width!}x${pfbxSizeVO.height!}' == '${queryTemplateAdSize!}'>selected</#if>>
                    ${pfbxSizeVO.width!} x ${pfbxSizeVO.height!}
                </option>
            </#list>
            </select>
        </td>
    </tr>
</table>
</form>
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addTemplateAd()"></td>
    </tr>
</table>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="150">廣告樣板序號</td>
        <td class="td01" width="200">廣告樣板名稱</td>
        <!-- 
        <td class="td01" width="100">廣告樣板寬度</td>
        <td class="td01" width="100">廣告樣板高度</td>
        -->
        <td class="td01" width="120">廣告樣板尺寸</td>
        <td class="td01" width="400">廣告樣板內容</td>
        <td class="td01" width="200">操作</td>
    </tr>
<#if templateAdList?exists>
    <#assign index=0>
    <#list templateAdList as templateAd>
    <#assign index = index+1>
    <tr> 
        <td class="td03">${index!}</td>
        <td class="td02">${templateAd.templateAdSeq!}</td>
        <td class="td03">${templateAd.templateAdName!}</td>
        <td class="td03">${templateAd.templateAdWidth!} x ${templateAd.templateAdHeight!}</td>
        <!--
        <td class="td03">${templateAd.templateAdWidth!}</td>
        <td class="td03">${templateAd.templateAdHeight!}</td>
        -->
        <td class="td02"><a href="javascript:showContent('${templateAd.templateAdFile!}');">${templateAd.templateAdFile!}</a></td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="updateTemplateAd('${templateAd.templateAdSeq!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deleteTemplateAd('${templateAd.templateAdSeq!}');">
        </td>
    </tr>
    </#list>
<#else>
987654321
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">

<form action="" method="post">
    <input type="hidden" id="templateAdSeq" name="templateAdSeq" value="">
</form>
 