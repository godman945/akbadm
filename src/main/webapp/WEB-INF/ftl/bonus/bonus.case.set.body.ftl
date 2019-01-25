<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />專案獎金設定</h2>

<form id="caseBonusSetForm" action="updateCaseBonusSet.html" method="post">
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
		<h2>專案獎金項目</h2>
		
		<#list bonusCaseFileSet as caseFileSet>
		
		<table width="500" rules="none" >			
			<tr>
		    	<td rowspan="${(caseFileSet?size+1)!}" width="150">
		    		<input type="checkbox" id="chkBonusSet${(caseFileSet_index+1)!}" name="chkBonusSet${(caseFileSet_index+1)!}" value="${caseFileSet["bonusItemId"]}" <#if caseFileSet??>${caseFileSet["checked"]!}</#if>> 
		    		${caseFileSet["title"]} <#if caseFileSet["startDate"]??> (${caseFileSet["startDate"]} ~ ${caseFileSet["endDate"]}) </#if>
		    	</td>
		    </tr>
		    <#list caseFileSet["data"] as caseSet>
		    <tr>
		    	<td width="20"></td>
		    	<td width="100" ><input type="radio" id="rdoBonusSet${(caseFileSet_index+1)!}" name="rdoBonusSet${(caseFileSet_index+1)!}" value="${(caseSet_index+1)!}" <#if bonusCaseDbSet??><#list bonusCaseDbSet as dbSet><#if (caseSet_index+1) == dbSet.subItemId && caseFileSet["bonusItemId"] == dbSet.bonusItem>checked</#if></#list></#if>></td>
		        <td >		        
	        	<#list caseSet as bonusBean>
	        		<#-- 
	        		enumBonusType[0] 達成獎金	        		
	        		enumBonusType[1] 開發獎金
	        		enumBonusType[2] 專案獎金
	        		-->
	        		<#if enumPfdBonusItem[1].itemType == caseFileSet["bonusType"]>
	        			<div>(${bonusBean.min?string('#,###')!} 間 ~ ${bonusBean.max?string('#,###')!} 間)  $ ${bonusBean.bonus!}</div>
	        		<#else>
	        			<div>$ ${bonusBean.min?string('#,###')!} ~ $ ${bonusBean.max?string('#,###')!}  (${bonusBean.bonus*100!}%)</div>
					</#if>
	        	</#list>
	        	</br>		        
				</td>
		    </tr>
			</#list>	          
		</table>
		<hr/>
		</#list>
	
		<center><input type="button" id="saveBtn" name="saveBtn" value="儲存專案獎金" <#if (pfdContract.status != enumContractStatus[2].statusId )>disabled</#if>/> <input type="button" value="返回合約清單" onclick="bonusContractSetList()" /></center>
	</div>
</div>
<input type="hidden" name="pfdContractId" value="${pfdContract.pfdContractId!}">
<input type="hidden" id="selCaseBonusSet" name="selCaseBonusSet" />
</form>