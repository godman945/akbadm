<#assign s=JspTaglibs["/struts-tags"]>

<table id="tableView" class="table01" width="950">
    <tr>
    	<td class="td01">勾選</td>
        <td class="td01">贈送日期</td>
        <td class="td01">使用期限</td>
        <td class="td01">帳戶編號</td>
        <td class="td01">帳戶名稱</td>
        <td class="td01">帳戶負責人</td>
        <td class="td01">贈送理由</td>
        <td class="td01">贈送方式</td>
        <td class="td01">贈送金額</td>
        <td class="td01">修改日期</td>
        <td class="td01">修改者</td>
    </tr>
<#if feedbackRecords?exists && (feedbackRecords?size > 0)>
	<#list feedbackRecords as record>
    <tr>
        <td class="td03"><input type="checkbox" id="chk_${record_index!}" name="chk_${record_index!}" value="${record.feedbackRecordId!}" /></td>
        <td class="td03">${record.giftDate?string("yyyy-MM-dd")!}</td>
        <td class="td03"><#if record.inviledDate??>${record.inviledDate?string("yyyy-MM-dd")!}</#if></td>
        <td class="td03">${record.pfpCustomerInfo.customerInfoId!}</td>
        <td class="td03">${record.pfpCustomerInfo.customerInfoTitle!}</td>
        <td class="td03">${record.pfpCustomerInfo.memberId!}</td>
        <td class="td03">${record.reason!}</td>
        <td class="td03">
        <#if enumSelCalculateCategory[0].category == record.calculateCategory>
       		${enumSelCalculateCategory[0].chName!}
        <#else>
        	${enumSelCalculateCategory[1].chName!}
        </#if>
        </td>
        <td class="td03">${record.feedbackMoney!}</td>
        <td class="td03">${record.updateDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
        <td class="td03">${record.editor!}</td>
    </tr>
	</#list>
</#if>
</table>

