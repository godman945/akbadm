<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>


<div style="clear:both;height:50%"></div>
			
<form method="post" id="tableForm" name="tableForm" >
<table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
	<tr>
		<th>編號</th>
		<th>名稱</th>
		<th>階層</th>
		<th>編輯</th>
		<th>下一層</th>
	</tr>	
</thead>
	<tbody>
		<#if dataList?exists>
		    <#list dataList as vo>
				<tr>
					<td class="td01">${vo.code!}</td>
					<td class="td01">${vo.name!}</td>
					<td class="td01">${vo.level!}</td>
					<td class="td01"><a href="javascript:updateWebsiteCategory('${vo.id!}','${vo.name!}');">修改</a></td>
					<td class="td01">
						<#if vo.level != 4>
					        <a href="javascript:findTableView('${vo.id!}','${vo.name!}');">NEXT</a>
						</#if>	
					</td>
				</tr>
		    </#list>
		<#else>
		<tr>
			<td colspan="4">
				沒有符合搜尋條件的內容。
			</td>
		</tr>
		</#if>
	</tbody>
</table>
</form>

<div style="display:none;" id="websiteCategoryDiv">
	<table width="500" class="table01" style="clear:both;background:#e4e3e0">
		<tr style="height:35px;">
			<th id="websiteCategoryDivTitle" style="font-size:20px;font-weight:bold;font-family:標楷體;">新增類別</th>
		</tr>
		<tr style="height:100px;">
			<td class="td03">
				<div style="height:20px;"></div>
				<div style="font-size:16px;">類別名稱：<input type="text" id="categoryName" name="categoryName" /></div>
				<div style="height:20px;"></div>
				<center>
					<input id="saveButton" onclick="saveBtn();" class="popbtn" type="button" value="確定">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input onclick="closeBtn();" class="popbtn" type="button" value="取消">
				</center>
			</td>
		</tr>
	</table>
</div>
	
<input type="hidden" id="parentId" name="parentId" value="${parentId!}" />
<input type="hidden" id="parentName" name="parentName" value="${parentName!}" />