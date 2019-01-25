<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />申請開發審核</h2>

<form action="applyForBusinessCheck.html" method="post">

<table>
    <tr>
        <td>送審日期:
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly />
        	~
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly />
		</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>統一編號:
            <input type="text" id="taxId" name="taxId" value="${taxId!}" />
        </td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table>
    <tr>
        <td>
            <input type="button" value="核准" onclick="doApprove()">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="拒絕" onclick="doReject()">
        </td>
    </tr>
</table>
<@t.insertAttribute name="page" />
<table class="table01">
    <tr>
        <td class="td01" width="50">全選<input type="checkbox" onclick="selectAll_new(this, 'applyForSeqs', 'rejectReason')" /></td>
        <td class="td01">申請時間</td>
        <td class="td01">統一編號</td>
        <td class="td01">宣傳網址</td>
        <td class="td01">公司名稱</td>
        <td class="td01">退件原因</td>
    </tr>

    <#list dataList as data>
    <tr>
        <td class="td03"><input type="checkbox" name="applyForSeqs" value="${data.applyForSeq!}" onclick="controlAdUI(this, 'rejectReason_${data.applyForSeq!}')"></td>
        <td class="td02">${data.applyForTime!}</td>
        <td class="td03">${data.taxId!}</td>
        <td class="td02">${data.adUrl!}</td>
        <td class="td02">
            <#list data.illegalReasonList as data2>
                ${data2}<br>
            </#list>
        </td>
        <td class="td02">
            <input type="text" name="rejectReason" id="rejectReason_${data.applyForSeq!}" disabled>
        </td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
