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
  #fancybox-close,.fancybox-close{ background: transparent url(/adm/html/img/pop-close.png) left top no-repeat!important; right:-30px!important; top:0px!important; width:30px!important; height: 30px!important;}
</style>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />影音廣告成效查詢</h2>

<form action="adTimeReport.html" method="get">
<table width="750">
    <tr>
        <td>查詢日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		    <br/>
            <BR>查詢條件 : 
            <select id="pfdCustomerInfoId" name="pfdCustomerInfoId">
                <option value="">全部經銷商</option>
                <#list pfdCustomerInfoIdMap?keys as skey>
		    		<option value="${skey}" <#if pfdCustomerInfoId?exists && pfdCustomerInfoId == skey>selected="selected"</#if>>${pfdCustomerInfoIdMap[skey]}</option>
		    	</#list>
		    </select>
		    <input type="text" id="memberId" name="memberId" value="${memberId!}" />
		    <br/>
		    <BR>計價方式 : 
		    <select id="adPriceType" name="adPriceType">
                <option value="">全部</option>
		    	<#list adPriceTypeSet as key>
		  			<option value="${key}" <#if adPriceType?exists && adPriceType == key>selected="selected"</#if> >${key}</option>
		  	    </#list>
		    </select>
		    <br/>
		    <BR>播放裝置 : 
		    <select id="adDevice" name="adDevice">
                <option value="">全部</option>
		    	<#list adDeviceMap?keys as key>
		  			<option value="${key}" <#if adDevice?exists && adDevice == key>selected="selected"</#if> >${adDeviceMap[key]}</option>
		  	    </#list>
		    </select>
		    <br/>
		    <BR>廣告尺寸 : 
		    <select id="adSize" name="adSize">
                <option value="">全部</option>
		    	<#list adSizeSet as key>
		  			<option value="${key}" <#if adSize?exists && adSize == key>selected="selected"</#if> >${key}</option>
		  	    </#list>
		    </select>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="doDownlaod()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if voList?exists && (voList?size>0)>
	<div>
		<span class="pages"><@t.insertAttribute name="page" /></span>			
	</div>
	
	<table id="tableView" class="table01 tablesorter" style="width:1450px;">
	    <thead>
		    <tr>
		    	<th class="td09" style="min-width:100px;">日期</th>
		        <th class="td09" style="min-width:120px;">經銷商</th>
		        <th class="td09" style="min-width:120px;">廣告客戶</th>
		        <th class="td09" style="min-width:350px;">影片明細</th>
		        <th class="td09" style="min-width:60px;">計價方式</th>
		        <th class="td09" style="min-width:60px;">播放裝置</th>
		        <th class="td09" style="min-width:60px;">廣告尺寸</th>
		        <th class="td09" style="min-width:60px;">曝光數</th>
		        <th class="td09" style="min-width:60px;">收視數</th>
		        <th class="td09" style="min-width:60px;">收視率</th>
		        <th class="td09" style="min-width:60px;">單次收視費用</th>
		        <th class="td09" style="min-width:60px;">千次曝光費用</th>
		        <th class="td09" style="min-width:60px;">費用</th>
		        <th class="td09" style="min-width:60px;">點擊數</th>
		        <th class="td09" style="min-width:60px;">點擊率</th>
		        <th class="td09" style="min-width:60px;">IDC播放次數</th>
		        <th class="td09" style="min-width:60px;">更多資訊</th>
		    </tr>
	    </thead>
	    <tbody>
	    <#list voList as vo>
		    <tr>
		        <td class="td09">${vo.adVideoDate?string("yyyy-MM-dd")}</td>
		        <td class="td09">${vo.pfdCustomerInfoName!}</td>
		        <td class="td09">${vo.pfpCustomerInfoId!}<br>${vo.pfpCustomerInfoName!}</td>
		        <td class="td08">
			        <div class="adreportdv">
						<span class="adboxdvimg">${vo.adDetailContent!} (影音廣告)</span>
					    <span class="adboxdvinf">
							<span>
							 	<b>${vo.adDetailContent!}</b><br>
							    <i>尺寸</i><b>${vo.tproWidth!}x${vo.tproHeight!}</b><br>
							    <span>${vo.adDetailRealUrl!}</span><br>
							    <a class="fancy" style="cursor:pointer" onclick="previewVideo('${akbPfpServer!}', '${vo.tproWidth!}', '${vo.tproHeight!}', '${vo.adDetailVideoUrl!}', '${vo.adDetailImg?url}', '${vo.adDetailRealUrl!}')" alt="預覽">預覽</a>
						    </span>
					    </span>
					</div>
		        </td>
		        <td class="td09">${vo.adPriceType!}</td>
		        <td class="td09">${vo.adDevice!}</td>
		        <td class="td09">${vo.tproWidth}x${vo.tproHeight}</td>
		        <td class="td09">${vo.adPv?string('#,###')}</td>
		        <td class="td09">${vo.adView?string('#,###')}</td>
		        <td class="td09">${vo.adViewRate?string('#,##0.00')}%</td>
		        <td class="td09">$ ${vo.adViewPrice?string('#,##0.00')}</td>
		        <td class="td09">$ ${vo.adPvPriceAvg?string('#,##0.00')}</td>
		        <td class="td09">$ ${vo.adPrice?string('#,##0.000')}</td>
		        <td class="td09">${vo.adClk?string('#,###')}</td>
		        <td class="td09">${vo.adClkRate?string('#,##0.00')}%</td>
		        <td class="td09">${vo.adVideoIdc?string('#,###')}</td>
		        <td class="td09"><a onclick="fundDetalView('${vo.adVideoDate?string("yyyy-MM-dd")}', '${vo.pfdCustomerInfoId}', '${vo.pfpCustomerInfoId}', '${vo.adId}');">查看</a></td>
		    </tr>
	    </#list>
	    </tbody>
	    <tr>
	    	<td class="td09" colspan="7" style="background-color:#99FFFF;" >總計：${totalCount!}筆</td>
	    	<td class="td09" style="background-color:#99FFFF;">${totalPv?string('#,###')!}</td>
	    	<td class="td09" style="background-color:#99FFFF;">${totalAdView?string('#,###')!}</td>
	    	<td class="td09" style="background-color:#99FFFF;">${totalAdViewRate?string('0.00')!}%</td>
	    	<td class="td09" style="background-color:#99FFFF;">$ ${totalAdViewPrice?string('#,##0.00')!}</td>
	    	<td class="td09" style="background-color:#99FFFF;">$ ${totalPvPriceAvg?string('#,##0.00')!}</td>
	    	<td class="td09" style="background-color:#99FFFF;">$ ${totalPrice?string('#,##0.000')!}</td>
	        <td class="td09" style="background-color:#99FFFF;">${totalClk?string('#,###')!}</td>
	        <td class="td09" style="background-color:#99FFFF;">${totalClkRate?string('0.00')!}%</td>
	    	<td class="td09" style="background-color:#99FFFF;">${totalVideoIdc?string('#,###')!}</td>
	        <td class="td09" style="background-color:#99FFFF;"></td>
	    </tr>
	</table>
<#else>
	${message!}
</#if>
</form>

<div style="display:none;">
	<div id="adVideoDetalDiv"></div>
</div>