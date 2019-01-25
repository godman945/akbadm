<#assign s=JspTaglibs["/struts-tags"]>
<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>
<style type="text/css">
	table.tablesorter2 thead tr .header {
		background-image: url(html/img/bg.gif);
		background-repeat: no-repeat;
		background-position: center right;
		cursor: pointer;
		padding-right:10px;
	}
	
	table.tablesorter2 thead tr .headerSortUp {
		background-image: url(html/img/asc.gif);
	}
	table.tablesorter2 thead tr .headerSortDown {
		background-image: url(html/img/desc.gif);
	}
</style>
<div class="container-fluid" ng-controller="searchCtrl">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>累計收益</strong></h3>
	
	<div class="container-fluid">
		<form class="form-horizontal" role="form" >
		
			<div class="form-group">
				<div class="control-label col-lg-2 col-md-2">
					<label class="control-label">關鍵字查詢</label>
				</div>
		  		<div class="control-label col-lg-4 col-md-5">
		    		<input type="text" class="form-control" placeholder="帳戶編號/帳戶名稱/會員帳號" ng-model="keyword" id="keyword" name="keyword" >
		  		</div>
			</div>
			
			<div class="form-group">
		  		<label class="control-label col-lg-2 col-md-2">帳戶類型</label>
		  		<label class="control-label col-lg-4 col-md-5">
			  		<select class="form-control" ng-model="category" id="category" name="category" >
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
			  		<select class="form-control" ng-model="status" id="status" name="status" >
			  			<option value="">所有分潤狀態</option>
			      		<#list enumBonusSet as bonusSet>
			  				<option value="${bonusSet.status}">${bonusSet.chName}</option>
			  			</#list>
			        </select>
		        </label>
			</div>
			<div class="form-group">        
		  		<label class="control-label col-lg-2">
		    		<button class="btn btn-default" ng-click="doSearch()"><strong>查詢</strong></button>
		  		</label>
		  		<label class="control-label col-lg-2">
		    		<button class="btn btn-default" onClick="doDownlaod()"><strong>下載</strong></button>
		  		</label>
			</div>
			<input type="hidden" id="downloadFlag" name="downloadFlag" />
		</form>		
	</div>
	<div compile-template ng-bind-html="bonusSetUpData"></div>
</div>

