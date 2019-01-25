<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" ng-controller="searchCtrl">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>單日明細</strong></h3>
	
	<div class="container-fluid"></div>
	
	<div style="height:30px;"></div>
	
	<button class="btn btn-default" onclick="location.href='pfbInComeReport.html?sdate=${sdate}&edate=${edate}&returnFlag=Y'" ><strong>返回</strong></button>
	
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>${reportDate}</strong></h3>
	
	<div class="container-fluid">
		<table class="table table-striped table-bordered">
	        <thead>
				<tr>
					<th class="text-center">明細項目</th>
					<th class="text-center">項目名稱</th>
					<th class="text-center">收入支出</th>
					<th class="text-center">儲值金</th>
					<th class="text-center">贈送金</th>
					<th class="text-center">後付費</th>
					<th class="text-center">小計</th>
				</tr>
	        </thead>
	        <tbody>
		        <#if PfbxInComeReportVoList??>
					<#list PfbxInComeReportVoList as vo>
						<tr>
				            <td class="text-center">${(vo.detailItem)!}</td>
				            <td class="text-center">${vo.detailItemName!}</td>
				            <td class="text-center">${vo.detailIncomeExpense!}</td>
				            <td class="text-right">$ ${vo.detailSave?string("#,###,###.##")!}</td>
				            <td class="text-right">$ ${vo.detailFree?string("#,###,###.##")!}</td>
				            <td class="text-right">$ ${vo.detailPostPaid?string("#,###,###.##")!}</td>
				            <td class="text-right">$ ${vo.detailTotal?string("#,###,###.##")!}</td>
			          	</tr>
			        </#list>
		    	</#if>
	        </tbody>
	       
	    </table>
	</div>
</div>

<form id="returnForm" action="pfbInComeReport.html" method="post">
    <input type="hidden" name="sdate" value="${sdate}" />
    <input type="hidden" name="edate" value="${edate}" />
    <input type="hidden" name="returnFlag" value="Y" />
</form>