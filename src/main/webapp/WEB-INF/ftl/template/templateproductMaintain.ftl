<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/javascript">

function addTemplateProduct() {
    window.location = "templateProductAdd.html";
}

function updateTemplateProduct(templateProductSeq) {
    document.getElementById("templateProductSeq").value = templateProductSeq;
    document.forms[1].action = "templateProductUpdate.html";
    document.forms[1].submit();
    
}

function deleteTemplateProduct(templateProductSeq) {
    if (confirm("確定刪除商品 [ " + templateProductSeq + " ] ?")) {
    	$("#templateProductSeq").val(templateProductSeq);
        document.getElementById("templateProductSeq").value = templateProductSeq;
        document.forms[1].action = "doTemplateProductDelete.html";
        document.forms[1].submit();
    }
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />商品樣板維護</h2>
<form action="templateProductQuery.html" method="post">
<table width="100%">
    <tr>
        <td>商品樣板序號:
        	<input type="text" name="queryTemplateProductSeq" value="${queryTemplateProductSeq!}" style="width:179px;" />        
            &nbsp;&nbsp;商品樣板名稱:
            <input type="text" name="queryTemplateProductName" value="${queryTemplateProductName!}" style="width:179px;" />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="查詢" />
		</td>
	</tr>
	<tr>
        <td>商品樣板類型:
            <select  id="queryTemplateProductType" name="queryTemplateProductType" style="width:183px;">
            	<option  value="">請選擇</option>
            <#list pfbxPositionMenuList as pfbxPositionMenuVO>
                <#if '${pfbxPositionMenuVO.pfbxMenuProperties!}' != '0'>
                <option  value=${pfbxPositionMenuVO.pfbxAdTemplateProductXType!} 
                	<#if '${pfbxPositionMenuVO.pfbxAdTemplateProductXType!}' == '${queryTemplateProductType!}'>selected</#if>>
                    ${pfbxPositionMenuVO.pfbxAdTemplateProductXType!}_${pfbxPositionMenuVO.pfbxMenuName!}
                </option>
                </#if>
            </#list>
            </select>
        	&nbsp;&nbsp;商品樣板尺寸:
            <select  id="queryTemplateProductSize" name="queryTemplateProductSize" style="width:183px;">
            	<option  value="">請選擇</option>
            <#list pfbxSizeList as pfbxSizeVO>
                <option value='${pfbxSizeVO.width!}x${pfbxSizeVO.height!}'
                	<#if '${pfbxSizeVO.width!}x${pfbxSizeVO.height!}' == '${queryTemplateProductSize!}'>selected</#if>>
                    ${pfbxSizeVO.width!} x ${pfbxSizeVO.height!}
                </option>
            </#list>
            </select>
        </td>
    </tr>
</table>
</form>

<!--
<form id="templateProductSizeQueryId" action="templateProductSizeQuery.html" method="post">
<input type="hidden" id="paramTemplateProductWidth" name = "paramTemplateProductWidth">
<input type="hidden" id="paramTemplateProductHeight" name="paramTemplateProductHeight">
<table>
 <tr>
    <td>選擇尺寸:</td>
		<td>
		    <select id="sizeSelectId" style="margin-left:23px;width:154px;" onchange="selectSizeView();">
				<option></option>
				<option>全選</option>
				<#if pfbxSizeList?exists>
					  <#list pfbxSizeList as pfbxSize>
						<option>${pfbxSize.size!}</option>
					  </#list>
				</#if>
			</select>
		</td>
 </tr>
</table>
</form>
-->
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addTemplateProduct()"></td>
    </tr>
</table>
<table class="table01" width="95%">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="150">商品樣板序號</td>
        <td class="td01" width="200">商品樣板名稱</td>
        
        <td class="td01" width="100">商品樣板尺寸</td>
        <!--
        <td class="td01" width="100">商品樣板寬度</td>
        <td class="td01" width="100">商品樣板高度</td>
        -->
        <td class="td01" width="400">商品樣板內容</td>
        <td class="td01" width="200">操作</td>
        <td class="td01" width="100">預覽</td>
    </tr>
<#if templateProductList?exists>
    <#assign index=0>
    <#list templateProductList as templateProduct>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td02">${templateProduct.templateProductSeq!}</td>
        <td class="td03">${templateProduct.templateProductName!}</td>
        
        <td class="td03" style="width:100px">${templateProduct.templateProductWidth!} x ${templateProduct.templateProductHeight!}</td>
        <!--
        <td class="td03">${templateProduct.templateProductWidth!}</td>
        <td class="td03">${templateProduct.templateProductHeight!}</td>
        -->
        <td class="td02"><a href="javascript:showContent('${templateProduct.templateProductFile!}');">${templateProduct.templateProductFile!}</a></td>
        <td class="td03">
            <input type="button" id="updateBtn" value="修改" onclick="updateTemplateProduct('${templateProduct.templateProductSeq!}');">
            <input type="button" id="deleteBtn" value="刪除" onclick="deleteTemplateProduct('${templateProduct.templateProductSeq!}');">
        </td>
        <!--<td class="td03"><input type="button" value="預覽" onClick='alex("${templateProduct.templateProductSeq!}");'></td>-->
        <td class="td03"> 
        	<input type="button" value="預覽" onClick='preView(this);'>
		  	<div style="display:none;">
		  	<!--tpro_201307010002<iframe  style="background-color:aliceblue;border:2px solid #FFFF63" height=${templateProduct.iframeHeight!} width=${templateProduct.iframeWidth!} src="adModel.html?adNo=ad_201307170008&amp;tproNo=${templateProduct.templateProductSeq!}" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe></div>-->
			<!--<iframe height="${templateProduct.templateProductHeight!}" width="${templateProduct.templateProductWidth!}" src="http://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=PFBC20150505001&amp;positionId=PP201505130039&amp;sampleId=us_201505050001&amp;tproId=${templateProduct.templateProductSeq!}&amp;format=0&amp;page=1&amp;padHeight=250&amp;padWidth=300&amp;keyword=&amp;ref=NzYzNA%3D%3D%0D%0A" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe>-->
			
			<#if '${templateProduct.templateProductXtype!}' == ''>
				<iframe  height=${templateProduct.iframeHeight!} width=${templateProduct.iframeWidth!} src="adModel.html?adNo=ad_201307170008&amp;tproNo=${templateProduct.templateProductSeq!}" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe>
				<#else>
				<iframe  style="background-color:aliceblue;border:2px solid #FFFF63" height=${templateProduct.iframeHeight!} width=${templateProduct.iframeWidth!} src="adModel.html?adNo=ad_201307170008&amp;tproNo=${templateProduct.templateProductSeq!}" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe>
			</#if>
			</div>
		</td>
    </tr>
    
    </#list>
<#else>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">
<form action="" method="post">
    <input type="hidden" id="templateProductSeq" name="templateProductSeq" value="">
</form>


           

