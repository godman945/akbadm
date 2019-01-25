<#assign s=JspTaglibs["/struts-tags"]>
		<#-- <div ng-controller="selectNobuttonsCtrl" ></div> -->
		<#if PfbxInComeReportVo??>
			<#list PfbxInComeReportVo as vo>
				<tr>
		            <td class="text-center">${(vo.reportdate)?string("yyyy-MM-dd")!}</td>
		            
		            <td class="text-right">$ ${vo.adclkprice?string("#,###,###.##")!}</td>
		            
		            <#-- adclkprice(PFP花費) sysclkprice(驗證花費) 誤差 1~-1內為正常，超過顯示紅底 -->
		            <#if ((vo.adclkprice - vo.sysclkprice) > 1) || ((vo.adclkprice - vo.sysclkprice) < -1) >
		            	<td class="text-right" style="font-weight: bolder; background-color: pink;">$ ${vo.sysclkprice?string("#,###,###.###")!}</td>
		            <#else>
		            	<td class="text-right">$ ${vo.sysclkprice?string("#,###,###.###")!}</td>
		            </#if>
		            
		            <td class="text-center" style="width:180px;" >
		            	<input type="button" name="" voId="${vo.id}" voDate="${(vo.reportdate)?string("yyyy-MM-dd")!}" id="PFPDetailBT_${vo.id}" class="openPFPDetail" value="PFP" />
		            	<input type="button" name="" voId="${vo.id}" voDate="${(vo.reportdate)?string("yyyy-MM-dd")!}" id="PFBDetailBT_${vo.id}" class="openPFBDetail" value="PFB" />
		            	<input type="button" name="" voId="${vo.id}" voDate="${(vo.reportdate)?string("yyyy-MM-dd")!}" id="PFDDetailBT_${vo.id}" class="openPFDDetail" value="PFD" />
		            </td>
		            
		            <td class="text-right">$ ${vo.income?string("#,###,###.##")!}</td>
		            
		            <td class="text-right">$ ${vo.expense?string("#,###,###.##")!}</td>
		            
		            <td class="text-right">$ ${vo.total?string("#,###,###.##")!}</td>
		            
		            <td class="text-center">
		            	<button class="btn btn-default detailBt" reportDate="${vo.reportdate?string("yyyy-MM-dd")!}" ><strong>檢視明細</strong></button>
					</td>
	          	</tr>
	        </#list>
	    </#if>