<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" ng-controller="searchCtrl">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>分潤查詢</strong></h3>
	
	<div class="container-fluid">
		<form class="form-horizontal" role="form">
		
			<div class="form-group">
                <div class="control-label col-lg-2 col-md-2">
					<label class="control-label">關鍵字查詢</label>
				</div>
		  		<div class="control-label col-lg-4 col-md-5">
		    		<input type="text" class="form-control" placeholder="帳戶編號/統一編號/會員帳號/身份證字號" ng-model="keyword">
		  		</div>
			</div>
			
			<div class="form-group">
		  		<label class="control-label col-lg-2 col-md-2">帳戶類型</label>
		  		<label class="control-label col-lg-4 col-md-5">
			  		<select class="form-control" ng-model="category">
			  			<option value="">所有帳戶類型</option>
			  			<#list enumAccountCategory as account>
			  				<option value="${account.category}">${account.chName}</option>
			  			</#list>
			        </select>
		        </label>
			</div>
			
			<div class="form-group">
		  		<label class="control-label col-lg-2 col-md-2">分潤狀態</label>
		  		<label class="control-label col-lg-4 col-md-5">
			  		<select class="form-control" ng-model="status">
			  			<option value="">所有分潤狀態</option>
			      		<#list enumBonusSet as bonusSet>
			  				<option value="${bonusSet.status}">${bonusSet.chName}</option>
			  			</#list>
			        </select>
		        </label>
			</div>
			<div class="form-group ">
				<div class="col-lg-offset-4 col-md-offset-1">
                    <button class="btn btn-default" ng-click="doSearch()"><strong>查詢</strong></button>
				</div>
			</div>
		</form>		
	</div>
	
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>客戶列表</strong></h3>
	
	<div class="container-fluid">
		<table class="table table-striped table-bordered">
	        <thead>
				<tr>
					<th class="text-center">流水號</th>
					<th class="text-center">帳戶編號</th>
					<th class="text-center">帳戶類型</th>
					<th class="text-center">統一編號/身份證字號</th>
					<th class="text-center">會員帳號</th>
					<th class="text-center">分潤狀態</th>
					<th class="text-center">客戶分潤 %</th>
					<th class="text-center">PChome分潤 %</th>
					<th class="text-center">分潤總計 %</th>
				</tr>
	        </thead>
	        <#-- <tbody compile-template ng-bind-html="bonusSetUpData"></tbody> -->	
	        <tbody compile-template ng-bind-html="bonusSetUpData"></tbody>	
	        <#-- 
	        <tbody>
	        	<tr ng-repeat="customer in customerBonus track by $index ">
		            <td class="text-center">{{$index+1}}</td>
		            <td class="text-center">
		            	{{ customer }}
		            	<a href ng-click="launch('create')"></a>
		            </td>
		            <td class="text-center">{{ customer.pfbxCustomerInfoName }}</td>
		            <td class="text-center"></td>
		            <td class="text-center"></td>
		            <td class="text-center"></td>
		            <td class="text-center"></td>
		            <td class="text-center"></td>
	          	</tr>
	        </tbody>
	         -->
	    </table>
	</div>
	
</div>

