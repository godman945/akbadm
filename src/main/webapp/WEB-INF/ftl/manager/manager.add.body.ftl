<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增管理帳戶</h2>

<form  method="post" action="saveManager.html">
<table id="tableManager" width="95%" class="table01" >
    <tr>
        <td width="15%" class="td06">管理系統</td>
        <td class="td02">		
    	<select id="system" name="system">
		<#list enumChannelCategory as channel>
			<option value="${channel.category!}">${channel.chName!}</option>
		</#list>	
		</select>
		</td>
    </tr>
    <tr>
        <td width="15%" class="td06">管理員名稱</td>
        <td class="td02"><input type="text" id="name" name="name" size="30"></td>
    </tr>
    <tr>
        <td width="15%" class="td06">管理員PChome 帳號 <br>(總管理小天使建立請洽RD)</td>
        <td class="td02">
        <input type="text" id="memberId" name="memberId" size="30">
        <span id="showMsg" name="showMsg" style="color:red;font-size: 180%"></span>
        </td>
    </tr>
    <tr>
        <td width="15%" class="td06">權限</td>
        <td class="td02">
        <select name="privilege">
		<#list enumManagerPrivilege[1..3] as manager>
			<option value="${manager.privilege!}">${manager.chName!}</option>
		</#list>	
		</select>
		</td>
    </tr>
    <tr>
        <td width="15%" class="td06">使用狀態</td>
        <td class="td02">
        <select name="status">
		<#list enumManagerStatus[0..1] as manager>
			<option value="${manager.status!}">${manager.chName!}</option>
		</#list>	
		</select>
		</td>
    </tr>
</table>
<br>
<table width="95%">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="新增">
            <input type="button" id="viewBtn" value="返回">
        </td>
    </tr>
</table>
</form>