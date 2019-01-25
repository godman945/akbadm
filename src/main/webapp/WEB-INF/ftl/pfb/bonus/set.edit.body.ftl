<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" ng-controller="editCtrl">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>分潤設定</strong></h3>
	
	<div class="container-fluid">
		<form class="form-horizontal" role="form" action="updatePfbBonusSet.html">
		
			<div class="form-group">
				<div class="container col-lg-6"">
					<table class="table table-striped table-bordered">
				        <tr>
							<td class="text-center col-lg-1"><strong>帳戶編號</strong></td>
							<td class="text-left col-lg-4">
								<input type="text" name="pfbId" value="${pfbxBonusVo.pfbxCustomerInfoId!}" readonly >								
							</td>
						</tr>
				        <tr>
							<td class="text-center col-lg-1"><strong>帳戶類型</strong></td>
							<td class="text-left col-lg-4">${pfbxBonusVo.pfbxCustomerCategoryName!}</td>
						</tr>
						<tr>
							<td class="text-center col-lg-1"><strong>統一編號/身份證字號</strong></td>
							<td class="text-left col-lg-4">${pfbxBonusVo.pfbxCustomerTaxId!}</td>
						</tr>
						<tr>
							<td class="text-center col-lg-1"><strong>會員帳號</strong></td>
							<td class="text-left col-lg-4">${pfbxBonusVo.pfbxCustomerMemberId!}</td>
						</tr>
						<tr>
							<td class="text-center col-lg-1"><strong>分潤狀態</strong></td>
							<td class="text-left col-lg-4">								
						  		<select name="status">						  			
						      		<#list enumBonusSet as bonusSet>
						      			<#if pfbxBonusVo.statusId == bonusSet.status>
						      				<option value="${bonusSet.status}" selected>${bonusSet.chName}</option>
						      			<#else>
						      				<option value="${bonusSet.status}">${bonusSet.chName}</option>
						      			</#if>						  				
						  			</#list>
						        </select>
							</td>
						</tr>
						<tr>
							<td class="text-center col-lg-1"><strong>客戶分潤 %</strong></td>
							<td class="text-left col-lg-4">
								<input type="text" name="percent" value="${pfbxBonusVo.pfbxCustomerBonusPercent!}" length="3">
							</td>
						</tr>
				    </table>
				</div>				
			</div>
			
			<div class="container-fluid">        
		  		<label class="control-label col-lg-3">
		    		<button class="btn btn-default" ng-click="submit()"><strong>儲存</strong></button>
		    		&nbsp;
		    		<input type="button" class="btn btn-default" style="font-weight:bold;" value="返回" onClick="backpage()" />
		  		</label>
			</div>
			
		</form>		
	</div>	
	<hr />
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>優惠列表</strong></h3>
	<div class="container-fluid">  
		<a href="pfbxBonusSetSpecial.html?pfbId=${pfbxBonusVo.pfbxCustomerInfoId!}" >  
    	<button class="btn btn-default"><strong>新增優惠方案</strong></button>
    	</a>
	</div>
	<br />
	<div class="container-fluid">
		<table class="table table-striped table-bordered">
	    	<thead>
				<tr>
					<th class="text-center">活動名稱</th>
					<th class="text-center">開始日期</th>
					<th class="text-center">結束日期</th>
					<th class="text-center">客戶分潤 %</th>
					<th class="text-center">編輯</th>
					<th class="text-center">刪除</th>
				</tr>
	    	</thead>
	    	<tbody>
				<#if pfbxBonusSetSpecialList??> 
					<#list pfbxBonusSetSpecialList as vo>
						<tr>
							<td class="text-center">${vo.specialName!}</td>
							<td class="text-center">${vo.startDate!}</td>
							<td class="text-center">${vo.endDate!}</td>
							<td class="text-center">${vo.pfbPercent?string("0.00")!} %</td>
							<td class="text-center">
								<input type="button" class="btn btn-default" style="font-weight:bold;" value="修改" onClick="updSpecial('${vo.id!}')" />
							</td>
							<td class="text-center">
								<input type="button" class="btn btn-default" style="font-weight:bold;" value="刪除" onClick="delSpecial('${vo.id!}')" />
							</td>
						</tr>
					</#list>
				</#if>
			</tbody>	
		</table>
	</div>
</div>

