<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />合約維護</h2>

<form action="doContractAdd.html" method="post">

<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">經銷商</td>
        <td class="td02">${pfdContract.pfdCustomerInfo.companyName} (${pfdContract.pfdCustomerInfo.customerInfoId})</td>
    </tr>
    <tr>
        <td class="td01">合約編號</td>
        <td class="td02">${pfdContract.pfdContractId}</td>
    </tr>
    <tr>
        <td class="td01">合約狀態</td>
        <td class="td02">${contractStatusMap[pfdContract.status]}</td>
    </tr>
    <tr>
        <td class="td01">簽約日期</td>
        <td class="td02">${pfdContract.contractDate?string("yyyy-MM-dd")}</td>
    </tr>
    <tr>
        <td class="td01">起始日期</td>
        <td class="td02">${pfdContract.startDate?string("yyyy-MM-dd")}</td>
    </tr>
    <tr>
        <td class="td01">結束日期</td>
        <td class="td02">${pfdContract.endDate?string("yyyy-MM-dd")}</td>
    </tr>
    <tr>
        <td class="td01">自動續約</td>
        <td class="td02"><input type="checkbox" id="continueFlag" name="continueFlag" value="y" <#if pfdContract.continueFlag?exists && pfdContract.continueFlag == "y">checked</#if>>到期自動續約 1 年</td>
    </tr>
    <tr>
        <td class="td01">付款方式</td>
        <td class="td02">
            <table>
                <#if pfdContract.pfpPayType?exists && (pfdContract.pfpPayType == "1" || pfdContract.pfpPayType == "3")>
                <tr>
                    <td valign="top">預付</td>
                    <td></td>
                </tr>
                </#if>
                <#if pfdContract.pfpPayType?exists && (pfdContract.pfpPayType == "2" || pfdContract.pfpPayType == "3")>
                <tr>
                    <td valign="top">後付：</td>
                    <td valign="top"></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <table>
                            <tr>
                                <td style="line-height:24pt;">
                                <#--可使用總額度 ${pfdContract.totalQuota!} 元<br>-->
                                付款條件 ${pfdContract.payDay!} 天<br>
                                逾期罰金 ${pfdContract.overdueFine!} %
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                </#if>
            </table>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">備註</td>
        <td class="td02">
            <textarea name="memo" cols=40 rows=5>${pfdContract.memo!}</textarea> ( 限 200 字元 )
        </td>
    </tr>
    <#if pfdContract.status == "1" || pfdContract.status == "2">
    <tr>
        <td width="100" class="td01">作廢原因</td>
        <td class="td02">
            <textarea name="closeReason" id="closeReason" cols=40 rows=5>${pfdContract.closeReason!}</textarea> ( 限 200 字元 )
        </td>
    </tr>
    </#if>
    <#if pfdContract.status == "0">
    <tr>
        <td width="100" class="td01">作廢原因</td>
        <td class="td02">${pfdContract.closeReason!}</td>
    </tr>
    <tr>
        <td width="100" class="td01">作廢時間</td>
        <td class="td02">${pfdContract.closeDate?string("yyyy-MM-dd HH:mm:ss")}</td>
    </tr>
    </#if>
</table>
<br>
<table width="600">
    <tr>
        <td align="center">
            <input type="button" value="儲存" onclick="doUpdate()">
            <#if pfdContract.status == "1" || pfdContract.status == "2">
            <input type="button" value="作廢" onclick="doClose()">
            </#if>
            <input type="button" value="返回" onclick="backToList()">
        </td>
    </tr>
</table>

<input type="hidden" name="targetContractId" value="${pfdContract.pfdContractId}" />
<input type="hidden" name="paramPfdCustomerInfoId" value="${paramPfdCustomerInfoId!}" />
<input type="hidden" name="paramContractStatus" value="${paramContractStatus!}" />
<input type="hidden" name="paramContractId" value="${paramContractId!}" />
</form>

<input type="hidden" id="messageId" value="${message!}">
