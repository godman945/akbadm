<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶使用者資料</h2>

<div  class="grtba">
<br>
<form method="post" id="userForm" action="accountUserUpdate.html">
<table width="50%" class="table01">
    <tr>
        <td width="30%" class="td01">姓名</td>
        <td width="70%" class="td02">${memberMap.user_name!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">姓別</td>
        <td width="70%" class="td02">
        <#if memberMap.sexuality=="M">
        	男
        <#else>
        	女
        </#if>
        </td>
    </tr>
    <tr>
        <td width="30%" class="td01">生日</td>
        <td width="70%" class="td02">${memberMap.birthday!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">聯絡電話</td>
        <td width="70%" class="td02">${memberMap.phone!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">手機號碼</td>
        <td width="70%" class="td02">${memberMap.cellphone!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">聯絡地址</td>
        <td width="70%" class="td02">${memberMap.address!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">電子信箱</td>
        <td width="70%" class="td02">${memberMap.email!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">權限</td>
        <td width="70%" class="td02">
        <#if user.privilegeId == 0>
        	帳戶負責人
        <#elseif user.privilegeId == 1>
        	帳戶管理者
        <#elseif user.privilegeId == 2>
        	廣告管理者
        <#elseif user.privilegeId == 3>
        	報表管理者
        <#elseif user.privilegeId == 4>
        	帳單管理者
        </#if>
        </td>
    </tr>
    <tr>
        <td width="30%" class="td01">加入日期</td>
        <td width="70%" class="td02">${user.createDate!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">上次登入時間</td>
        <td width="70%" class="td02">${user.lastLoginDate!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">上次登入IP</td>
        <td width="70%" class="td02">${user.lastLoginIp!}</td>
    </tr>
    <tr>
        <td width="30%" class="td01">會員狀態</td>
        <td width="70%" class="td02">
        <select name="status" id="status">
        <#list userStatus as userStatus>        
        	<#if userStatus_index < 4>
				<#if userStatus.status == user.status >
					<option value="${userStatus.status!}" selected >${userStatus.chName!}</option>
				<#else>
					<option value="${userStatus.status!}" >${userStatus.chName!}</option>
				</#if>
			</#if>
		</#list>
		</select>
		<input type="text" id="message" name="message" maxlength="20"> 
		<input type="button" id="modifyBtn" value="修改"> 
		<a href="#" id="userLog" target="_blank">異動記錄</a>
        </td>
    </tr>
    
</table>
<input type="hidden" id="userId" name="userId" value="${user.userId!}" />
</form>
</div>