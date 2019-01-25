<#assign s=JspTaglibs["/struts-tags"]>

<form id="formBonusInvoice" action="updateBonusInvoice.html" method="post">
<table width="500" class="table01" >
    <tr>
        <td width="100" class="td03">帳務月份</td>
        <td class="td02">${pfdBonusInvoice.closeYear!}-${pfdBonusInvoice.closeMonth?string("00")}</td>
    </tr>
    <tr>
        <td width="100" class="td03">經銷商</td>        
        <td class="td02">
        	${pfdBonusInvoice.pfdContract.pfdCustomerInfo.customerInfoId!} / ${pfdBonusInvoice.pfdContract.pfdCustomerInfo.companyName!}        	
        </td>
    </tr>
    <tr>
        <td width="100" class="td03">帳戶付款方式</td>
        <td class="td02">
	        <#if enumPfdAccountPayType[0].payType = pfdBonusInvoice.pfdContract.pfpPayType>
	        	${enumPfdAccountPayType[0].payName!}	
			<#elseif enumPfdAccountPayType[1].payType = pfdBonusInvoice.pfdContract.pfpPayType>	
		       	${enumPfdAccountPayType[1].payName!}      
	       	<#elseif enumPfdAccountPayType[2].payType = pfdBonusInvoice.pfdContract.pfpPayType>	
		       	${enumPfdAccountPayType[2].payName!}   	
	        </#if>
		</td>
    </tr>
    
   														 <!--   應收廣告費用內容       -->   
    <tr>    
    	<td class="td01" width="20%" colspan="2">應收廣告費用</td>
    </tr>
    <tr>
        <td width="100" class="td03">應收廣告費用</td>
        <td class="td02">${totalAdClkPrice?string('#,###')!}</td>
    </tr>
    <tr>
        <td width="100" class="td03">發票號碼 </td>
        <td class="td02">
        <input type="text" id="pfdInvoiceSno" name="pfdInvoiceSno" value="${pfdBonusInvoice.pfdInvoiceSno!}" size="30">
        </td>
    </tr>
    <tr>
        <td width="100" class="td03">發票日期</td>
        <td class="td02">
        <input type="text" id="pfdInvoiceDate" name="pfdInvoiceDate" value="<#if pfdBonusInvoice.pfdInvoiceDate??>${pfdBonusInvoice.pfdInvoiceDate?string("yyyy-MM-dd")!}</#if>" size="30" readonly>
        </td>
    </tr>    
    <tr>
        <td width="100" class="td03">發票金額(含稅)</td>
        <td class="td02">
	        ${pfdBonusInvoice.pfdInvoiceMoney!}
        </td>
    </tr>
    <tr>
        <td width="100" class="td03">付款方式</td>
        <td class="td02">
        	<#if pfdBonusInvoice.pfdPayCategory?? && pfdBonusInvoice.pfdPayCategory = '1'>
        		<input type="radio" id="pfdRemit" name="pfdPayCategory" value="3" />電匯
        		<input type="radio" id="pfdCheck" name="pfdPayCategory" value="1" checked />支票
        	<#else>
        		<input type="radio" id="pfdRemit" name="pfdPayCategory" value="3" checked />電匯
        		<input type="radio" id="pfdCheck" name="pfdPayCategory" value="1" />支票
        	</#if>    
        </td>
    </tr>
	<tr class="pfdCheckTr" style="display:none;" >
        <td width="100" class="td03">支票號碼</td>
        <td class="td02">
        <input type="text" id="pfdCheckSno" name="pfdCheckSno" value="${pfdBonusInvoice.pfdCheckSno!}" size="30">
        </td>
    </tr>
    <tr class="pfdCheckTr" style="display:none;" >
        <td width="100" class="td03">支票到期日期</td>
        <td class="td02">
        <input type="text" id="pfdCheckCloseDate" name="pfdCheckCloseDate" value="<#if pfdBonusInvoice.pfdCheckCloseDate??>${pfdBonusInvoice.pfdCheckCloseDate?string("yyyy-MM-dd")!}</#if>" size="30">
        </td>
    </tr>
	
    
	
															<!--   應付獎金內容    -->
    <tr>    
    	<td class="td01" width="20%" colspan="2">應付獎金</td>
    </tr>    	
    <tr>
        <td width="100" class="td03">應付獎金</td>
        <td class="td02">${totalBonus?string('#,###')!}</td>
    </tr>
    <tr>
        <td width="100" class="td03">付款憑證</td>
        <td class="td02">
        	<#if pfdBonusInvoice.bonusType?? && pfdBonusInvoice.bonusType = '2'>
        		<input type="radio" id="pfdFinance" name="bonusType" value="1" />發票
        		<input type="radio" id="pfdDebit" name="bonusType" value="2" checked />折讓單
        	<#else>
        		<input type="radio" id="pfdFinance" name="bonusType" value="1" checked />發票
        		<input type="radio" id="pfdDebit" name="bonusType" value="2" />折讓單
        	</#if>    
        </td>
    </tr>
    <tr class="pfdFinanceTr">
        <td width="100" class="td03">發票號碼 </td>
        <td class="td02">
            <input type="text" id="financeInvoiceSno" name="financeInvoiceSno" value="${pfdBonusInvoice.financeInvoiceSno!}" size="30">
        </td>
    </tr>
    <tr class="pfdFinanceTr">
        <td width="100" class="td03">發票日期</td>
        <td class="td02">
        <input type="text" id="financeInvoiceDate" name="financeInvoiceDate" value="<#if pfdBonusInvoice.financeInvoiceDate??>${pfdBonusInvoice.financeInvoiceDate?string("yyyy-MM-dd")!}</#if>" size="30" readonly>
        </td>
    </tr>    
    <tr class="pfdFinanceTr">
        <td width="100" class="td03">發票金額(含稅)</td>
        <td class="td02">
       		${pfdBonusInvoice.financeInvoiceMoney!}
        </td>
    </tr>
    <tr class="pfdFinanceTr">
        <td width="100" class="td03">付款日期</td>
        <td class="td02"><input type="text" id="financePayDate" name="financePayDate" value="<#if pfdBonusInvoice.financePayDate??>${pfdBonusInvoice.financePayDate?string("yyyy-MM-dd")!}</#if>" size="30" readonly></td>
    </tr>
    <tr class="pfdDebitTr" style="display:none;">
        <td width="100" class="td03">折讓金額(含稅)</td>
        <td class="td02">
        <input type="text" id="debitMoney" name="debitMoney" value="${pfdBonusInvoice.debitMoney!}" size="30">
        </td>
    </tr>
    <tr class="pfdDebitTr" style="display:none;">
        <td width="100" class="td03">折讓日期</td>
        <td class="td02"><input type="text" id="debitDate" name="debitDate" value="<#if pfdBonusInvoice.debitDate??>${pfdBonusInvoice.debitDate?string("yyyy-MM-dd")!}</#if>" size="30" readonly></td>
    </tr>
    
									    <!--  帳款進度內容    -->
    <tr>    
    	<td class="td01" width="20%" colspan="2">帳款進度</td>
    </tr>
    <tr>
    	<td width="100" class="td03" rowspan="2">付款狀態</td>
    	<td class="td02">
    		<select id="billStatus" name="billStatus">
        	<#list pfdBnousBillMap?keys as skey>
        		<#if pfdBonusInvoice.billStatus?? && skey = pfdBonusInvoice.billStatus>
        			<option value="${skey}" selected>${pfdBnousBillMap[skey]}</option>
        		<#else>
        			<option value="${skey}">${pfdBnousBillMap[skey]}</option>
        		</#if>
        	</#list>
        </select>
    	</td>
    </tr>
    
    	<td class="td02">
    		<input type="text" id="billNote" name="billNote" value="${pfdBonusInvoice.billNote!}" size="30" placeholder="請填入付款失敗原因..." >
    	</td>
    <tr>
    </tr>
</table>
<br>

<input type="hidden" id="bonusInvoiceId" name="bonusInvoiceId" value="${pfdBonusInvoice.id!}" />
<input type="hidden" id="pfdCustomerInfoId" class="pfdCustomerInfoId"  name="pfdCustomerInfoId" value="${pfdBonusInvoice.pfdContract.pfdCustomerInfo.customerInfoId!}"/>
<input type="hidden" id="closeYear" class="closeYear"  name="closeYear" value="${pfdBonusInvoice.closeYear?string("00")}"/>
<input type="hidden" id="closeMonth" class="closeMonth"  name="closeMonth" value="${pfdBonusInvoice.closeMonth?string("00")}"/>
<input type="hidden" id="pfdInvoiceMoney" name="pfdInvoiceMoney" value="${pfdBonusInvoice.pfdInvoiceMoney!}"/>
<input type="hidden" id="financeInvoiceMoney" name="financeInvoiceMoney" value="${pfdBonusInvoice.financeInvoiceMoney!}"/>

<table width="500">
    <tr>
        <td align="center">
            <input type="button" id="update" value="儲存">
            <input type="button" id="cancel" value="取消">
        </td>
    </tr>
</table>
</form>
