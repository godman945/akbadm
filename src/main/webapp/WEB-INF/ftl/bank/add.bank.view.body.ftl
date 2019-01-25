<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="container-fluid" ng-controller="bankCtrl">

	<div class="container-fluid">
		<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>新增銀行資料</strong></h3>	
	</div>
    
	<div class="container-fluid">
		
		<form name="bankForm" class="form-horizontal">
			<div class="form-group">
	    		<label class="control-label col-lg-1">銀行代碼</label>
				<div class="col-lg-2" >
				    <input type="text" class="form-control" placeholder="銀行代碼" ng-model="bankCode">
				</div>	
			</div>			
			<div class="form-group">
	    		<label class="control-label col-lg-1">銀行名稱</label>	
	    		<div class="col-lg-2">    		
	      			<input type="text" class="form-control" placeholder="銀行名稱" ng-model="bankName">
	      		</div>   		
	  		</div>
	  		<div class="form-group">
	    		<label class="control-label col-lg-1">分行代碼</label>	
	    		<div class="col-lg-2">    		
	      			<input type="text" class="form-control" placeholder="分行代碼" ng-model="branchCode">
	      		</div>   		
	  		</div>
	  		<div class="form-group">
	    		<label class="control-label col-lg-1">分行名稱</label>	
	    		<div class="col-lg-2">    		
	      			<input type="text" class="form-control" placeholder="分行名稱" ng-model="branchName">
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">   
		  		<div class="col-lg-offset-2">
		    		<button class="btn btn-default" ng-click="doAdd()"><strong>新增</strong></button>
		  		</div>
			</div>
		</form>		
	</div>
		
	<div class="container-fluid">
		<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>銀行列表</strong></h3>	
	</div>	

	<div class="container-fluid">
		<table class="table table-bordered table-striped ">
	        <thead>
				<tr>
					<th class="text-center">流水號</th>
					<th class="text-center">銀行名稱</th>
					<th class="text-center">銀行代碼</th>
					<th class="text-center">本行序號</th>					
				</tr>
	        </thead>

	        <tbody compile-template ng-bind-html="bankDate"></tbody>	

	    </table>
	</div>
</div>
