<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />帳戶名細</h2>

<div  class="grtba">
		
<table width="95%" class="srchtb">
    <tr>
    	<td align="right">使用者帳戶</td>
		<td align="left">
        	<input type="text" id="keyword" name="keyword" />
		</td>
		<td align="right">訂單日期</td>
		<td align="left">
			<div class="cal">
	        	<input type="text" id="transStartDate" name="transStartDate" value="${transStartDate!}" readonly/> 至 <input type="text" id="transEndDate" name="transEndDate" value="${transEndDate!}" readonly/>
	        </div>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="center">
		    <input type="button" id="search" value="查詢" />
		</td>
	</tr>
</table>
</from>
</div>

<br>
<div id="transTable"></div>