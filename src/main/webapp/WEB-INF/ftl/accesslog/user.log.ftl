<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />異動記錄</h2>

<div  class="grtba">
<br>
<table width="100%" class="table01">
    <tr>
        <td width="15%" class="td01">異動時間</td>
        <td width="5%" class="td01">異動來源</td>
        <td width="55%" class="td01">原因</td>
        <td width="25%" class="td01">修改者</td>        
    </tr>
<#list accesslogs as list>
	<tr>
        <td width="15%" class="td03">${list.createDate!}</td>
        <td width="5%" class="td03">${list.channel!}</td>
        <td width="55%" class="td02">${list.message!}</td>
        <td width="25%" class="td03">${list.memberId!}</td>        
    </tr>
</#list>    
</table>

</div>