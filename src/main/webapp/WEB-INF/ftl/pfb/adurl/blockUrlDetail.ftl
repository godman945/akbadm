<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="container-fluid" ng-controller="searchCtrl">
	
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>已封鎖網址明細</strong>
	</h2>
	
	<div class="container-fluid">
		<strong>帳戶編號 :</strong> ${id!} <br>
		<strong>會員帳號 :</strong> ${pfbInfo.memberId!} <br> 
		<strong>帳戶類別 :</strong>
		<#if pfbInfo.category?exists && pfbInfo.category == '1'>
        	個人戶
		<#elseif pfbInfo.category?exists && pfbInfo.category == '2'>
		    公司戶                  	
        </#if>
		<br> 
		<strong>公司名稱 :</strong> ${pfbInfo.companyName!} <br>
		<strong>廣告播放網址 :</strong> ${pfbInfo.websiteDisplayUrl!} <br>
	</div>
	
	<div style="height:10px;"></div>
	
	<form class="form-horizontal" id="reSearchForm" action="pfbBlockUrlDetail.html" method="post">
		<input type="hidden" name="id" id="id" value="${id!}" />
		<input type="hidden" name="keyword" id="keyword" value="${keyword!}" />
		<input type="hidden" name="category" id="category" value="${category!}" />
		
		<input type="hidden" name="blockStatus" id="blockStatus" value="" />
		<input type="hidden" name="blockDesc" id="blockDesc" value="" />
		<input type="hidden" name="blockUrl" id="blockUrl" value="" />
		<input type="hidden" name="unblockId" id="unblockId" value="" />
		
		
		<div style="margin-top:20px;"></div>
		<strong style="margin-left:15px;">網址查詢 : </strong>
		<input type="text" class="" style="width:300px;" placeholder="請輸入網址" name="searchUrl" value="${searchUrl!}" />
		&nbsp;
		<input type="button" class="btn btn-default" id="reSearchForm_subBT" value="查詢" />
		
		<div style="margin-top:10px;"></div>
		<strong style="margin-left:15px;">新增封鎖網址 : </strong>
		<input type="text" class="" style="width:200px;" placeholder="請輸入網址" id="fastBlockUrl" name="" value="" />
		<input type="text" class="" style="width:200px;" placeholder="請輸入封鎖原因" id="fastBlockDesc" name="" value="" />
		&nbsp;
		<input type="button" class="btn btn-default" id="fastBlockUrlBT" value="封鎖" />
		<input type="button" class="btn btn-default" id="detail_bk_listBT" value="返回" />
	</form>
	
	<div style="height:30px;"></div>
	
	<div class="container-fluid" style="text-align:left;">
		<table class="table table-striped table-bordered"  style="width:900px;">
	        <thead>
				<tr>
					<th class="text-center">網址</th>
					<th class="text-center">網址審核</th>
					<th class="text-center">封鎖日期</th>
				</tr>
	        </thead>
	        <tbody>
		        <#if urlVOS??>
					<#list urlVOS as vo>
						<tr>
				            <td class="text-center">${vo.detailurl!}</td>
				            <td class="text-center">
				            	<input type="text" name="" id="" class="" placeholder="封鎖原因" value="${vo.detaildesc}" disabled="disabled" />
                            	<input type="button" name="" id="" class="btn btn-default unblockUrlBT" unblockId="${vo.detailid!}" url="${vo.detailurl!}" value="解除封鎖" />
				            </td>
				            <td class="text-center">${vo.detaildate?string("yyyy-MM-dd")}</td>
			          	</tr>
			        </#list>
		    	</#if>
	        </tbody>
	       
	    </table>
	</div>
</div>