<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" ng-controller="searchCtrl" id="RightBody">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>盈虧查詢</strong></h3>
	
	<div class="container-fluid">
		<form name="searchForm" class="form-horizontal" role="form">
		
			<div class="form-group" style="">
	    		<label class="control-label col-lg-1">查詢日期</label>
				<div class="col-lg-2 input-daterange input-group" id="datepicker" style="width:300px;">
				    <input type="text" class="form-control" name="startDate" id="startDate" ng-model="" value="${sdate!}" required/>
				    <span class="input-group-addon">to</span>
				    <input type="text" class="form-control" name="endDate" id="endDate" ng-model="" value="${edate!}" required/>
				    <input type="hidden" id="returnFlag" name="returnFlag" value="${returnFlag!}" />
				</div>
			</div>
			
			<div class="form-group">
		  		<label class="control-label col-lg-2">
		    		<button class="btn btn-default" id="searchBt" ng-click="doSearch()"><strong>查詢</strong></button>
		  		</label>
			</div>
		</form>
	</div>
	
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>收支列表</strong></h3>
	
	<div class="container-fluid">
		<table class="table table-striped table-bordered">
	        <thead>
				<tr>
					<th class="text-center">日期</th>
					<th class="text-center">PFP花費</th>
					<th class="text-center">驗證花費</th>
					<th class="text-center">PFP花費明細</th>
					<th class="text-center">收入(不含禮金)</th>
					<th class="text-center">支出(含禮金)</th>
					<th class="text-center">盈虧</th>
					<th class="text-center">明細</th>
				</tr>
	        </thead>
	        
	        <tbody compile-template ng-bind-html="bonusSetUpData" id="AjaxBackData"></tbody>
	       
	    </table>
	</div>
	
	<div id="dialog">
        
    </div>
    
</div>

