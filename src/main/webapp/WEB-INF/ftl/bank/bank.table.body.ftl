<#assign s=JspTaglibs["/struts-tags"]>
		<#if banks??>			
			<#list banks as bank>				
				<tr>
		            <td class="text-center">${bank.id!}</td>
		            <td class="text-center">${bank.bankName!}</td>      		            
		            <td class="text-center">${bank.bankCode!}</td>		            
		            <td class="text-center">${bank.parentBank!}</td>		            
	          	</tr>	          	
	        </#list>	        
	    </#if>	        