<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />版位定價查詢</h2>

<form action="pfbPositionPriceQuery.html" method="post">
<table width="750">
    <tr>
        <td>PFB 帳號：
            <input type="text" id="customerInfoId" name="customerInfoId" maxlength="20" value="${customerInfoId!}">
        </td>
    </tr>
    <tr>
        <td>PFB 版位編號：
            <input type="text" id="pId" name="pid" maxlength="20" value="${pid!}">
        </td>
	</tr>
    <tr>
        <td>版位價格＞
            <input type="text" id="pPrice" name="pprice" maxlength="8" value="${pprice!}">
        </td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()">
            &nbsp;
            <input type="button" value="清除" onclick="doClear()">
        </td>
    </tr>
</table>


<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>
	<table class="table01" width="800">
	    <tr>
	        <td class="td01" width="95">帳號</td>
	        <td class="td01" width="95">版位編號</td>
	        <td class="td01" width="80">版位名稱</td>
	        <td class="td01" width="80">版位類型</td>
	        <td class="td01" width="80">出價</td>
	        <td class="td01" width="120">更新日期</td>
	        <td class="td01" width="80">動作</td>
	    </tr>
	    <#list dataList as data>
	    <tr>
	        <td class="td03">${data.customerInfoId!}</td>
	        <td class="td03">${data.pId!}</td>
 			<td class="td03">${data.pName!}</td>
	        <td class="td03">${data.pType!}</td>
	        <td class="td03">
	        	<input type="text" id="queryDataPPrice" name="queryDataPPrice" pid="${data.pId!}" maxlength="8" style="width: 65px;" value="${data.pPrice!}">
	        </td>
	        <td class="td03" date>${data.updateDate!}</td>
	        <td class="td03"><button type="button" onclick="updatePrice('${data.customerInfoId!}', '${data.pId!}');">更新出價</button></td>
	    </tr>
	    </#list>
	</table>
<#else>
	<#if message?exists && message!="">
		${message!}
	</#if>
</#if>

</form>
