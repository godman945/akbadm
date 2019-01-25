<#assign s=JspTaglibs["/struts-tags"]>

<center><div class="titel_t">ARW 廣告權重調整</div></center>
<br>
<table width="100%">
    <tr>
        <td class="td05">新增ARW 廣告權重</td>
    </tr>
</table>

<br>

<form action="arwManagementSave.html" method="post">
	<table width="500" class="table01">
		<tr>
		 	<td width="100" class="td01">經銷商帳號</td>
		 	<td class="td02">
		 		<select id="pfdCustomerSelect" name="pfdCustomerSelect">
		 		<option value="" >請選擇</option>
		 		<#list pfdCustomerList as list>
					<option value="${list.pfdCustomerInfoId!}" >${list.pfdCustomerInfoTitle!}</option>
				</#list>
				</select>
		 	</td>
		</tr>
		<tr>
			<td width="100" class="td01">PFP 帳號 / 名稱</td>
			<td class="td02">
				<select id="customerInfoId" name="customerInfoId">
				<option value="" >請選擇</option>
				</select>
				
				<input type="hidden" id="customerInfoTitle" name="customerInfoTitle">
			</td>
		</tr>
		<tr>
			<td width="100" class="td01">ＡＲＷ值（2~20）</td>
			<td class="td02">
				<input type="number" id="arwValue" name="arwValue" min="2" max="20" value="2">
			</td>
		</tr>
		<tr>
			<td width="100" class="td01">走期狀態</td>
			<td class="td02">
				<select id="dateFlag" name="dateFlag">
					<#list dateFlagMap?keys as key> <#-- 改由後端(EnumDateFlag.java)取得，以利維護的一致性 -->
                        <option value="${key}" <#if "0" == key>selected</#if>>${dateFlagMap[key]}</option>
                    </#list>
				</select>
			</td>
		</tr>
		<tr>
			<td width="100" class="td01">開始日</td>
			<td class="td02">
				<input type="text" id="startDate" name="startDate" readonly>
			</td>
		</tr>
		<tr>
			<td width="100" class="td01">到期日</td>
			<td class="td02">
				<input type="text" id="endDate" name="endDate" readonly>
			</td>
		</tr>
	</table>
	<br>
	<table width="500">
	    <tr>
	        <td align="center">
	            <input type="submit" id="saveBtn" value="儲存">
	            <input type="button" value="返回" onclick="javascript:window.location='arwManagement.html'">
	        </td>
	    </tr>
	</table>
</form>