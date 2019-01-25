<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="container-fluid" ng-controller="searchCtrl">    
	
	<div class="container-fluid">
		<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>請款資料核對</strong></h3>	
	</div>	
	
	<form name="saveForm" class="form-horizontal" action="updateCheckStatus.html">
	<div class="container-fluid">
		<table class="table table-bordered">
	        <thead>
				<tr>
					<th class="text-center">核對項目</th>
					<th class="text-center">核對內容</th>
					<th class="text-center">影像檔</th>
					<th class="text-center">核對結果</th>	
					<th class="text-center">結果說明</th>									
				</tr>
	        </thead>

	        <tbody>
	        	<tr>
					<td class="text-center">銀行資料</td>
					<td class="text-left">
						${pfbxApplyBonusCheckVo.bankName!} (${pfbxApplyBonusCheckVo.bankCode!}) 
						<br>
						${pfbxApplyBonusCheckVo.bankBranchName!} (${pfbxApplyBonusCheckVo.bankBranchCode!})
						<br>
						${pfbxApplyBonusCheckVo.bankAccountName!} 
						<br>
						${pfbxApplyBonusCheckVo.bankAccountNumber!}
					</td>
					<td class="text-center">影像檔</td>
					<td class="text-center">						 		
	      				<select class="form-control" name="bankCheckStatus" ng-model="bankCheckStatus">
							<#list enumPfbApplyCheck[1..3] as check>
								<#if check.status == pfbxApplyBonusCheckVo.bankCheckStatus>
									<option value="${check.status!}" selected>${check.chName!}</option>
								<#else>
									<option value="${check.status!}" >${check.chName!}</option>
								</#if>
							</#list>
						</select>						
					</td>	
					<td class="text-center">
						<input type="text" class="form-control" name="bankCheckNote" ng-model="bankCheckNote" value="${pfbxApplyBonusCheckVo.bankCheckNote!}"/>
					</td>										
				</tr>
				<#-- pfb 帳戶是個人，需要附上個人資料 -->
				<#if pfbxApplyBonusCheckVo.pfbCategory == enumPfbxAccountCategory[0].category>
					<tr>
						<td class="text-center">個人資料</td>
						<td class="text-left">
							${pfbxApplyBonusCheckVo.personalBirthday!} 
							<br>
							${pfbxApplyBonusCheckVo.personalAddress!} 							
						</td>
						<td class="text-center">影像檔</td>
						<td class="text-center">
							<select class="form-control" name="personalCheckStatus" ng-model="personalCheckStatus">
								<#list enumPfbApplyCheck[1..3] as check>
									<#if pfbxApplyBonusCheckVo.personalCheckStatus??>									
										<#if check.status == pfbxApplyBonusCheckVo.personalCheckStatus>
											<option value="${check.status!}" selected>${check.chName!}</option>
										<#else>
											<option value="${check.status!}" >${check.chName!}</option>
										</#if>
									<#else>
										<option value="${check.status!}" >${check.chName!}</option>
									</#if>
								</#list>
							</select>	
						</td>	
						<td class="text-center">
							<input type="text" class="form-control" name="personalCheckNote" ng-model="personalCheckNote" value="${pfbxApplyBonusCheckVo.personalCheckNote!}"/>
						</td>										
					</tr>
				</#if>
				<tr>
					<td class="text-center">請款資料</td>
					<td class="text-left">
						請款金額: $${pfbxApplyBonusCheckVo.applyMoney?string("##,###")!} 						
					</td>
					<td class="text-center">影像檔</td>
					<td class="text-center">
						<select class="form-control" name="applyCheckStatus" ng-model="applyCheckStatus">
							<#list enumPfbApplyCheck[1..3] as check>
								<#if check.status == pfbxApplyBonusCheckVo.applyCheckStatus>
									<option value="${check.status!}" selected>${check.chName!}</option>
								<#else>
									<option value="${check.status!}" >${check.chName!}</option>
								</#if>
							</#list>
						</select>	
					</td>		
					<td class="text-center">
						<input type="text" class="form-control" name="applyCheckNote" ng-model="applyCheckNote" value="${pfbxApplyBonusCheckVo.applyCheckNote!}"/>
					</td>										
				</tr>				
	        </tbody>
	    </table>
	</div>
	
	<div class="container-fluid">
		<input type="hidden" class="form-control" name="pfbxApplyBonusId" ng-model="pfbxApplyBonusId" value="${pfbxApplyBonusCheckVo.pfbxApplyBonusId!}"/>
		<div class="col-lg-1">
    		<button class="btn btn-default" ng-click="submit()"><strong>儲存</strong></button>
  		</div>
	</div>
	
	</form>
</div>
