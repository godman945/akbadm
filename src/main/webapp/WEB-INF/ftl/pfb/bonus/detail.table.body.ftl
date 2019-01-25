<#assign s=JspTaglibs["/struts-tags"]>
		<#-- <div ng-controller="selectNobuttonsCtrl" ></div> -->
<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>客戶列表</strong></h3>
	
<div class="container-fluid">
	<table id="tableView" class="table table-striped table-bordered tablesorter2">
		<thead>
			<tr>
				<th class="text-center">流水號</th>
				<th class="text-center">帳戶編號</th>
				<th class="text-center">帳戶名稱</th>
				<th class="text-center">帳戶類型</th>
				<th class="text-center">統一編號</th>
				<th class="text-center">會員帳號</th>
				<th class="text-center">分潤狀態</th>
				<th class="text-center">已付金額</th>
				<th class="text-center">請款金額</th>
				<th class="text-center">待結金額</th>
				<!-- <th class="text-center">總金額</th> -->
			</tr>
        </thead>
        <tbody>
		<#if pfbxBonusVo??>
			<#list pfbxBonusVo as vo>				
				<tr>
		            <td class="text-center">${(vo_index+1)!}</td>
		            <td class="text-center">
		            	<a href ng-click="doOpen('${vo.pfbxCustomerInfoId!}')" >${vo.pfbxCustomerInfoId!}</a>
		            </td>
		            <td class="text-center">${vo.pfbxCustomerInfoName!}</td>
		            <td class="text-center">${vo.pfbxCustomerCategoryName!}</td>
		            <td class="text-center">${vo.pfbxCustomerTaxId!}</td>
		            <td class="text-center">${vo.pfbxCustomerMemberId!}</td>
		            <td class="text-center">${vo.statusName!}</td>	            	            
		            <td class="text-right">$ ${vo.totalPayBonus?string("#,###,###.##")!}</td>
		            <td class="text-right">$ ${vo.applyBonus?string("#,###,###.##")!}</td>
		            <td class="text-right">$ ${vo.waitBonus?string("#,###,###.##")!}</td>
		            <!-- <td class="text-right">$ ${vo.totalBonus?string("#,###,###.##")!}</td>-->
	          	</tr>
	        </#list>
	        </tbody>
	        <tr>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td class="text-center" style="background-color:#99FFFF;">筆數：${totalVO.totalSize?string("#,###,###.##")!}</td>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td style="background-color:#99FFFF;"></td>
	        	 <td class="text-right" style="background-color:#99FFFF;">$ ${totalVO.totalPayBonus?string("#,###,###.##")!}</td>
	             <td class="text-right" style="background-color:#99FFFF;">$ ${totalVO.applyBonus?string("#,###,###.##")!}</td>
	             <td class="text-right" style="background-color:#99FFFF;">$ ${totalVO.waitBonus?string("#,###,###.##")!}</td>
	        </tr>
	    </#if>
	</table>
</div>
<script type="text/javascript">
$(document).ready(function(){
	tableSorter();
});
</script>      