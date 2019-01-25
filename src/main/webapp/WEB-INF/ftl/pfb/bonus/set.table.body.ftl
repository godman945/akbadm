<#assign s=JspTaglibs["/struts-tags"]>
		<#-- <div ng-controller="selectNobuttonsCtrl" ></div> -->
		<#if pfbxBonusVo??>			
			<#list pfbxBonusVo as vo>				
				<tr>
		            <td class="text-center">${(vo_index+1)!}</td>
		            <td class="text-center">
		            	<a href ng-click="doEdit('${vo.pfbxCustomerInfoId!}')">${vo.pfbxCustomerInfoId!}</a>
		            	<#-- <button ng-click="doEdit()">Error Dialog</button> -->
		            </td>
		            <td class="text-center">${vo.pfbxCustomerCategoryName!}</td>
		            <td class="text-center">${vo.pfbxCustomerTaxId!}</td>
		            <td class="text-center">${vo.pfbxCustomerMemberId!}</td>
		            <td class="text-center">${vo.statusName!}</td>	            	
		            <#-- 
		            <td class="text-center">
		            	<a href="#" editable-select="user.status" buttons="no" e-ng-options="s.value as s.text for s in statuses">
    						{{ showStatus() }}
  						</a>
  					</td>
  					-->		            
		            <td class="text-center">${vo.pfbxCustomerBonusPercent?string("#.##")!} %</td>
		            <td class="text-center">${vo.pfbxCustomerBonusPchomeChargePercent?string("#.##")!} %</td>
		            <td class="text-center">${vo.pfbxCustomerBonusTotalPercent?string("#.##")!} %</td>
	          	</tr>	          	
	        </#list>
	        
	    </#if>	        