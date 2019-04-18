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
</style>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />商品審核</h2>

<form action="" method="post">

<table>
    <tr>
        <td>客戶名稱:
            <select id="pfpCustomerInfoId" name="pfpCustomerInfoId">
                <option value="">全部</option>
   	            <#list pfpCustomerInfoList as map>
		  		    <option value="${map.pfp_customer_info_id}" <#if pfpCustomerInfoId?exists && pfpCustomerInfoId == map.pfp_customer_info_id>selected="selected"</#if>>${map.customer_info_title!}</option>
		  	    </#list>
		    </select>
        </td>
        <td>商品目錄名稱:
            <select id="catalogSeq" name="catalogSeq">
                <option value="">全部</option>
   	            <#list catalogList as map>
		  		    <option value="${map.catalog_seq}" <#if catalogSeq?exists && catalogSeq == map.catalog_seq>selected="selected"</#if>>${map.catalog_name!}</option>
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

<#if catalogProdEcList?exists && (catalogProdEcList?size>0)>

<table>
    <tr style="vertical-align:top;">
        <td>
        	<input type="button" value="核准" onclick="doApprove()">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="拒絕" onclick="doReject()">
        </td>
        <td>
        	&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="allReject" />退件原因&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="text" id="allRejectReason" name="allRejectReason" disabled />
        </td>
    </tr>
</table>
<@t.insertAttribute name="page" />
<table class="table01">
    <tr>
        <td class="td01" width="50">全選<input type="checkbox" onclick="selectAll_new(this, 'catalogProdIds')" /></td>
        <td class="td01">商品明細</td>
        <td class="td01" width="200">帳戶名稱</td>
        <td class="td01" width="100">商品目錄名稱</td>
        <td class="td01" width="150">日期</td>
    </tr>

    <#list catalogProdEcList as map>
    <tr>
        <td class="td03"><input type="checkbox" name="catalogProdIds" value="${map.id!}" /></td>
        <td class="td02">
			<div class="adreportdv">
				<span class="adboxdvimg"><a href="${map.ec_url!}" target="_blank"><img src="${map.ec_img!}" /></a></span>
		    </div>
			<div>
				<div>${map.ec_name!}</div>
				<div>原價 $${map.ec_price!}</div>
				<div>特價 $${map.ec_discount_price!}</div>
				<div>${map.ec_url!}</div>
			</div>
		</td>
        <td class="td03">${map.customer_info_title!}</td>
        <td class="td03">${map.catalog_name!}</td>
        <td class="td03">${map.ec_send_verify_time?string("yyyy-MM-dd HH:mm:ss")!}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
