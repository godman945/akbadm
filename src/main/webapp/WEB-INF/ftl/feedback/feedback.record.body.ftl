<#assign s=JspTaglibs["/struts-tags"]>

<div class="cont">
	<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />回饋金搜尋</h2>
	
	<div  class="grtba">
		
<table width="95%" class="srchtb">
    <tr>
    	<td align="right">帳戶編號 or 帳戶名稱</td>
		<td align="left">
        	<input type="text" id="accountName" name="accountName" />
		</td>
		<td align="right">贈送日期</td>
    	<td align="left">
    		<input type="text" id="sDate" name="sDate" value="${sDate}" readonly="readonly" />
    		&nbsp;&nbsp; ~ &nbsp;&nbsp;
    		<input type="text" id="eDate" name="eDate" value="${eDate}" readonly="readonly" />
    	</td>
	</tr>
    <tr>
    	<td colspan="6" align="center"></td>
	</tr>
	<tr>
		<td colspan="6" align="center">
		    <input type="button" id="search" value="查詢" />
		</td>
	</tr>
</table>
</from>
</div>

<br>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />回饋金記錄表</h2>
<div class="grtba">

<input type="button" id="add" name="add" value="新增"/>
<input type="button" id="delete" name="delete" value="刪除"/>

<div id="feedbackRecordTable"></div>

</div>
</div>

