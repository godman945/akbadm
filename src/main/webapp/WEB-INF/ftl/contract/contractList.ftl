<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<style type="text/css"> 
#pp1 a:link { 
	color: red; 
	font-size:20px;
	font-family:DFKai-sb;
} 
</style> 
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />經銷商合約管理</h2>

<form action="applyForBusinessCheck.html" method="post">

<table>
    <tr>
        <td>合約編號:
            <input type="text" name="paramContractId" id="paramContractId" value="${paramContractId!}" size="14" maxlength="14" />
   	    </td>
   	    <td>&nbsp;&nbsp;</td>
        <td>經銷商:
            <select name="paramPfdCustomerInfoId">
                <option value="">全部</option>
		    	<#list pfdCustomerInfoMap?keys as skey>
		    	    <option value="${skey}" <#if paramPfdCustomerInfoId?exists && paramPfdCustomerInfoId = skey>selected="selected" </#if>>${pfdCustomerInfoMap[skey]}</option>
		    	</#list>
		    </select>
		</td>
		<td>&nbsp;&nbsp;</td>
        <td>合約狀態:
            <select name="paramContractStatus">
                <option value="">全部</option>
		    	<#list contractStatusMap?keys as skey>
		    	    <option value="${skey}" <#if paramContractStatus?exists && paramContractStatus = skey>selected="selected" </#if>>${contractStatusMap[skey]}</option>
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

<table>
    <tr>
        <td>
            <input type="button" value="新增" onclick="add()">
        </td>
         <td>
        	<div id="pp1">
        		<div style="display:inline-flex;font-size:20px;color: red;font-family:DFKai-sb;">注意! 新增合約請記得作</div><a href="/adm/bonusSetList.html">佣金設定</a>
        	</div>
        </td>
    </tr>
</table>

<#if dataList?exists && (dataList?size>0)>


<@t.insertAttribute name="page" />
<table class="table01">
    <tr>
        <td class="td01">NO.</td>
        <td class="td01">合約編號</td>
        <td class="td01">經銷商</td>
        <td class="td01">簽約日期</td>
        <td class="td01">合約狀態</td>
        <td class="td01">合約走期</td>
        <td class="td01">建立時間</td>
        <td class="td01">更新時間</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td02"><a href="javascript:update('${data.pfdContractId}');">${data.pfdContractId}</a></td>
        <td class="td02">${data.pfdCustomerInfo.companyName}(${data.pfdCustomerInfo.customerInfoId})</td>
        <td class="td03">${data.contractDate?string("yyyy-MM-dd")}</td>
        <td class="td02">${contractStatusMap[data.status]}</td>
        <td class="td03">${data.startDate?string("yyyy-MM-dd")} ~ ${data.endDate?string("yyyy-MM-dd")}</td>
        <td class="td03">${data.createDate?string("yyyy-MM-dd")}</td>
        <td class="td03">${data.updateDate?string("yyyy-MM-dd")}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
