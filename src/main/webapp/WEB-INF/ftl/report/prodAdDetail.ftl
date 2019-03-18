<#assign s=JspTaglibs["/struts-tags"]>

<form id="" method="post">

	<#if startDate !="" && endDate !="">
	 	日期：${startDate!} ~ ${endDate!} <br> 
	<#else>
		<#if startDate !="" >
			日期：${startDate!} <br>
		<#else>
			日期：${endDate!} <br>
		</#if>
	</#if>
	
	廣告客戶名稱：${pfpCustomerName!}<br>
	商品廣告名稱：${prodAdName!}<br>
	<br>


	<#if prodAdVoList?exists && (prodAdVoList?size > 0)>
		<table class="table01" width="100%" id="">
			<tr>
		        <td class="td01" width="25%">商品明細</td>
		        <td class="td01" width="25%">陳列次數</td>
		        <td class="td01" width="25%">商品點選數</td>
		        <td class="td01" width="25%">商品點選率</td>
		    </tr>    
		    
		    <#list prodAdVoList as voList>
			    <tr>
				    <td class="td03">
					   	<img src="${voList.prodImg!}" width="15%" align="left">
					    <span style="text-align: center; vertical-align:middle;">${voList.prodName!}</span>
				    </td>	 
			    	<td class="td03" width="20%">${voList.pv!}</td>
			    	<td class="td03" width="20%">${voList.clk!}</td>
			    	<td class="td03" width="20%">${voList.clkRate!}</td>
			    </tr>  
		    </#list>  
		        
		        
		   	<tr>
		        <td class="td01" width="25%">總計：${sumProdAdVo.rowTotal!}</td>
		        <td class="td01" width="25%">${sumProdAdVo.pv!}</td>
		        <td class="td01" width="25%">${sumProdAdVo.clk!}</td>
		        <td class="td01" width="25%">${sumProdAdVo.clkRate!}</td>
		    </tr>
			
		</table>
	<#else>
		<#if message?exists && message != "">
			${message!}
		</#if>
	</#if>
	
<!-- <input type="hidden" id="pfdCustomerInfoId" name="pfdCustomerInfoId" value="${pfdCustomerInfoId!}"/> 
<input type="hidden" id="invoiceStrDate" name="invoiceStrDate" value="${invoiceStrDate!}"/> 
<input type="hidden" id="invoiceEndDate" name="invoiceEndDate" value="${invoiceEndDate!}"/> --> 
</form>
