<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />合約建立</h2>

<form action="doContractAdd.html" method="post">

<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">經銷商 <font color="red">*</font></td>
        <td class="td02">
            <select name="pfdCustomerInfoId" id="pfdCustomerInfoId">
                <option value="">-- 請選擇 --</option>
		    	<#list pfdCustomerInfoMap?keys as skey>
		    	    <option value="${skey}" <#if paramPfdCustomerInfoId?exists && paramPfdCustomerInfoId = skey>selected="selected" </#if>>${pfdCustomerInfoMap[skey]}</option>
		    	</#list>
		    </select>
        </td>
    </tr>
    <tr>
        <td class="td01">合約編號 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="contractId" id="contractId" value="${contractId!}" size="14" maxlength="14" /> ( 限 14 字元 )</td>
    </tr>
    <tr>
        <td class="td01">簽約日期 <font color="red">*</font></td>
        <td class="td02"><input type="text" id="contractDate" name="contractDate" value="${contractDate!}"  readonly/></td>
    </tr>
    <tr>
        <td class="td01">起始日期 <font color="red">*</font></td>
        <td class="td02"><input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/></td>
    </tr>
    <tr>
        <td class="td01">結束日期 <font color="red">*</font></td>
        <td class="td02"><input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/></td>
    </tr>
    <tr>
        <td class="td01">自動續約</td>
        <td class="td02"><input type="checkbox" id="continueFlag" name="continueFlag" value="y" <#if pfdContract.continueFlag?exists && pfdContract.continueFlag == "y">checked</#if>>到期自動續約 1 年</td>
    </tr>
    <tr>
        <td class="td01">付款方式 <font color="red">*</font></td>
        <td class="td02">
            <table>
                <tr>
                    <td valign="top"><input type="checkbox" id="prePay" name="prePay" value="true" <#if prePay?exists && prePay == "true">checked</#if>>預付</td>
                    <td></td>
                </tr>
                <tr>
                    <td valign="top"><input type="checkbox" id="payAfter" name="payAfter" value="true" onclick="controlAdUI(this, 'totalQuota', 'payDay', 'overdueFine')" <#if payAfter?exists && payAfter == "true">checked</#if>>後付</td>
                    <td valign="top"></td>
                </tr>
                <tr id="payAfterData" style="display: none;">
                    <td></td>
                    <td>
                        <table>
                            <tr>
                                <td style="line-height:24pt;"><#--可使用總額度 <input type="text" id="totalQuota" name="totalQuota" value="${totalQuota!}" size="10" maxlength="10" /> 元 ( 整數 )<br>-->
                                付款條件 <input type="text" id="payDay" name="payDay" value="${payDay!}" size="2" maxlength="2" /> 天 ( 整數 )<br>
                                逾期罰金 <input type="text" id="overdueFine" name="overdueFine" value="${overdueFine!}" size="5" maxlength="5" /> % ( 最多小數2位 ex. 12.30 )
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>


            </table>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">備註</td>
        <td class="td02">
            <textarea name="memo" cols=40 rows=5>${memo!}</textarea> ( 限 200 字元 )
        </td>
    </tr>
</table>
<br>
<table width="600">
    <tr>
        <td align="center">
            <input type="button" value="確定" onclick="doAdd()">
            <input type="button" value="返回" onclick="backToList()">
        </td>
    </tr>
</table>

<input type="hidden" name="paramPfdCustomerInfoId" value="${paramPfdCustomerInfoId!}" />
<input type="hidden" name="paramContractStatus" value="${paramContractStatus!}" />
<input type="hidden" name="paramContractId" value="${paramContractId!}" />
</form>

<input type="hidden" id="messageId" value="${message!}">
