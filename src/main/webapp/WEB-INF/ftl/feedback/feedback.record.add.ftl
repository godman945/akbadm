<#assign s=JspTaglibs["/struts-tags"]>

<center><div class="titel_t">發放回饋金</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">贈送回饋金</td>
    </tr>
</table>

<br>

<form action="feedbackRecordCreate.html" method="post">
<table width="500" class="table01">
    <tr>
        <td width="100" class="td01">發放選項</td>
        <td class="td02">
        <#if selAccountCategory == enumSelAccountCategory[0].category >
        	<input type="radio" id="only" name="selAccountCategory" value="${enumSelAccountCategory[0].category!}" checked >${enumSelAccountCategory[0].chName!}
    		<input type="radio" id="all" name="selAccountCategory" value="${enumSelAccountCategory[1].category!}" >${enumSelAccountCategory[1].chName!}	
        <#elseif selAccountCategory == enumSelAccountCategory[1].category >
    		<input type="radio" id="only" name="selAccountCategory" value="${enumSelAccountCategory[0].category!}" >${enumSelAccountCategory[0].chName!}
    		<input type="radio" id="all" name="selAccountCategory" value="${enumSelAccountCategory[1].category!}" checked>${enumSelAccountCategory[1].chName!}
		</#if>
		</td>
    </tr>
    <tr id="pfdIdTr" style="display:none;">
    	<td width="100" class="td01">經銷商</td>
    	<td class="td02">
    		<table class="table01" >	
    		<#if pfdCustomerInfoList?exists && (pfdCustomerInfoList?size > 0)>
				<#list pfdCustomerInfoList as data>
					<#if (data_index%2) = 0 >
						<tr>
							<td style="width:50%" class="td02">
								<#if data.customerInfoId == 'PFDC20140520001'>
									<input type="checkbox" name="pfdAccount" value="${data.customerInfoId}" checked > ${data.companyName}
								<#else>
									<input type="checkbox" name="pfdAccount" value="${data.customerInfoId}" > ${data.companyName}
								</#if>
							</td>
					<#else>
							<td style="width:50%" class="td02">
								<#if data.customerInfoId == 'PFDC20140520001'>
									<input type="checkbox" name="pfdAccount" value="${data.customerInfoId}" checked > ${data.companyName}
								<#else>
									<input type="checkbox" name="pfdAccount" value="${data.customerInfoId}" > ${data.companyName}
								</#if>
							</td>
						</tr>
					</#if>
				</#list>
			</#if>
			</table>
    	</td>
    </tr>
    <tr>
        <td width="100" class="td01">入帳方式</td>
        <td class="td02">	
        <#if selCalculateCategory == enumSelCalculateCategory[0].category >
        	<input type="radio" id="quartzs" name="selCalculateCategory" value="${enumSelCalculateCategory[0].category!}" checked>${enumSelCalculateCategory[0].chName!}	
    		<input type="radio" id="now" name="selCalculateCategory" value="${enumSelCalculateCategory[1].category!}" >${enumSelCalculateCategory[1].chName!}(只能選擇今天日期)
    	<#elseif selCalculateCategory == enumSelCalculateCategory[1].category >
    		<input type="radio" id="quartzs" name="selCalculateCategory" value="${enumSelCalculateCategory[0].category!}" >${enumSelCalculateCategory[0].chName!}	
    		<input type="radio" id="now" name="selCalculateCategory" value="${enumSelCalculateCategory[1].category!}" checked>${enumSelCalculateCategory[1].chName!}(只能選擇今天日期)
    	</#if>
		</td>
    </tr>
    <tr>
        <td width="100" class="td01">會員中心帳號</td>
        <td class="td02">
        	<input type="text" id="memberId" name="memberId" value="${memberId!}" ><br/>
        	<span id="errorMessage" name="errorMessage" style="color:red;size:5">${errorMessage!}</span>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">贈送金額</td>
        <td class="td02"><input type="text" id="feedbackMoney" name="feedbackMoney" value="${feedbackMoney!}" ></td>
    </tr>
    <tr>
        <td width="100" class="td01">贈送日期</td>
        <td class="td02"><input type="text" id="giftDate" name="giftDate" value="${giftDate!}" readonly></td>
    </tr>
    <tr>
        <td width="100" class="td01">使用期限</td>
        <td class="td02"><input type="text" id="inviledDate" name="inviledDate" value="${inviledDate!}" readonly></td>
    </tr>
    <tr>
        <td width="100" class="td01">理由</td>
        <td class="td02">
            <input type="text" id="reason" name="reason" size="50" value="${reason!}" >
        </td>
    </tr>    
    <tr>
        <td width="100" class="td01">建立者</td>
        <td class="td02"><input type="text" name="author" value="${Session.session_user_id}" size="50" readonly></td>
    </tr>
</table>
<br>
<table width="500">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="新增">
            <input type="button" id="viewBtn" value="返回">
        </td>
    </tr>
</table>
</form>

