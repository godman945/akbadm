<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<#setting url_escaping_charset="UTF-8">

<style type="text/css">
  .adreportdv {overflow: hidden; text-overflow: ellipsis;word-break: break-all;min-width: 350px;min-height:40px;max-height:150px;font: 400 13px/16px Verdana,微軟正黑體;overflow:hidden;display:table; display:block\9;}
  .adreportdv .adboxdvimg{max-height:150px;width:150px;display:table-cell;display:block\9;float:left\9;vertical-align:middle;text-align:center;}
  .adreportdv .adboxdvinf{display:table-cell;display:block\9;float:left\9;vertical-align:middle;min-width: 200px;width: 200px\9;text-align: left;}
  .adreportdv .adboxdvinf>span{min-height:40px;display:table-cell;display:block\9;vertical-align:middle;min-width: 200px;padding: 0 0 0 10px;}
  .adreportdv .adboxdvimg img{max-width:150px;max-height:150px;}
  .adreportdv i{padding-right:10px;font-style:normal;color: #888;}
  .adreportdv b{font-weight:400}
  .adboxdvinf>span>i:first-child+b{font-weight:bold}
  #fancybox-close,.fancybox-close{ background: transparent url(<@s.url value="/" />html/img/pop-close.png) left top no-repeat!important; right:-30px!important; top:0px!important; width:30px!important; height: 30px!important;}
  .center {text-align: center;}
</style>
<script type="text/javascript">
var currentImageArray = new Array();
var logoSaleImgArray = new Array();
var saleImgArray = new Array();
<#list adDataList as adData>
	logoSaleImgArray["${adData.adSeq!}"] = new Array();
	saleImgArray["${adData.adSeq!}"] = new Array();

    <#assign i = 0>
	<#list adData.adDetailLogoSaleImgList as logoSaleImg>
		logoSaleImgArray["${adData.adSeq!}"][${i}] = "${logoSaleImg}";
    	<#assign i = i+1>
	</#list>

    <#assign i = 0>
	<#list adData.adDetailSaleImgList as saleImg>
		saleImgArray["${adData.adSeq!}"][${i}] = "${saleImg}";
    	<#assign i = i+1>
	</#list>
</#list>

function openDialog(imageArray) {
	currentImageArray = imageArray;
	showImage(0);
	$('#dialogDiv').dialog('open');
}

function showImage(index) {
	var total = currentImageArray.length;
	var previousIndex = 0;
	var nextIndex = 0;
	var dialogHtml = "";
	var imageSizeHtml = "";

	if (total == 0) {
		imageSizeHtml = "0 / 0";

		$('#dialogDiv').html(dialogHtml);
		$('#dialogDiv').dialog({title:imageSizeHtml});

		return;
	}
	
	previousIndex = index == 0 ? total - 1 : (index - 1)  % total;
	nextIndex = (index + 1) % total;

	dialogHtml = "<div>";
	dialogHtml += "<a href='#' onclick='showImage(" + previousIndex + ")'>上一張</a>&nbsp;";
	dialogHtml += "<a href='#' onclick='showImage(" + nextIndex + ")'>下一張</a>";
	dialogHtml += "</div>";
	dialogHtml += "<div>";
	dialogHtml += "<img src='" + currentImageArray[index] + "' />";
	dialogHtml += "</div>";
	imageSizeHtml = (index + 1) + " / " + total;

	$('#dialogDiv').html(dialogHtml);
	$('#dialogDiv').dialog({title:imageSizeHtml});
}
</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告審核</h2>

<form action="adCheck.html" method="post">

<table width="1200">
    <tr>
        <td>送審日期:
        	<input type="text" id="startDate" name="sendVerifyStartTime" value="${sendVerifyStartTime!}"  readonly/>
        	&nbsp; ~ &nbsp;
            <input type="text" id="endDate" name="sendVerifyEndTime" value="${sendVerifyEndTime!}"  readonly/>
		</td>
        <td>廣告樣式:
            <select name="style">
                <option value="">全部</option>
   	            <#list adStyleSelectOptionsMap?keys as skey>
		  		    <option value="${skey}" <#if style?exists && style = skey>selected="selected" </#if>>${adStyleSelectOptionsMap[skey]}</option>
		  	    </#list>
		    </select>
        </td>
        <td>廣告狀態:
            <select name="status">
                <option value="">全部</option>
   	            <#list adStatusSelectOptionsMap?keys as skey>
		  		    <option value="${skey}" <#if status?exists && status = skey>selected="selected" </#if>>${adStatusSelectOptionsMap[skey]}</option>
		  	    </#list>
		    </select>
        </td>
        
        <td>經銷商名稱:
            <select id="adPfdAllNameMapSelect">
                <option value="">全部</option>
   	            <#list adPfdAllNameMap?keys as key>
		  		    <option value="${key}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId = key>selected="selected" </#if>>${adPfdAllNameMap[key]}</option>
		  	    </#list>
		    </select>
        </td>
        
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if adDataList?exists && (adDataList?size>0)>

<table>
    <tr style="vertical-align:top;">
        <td>
        	<input type="button" value="核准" onclick="doApprove()">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="核准/待客戶確認" onclick="doApproveAndPause()">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="拒絕" onclick="doReject()">
        </td>
        <td>
        	&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allReject" />退件原因&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="text" id="allRejectReason" name="allRejectReason" disabled />
        </td>
        <td>
        	&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allAdCategoryCheck" />廣告類別&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <td style="width:100px;padding: 1px 10px 1px 1px;">
        	<select class="adCategory" id="allAdCategory_1" disabled onchange ="changeCategoryItems('allAdCategory_1');">
        		 <option value="">--廣告類別--</option>
        	</select>
        </td>
        <td style="width:100px;padding: 1px 10px 1px 10px;">
        	<select class="adCategory" id="allAdCategory_2" disabled onchange ="changeCategoryItems('allAdCategory_2');">
        		 <option value="">--廣告類別--</option>
        	</select>
        </td>
        <td style="width:100px;padding: 1px 10px 1px 10px;">
        	<select class="adCategory" id="allAdCategory_3" disabled onchange ="changeCategoryItems('allAdCategory_3');">
        		 <option value="">--廣告類別--</option>
        	</select>
        </td>
    </tr>
</table>
<@t.insertAttribute name="page" />
<table class="table01" width="1200">
    <tr>
    	<!-- -->
    	<td style="display:none" class="td01" width="100">CODE</td>
    	<td style="display:none" class="td01" width="100">CATEGORY</td>
        <td class="td01" width="50">全選<input type="checkbox" onclick="selectAll_new(this, 'adSeqs', 'adCategory', 'rejectReason')" /></td>
        
        <td class="td01" width="200">經銷商名稱</td>
        <td class="td01">廣告預覽</td>
        <td class="td01" width="100">審核狀態</td>
        <td class="td01" width="200">違規項目</td>
        <td class="td01" width="100">廣告類別</td>
        <td class="td01" width="100">退件原因</td>
        <td class="td01" width="100">廣告活動</td>
        <td class="td01" width="100">廣告群組</td>
        <td class="td01" width="100">帳戶名稱</td>
        <td class="td01" width="150">送審日期</td>
        <td class="td01" width="260">廣告序號</td>
    </tr>

    <#assign index=(pageNo-1)*pageSize>
    <#list adDataList as adData>
    <#assign index = index+1>
    <tr>
    	<td style="display:none" class="td03" rowspan="3"><input  id="adCategoryCode_${adData.adSeq!}" type="text" name="adCategoryCode" ></td>
        <td style="display:none"  class="td03" rowspan="3"><input  id="adCategoryId_${adData.adSeq!}"  type="text" name ="adCategory"></td>
        <!--
        <td class="td03" rowspan="3"><input type="checkbox" name="adSeqs" value="${adData.adSeq!}" onclick="controlAdUI(this, 'adCategory_${adData.adSeq!}', 'rejectReason_${adData.adSeq!}')"></td>
        -->
        <td class="td03" rowspan="3"><input type="checkbox" name="adSeqs" value="${adData.adSeq!}" onclick="controlAdSelectUI(this, 'adCategory_${adData.adSeq!}', 'rejectReason_${adData.adSeq!}')"></td>
        <td class="td03" rowspan="3"> 
				<#list adPfdNameMap?keys as key>
					<#if adData.adSeq == key>
						${adPfdNameMap[key]}
					</#if>
		  	    </#list>
		</td>
        <td class="td02" rowspan="3">
			<#if "IMG" == adData.adStyle && "N" == adData.html5Tag>
 				<div class="adreportdv">
					<span class="adboxdvimg"><a href="${adData.realUrl!}" target="_blank"><img src="${adData.originalImg!}" /></a></span>
				    <span class="adboxdvinf">
						 <span>
						 	 <b>${adData.title!}</b><br>
						     <i>尺寸</i><b>${adData.imgWidth!} x ${adData.imgHeight!}</b><br>
						     <span>${adData.showUrl!}</span><br>
						     <a class="fancy" style="cursor:pointer" onclick="preview('${adData.originalImg!}')" alt="預覽">預覽</a>
						     <br><#if adData.thirdCode!=''>第三方偵測:<textarea  style="width:150px;height:80px;">${adData.thirdCode!}</textarea></#if>
					     </span>
				     </span>
			     </div>
			<#elseif "IMG" == adData.adStyle && "Y" == adData.html5Tag>
				<div class="adreportdv">
					<span class="adboxdvimg">${adData.zipTitle!}</span>
				    <span class="adboxdvinf">
						 <span>
						 	 <b>${adData.title!}</b><br>
						     <i>尺寸</i><b>${adData.imgWidth!} x ${adData.imgHeight!}</b><br>
						     <a href="${adData.realUrl!}" target="_blank">${adData.showUrl!}</a><br>
						     <a href="${adData.originalImg!}" target="_blank">查看完整廣告內容</a>
						     <#if adData.zipFile?exists>
						     <br><a href="${adData.zipFile!}" target="_blank">ZIP檔下載</a>
						     	<br><#if adData.thirdCode!=''>第三方偵測:<textarea  style="width:150px;height:80px;">${adData.thirdCode!}</textarea></#if>
						     </#if>
					     </span>
				     </span>
			     </div>
			<#elseif "TMG" == adData.adStyle>
				<span>
					<iframe height="120" width="350" src="adModel.html?adNo=${adData.adSeq!}&amp;tproNo=tpro_201306280001" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe> 
					<br><#if adData.thirdCode!=''>第三方偵測:<textarea  style="width:150px;height:80px;">${adData.thirdCode!}</textarea></#if>
				</span>
			<#elseif "VIDEO" == adData.adStyle>
				<div class="adreportdv">
					<span class="adboxdvimg">${adData.adDetailContent!} (影音廣告)</span>
				    <span class="adboxdvinf">
						 <span>
						 	 <b>${adData.adDetailContent!}</b><br>
						     <i>尺寸</i><b>${adData.adDetailVideoWidth!}x${adData.adDetailVideoHeight!}</b><br>
						     <span>${adData.adDetailRealUrl!}</span><br>
						     <a class="fancy" style="cursor:pointer" onclick="previewVideo('${akbPfpServer!}', '${adData.adDetailVideoWidth!}', '${adData.adDetailVideoHeight!}', '${adData.adDetailVideoUrl!}', '${adData.adDetailImg?url}', '${adData.adDetailRealUrl!}')" alt="預覽">預覽</a>
						      <br><#if adData.thirdCode!=''>第三方偵測:<textarea  style="width:150px;height:80px;">${adData.thirdCode!}</textarea></#if>
					     </span>
				     </span>
			     </div>
			<#elseif "PROD" == adData.adStyle>
				<div class="adreportdv">
					<div>
						<span class="adboxdvimg"><iframe width="300" height="250" src="adProdModel.html?posterType=${adData.adbgType!}&previewTpro=${adData.previewTpro!}&btnBgColor=${adData.adDetailBuybtnBgColor?url}&btnFontColor=${adData.adDetailBuybtnFontColor?url}&btnTxt=${adData.adDetailBuybtnTxt?url?url}&disBgColor=${adData.adDetailDisBgColor?url}&disFontColor=${adData.adDetailDisFontColor?url}&disTxtType=${adData.adDetailDisTxtType!}&logoBgColor=${adData.adDetailLogoBgColor?url}&logoFontColor=${adData.adDetailLogoFontColor?url}&userLogoPath=${adData.adDetailLogoImgUrl!}&logoText=${adData.adDetailLogoTxt!}&prodLogoType=${adData.adDetailLogoType!}&realUrl=${adData.adDetailProdAdUrl!}&catalogGroupId=${adData.adDetailProdGroup!}&imgProportiona=${adData.adDetailProdImgShowType!}&imgShowType=${adData.adDetailSaleImgShowType!}&saleImg=${adData.adDetailSaleImg!}&saleEndImg=${adData.adDetailSaleEndImg!}"></iframe></span>
					    <span class="adboxdvinf">
						    <div>${adData.adDetailProdReportName!}</div>
							<div>${adData.adDetailProdAdUrl!}</div>
					    </span>
				    </div>
				    <div>
				    	<span><a onclick="openDialog(logoSaleImgArray['${adData.adSeq!}'])">行銷圖像</a></span>
				    	<span><a onclick="openDialog(saleImgArray['${adData.adSeq!}'])">結尾行銷圖像</a></span>
				    	<span>${adData.adDetailLogoTxt!}</span>
				    </div>
			     </div>
        	</#if>
		</td>
        <td class="td03" rowspan="3">${adData.status!}</td>
        <td class="td02" rowspan="3">
        	${adData.illegalKeyWord!}
        	<br><#if adData.thirdCode!=''>第三方偵測:<textarea  style="width:150px;height:80px;">${adData.thirdCode!}</textarea></#if>
        </td>
        <td class="td03" style="height:50px;">
         	<select   class="adCategory" id="adCategory_${adData.adSeq!}_1" disabled onchange ="changeCategoryItems('adCategory_${adData.adSeq!}_1');">
        		 <option value="">--廣告類別--</option>
        	</select>
        	<#if pfpAdCategoryMappingList?exists && (pfpAdCategoryMappingList?size>0)>
        	    <#list pfpAdCategoryMappingList as pfpAdCategoryMappingListVO>
        	         <#if "${adData.adSeq!}" == "${pfpAdCategoryMappingListVO.adSeq!}" >
                        <input type="hidden" name ="adCategoryCodeMapping"  value="${pfpAdCategoryMappingListVO.code!}">
                    </#if>
        	    </#list>
        	</#if>
        </td>
        <td class="td02" rowspan="3">
            <input type="text"  name="rejectReason" id="rejectReason_${adData.adSeq!}" value="${adData.adVerifyRejectReason!}"  disabled>
        </td>
        <td class="td02" rowspan="3">${adData.adGroupName!}</td>
        <td class="td02" rowspan="3">${adData.adActionName!}</td>
        <td class="td02" rowspan="3">${adData.customerName!}</td>
        <td class="td03" rowspan="3">${adData.sendVerifyTime!}</td>
        <td class="td03" rowspan="3">${adData.adSeq!}</td>
    </tr>
    <tr>
    	<td class="td03" style="height:50px;">
         	<select   class="adCategory" id="adCategory_${adData.adSeq!}_2" disabled onchange ="changeCategoryItems('adCategory_${adData.adSeq!}_2');">
        		 <option value="">--廣告類別--</option>
        	</select>
        </td>
    </tr>
    <tr>
    	<td class="td03" style="height:50px;">
         	<select   class="adCategory" id="adCategory_${adData.adSeq!}_3" disabled onchange ="changeCategoryItems('adCategory_${adData.adSeq!}_3');">
        		 <option value="">--廣告類別--</option>
        	</select>
        </td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>
<div id="dialogDiv" class="center display:none"></div>
<input id="pfdCustomerInfoId" name="pfdCustomerInfoId" type="hidden" value=""/>
</form>
