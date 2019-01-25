<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" xmlns="http://www.w3.org/1999/html">
    <h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle"/><strong>請款單列表</strong></h3>
</div>

<#-- 此按鈕16到20號才會顯示，執行重跑請款狀態為申請失敗的使用 -->
<#if (dateDay gte 16 && dateDay <= 20)>
<div class="form-group">
	<input class="btn btn-default" type="button" style="font-weight:bold" value="16號排程重新檢查" onclick="renewRunPaymentRequest();">
</div>
</#if>

<table class="table table-bordered table-striped ">
    <thead>
    <tr>
        <th class="text-center">請款日期</th>
        <th class="text-center">請款單號</th>
        <th class="text-center">帳戶名稱</th>
        <th class="text-center">帳戶編號</th>
        <th class="text-center">帳戶類型</th>
        <th class="text-center">統一編號/身分證字號</th>
        <th class="text-center">會員帳號</th>
        <th class="text-center">請款金額</th>
    <#--<th class="text-center">銀行</th>-->
    <#--<th class="text-center">銀行狀態</th>-->
    <#--<th class="text-center">領款人</th>-->
    <#--<th class="text-center">領款人狀態</th>-->
    </tr>
    </thead>

    <tr style="height:5px;"></tr>
    <tr style="height:5px;"></tr>

<#if pfbxApplyBonusVos??>
    <#list pfbxApplyBonusVos as vo>
        <tr>
            <td class="text-center">${vo.pfbxApplyDate?string("yyyy-MM-dd")!}</td>
            <td class="text-center">${vo.pfbxApplyBonusId!}</td>
            <td class="text-center">${vo.pfbxCustomerInfoName!}</td>
            <td class="text-center">
                <!--<a href ng-click="goDetail('${vo.pfbxCustomerInfoId!}')" >${vo.pfbxCustomerInfoId!}</a>-->
                <a href="pfbAccountUpdate.html?targetId=${vo.pfbxCustomerInfoId!}" target=_blank>${vo.pfbxCustomerInfoId!}</a>
            </td>
            <td class="text-center">${vo.pfbxCustomerCategoryName!}</td>
            <td class="text-center">${vo.pfbxCustomerTaxId!}</td>
            <td class="text-center">${vo.pfbxCustomerMemberId!}</td>
            <td class="text-right">$ ${vo.applyMoney?string("#,###,###.##")!}</td>
        <#--<td class="text-center">${vo.bankName!}</td>-->
        <#--<td class="text-center">${vo.bankCheckStatus!}</td>-->
        <#--<td class="text-center">${vo.personalName!}</td>-->
        <#--<td class="text-center">${vo.personalCheckStatus!}</td>-->
        </tr>
        <tr>
            <td colspan="8">
                <form class="form-inline" role="form">
                    <div class="form-group text-center">發票/收據 審核>>
                        <select id="doUpInvoiceSel_${vo.pfbxApplyBonusId}" voId="${vo.pfbxApplyBonusId}" class="doUpInvoiceSEL">
                            <#list enumPfbApplyInvoiceStatus as e>
                                <option value="${e.status}" <#if (e.status==vo.pfbApplyInvoiceStatus)>selected="selected"</#if> >${e.chName}</option>
                            </#list>
                        </select>
                        <input type="text" id="doUpInvoiceText_${vo.pfbxApplyBonusId}" name="doUpInvoiceText_${vo.pfbxApplyBonusId}"
                               style="width: 140px;" placeholder="失敗原因(僅失敗要填)" value="${vo.pfbApplyInvoiceNote!}"/>
                        <button class="btn btn-default doUpInvoiceBT" ng-click="doUpInvoice()" thisId="${vo.pfbxApplyBonusId}"><strong>修改</strong></button>
                    </div>
                    
                	<#--個人戶才有此項目-->
                    <#if vo.pfbxCustomerCategory == '1'>
                        <div class="form-group text-center " >請款同意確認>>
                            <select id="doUpInvoiceCheckSel_${vo.pfbxApplyBonusId}" voId="${vo.pfbxApplyBonusId}" class="doUpInvoiceCheckSEL">
                                <#list enumPfbApplyInvoiceCheckStatus as e>
                                    <option value="${e.status}"
                                            <#if vo.pfbApplyInvoiceCheckStatus?exists
                                                && (e.status == vo.pfbApplyInvoiceCheckStatus)>selected="selected"</#if> >${e.chName}</option>
                                </#list>
                            </select>
                            <button class="btn btn-default doUpInvoiceCheckBT" ng-click="doUpInvoiceCheck()" thisId="${vo.pfbxApplyBonusId}"><strong>修改</strong></button>
                        </div>
                    </#if>

                    <div class="form-group text-center ">請款狀態>>
                        <select id="doUpApplySel_${vo.pfbxApplyBonusId}" voId="${vo.pfbxApplyBonusId}" class="doUpApplySEL"
                            <#if (vo.pfbxApplyStatus == '4') || (vo.pfbxApplyStatus == '5')>
                                disabled="disabled"
                            </#if>>
                            <#list enumPfbApplyStatus as e>
                                <option value="${e.status}" <#if (e.status==vo.pfbxApplyStatus)>selected="selected"</#if> >${e.chName}</option>
                            </#list>
                        </select>
                        <input type="text" id="doUpApplyText_${vo.pfbxApplyBonusId}" name="doUpApplyText_${vo.pfbxApplyBonusId}"
                               style="width: 140px;" placeholder="失敗原因(僅失敗要填)" value="${vo.pfbxApplyNote!}"/>
                        <button class="btn btn-default doUpApplyBT" ng-click="doUpApply()" thisId="${vo.pfbxApplyBonusId}"><strong>修改</strong></button>
                    </div>
                    
                </form>
            </td>
        </tr>

        <tr style="height:5px;"></tr>
        <tr style="height:5px;"></tr>
    </#list>
</#if>

</table>
<input type="hidden" id="errorMsg" name="errorMsg" value="${errorMsg}" />