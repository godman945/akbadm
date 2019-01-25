<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<div class="cont">
    <h2>
        <img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告商品測試
    </h2>
    <div class="grtba borderbox" style="height: 80px;">
        <div style="clear:both;background:#e4e3e0" >
          <form id="templateProductSearchForm" action="templateProductSearch.html" method="post">
                <table width="75%">
                    <tr>
                        <td>請選擇類型 :
                        	<select id="adTypeSelect" style="width:82px;" onChange="templateTypeSelect();">
                        	 <option value="0"></option>
                        	 <option value="1">搜尋</option>
                        	 <option value="2">內容</option>
                        	</select>
                        </td>
                         <td>請選擇尺寸 :
                        	<select id="sizeSelect" style="width:150px;">
                        	<#if pfbxSizeList?exists>
                        	 	<#list pfbxSizeList as pfbxSize>
                        	 	 <option>${pfbxSize.width!} x ${pfbxSize.height!}</option>
                        	 	</#list>
                        	 	<#else>
                        	 </#if>
                        	</select>
                        </td>
                        <td>
                            <input type="button" value="搜尋" onClick="searchBtn()">
                        </td>
                    </tr>
                    <tr id="content" style="display:none;">
                     <td>廣告類型 :
                      	<select id="templateContent" style="width:150px;" >
                        	 <option value="0"></option>
                        	 <option value="1">文字</option>
                        	 <option value="2">圖片</option>
                        	 <option value="3">Flash</option>
                        	 <option value="4">圖文</option>
                        </select>
                     </td>
                    </tr>
                    <tr id="search" style="display:none;">
                     <td>請輸入關鍵字 :
                      <input id="keyWord" type="text" value="" style="width:200xp;">
                     </td>
                    </tr>
                </table>
                
				<input type="hidden" id="templateAdType"  name="templateAdType"  value =<#if templateAdType ? exists>'${templateAdType}'</#if>>
                <input type="hidden" id="adType"  name="adType"  value =<#if adType ? exists>'${adType}'</#if>>
                <input type="hidden" id="keyWordText"  name="keyWordText"  value ='${keyWordText!}'>
                <input type="hidden" id="size"  name="size"  value ='${size!}'>
            </form>
        </div>
    </div>
</div>  
	<!--使用內容廣告商品樣版-->
		<#if templateProductQueryList?exists>
		<div id="adContent" style="width:100%;background-color:aliceblue;">
		<#if '${templateProductQueryList?size!}' != '0'>
			<table border="1">
				<tr>
    				<th style="text-align:center;">tpro</th>
    				<th style="text-align:center;">商品名稱</th>
    				<th style="text-align:center;">樣版類型</th>
    				<th style="text-align:center;">類型</th>
    				<th style="text-align:center;">Demo</th>
  				</tr>
   			<#list templateProductQueryList as admTemplateProduct>
   				<tr border="1">
   					<td style="text-align:center;">
   						${admTemplateProduct.getTemplateProductSeq()!}
   					</td>
   					<td style="text-align:center;">
   						${admTemplateProduct.getTemplateProductName()!}
   					</td>
   					<td style="text-align:center;">
   						<#if '${admTemplateProduct.getAdmTemplateAdType()!}' == '1'>
   						文字
   						<#elseif '${admTemplateProduct.getAdmTemplateAdType()!}' == '2'>
   						圖片
   						<#elseif '${admTemplateProduct.getAdmTemplateAdType()!}' == '3'>
   						Flash
   						<#elseif '${admTemplateProduct.getAdmTemplateAdType()!}' == '4'>
   						圖文
   						</#if>
   					</td>
   					<td style="text-align:center;">
   						<#if '${admTemplateProduct.getAdType()!}' == '1'>
   						 	搜尋廣告
   						 <#else>
   						 	聯播廣告
   						</#if>
   					</td>
   					<td style="text-align:center;">
   						<#if '${admTemplateProduct.getAdType()!}' == '2'>
   							<script src='http://kwstg1.mypchome.com.tw/adm/adtest2.html?tproId=${admTemplateProduct.getTemplateProductSeq()!}&format=1'></script>
   							<#else>
   							<script src='http://kwstg1.mypchome.com.tw/adm/adtest2.html?tproId=${admTemplateProduct.getTemplateProductSeq()!}&keyWord=${keyWordText!}&format=1'></script>
   						</#if>
   					</td>
   				<tr>
   				
   			</#list>
   			<table>
   		  </#if>	
  		</#if>
	</div>
	
