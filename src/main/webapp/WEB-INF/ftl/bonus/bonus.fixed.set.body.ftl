<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />固定獎金設定</h2>

<form id="fixedBonusSetForm" action="updateFixedBonusSet.html" method="post">
<div class="abgne_tab">
	<div class="tab_container">
		<h2>經銷商選擇</h2>
		<table width="650" rules="none">
		    <tr>
		        <td>
		        ${pfdContract.pfdCustomerInfo.companyName!} (${pfdContract.startDate?string("yyyy-MM-dd")!} ~ ${pfdContract.endDate?string("yyyy-MM-dd")!})
				<#if (pfdContract.status != enumContractStatus[2].statusId )> <font color="#FF0000">合約啟用、作廢或過期失效，都無法重新設定 </font></#if>		    	
		    	</td>
		    </tr>
		</table>
		<br/>
		<h2>獎金計算方式</h2>
		
		<#list bonusFixedFileSet as fixedFileSet>
			<#if enumPfdBonusItem[0].itemType == fixedFileSet["bonusItemId"]>
				<#-- 月固定獎金設定 -->
				<table width="500" rules="none">
					<tr>
				    	<td rowspan="${(fixedFileSet["data"]?size+1)!}" width="100"> 
				    		<input type="checkbox" id="chkBonusSet${(fixedFileSet_index+1)!}" name="chkBonusSet${(fixedFileSet_index+1)!}" value="${fixedFileSet["bonusItemId"]!}" <#if fixedFileSet??>${fixedFileSet["checked"]!}</#if> <#if closeFlag == 'Y' >disabled</#if>> 
				    		${fixedFileSet["title"]!}
				    	</td>
				    </tr>
				    <#list fixedFileSet["data"] as fixedSet>
					    <tr>		    	
					    	<td width="100" >
					    		<input type="radio" id="rdoBonusSet${fixedFileSet_index+1!}" name="rdoBonusSet${fixedFileSet_index+1!}" value="${fixedSet_index+1!}" <#if bonusFixedDbSet??><#list bonusFixedDbSet as dbSet><#if fixedFileSet["bonusItemId"] == dbSet.bonusItem && (fixedSet_index+1) == dbSet.subItemId >checked</#if></#list></#if> <#if closeFlag == 'Y' >disabled</#if>> 
								${fixedSet.bonus*100!} %
							</td>		        
					    </tr>				            
				    </#list>
				</table>
			<#else>
				<#-- 季、年固定獎金設定 -->
				<table width="600" rules="none">
					<tr>
				    	<td rowspan="${(fixedFileSet["data"]?size+1)!}" width="100"> 
				    		<input type="checkbox" id="chkBonusSet${(fixedFileSet_index+1)!}" name="chkBonusSet${(fixedFileSet_index+1)!}" value="${fixedFileSet["bonusItemId"]!}" <#if fixedFileSet??>${fixedFileSet["checked"]!}</#if> <#if closeFlag == 'Y' >disabled</#if>> 
				    		${fixedFileSet["title"]!} 
				    	</td>
				    </tr>
				    <#list fixedFileSet["data"] as fixedSet>
					    <tr>	
					    	<td width="50"></td>	    	
					    	<td width="100">
					    		<input type="radio" id="rdoBonusSet${fixedFileSet_index+1!}" name="rdoBonusSet${fixedFileSet_index+1!}" value="${fixedSet_index+1!}" <#if bonusFixedDbSet??><#list bonusFixedDbSet as dbSet><#if fixedFileSet["bonusItemId"] == dbSet.bonusItem && (fixedSet_index+1) == dbSet.subItemId >checked</#if></#list></#if> <#if closeFlag == 'Y' >disabled</#if>>  
					    	</td>
					    	<td >
					    	<#list fixedSet as bonusBean>
					    		<div>累計廣告費用 $ ${bonusBean.min?string('#,###')!} ~ $ ${bonusBean.max?string('#,###')!}，獎金比率 ${bonusBean.bonus*100!} %</div>								
							</#list>
							</br>
							</td>		        
					    </tr>			            
				    </#list>
				</table>
			</#if>
			<hr/>
		</#list>
		<center><input type="button" id="saveBtn" name="saveBtn" value="儲存設定" <#if (pfdContract.status != enumContractStatus[2].statusId )>disabled</#if>/> <input type="button" value="返回合約清單" onclick="bonusContractSetList()" /></center>
	</div>
</div>
<input type="hidden" name="pfdContractId" value="${pfdContract.pfdContractId!}">
<input type="hidden" id="selFixedBonusSet" name="selFixedBonusSet" />
</form>