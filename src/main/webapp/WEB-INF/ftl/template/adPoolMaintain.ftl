<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/javascript">
function addAdPool() {
    window.location = "adPoolAdd.html";
}
</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告來源維護</h2>
<form action="adPoolQuery.html" method="post">
<table width="100%">
    <tr>
        <td>AdPool序號:
        	<input type="text" id="queryAdPoolSeq" name="queryAdPoolSeq" value="${queryAdPoolSeq!}" />        
            &nbsp;&nbsp;AdPool名稱:
            <input type="text" id="queryAdPoolName" name="queryAdPoolName" value="${queryAdPoolName!}" />
            &nbsp;&nbsp;區分廠商:
            <select id="queryDiffComapny" name="queryDiffComapny">
            	<option value="">--請選擇是否區分廠商--</option>
            	<option value="Y">Y</option>
            	<option value="N">N</option>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="查詢" />
		</td>
	</tr>
</table>
</form>
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addAdPool()"></td>
    </tr>
</table>
<#if adPoolList?exists>
<table class="table01">
    <tr>
        <td class="td01" width="50">NO.</td>
        <td class="td01" width="150">資料來源序號</td>
        <td class="td01" width="200">資料來源名稱</td>
        <td class="td01" width="100">區分廠商</td>
        <#--<td class="td01" width="100">廣告定義數量</td>-->
        <td class="td01" width="200">操作</td>
    </tr>
    <#assign index=0>
    <#list adPoolList as adPool>
    <#assign index = index+1>
    <tr>
        <td class="td03">${index!}</td>
        <td class="td02">${adPool.adPoolSeq!}</td>
        <td class="td03">${adPool.adPoolName!}</td>
        <td class="td03">${adPool.diffCompany!}</td>
        <#--<td class="td03"><a href="defineAdQuery.html?queryDefineAdSeq=${adPool.adPoolSeq!}">${adPool.dadCount!}</a></td>-->
        <td class="td03">
        <#if (adPool.admTemplateAds?size == 0) && (adPool.admRelateTadDads?size == 0)>
            <input type="button" id="updateBtn" value="修改" onclick="window.location.href='adPoolUpdate.html?paramAdPoolSeq=${adPool.adPoolSeq!}';">
            <input type="button" id="deleteBtn" value="刪除" onclick="window.location.href='doAdPoolDelete.html?paramAdPoolSeq=${adPool.adPoolSeq!}';">
        </#if>
        </td>
    </tr>
    </#list>
</table>
<#else>
查無此資料!!!
</#if>

<input type="hidden" id="messageId" value="${message!}">
