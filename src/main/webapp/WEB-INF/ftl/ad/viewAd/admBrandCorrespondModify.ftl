<#assign s=JspTaglibs["/struts-tags"]>

<center><div class="titel_t">品牌對應關鍵字管理</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">修改品牌對應關鍵字</td>
    </tr>
</table>

<br>

<table width="500" class="table01">
	<#list dataList as list>
	<input type="hidden" id="dataId" name="dataId" value="${list.id!}" />
	<tr>
	 	<td width="100" class="td01">英文關鍵字</td>
	 	<td class="td02">
	 		<input type="text" id="brandEng" name="brandEng" maxlength="20" value="${list.brand_eng!}"/>
	 	</td>
	</tr>
	<tr>
		<td width="100" class="td01">中文關鍵字</td>
		<td class="td02">
			<input type="text" id="brandCh" name="brandCh" maxlength="20" value="${list.brand_ch!}"/>
		</td>
	</tr>
	</#list>
</table>
<br>
<table width="500">
    <tr>
        <td align="center">
            <input type="button" id="updateBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='admBrandCorrespond.html'">
        </td>
    </tr>
</table>