<#assign s=JspTaglibs["/struts-tags"]>

<table id="tableView" class="table01" width="95%">
    <tr>
    	<td class="td01">系統名稱</td>
        <td class="td01">管理者信箱</td>
        <td class="td01">管理者名稱</td>
        <td class="td01">狀態</td>
        <td class="td01">權限</td>
        <td class="td01">上次異動時間</td>
        <td class="td01">新增時間</td>
    </tr>
    
    <#if vos??>
    	
    	<#list vos as vo>
    	
    	<tr>
	        <td class="td03">${vo.system!}</td>
	        <td class="td03"><#if vo.privilegeId != "0"><a href="" onclick="javascript: modifyManager('${vo.id!}'); return false;">${vo.memberId!}</a><#else>${vo.memberId!}</#if></td>
	        <td class="td03">${vo.chName!}</td>
	        <td class="td03">${vo.status!}</td>
	        <td class="td03">${vo.privilegeName!}</td>
	        <td class="td03">${vo.updateDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
	        <td class="td03">${vo.createDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
    	</tr>
    	
    	</#list>	
    </#if>
</table>

