<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />未達每日花費上限報表</h2>

<form id="costShortRankForm" method="post">
<table width="600">
    <tr>
		<td align="right">查詢日期：</td>
		<td align="left">
			<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>
        	&nbsp;&nbsp; ~ &nbsp;&nbsp;
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		</td>
		<td align="right">查詢筆數：</td>
		<td align="left">
			<select id="pageSize" name="pageSize" >
            	<option value="50" selected="selected">50</option>
		    	<option value="100" >100</option>
		    	<option value="500" >500</option>
		    	<option value="1000" >1000</option>

		    </select>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="center">
            <br>
            <input type="button" id="search" value="查詢" />
            &nbsp;
            <input type="button" id="download" value="下載" />
        </td>
	</tr>
</table>

</form>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<div id="costShortRankTable"></div>


