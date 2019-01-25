<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="cont">
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />網站類別設定
	</h2>
	<div class="grtba borderbox">
		<div style="clear:both;background:#e4e3e0">
		    <form method="post" id="searchForm" name="searchForm">
				<table width="90%" height=60px; >
					<tr>
						<td width="10%" align="right" style="font-size:20px;font-weight:bold;font-family:標楷體;">上一層：</td>
						<td id="nameText" width="80%" align="left" style="font-size:20px;font-weight:bold;font-family:標楷體;">${parentName!}</td>
						<td width="10%" align="left"><input type="button" id="add" name="add" value="新增類別" onclick="addWebsiteCategory()" ></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="tableList"></div>

	</div>

	</div>
</div> 
  