<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />品牌對應關鍵字管理</h2>

<form id="queryFrom" action="admBrandCorrespond.html" method="post">
	
	<table width="100%">
	    <tr>
	        <td>關鍵字查詢:
	        	<input type="text" id="queryString" name="queryString" value="${queryString!}" />
	            <input type="button" value="查詢" onclick="query();"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="checkbox" name="checkboxButton" value="all">中英文均有字詞
	            <input type="checkbox" name="checkboxButton" value="eng">只查英文關鍵字無NA
	            <input type="checkbox" name="checkboxButton" value="ch">只查中文關鍵字無NA
			</td>
		</tr>
	</table>
	<hr>

	<span class="pages">
		<input type="button" id="addBtn" value="新增" onclick="addView();">
		<input type="button" id="downloadBtn" value="下載" onclick="download();">
		
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		共 ${totalCount!} 筆
		<input type="hidden" id="page" name="page" value="${page!}" />
		<input type="hidden" id="totalPage" name="totalPage" value="${totalPage!}" />
		
		<img style="vertical-align:middle" id="fpage" src="<@s.url value="/html/img/"/>page_first.gif" />
		<img style="vertical-align:middle" id="ppage" src="<@s.url value="/html/img/"/>page_pre.gif" />
		${page}/${totalPage}
		<img style="vertical-align:middle" id="npage" src="<@s.url value="/html/img/"/>page_next.gif" />
		<img style="vertical-align:middle" id="epage" src="<@s.url value="/html/img/"/>page_end.gif" />

		&nbsp&nbsp

		顯示
		<select id="pageSizeSelect" name="pageSize" style="vertical-align:middle" >
		    <#list pageSizeList as ps>
		        <option>${ps}</option>
		    </#list>
		</select>
		行
	</span>
	<br>

	<#if dataList?exists && (dataList?size > 0)>
		<table class="table01" width="95%">
			<tr>
				<td class="td01" width="50">編號</td>
				<td class="td01" width="50">英文關鍵字</td>
				<td class="td01" width="50">中文關鍵字</td>
				<td class="td01" width="50">功能</td>
				<td class="td01" width="50">修改日期</td>
				<td class="td01" width="50">新增日期</td>
			</tr>
		
			<#list dataList as tableDatalist>
			<tr>
				<td class="td02">${tableDatalist.id!}</td>
				<td class="td02">${tableDatalist.brand_eng!}</td>
				<td class="td02">${tableDatalist.brand_ch!}</td>
				<td class="td03">
					<input type="button" value="修改" onclick="modifyView('${tableDatalist.id!}');">
					<input type="button" value="刪除" onclick="deleteBrandCorrespondValue('${tableDatalist.id!}');">
				</td>
				<td class="td03">${tableDatalist.update_date!}</td>
				<td class="td03">${tableDatalist.create_date!}</td>
			</tr>
			</#list>
		
		</table>

	<#else>
		查無資料。
	</#if>

</form>

<#-- 顯示訊息用 -->
<div id="dialog" class="dialog"></div>

<form id="excerptFrom" name="excerptFrom" action="admBrandCorrespondDownload.html" method="post">
	<input type="hidden" id="contentPath" name="contentPath" value="<@s.url value="/html/img/"/>">
	<input type="hidden" id="formPage" name="formPage" value="${page}">	
	<input type="hidden" id="fpageSize" name="fpageSize" value="${pageSize}">
	<input type="hidden" id="ftotalPage" name="ftotalPage" value="${totalPage}">
	<input type="hidden" id="hiddenQueryString" name="hiddenQueryString" value="${queryString}">
	<input type="hidden" id="hiddenCheckboxVal" name="hiddenCheckboxVal" value="${checkboxButton}">
</form>