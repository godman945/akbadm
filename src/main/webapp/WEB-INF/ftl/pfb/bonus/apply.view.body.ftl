<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="container-fluid" ng-controller="searchCtrl">

	<div class="container-fluid">
		<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>客戶請款單</strong></h3>	
	</div>
    
	<div class="container-fluid">
		
		<form name="searchForm" class="form-horizontal">
			<div class="form-group">
	    		<label class="control-label col-lg-2 col-md-2">查詢日期</label>
				<div class="col-lg-3 col-md-5 input-daterange input-group" style="padding-left: 15px ; padding-right: 15px;" id="datepicker">
				    <input type="text" class="form-control" name="startDate" id="startDate" ng-model="startDate" />
				    <span class="input-group-addon">to</span>
				    <input type="text" class="form-control" name="endDate" id="endDate" ng-model="endDate" />
				</div>	
			</div>			
			<div class="form-group">
	    		<label class="control-label col-lg-2 col-md-2">關鍵字查詢</label>
	    		<div class="col-lg-3 col-md-5">
	      			<input type="text" class="form-control" placeholder="請款單號/帳戶編號/統一編號/身分證字號/會員帳號" ng-model="keyword" id="keyword">
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">
	    		<label class="control-label col-lg-2 col-md-2">帳戶類型</label>
	    		<div class="col-lg-3 col-md-5">
	      			<select class="form-control" ng-model="category" id="category">
			  			<option value="">所有帳戶類型</option>
			  			<#list enumPfbxAccountCategory as account>
			  				<option value="${account.category}">${account.chName}</option>
			  			</#list>
			        </select> 
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">
	    		<label class="control-label col-lg-2 col-md-2">請款狀態</label>
	    		<div class="col-lg-3 col-md-5">
	      			<select class="form-control" ng-model="status" id="status">
			  			<option value="">所有請款狀態</option>
			      		<#list enumPfbApplyBonus as applyBonus>
			  				<option value="${applyBonus.status}">${applyBonus.chName}</option>
			  			</#list>
			        </select> 
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">   
		  		<div class="col-lg-offset-3">
		    		<button class="btn btn-default" ng-click="doSearch()"><strong>查詢</strong></button>
		  		</div>
			</div>
		</form>		
	</div>
		
	<div class="container-fluid" compile-template ng-bind-html="bonusSetUpData" id="tableDIV">
		<table class="table table-bordered table-striped ">
	        <thead>
				<tr>
					<th class="text-center">請款日期</th>
					<th class="text-center">請款單號</th>
					<th class="text-center">帳戶名稱</th>
					<th class="text-center">帳戶編號</th>
					<th class="text-center">帳戶類型</th>
					<th class="text-center">統一編號</th>
					<th class="text-center">會員帳號</th>
					<th class="text-center">請款金額</th>
					<#--<th class="text-center">銀行</th>-->
					<#--<th class="text-center">銀行狀態</th>-->
					<#--<th class="text-center">領款人</th>-->
					<#--<th class="text-center">領款人狀態</th>-->
					<th class="text-center">發票/收據 審核</th> 
					<th class="text-center">請款單狀態</th>
				</tr>
	        </thead>

	        <tbody compile-template ng-bind-html="bonusSetUpData"></tbody>

	    </table>
	</div>
</div>



<#-- <input class="form-control" type="text" value="${startDate!}~${endDate!}" ng-click="doOpen()" readonly> -->