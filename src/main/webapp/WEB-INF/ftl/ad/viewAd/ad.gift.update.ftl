<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2>
	<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />修改廣告禮金活動
	<#if updateFlag == "n">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="color:red;font-size:16px;">禮金序號已被使用，無法修改</span>
	</#if>
</h2>	
<form method="post" id="updateFreeActionForm" action="updateFreeAction.html" >
<table class="table01" width="650px" style="font-size:14px;" cellpadding="5">
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
			<input type="text" id="giftCondition" name="giftCondition" value="${giftCondition!}" maxlength="10" <#if payment == 'N'>readonly</#if>>
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>贈送金額</b></td>
		<td class="td02">
			<input type="text" id="giftMoney" name="giftMoney" value="${giftMoney!}" maxlength="10" >
		</td>
	</tr>
	<tr>
		<td width="150px" height="25px" class="td01" ><b>禮金失效日期</b></td>
		<td class="td02">
			<input type="text" id="inviledDate" name="inviledDate" value="${inviledDate!}"  readonly/>
		</td>
	</tr>
</table>
<br/>
<table width="600px" >
	<tr>
		<td style="text-align: center">
			<#if updateFlag == "y">
			<input type="button" value="更新" onclick="updateFreeAction()" >
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</#if>
			<input type="button" id="returnBtn" name="returnBtn" value="返回" onclick="returnView()" >
		</td>
	</tr>
</table>
<input type="hidden" id="actionId" name="actionId" value="${actionId!}" />
<input type="hidden" id="updateFlag" name="updateFlag" value="${updateFlag!}" />
</form>
