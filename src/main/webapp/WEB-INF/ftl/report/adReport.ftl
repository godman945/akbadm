<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>
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
  
</style>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告明細成效</h2>

<form action="adReport.html" method="post">

<table width="750">
    <tr>
        <td>廣告型式:
            <select id="adType" name="adType">
                <option value="">全部</option>
   	            <#list adTypeSelectOptionsMap?keys as skey>
		  		    <option value="${skey}" <#if adType?exists && adType = skey>selected="selected" </#if>>${adTypeSelectOptionsMap[skey]}</option>
		  	    </#list>
		    </select>
           
        </td>
    </tr>
    <tr><td></td></tr>
    <tr>
        <td>查詢日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		</td>
	</tr>
    <tr>
        <td>經銷歸屬:
        	<select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部</option>
				<#if companyList?exists && (companyList?size>0)>
   	            	<#list companyList as company>
		  		    <option value="${company.customerInfoId}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId = company.customerInfoId>selected="selected" </#if>>${company.companyName}</option>
		  	    	</#list>
		  	    </#if>
		    </select>
		    &nbsp;&nbsp;
		    <select id="searchType" name="searchType">
		    	<option value="">全部</option>
		    	<option value="searchPfpId" <#if searchType == 'searchPfpId'>selected</#if> >帳戶編號</option>
		    	<option value="searchPfpTitle" <#if searchType == 'searchPfpTitle'>selected</#if> >廣告客戶</option>
		    </select>
		    &nbsp;&nbsp;
		    <input type="text" id="searchText" name="searchText" style="width:150px;" value="${searchText!}" />
		    
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="downloadReport()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if voList?exists && (voList?size>0)>
<div>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>
<table id="tableView" class="table01 tablesorter" style="width:1335px;word-wrap:break-word;word-break:break-all;">
    <thead>
	    <tr>
	        <th class="td09" style="min-width:120px;">帳戶名稱</td>
	        <th class="td09" style="min-width:350px;">廣告內容</td>
	        <th class="td09" style="min-width:100px;">廣告活動</td>
	        <th class="td09" style="min-width:100px;">廣告群組</td>
	        <th class="td09" style="min-width:300px;">曝光位置</td>
	        <th class="td09" style="min-width:90px;">曝光數</td>
	        <th class="td09" style="min-width:60px;">互動數</td>
	        <th class="td09" style="min-width:50px;">互動率</td>
	        <th class="td09" style="min-width:90px;">平均互動出價</td>
	        <th class="td09" style="min-width:90px;">千次曝光出價</td>
	        <th class="td09" style="min-width:80px;">費用</td>
	        <th class="td09" style="min-width:70px;">每日成效</td>
	    </tr>
    </thead>
    <tbody>
    <#list voList as data>
	    <tr>
	        <td class="td09">${data.customerName!}<br>${data.customerId!}</td>
	        <td class="td09">
	        	<#if "IMG" == data.adStyle && "N" == data.html5Tag>
	 				<div class="adreportdv">
						<span class="adboxdvimg"><a href="${data.realUrl!}" target="_blank"><img src="${data.originalImg!}" /></a></span>
					    <span class="adboxdvinf">
							 <span>
							 	 <b>${data.title!}</b><br>
							     <i>尺寸</i><b>${data.imgWidth!} x ${data.imgHeight!}</b><br>
							     <span>${data.showUrl!}</span><br>
							     <a class="fancy" style="cursor:pointer" onclick="preview('${data.originalImg!}')" alt="預覽">預覽</a>
						     </span>
					     </span>
				     </div>
				<#elseif "IMG" == data.adStyle && "Y" == data.html5Tag>
					<div class="adreportdv">
		        		<span class="adboxdvimg">${data.zipTitle!}</span>
			        	<span class="adboxdvinf">
					        <span>
					        	<b>${data.title!}</b><br>
					            <i>尺寸</i><b>${data.imgWidth!} x ${data.imgHeight!}</b><br>
					            <span>${data.showUrl!}</span><br>
					            <a class="fancy" style="cursor:pointer" onclick="preViewHtml5('${data.imgWidth!}','${data.imgHeight!}','${data.originalImg!}','${data.realUrl!}')" alt="預覽">預覽</a>
				            </span>
			        	</span>
		        	</div>
				<#elseif "TMG" == data.adStyle>
					${data.content!}
				<#elseif "VIDEO" == data.adStyle>
					<div class="adreportdv">
						<span class="adboxdvimg">${data.adDetailContent!} (影音廣告)</span>
					    <span class="adboxdvinf">
							<span>
							 	<b>${data.adDetailContent!}</b><br>
							    <i>尺寸</i><b>${data.adDetailVideoWidth!}x${data.adDetailVideoHeight!}</b><br>
					            <span>${data.adDetailRealUrl!}</span><br>
							    <a class="fancy" style="cursor:pointer" onclick="previewVideo('${akbPfpServer!}', '${data.adDetailVideoWidth!}', '${data.adDetailVideoHeight!}', '${data.adDetailVideoUrl!}', '${data.adDetailImg?url}', '${data.adDetailRealUrl!}')" alt="預覽">預覽</a>
						    </span>
					    </span>
				     </div>
	        	</#if>
	        </td>
	        <td class="td09">${data.adAction!}</td>
	        <td class="td09">${data.adGroup!}</td>
	        <td class="td08">${data.realUrl!}</td>
	        <td class="td10">${data.kwPvSum!}</td>
	        <td class="td10">${data.kwClkSum!}</td>
	        <td class="td10">${data.kwClkRate!}</td>
	        <td class="td10">$ ${data.clkPriceAvg!}</td>
	        <td class="td10">$ ${data.pvPriceAvg!}</td>
	        <td class="td10">$ ${data.kwPriceSum!}</td>
	        <td class="td09">
	        	<a style="cursor:pointer;" onclick="fundDetalView('${startDate!}','${endDate!}','${data.adSeq!}','${data.title!}')" >查看</a>
	        </td>
	    </tr>
    </#list>
    <tbody>
    <tr>
    	<td class="td09" colspan="5" style="background-color:#99FFFF;height:30px;" >總計：${totalSize!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >${totalVO.kwPvSum!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >${totalVO.kwClkSum!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >${totalVO.kwClkRate!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >$ ${totalVO.clkPriceAvg!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >$ ${totalVO.pvPriceAvg!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" >$ ${totalVO.kwPriceSum!}</td>
    	<td class="td10" style="background-color:#99FFFF;height:30px;" ></td>
    </tr>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
<div style="display:none;">
	<div id="adDetalDiv">
	
	</div>
</div>