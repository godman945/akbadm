<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />管理者列表</h2>

選擇系統
<select id="system">
	<option value="">全部系統</option>
	<#list enumChannelCategory as channel>
		<option value="${channel.category!}">${channel.chName!}</option>
	</#list>
</select>


<input type="button" id="add" name="add" value="新增管理者" style="float:right;margin-right:84px"/>

<form id="formManager" method="post">
<div id="managerListTable" style="padding-top:10px;"></div>
<input type="hidden" id="managerId" name="managerId"/>
</form>