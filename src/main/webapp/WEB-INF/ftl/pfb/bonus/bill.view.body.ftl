<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid">
		
	<div class="row-fluid">
		<table class="table table-bordered">
			<caption>
				<h4><strong>請款結帳單</strong></h4>
				<h4><strong>請款月份:${strYear!}/${strMonth!} ~ ${endYear!}/${endMonth!}</strong></h4>
			</caption>            
        	<tr>
                <td class="text-left">帳戶編號:${pfbxBonusVo.pfbxCustomerInfoId!}</td>   
                <td class="text-left">付款人:網路家庭國際資訊股份有限公司</td>          
            </tr>
            <tr>
                <td class="text-left">
                	收款人:
                	<#if pfbxBonusVo.pfbxCustomerInfoName??>
						${pfbxBonusVo.pfbxCustomerInfoContactName!}
					<#else>
						${pfbxBonusVo.pfbxCustomerInfoName!}
					</#if>
				</td>  
                <td class="text-left">付款日期:${payDate!}</td>            
            </tr>
            <tr>
                <td class="text-left">帳戶類型:${pfbxBonusVo.pfbxCustomerCategoryName!}</td>  
                <td class="text-left">付款類型:匯款</td>           
            </tr>
		</table>            
	</div>
	
	<div class="row-fluid">
		<table class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th class="text-center">月份</th>
                    <th class="text-center">項目說明</th>
                    <th class="text-center">請款金額 </th>                    
                </tr>
            </thead>
            <tbody>
            <#if pfbxBonusBillVos??>
	            <#list pfbxBonusBillVos as vo>
	                <tr>
	                    <td class="text-center">${vo.transDate!}</td>
	                    <td class="text-center">${vo.transDesc!}</td>    
	                    <td class="text-right">$ ${vo.transMoney?string("#,###")!}</td>
	                </tr>
	           </#list>
	        </#if>
            </tbody>
        </table>
	</div>
</div>

