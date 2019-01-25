<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告定義維護</h2>
<form action="defineAdQuery.html" method="post">
<table>
    <tr>
        <td>廣告來源序號:
            <input type="text" name="queryAdPoolSeq" value="${queryAdPoolSeq!}" />
            &nbsp;&nbsp;廣告定義序號:
        	<input type="text" name="queryDefineAdSeq" value="${queryDefineAdSeq!}" />
            &nbsp;&nbsp;廣告定義名稱:
            <input type="text" name="queryDefineAdName" value="${queryDefineAdName!}" />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="查詢" />
		</td>
	</tr>
</table>
</form>
<br>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<@t.insertAttribute name="list" />
<input type="hidden" id="messageId" value="${message!}" />