<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<span class="pages"><@t.insertAttribute name="page" /></span>

<table class="table01" width="800">

    <tr>
        <td class="td01" width="20">黑</td>
        <td class="td01" width="150">帳戶編號</td>
        <td class="td01" width="200">經銷商</td>
        <td class="td01" width="150">會員帳號</td>
        <td class="td01" width="100">帳戶名稱</td>
        <td class="td01" width="50">帳戶類別</td>
        <td class="td01" width="100">產業類別</td>
        <td class="td01" width="50">狀態</td>
        <td class="td01" width="80">帳戶申請日</td>
        <td class="td01" width="80">帳戶開通日</td>
        <td class="td01" width="50">帳戶餘額</td>
        <td class="td01" width="50">剩餘天數</td>
    </tr>
<#if customerInfos?exists>
	
	<#list customerInfos as custInfo>
		<tr>
		    <td class="td03">
		    	<#if custInfo.black?exists>
		    		<#if custInfo.black=="y">●</#if>
				</#if>
		    </td>
	        <td class="td03">${custInfo.customerInfoId!}</td>
	        <td class="td03"><#if custInfo.pfdUserAdAccountRefs?exists>${custInfo.pfdUserAdAccountRefs[0].pfdCustomerInfo.companyName!}</#if></td>
	        <td class="td03">${custInfo.memberId!}</td>
	        <td class="td02"><a href="#" id="accountInfo" onClick="accountInfo('${custInfo.customerInfoId!}')">${custInfo.customerInfoTitle!}</a></td>
	        <td class="td03"">
			<#if custInfo.category=="1">
				個人戶
			<#elseif custInfo.category=="2">
				公司戶
			</#if>
			</td>
	        <td class="td03">${custInfo.industry!}</td>
	        <td class="td03">
			<#if custInfo.status=="0">
				關閉
			<#elseif custInfo.status=="1">
				開啟
			<#elseif custInfo.status=="2">
				停權
			<#elseif custInfo.status=="4">
				申請中			
			</#if>
			</td>
	        <td class="td03">${custInfo.createDate?string("yyyy/MM/dd")}</td>
	        <td class="td03"><#if custInfo.activateDate?exists>${custInfo.activateDate?string("yyyy/MM/dd")}</#if></td>
	        <td class="td04">${custInfo.remain!}</td>
	        <td class="td04">${custInfo.remainDays!}</td>
    	</tr>
	</#list>
</#if>

</table>