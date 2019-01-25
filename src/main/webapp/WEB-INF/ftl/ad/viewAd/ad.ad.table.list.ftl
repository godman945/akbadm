<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
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
<form method="post" id="tableForm" name="tableForm" >
<div>
    <span style="padding:10px;float:left">      
    	下架原因：<input type="text" id="illegalize" name="illegalize">
	    <input type="button" id="btnIllegalize" name="btnIllegalize" onClick="Illegalize()" value="確認下架" />
	</span>
	<span class="pages"><@t.insertAttribute name="page" /></span>			
</div>			

<div style="clear:both;height:50%"></div>

<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th width="50"><a href="#" onclick="checkAll()">全選</a></th>
		<th>新增日期</th>
		<th>會員帳號</th>
		<th>帳戶名稱</th>
		<th>廣告</th>
		<th style="width:6%">狀態</th>
		<th style="width:6%;min-width:130px">廣告類別</th>
		<th>裝置</th>
		<th style="width:6%">曝光數</th>
		<th style="width:6%">點擊數</th>
		<th style="width:6%">點閱率(%)</th>
		<th style="width:8%">平均點選費用</th>
		<th style="width:6%;min-width:65px">費用</th>
		<th>無效點擊數</th>
		<th>無效點擊費用</th>
		<th>廣告</th>
		<th>分類</th>
		<th>分類狀態</th>
	</tr>
</thead>
<tbody>
	<#if adAdViewVO?exists>
	
	    <#list adAdViewVO as vo>
			<tr>
				<td>
				<#--
				<input type="checkbox" id="chkAdAd_${vo_index!}" name="chkAdAd" value="${vo.adSeq!}"/>
				 -->
				<#if vo.adStatus != 0 >
			        <input type="checkbox" id="chkY_${vo_index!}" name="chkY" value="${vo.getAdSeq()!}" />
				<#else>
			        <input type="checkbox" id="chkN_${vo_index!}" name="chkN" value="${vo.getAdSeq()!}"/>
				</#if>
				</td>
				<td class="td01">${vo.adCreateTime!}</td>
				<td class="td01">${vo.memberId!}</td>
				<td class="td01">${vo.customerInfoTitle!}</td>
		        <td height="35" > 
		        	<#if "IMG" == vo.adStyle  && "N" == vo.html5Tag>
 						<div class="adreportdv">
							<span class="adboxdvimg"><a href="${vo.realUrl!}" target="_blank"><img src="${vo.originalImg!}" /></a></span>
				    		<span class="adboxdvinf">
						 		<span>
						 			<b>${vo.title!}</b><br>
						     		<i>尺寸</i><b>${vo.imgWidth!} x ${vo.imgHeight!}</b><br>
						     		<span>${vo.showUrl!}</span><br>
						     		<a class="fancy" style="cursor:pointer" onclick="preview('${vo.originalImg!}')" alt="預覽">預覽</a>
					     		</span>
				     		</span>
			     		</div>
			     	<#elseif "IMG" == vo.adStyle  && "Y" == vo.html5Tag>
			     		<div class="adreportdv">
			        		<span class="adboxdvimg">${vo.zipTitle!}</span>
				        	<span class="adboxdvinf">
						        <span>
						        	<b>${vo.title!}</b><br>
						            <i>尺寸</i><b>${vo.imgWidth!} x ${vo.imgHeight!}</b><br>
						            <span>${vo.showUrl!}</span><br>
						            <a class="fancy" style="cursor:pointer" onclick="preViewHtml5('${vo.imgWidth!}','${vo.imgHeight!}','${vo.originalImg!}','${vo.realUrl!}')" alt="預覽">預覽</a>
					            </span>
				        	</span>
			        	</div>
					<#elseif "TMG" == vo.adStyle>
		        		<iframe height="120" width="350" src="adModel.html?adNo=${vo.adSeq!}&tproNo=${vo.adTemplateNo!}" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe>
						</iframe> 
					</#if>   	
		        </td>
		        <td class="td02">
		        ${vo.adStatusDesc!}
		        <#if '${vo.adStatusDesc!}' == '違規下架' ||  '${vo.adStatusDesc!}' == '已拒絕'>
		        <img src="<@s.url value="/" />html/img/icon_Q.gif" align="absmiddle" title="${vo.adRejectReason!}">
		        </#if>
		        </td>
		        <td class="td01" height="60">
					<div style="min-height:20px;"><input type="hidden" value="${vo.adCategoryCode!}" />
					${vo.adCategoryName!}</div>
					<hr  style="border:0;background-color:#AAAAAA;height:1px;" />
					<div style="min-height:20px;">${vo.adCategoryName2!}</div>
					<hr  style="border:0;background-color:#AAAAAA;height:1px;" />
					<div style="min-height:20px;">${vo.adCategoryName3!}</div>
				</td>
				<td class="td01">${vo.adPvclkDevice}</td>
				<td class="td01">${vo.adPv?string('#,###')!}</td>
				<td class="td01">${vo.adClk?string('#,###')!}</td>
				<td class="td01">${vo.adClkRate?string('#.##')!}</td>
				<td class="td01">${vo.adClkPriceAvg?string('#.##')!}</td>
				<td class="td01">${vo.adClkPrice?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClk?string('#,###')!}</td>
				<td class="td01">${vo.adInvalidClkPrice?string('#,###')!}</td>
				<td>${vo.adActionName!}</td>
				<td>${vo.adGroupName!}<br><input type="button" id="btnKW" name="btnKW" value="關鍵字" onclick="javascript:goKW('${vo.adGroupSeq!}');"></td>
				<td>${vo.adGroupStatusDesc!}</td>
			</tr>
	    </#list>
	<#else>
	<tr>
		<td colspan="16">
			此分類中尚未建立廣告明細。請按「製作新廣告」按鈕，新增廣告明細。
		</td>
	</tr>
	</#if>
</tbody>
	<tr class="tbg">
		<td colspan="8">總計：${totalSize!}筆</td>
		<td class="td01">${totalPv?string('#,###')!}</td>
		<td class="td01">${totalClk?string('#,###')!}</td>			
		<td class="td01">${totalClkRate?string('#.##')!}</td>
		<td class="td01">${totalAvgCost?string('#.##')!}</td>
		<td class="td01">${totalCost?string('#,###')!}</td>
		<td class="td01">${totalInvalidClk?string('#,###')!}</td>
		<td class="td01">${totalInvalidClkPrice?string('#,###')!}</td>
		<td colspan="3"></td>
	</tr>
</table>
<input type="hidden" id="adAdSeq" name="adAdSeq" />
<input type="hidden" id="status" name="status" />
<input type="hidden" id="adclkDevice" name="adclkDevice" value="${adclkDevice!}" />
<iframe id="ifrmIllegalize" name="ifrmIllegalize" style="display:none;height:150px;width:600px"></iframe>
</form>
