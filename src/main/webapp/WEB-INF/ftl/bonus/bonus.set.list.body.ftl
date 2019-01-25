<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />佣金設定</h2>

<table width="95%">   
</table>
<br>
<form id="formBonusSet" method="post">
<table class="table01" width="1000">
    <tr>
        <td class="td01" width="50">編號</td>
        <td class="td01" width="150">經銷商</td>
        <td class="td01" width="150">合約效期</td>
        <td class="td01" width="80">合約狀態</td>
        <td class="td01" width="150">建立日期</td>
        <td class="td01" width="150">修改日期</td>
        <td class="td01" width="80">編輯</td>
        <td class="td01" width="80" style="visibility:hidden">編輯</td>
    </tr>
    <#list pfdContracts as contract>
    <tr>
        <td class="td03">${(contract_index+1)!}</td>
        <td class="td02">${contract.pfdCustomerInfo.companyName!}</td>
        <td class="td03">${contract.startDate?string("yyyy-MM-dd")!} ~ ${contract.endDate?string("yyyy-MM-dd")!}</td>
        <td class="td03">
        <#list enumContractStatus as status>
        	<#if status.statusId = contract.status>
        		${status.statusName!}
        	</#if>
        </#list>

        </td>
        <td class="td03">${contract.createDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
        <td class="td03">${contract.updateDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
        <td class="td03"><input type="button" value="固定獎金" onclick="fixedBonusSet('${contract.pfdContractId!}')"/></td>
        <td class="td03" style="visibility:hidden" ><input type="button" value="專案獎金" onclick="caseBonusSet('${contract.pfdContractId!}')"/></td>
        
    </tr>
    </#list> 
</table>
<input type="hidden" id="pfdContractId" name="pfdContractId" />
</form>