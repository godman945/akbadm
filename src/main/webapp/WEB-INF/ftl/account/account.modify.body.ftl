<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶資料</h2>

<div  class="grtba">
<br>
<form method="post" id="accountForm" action="accountUpdate.html">
<input type="hidden" id="customerInfoId" name="customerInfoId" value="${customerInfoId!}" />
<input type="hidden" id="hidBlack" value="${black!}" />

<table width="600" class="table01">
    <tr>
        <td class="td01">帳戶編號</td>
        <td class="td02">${customerInfo.customerInfoId!}</td>
    </tr>
    <tr>
        <td class="td01">帳戶名稱 </td>
        <td class="td02">${customerInfo.customerInfoTitle!}</td>
    </tr>
    <tr>
        <td class="td01">帳戶狀態</td>
        <td class="td02">
		<select name="status" id="status">
            <#list accountStatus as statusList>
                <#if statusList.status == customerInfo.status >
                    <option value="${statusList.status!}" selected>${statusList.description!}</option>
                <#else>
				    <option value="${statusList.status!}" >${statusList.description!}</option>
			    </#if>
            </#list>
        </select>
		&nbsp;原因：	<input type="text" id="message" name="message" maxlength="20"> 
		&nbsp;<a href="#" id="accountLog" target="_blank">異動記錄</a>
        </td>
    </tr>
    <tr>
        <td class="td01">黑名單</td>
        <td class="td02">
            <input type="checkbox" id="black" name="black" value="y" onclick="controlBlackReasonUI(this)" <#if black?exists && black=="y">checked</#if> />黑名單
            &nbsp;原因：	<input type="text" id="blackReason" name="blackReason" value="${blackReason!}" maxlength="20" <#if !black?exists || black!="y">disabled</#if>>
            &nbsp;${blackTime!}
        </td>
    </tr>
    <tr>
        <td class="td01">帳戶類別 </td>
        <td class="td02">
        <#if customerInfo.category=="1">
			個人戶
		<#elseif customerInfo.category=="2">
			公司戶
		</#if>
        </td>
    </tr>
    <tr>
        <td class="td01">公司名稱</td>
        <td class="td02">${customerInfo.companyTitle!}</td>
    </tr>
    <tr>
        <td class="td01">統一編號 </td>
        <td class="td02">${customerInfo.registration!}</td>
    </tr>
    <tr>
        <td class="td01">產業類別</td>
        <td class="td02">${customerInfo.industry!}</td>
    </tr>
    <tr>
        <td class="td01">廣告連結位址 </td>
        <td class="td02">${customerInfo.urlAddress!}</td>
    </tr>
    <tr>
        <td class="td01">付款方式</td>
        <td class="td02">
        <#if customerInfo.payType == enumPfdAccountPayType[0].payType>
			${enumPfdAccountPayType[0].payName!}
		<#elseif customerInfo.payType == enumPfdAccountPayType[1].payType>
			${enumPfdAccountPayType[1].payName!}
		</#if>
        </td>
    </tr>
    <tr>
        <td class="td01">帳戶申請日</td>
        <td class="td02"><#if customerInfo.createDate??>${customerInfo.createDate?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
    </tr>
    <tr>
        <td class="td01">帳戶開通日</td>
        <td class="td02"><#if customerInfo.activateDate??>${customerInfo.activateDate?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
    </tr>
    <tr>
        <td class="td01">帳戶總儲值金額</td>
        <td class="td02"> $ ${customerInfo.totalAddMoney?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">帳戶總花費金額</td>
        <td class="td02"> $ ${customerInfo.totalSpend?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">帳戶總調整金額 </td>
        <td class="td02"> $ ${customerInfo.totalRetrieve?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">後付總額度</td> 
        <td class="td02"> $ ${customerInfo.totalLaterAddMoney?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">後付已使用金額</td>
        <td class="td02"> $ ${customerInfo.totalLaterSpend?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">帳戶可用金額</td>
        <td class="td02"> $ ${customerInfo.remain?string("#,###")!}</td>
    </tr>
    <tr>
        <td class="td01">經銷商</td>
        <td class="td02"><#if pfdUserAdAccountRef??>${pfdUserAdAccountRef.pfdCustomerInfo.companyName!}</#if></td>
    </tr>
    <tr>
        <td class="td01">負責業務</td>
        <td class="td02"><#if pfdUserAdAccountRef??>${pfdUserAdAccountRef.pfdUser.userName!}</#if></td>
    </tr>
</table>
<br>
<input type="button" id="modifyBtn" value="儲存">&nbsp;
<input type="button" value="返回" onclick="javascript:window.location='accountList.html'">
</form>

</div>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶使用者</h2>

<div  class="grtba">
<form method="post" id="userForm" action="accountUserModify.html">
<table width="50%" class="table01">
    <tr>
        <td width="20%" class="td01">使用者暱稱</td>
        <td width="10%" class="td01">狀態</td>
        <td width="10%" class="td01">權限</td>
        <td width="20%" class="td01">加入日期</td>
        <td width="20%" class="td01">上次登入時間</td>
        <td width="20%" class="td01">上次登入IP</td>
    </tr>
<#list users as list>
	<tr>
        <td width="20%" class="td02">
        <a href="#" id="userInfo" onClick="userInfo('${list.userId!}');">${list.userName!}</a>

        </td>
        <td width="10%" class="td02">
        <#if list.status == "0">
        	關閉
        <#elseif list.status == "1">
        	開啟
        <#elseif list.status == "2">
        	刪除
        <#elseif list.status == "3">
        	邀請中
        <#elseif list.status == "4">
        	邀請中
        </#if>
        </td>
        <td width="10%" class="td02">
        <#if list.privilegeId == 0>
        	總管理者
        <#elseif list.privilegeId == 1>
        	管理全帳戶
        <#elseif list.privilegeId == 2>
        	廣告、報管、帳單管理
        <#elseif list.privilegeId == 3>
        	報表、帳單管理
        <#elseif list.privilegeId == 4>
        	帳單管理
        </#if>
        </td>
        <td width="20%" class="td02"><#if list.createDate??>${list.createDate?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
        <td width="20%" class="td02"><#if list.lastLoginDate??>${list.lastLoginDate?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
        <td width="20%" class="td02">${list.lastLoginIp!}</td>
    </tr>
</#list> 
<input type="hidden" id="userId" name="userId"/> 
</table>
</form>
</div>
