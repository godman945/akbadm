<#assign s=JspTaglibs["/struts-tags"]>

<div class="container-fluid" ng-controller="transCtrl">

	<div class="row-fluid">
		<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>累計收益 -- 帳戶明細</strong></h3>
	</div>
	
	<div style="height:30px;"></div>
	
	<button class="btn btn-default" ng-click="" onclick="history.back();" ><strong>返回</strong></button>
		
	<div class="row-fluid">
		<table class="table table-bordered">
			<caption>
				<h4><strong>
					帳戶名稱：
					<#if pfbxBonusVo.pfbxCustomerInfoName??>
						${pfbxBonusVo.pfbxCustomerInfoName!}
					<#else>
						${pfbxBonusVo.pfbxCustomerInfoContactName!}
					</#if>
				</strong></h4>
			</caption>
			<thead>
                <tr >
                    <th class="text-center">待結金額</th>
                    <th class="text-center">請款金額</th>
                    <th class="text-center">上次已付金額</th>
                    <th class="text-center">已付金額</th>
                    <th class="text-center">請款門檻</th>
                    <th class="text-center">帳戶類別</th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <td class="text-right">NT$ ${pfbxBonusVo.waitBonus?string("#,###,###.##")!}</td>
                    <td class="text-right">NT$ ${pfbxBonusVo.applyBonus?string("#,###,###.##")!}</td>
                    <td class="text-right">NT$ ${pfbxBonusVo.lastPayBonus?string("#,###,###.##")!}</td>
                    <td class="text-right">NT$ ${pfbxBonusVo.totalPayBonus?string("#,###,###.##")!}</td>
                    <td class="text-right">NT$ ${pfbxBonusVo.applyMinLimit?string("#,###,###.##")!}</td>
                    <td class="text-center">${pfbxBonusVo.pfbxCustomerCategoryName!}</td>
                </tr>
            </tbody>
		</table>
	</div>
	
	<div class="row-fluid">
		<table class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th class="text-center">月份</th>
                    <th class="text-center">項目說明</th>
                    <th class="text-center">新增收益 </th>
                    <th class="text-center">已付收益 </th>
                    <th class="text-center">收益餘額</th>
                </tr>
            </thead>
            <tbody>
            <#if pfbxBonusTransVos??>
	            <#list pfbxBonusTransVos as vo>
	                <tr>
	                    <td class="text-center">${vo.transDate!}</td>
	                    <td class="text-center">
	                    <#if vo.transDesc?? && vo.transDesc != "">
	                    	${vo.transDesc!}
	                    <#else>
	                    	<a href ng-click="doOpen('${vo.pfbxCustomerInfoId!}','${vo.strYear!}','${vo.strMonth!}','${vo.endYear!}','${vo.endMonth!}','${vo.payDate!}')" target=_blank>明細</a>｜
	                    	<a href ng-click="doDownload('${vo.pfbxCustomerInfoId!}','${vo.strYear!}','${vo.strMonth!}','${vo.endYear!}','${vo.endMonth!}','${vo.payDate!}')">下載</a>
	                    </#if>	
	                    </td>
	                    <td class="text-right">$ ${vo.waitBonus?string("#,###,###.##")!}</td>
	                    <td class="text-right">$ ${vo.payBonus?string("#,###,###.##")!}</td>
	                    <td class="text-right">$ ${vo.remain?string("#,###,###.##")!}</td>
	                </tr>
	           </#list>
           </#if>
           <#--
                <tr>
                	<td class="text-center">2014/1~2014/6 <b class="memo f-xs">(已付款)</b></td>
                    <td class="text-center"><a href="sample.pdf">明細</a> ｜ <a href="sample.pdf">下載</a></td>
                    <td class="text-right">NT$1,025</td>
                    <td class="text-right">(NT$1,025)</td>
                    <td class="text-right">NT$0</td>
                </tr>
                <tr>
                    <td class="text-center">2014/7</td>
                    <td class="text-center">聯播網廣告收益-付款</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$1025</td>
                    <td class="text-right">$0</td>
                </tr>
                <tr>
                    <td class="text-center">2014/6</td>
                    <td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$155</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$1025</td>
                </tr>
                <tr>
                    <td class="text-center">2014/5</td>
                	<td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$350</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$870</td>
                </tr>
                <tr>
                    <td class="text-center">2014/4</td>
                    <td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$250</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$520</td>
                </tr>
                <tr>
                    <td class="text-center">2014/3</td>
                    <td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$150</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$270</td>
                </tr>
                <tr>
                    <td class="text-center">2014/2</td>
                    <td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$70</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$120</td>
                </tr>
                <tr>
                    <td class="text-center">2014/1</td>
                    <td class="text-center">聯播網廣告收益-新增</td>
                    <td class="text-right">$50</td>
                    <td class="text-right">$0</td>
                    <td class="text-right">$50</td>
                </tr>
             -->
            </tbody>
        </table>
	</div>
</div>

