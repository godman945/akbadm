<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2>
	<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增廣告禮金活動
</h2>	
<form method="post" id="addFreeActionForm" action="addFreeAction.html" >
<table class="table01" width="600px" style="font-size:14px;" cellpadding="5">
	<tr>
		<td width="150px" height="25px" class="td01" ><b>禮金動作</b></td>
		<td class="td02">
			<select id="payment" name="payment">
            	<#list searchPayment as payType>
            		<option value="${payType.status!}" <#if payType.status == payment>selected</#if>>${payType.chName!}</option>
                </#list>
            </select>
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>禮金序號輸入頁</b></td>
		<td class="td02">
			<select id="giftStyle" name="giftStyle">
            	<#list searchGiftStyle as gifType>
            		<option value="${gifType.status!}" <#if gifType.status == giftStyle>selected</#if>>${gifType.chName!}</option>
                </#list>
            </select>
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>活動日期</b></td>
		<td class="td02">
			<input type="text" id="actionStartDate" name="actionStartDate" value="${actionStartDate!}"  readonly/>
			&nbsp;&nbsp;~&nbsp;&nbsp;
			<input type="text" id="actionEndDate" name="actionEndDate" value="${actionEndDate!}"  readonly/>
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>活動名稱</b></td>
		<td class="td02">
			<input type="text" id="actionName" name="actionName" value="${actionName!}" maxlength="20" >
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>說明</b></td>
		<td class="td02">
			<input type="text" id="note" name="note" value="${note!}" style="width:420px;" maxlength="200" >
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>儲值金額</b></td>
		<td class="td02">
			<input type="text" id="giftCondition" name="giftCondition" value="500" maxlength="10" >
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>贈送金額</b></td>
		<td class="td02">
			<input type="text" id="giftMoney" name="giftMoney" value="500" maxlength="10" >
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>禮金失效日期</b></td>
		<td class="td02">
			<input type="text" id="inviledDate" name="inviledDate" value="${inviledDate!}"  readonly/>
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>禮金序號設定</b></td>
		<td class="td02">
			<div style="float:left;height:45px;">
			<input type="radio" id="shared" name="shared" value="Y" checked />相同序號<br/>
			<input type="radio" id="shared" name="shared" value="N" />不同序號
			</div>
			<div style="float:right;height:45px;line-height:40px;">
			序號開頭前兩碼&nbsp;
			<input type="text" id="giftSnoHead" name="giftSnoHead" value="${giftSnoHead!}" style="width:30px" maxlength="2" >
			&nbsp;&nbsp;&nbsp;&nbsp;
			新增序號組數&nbsp;
			<input type="text" id="snoCount" name="snoCount" value="${snoCount!}" style="width:50px" maxlength="4" readonly >
			&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</td>
	</tr>
</table>
<br/>
<table width="600px" >
	<tr>
		<td style="text-align: center">
			<input type="button" value="新增" onclick="addFreeAction()" >
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="返回" onclick="returnView()" >
		</td>
	</tr>
</table>

</form>
