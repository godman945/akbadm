<#assign s=JspTaglibs["/struts-tags"]>

<form id="formDownloadBonusBill" method="post">

<table class="table01" width="1850" id="tableInvoice">
	<tr>
		<td class="td01" width="2%" rowspan="2"></td>
        <td class="td01" width="3%" rowspan="2">帳務月份</td>
        <td class="td01" width="7%" rowspan="2">經銷商編號</td>
        <td class="td01" width="5%" rowspan="2">經銷商名稱</td>
        <td class="td01" width="4%" rowspan="2">統一編號</td>
        <td class="td01" width="8%" rowspan="2">開立發票</td>
        <td class="td01" width="5%" rowspan="2">廣告點擊費用</td>
        <td class="td01" width="5%" rowspan="2">應付獎金</td>
        <td class="td01" width="5%" rowspan="2">禮金費用</td>
        <td class="td01" width="5%" rowspan="2">對帳單</td>
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
	<#list pfdBonusInvoices as bonusInvoice>
		<tr>
			<!--沖帳checkbox-->
			<td class="td03" >
				<#-- 後付才能做沖帳 -->					
				<#if enumPfdAccountPayType[0].payType = bonusInvoice.balanceFlag || bonusInvoice.balanceDate?? || bonusInvoice.billStatus != "4" >
					<input type="checkbox" id="chk_${bonusInvoice_index!}" value="${bonusInvoice.pfdCustomerInfoId!}_${bonusInvoice.closeYear!}_${bonusInvoice.closeMonth!}" disabled>
				<#else>
					<input type="checkbox" id="chk_${bonusInvoice_index!}" value="${bonusInvoice.pfdCustomerInfoId!}_${bonusInvoice.closeYear!}_${bonusInvoice.closeMonth!}" >
				</#if>
			</td>
			<!--帳務月份-->	
	        <td class="td03" >${bonusInvoice.closeYear!}-${bonusInvoice.closeMonth?string("00")!}</td>	        
	        <!--經銷商編號-->	
	        <td class="td03" >${bonusInvoice.pfdCustomerInfoId!}</td>
	        <!--經銷商名稱-->	
	        <td class="td03" >${bonusInvoice.companyName!}</td>
	        <!--統一編號-->	
	        <td class="td03" >${bonusInvoice.companyTaxId!}</td>
	        <!--開立發票-->
	        <td class="td04" >	 
		        <#if enumPfdAccountPayType[0].payType = bonusInvoice.payType>
		        	${enumPfdAccountPayType[0].payName!}	
		       	<#elseif enumPfdAccountPayType[1].payType = bonusInvoice.payType>	
		       		${enumPfdAccountPayType[1].payName!}     
		       	<#elseif enumPfdAccountPayType[2].payType = bonusInvoice.payType>	
		       		${enumPfdAccountPayType[2].payName!}       	
		        </#if>       
		        <input type="button" value="輸入發票" onclick="modifyBonusInvoice('${bonusInvoice.pfdCustomerInfoId!}','${bonusInvoice.closeYear!}','${bonusInvoice.closeMonth?string("00")!}')" <#if bonusInvoice.balanceDate??>disabled</#if> />
	        </td>
	        <!--廣告點擊費用-->
	        <td class="td04" >$ ${bonusInvoice.totalAdClkPrice?string('#,###')!}</td>
	        <!--應付獎金-->
	        <td class="td04" >$ ${bonusInvoice.totalBonus?string('#,###')!}</td>
	        <!--禮金費用-->
	        <td class="td04" >$ ${bonusInvoice.pfdFreeMoney?string('#,###')!}</td>
	        <!--對帳單--> 
	        <td class="td03" >
		        <#if bonusInvoice.billStatus != "4" && bonusInvoice.thisMonth == "Y" >
	        		<a href="javascript: downloadBonusBill('${bonusInvoice.pfdCustomerInfoId!}','${bonusInvoice.closeYear!}','${bonusInvoice.closeMonth!}'); javascript:void(0);">對帳單下載</a>        	
	        	<#elseif bonusInvoice.billStatus == "4" && bonusInvoice.thisMonth == "Y" >
	        		對帳單下載
	        	<#elseif bonusInvoice.download == "N" >
	        		尚未到請款時間
	        	<#else>
	        		已過請款時間
	        	</#if>
	        </td>	

	        <!--應收廣告費用-->        
	        <td class="td03" >${bonusInvoice.pfdInvoiceSno!}</td>	<!--發票號碼-->        
	        <td class="td03" ><#if bonusInvoice.pfdInvoiceDate??>${bonusInvoice.pfdInvoiceDate?string("yyyy-MM-dd")!}</#if></td>	<!--發票日期-->
			<td class="td04" ><#if bonusInvoice.pfdInvoiceMoney??>$ ${bonusInvoice.pfdInvoiceMoney?string('#,###')}</#if></td>	<!--發票金額(含稅)-->
			<td class="td03" >	<!--經銷商(廣告商)付款方式 1支票 2現金 3電匯-->
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
			<td class="td03" >${bonusInvoice.pfdCheckSno!}</td>	<!--支票號碼-->
			<td class="td03" ><#if bonusInvoice.pfdCheckCloseDate??>${bonusInvoice.pfdCheckCloseDate?string("yyyy-MM-dd")!}</#if></td>	<!--支票到期日期-->		
			
			
			<!--應付獎金-->
	        <td class="td03" >${bonusInvoice.financeInvoiceSno!}</td>	<!--發票號碼-->
	        <td class="td03" >	<!--發票日期-->
		        <#if bonusInvoice.financeInvoiceDate?? >
		        	${bonusInvoice.financeInvoiceDate?string("yyyy-MM-dd")!}
		        </#if>
	        </td>
	        <td class="td04" ><#if bonusInvoice.financeInvoiceMoney??>$ ${bonusInvoice.financeInvoiceMoney?string('#,###')}</#if></td>	<!--發票金額(含稅)-->
	        <td class="td03" >	<!--付款日期-->
		        <#if bonusInvoice.financePayDate?? >
		        	${bonusInvoice.financePayDate?string("yyyy-MM-dd")!}
		        </#if>	        
	        </td>	
	        
	        
			<!--折讓金額(含稅)-->  
			<td class="td04" ><#if bonusInvoice.debitMoney??>$ ${bonusInvoice.debitMoney?string('#,###')!}</#if></td>
			<!--折讓日期-->  
			<td class="td03" ><#if bonusInvoice.debitDate??>${bonusInvoice.debitDate?string("yyyy-MM-dd")!}</#if></td>		
			<!--沖帳日期-->  
			<td class="td03" ><#if bonusInvoice.balanceDate??>${bonusInvoice.balanceDate?string("yyyy-MM-dd")!}</#if></td>		
					
	    </tr>	    
	</#list>	
</table>
<input type="hidden" id="pfdId" name="pfdId" /> 
<input type="hidden" id="year" name="year" /> 
<input type="hidden" id="month" name="month" /> 
<input type="hidden" id="payType" name="payType" /> 

</form>
