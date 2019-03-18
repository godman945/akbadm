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

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />LOGO審核</h2>

<form action="" method="post">

<table>
    <tr>
        <td>客戶名稱:
            <select name="pfpCustomerInfoId">
                <option value="">全部</option>
   	            <#list pfpCustomerInfoList as map>
		  		    <option value="${map.pfp_customer_info_id}" <#if pfpCustomerInfoId?exists && pfpCustomerInfoId == map.pfp_customer_info_id>selected="selected"</#if>>${map.customer_info_title!}</option>
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

<#if catalogLogoList?exists && (catalogLogoList?size>0)>

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
        <td class="td01" width="50">全選<input type="checkbox" onclick="selectAll_new(this, 'catalogLogoSeqs')" /></td>
        <td class="td01">LOGO</td>
        <td class="td01" width="100">經銷商名稱</td>
        <td class="td01" width="200">帳戶名稱</td>
        <td class="td01" width="150">日期</td>
    </tr>

    <#list catalogLogoList as map>
    <tr>
        <td class="td03"><input type="checkbox" name="catalogLogoSeqs" value="${map.catalog_logo_seq!}" /></td>
        <td class="td02">
			<div class="adreportdv">
				<span class="adboxdvimg"><img src="${map.catalog_logo_url!}" /></span>
			    <span class="adboxdvinf">
					<#if map.catalog_logo_type == "0">1比1
					<#elseif map.catalog_logo_type == "1">4比1
					</#if>
					<br />
				    <a class="fancy" style="cursor:pointer" onclick="preview('${map.catalog_logo_url!}')" alt="預覽">預覽</a>
			    </span>
		    </div>
		</td>
        <td class="td03">${map.pfd_company_name!}</td>
        <td class="td03">${map.customer_info_title!}</td>
        <td class="td03">${map.logo_send_verify_time?string("yyyy-MM-dd HH:mm:ss")!}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
