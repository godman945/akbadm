<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script type="text/ng-template" id="dateSelectDialog">

	<div class="container-fluid"> 
	
		<form class="form-horizontal">
			<div class="form-group">
				<label class="col-lg-offset-1 col-lg-10">請選擇日期區間</label>	
			</div>
			
			<div class="form-group">
	    		<label class="col-lg-offset-1 col-lg-3">開始日期</label>	
	    		<div class="col-lg-7">    		
	      			<input class="form-control" type="text" value="${startDate!}" readonly>	 
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">
	    		<label class="col-lg-offset-1 col-lg-3">結束日期</label>	
	    		<div class="col-lg-7">    		
	      			<input class="form-control" type="text" value="${endDate!}" readonly>	 
	      		</div>   		
	  		</div>
	  		
	  		<div class="form-group">
				<label class="col-lg-offset-1 col-lg-10">或者，請選擇以下的預設日期範圍</label>	
			</div>
			
			<div class="form-group">	    			
	    		<div class="col-lg-offset-1 col-lg-10">    		
	      			<select class="form-control" ng-model="category">
			  			<#list dateSelectMap?keys as itemKey>
							<#assign item = dateSelectMap[itemKey]>
								<option value="${item}">${itemKey}</option>
					    </#list>
			        </select> 
	      		</div>   		
	  		</div>
			
			<div class="form-group">   
		  		<div class="col-lg-offset-3 col-lg-3">
		    		<button class="btn btn-default" ng-click="doSearch()">確定</button>
		  		</div>
		  		<div class="col-lg-3">
		    		<button class="btn btn-default" ng-click="doClose()">取消</button>
		  		</div>
			</div>
		</form>	

	</div>
	
</script> 