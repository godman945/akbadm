<#assign s=JspTaglibs["/struts-tags"]>

<input type="button" id="download" name="downloadBtn" value="下載報表" onclick="downloadReport()"/>

<form id="formDownloadBalanceInvoice" method="post">

<table class="table01" width="1850" id="tableInvoice">
	<tr>
        <td class="td01" width="3%" rowspan="2">帳務月份</td>
        <td class="td01" width="7%" rowspan="2">經銷商編號</td>
        <td class="td01" width="5%" rowspan="2">經銷商名稱</td>
        <td class="td01" width="4%" rowspan="2">統一編號</td>
        <td class="td01" width="6%" rowspan="2">付款狀態</td>
        <td class="td01" width="5%" rowspan="2">廣告點擊費用</td>
        <td class="td01" width="5%" rowspan="2">應付獎金</td>
        <td class="td01" width="23%" colspan="6">應收廣告費用</td>  
        <td class="td01" width="20%" colspan="4">應付獎金</td>
        <td class="td01" width="5%" rowspan="2">折讓金額(含稅)</td>
        <td class="td01" width="5%" rowspan="2">折讓日期</td>  
        <td class="td01" width="5%" rowspan="2">沖帳日期</td>       
    </tr>        
    <tr>    	
    	<td class="td01" >發票號碼</td>
        <td class="td01" >發票日期</td>
        <td class="td01" >發票金額(含稅)</td>
        <td class="td01" >付款方式</td>
       	<td class="td01" >支票號碼</td>
    	<td class="td01" >支票到期日期</td>
        <td class="td01" >發票號碼</td>
        <td class="td01" >發票日期</td>
    	<td class="td01" >發票金額(含稅)</td>
        <td class="td01" >付款日期</td>        
    </tr>    	
	<#list pfdBonusInvoicesVO as bonusInvoice>
		<tr>			
	        <td class="td03" >${bonusInvoice.closeYear!}-${bonusInvoice.closeMonth?string("00")!}</td>	        
	        <td class="td03" >${bonusInvoice.pfdCustomerInfoId!}</td>
	        <td class="td03" >${bonusInvoice.companyName!}</td>
	        <td class="td03" >${bonusInvoice.companyTaxId!}</td>
	        <td class="td03" >
	        	<#if enumPfdAccountPayType[0].payType = bonusInvoice.payType>
		        	${enumPfdAccountPayType[0].payName!}	
		       	<#elseif enumPfdAccountPayType[1].payType = bonusInvoice.payType>	
		       		${enumPfdAccountPayType[1].payName!}     
		       	<#elseif enumPfdAccountPayType[2].payType = bonusInvoice.payType>	
		       		${enumPfdAccountPayType[2].payName!}       	
		        </#if>   
	        </td>	        
	        <td class="td04" >$ ${bonusInvoice.totalAdClkPrice?string('#,###')!}</td>
	        <td class="td04" >$ ${bonusInvoice.totalBonus?string('#,###')!}</td>	
	        

	                						    <!--應收廣告費用-->
	        <td class="td03" >${bonusInvoice.pfdInvoiceSno!}</td>
	        <td class="td03" ><#if bonusInvoice.pfdInvoiceDate??>${bonusInvoice.pfdInvoiceDate?string("yyyy-MM-dd")!}</#if></td>
			<td class="td03" ><#if bonusInvoice.pfdInvoiceMoney??>$ ${bonusInvoice.pfdInvoiceMoney?string('#,###')}</#if></td>
			<td class="td03" >
			<#if bonusInvoice.pfdPayCategory??>
				<#if enumInvoicPayType[0].type = bonusInvoice.pfdPayCategory>
					${enumInvoicPayType[0].chName!}
				<#elseif enumInvoicPayType[1].type = bonusInvoice.pfdPayCategory>
					${enumInvoicPayType[1].chName!}
				<#elseif enumInvoicPayType[2].type = bonusInvoice.pfdPayCategory>
					${enumInvoicPayType[2].chName!}
				</#if>
			</#if>
			</td>
			<td class="td03" >${bonusInvoice.pfdCheckSno!}</td>
			<td class="td03" ><#if bonusInvoice.pfdCheckCloseDate??>${bonusInvoice.pfdCheckCloseDate?string("yyyy-MM-dd")!}</#if></td>			
			
			
			        							<!--應付獎金-->
	        <td class="td03" >${bonusInvoice.financeInvoiceSno!}</td>
	        <td class="td03" >
		        <#if bonusInvoice.financeInvoiceDate?? >
		        	${bonusInvoice.financeInvoiceDate?string("yyyy-MM-dd")!}
		        </#if>
	        </td>
	        <td class="td03" ><#if bonusInvoice.financeInvoiceMoney??>$ ${bonusInvoice.financeInvoiceMoney?string('#,###')}</#if></td>
	        <td class="td03" >
		        <#if bonusInvoice.financePayDate?? >
		        	${bonusInvoice.financePayDate?string("yyyy-MM-dd")!}
		        </#if>	        
	        </td>
			
			
			<!--折讓金額-->
			<td class="td03" ><#if bonusInvoice.debitMoney??>$ ${bonusInvoice.debitMoney?string('#,###')!}</#if></td>
			<!--折讓日期-->	
			<td class="td03" ><#if bonusInvoice.debitDate??>${bonusInvoice.debitDate?string("yyyy-MM-dd")!}</#if></td>		
			<!--沖帳日期-->
			<td class="td03" ><#if bonusInvoice.balanceDate??>${bonusInvoice.balanceDate?string("yyyy-MM-dd")!}</#if></td>				
	    </tr>	    
	</#list>	
</table>
<input type="hidden" id="pfdCustomerInfoId" name="pfdCustomerInfoId" value="${pfdCustomerInfoId!}"/> 
<input type="hidden" id="invoiceStrDate" name="invoiceStrDate" value="${invoiceStrDate!}"/> 
<input type="hidden" id="invoiceEndDate" name="invoiceEndDate" value="${invoiceEndDate!}"/> 
</form>
