<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增管理帳戶</h2>

<form  method="post" action="updateManager.html">
<table id="tableManager" width="95%" class="table01" >
    <tr>
        <td width="15%" class="td06">管理系統</td>
        <td class="td02">${managerVO.system!}</td>
    </tr>
    <tr>
        <td width="15%" class="td06">管理員名稱</td>
        <td class="td02"><input type="text" id="name" name="name" value="${managerVO.name!}" size="30"></td>
    </tr>
    <tr>
        <td width="15%" class="td06">管理員PChome 帳號 <br>(總管理小天使建立請洽RD)</td>
        <td class="td02">${managerVO.memberId!}</td>
    </tr>
    <tr>
        <td width="15%" class="td06">權限</td>
        <td class="td02">
        <select name="privilege">
		<#list enumManagerPrivilege[1..3] as manager>			
			<option value="${manager.privilege!}" <#if manager.privilege == managerVO.privilege>selected</#if>>${manager.chName!}</option>			
		</#list>	
		</select>
		</td>
    </tr>
    <tr>
        <td width="15%" class="td06">使用狀態</td>
        <td class="td02">
        <select name="status">
		<#list enumManagerStatus as manager>
			<option value="${manager.status!}" <#if manager.status == managerVO.status>selected</#if>>${manager.chName!}</option>
		</#list>	
		</select>
		</td>
    </tr>
    <#if managerPfdVOs??>
    <tr>
        <td width="15%" class="td06">負責代理商</td>
        <td class="td02">	
        <#list managerPfdVOs as vo>        
        	<input type="checkbox" name="pfdAccount" value="${vo.customerInfoId}" <#if vo.isChecked?? && vo.isChecked == "true">checked</#if>> ${vo.customerInfoName}
        </#list>
		</td>
    </tr>
    </#if>
    <#if managerPfpVOs??>
    <tr>
        <td width="10%" class="td06">負責關鍵字帳戶</td>
        <td class="td02">
        <#list managerPfpVOs as vo>        	
	    	<table width="95%" class="table01" id="table_${vo.pfdCustomerInfoId}" >
	    		<tr>
			        <td width="100%" class="td03" colspan="4">
			        	<input type="checkbox" onclick="selAll('${vo.pfdCustomerInfoId}')">全選
			        	 ${vo.pfdCustomerInfoName}
			        </td>
			    </tr>
			    <#list vo.list as voList>
			    	<#if (voList_index/4) = 0 >
					    <tr>
					        <td class="td02"><input type="checkbox" name="pfpAccount" value="${voList.pfpCustomerInfoId}" <#if voList.isChecked?? && voList.isChecked == "true">checked</#if>>${voList.pfpCustomerInfoName}</td>				    
					<#elseif (voList_index%4) = 3 >				    
					    <td class="td02"><input type="checkbox" name="pfpAccount" value="${voList.pfpCustomerInfoId}" <#if voList.isChecked?? && voList.isChecked == "true">checked</#if>>${voList.pfpCustomerInfoName}</td>
					    </tr>
					<#else>
					    <td class="td02"><input type="checkbox" name="pfpAccount" value="${voList.pfpCustomerInfoId}" <#if voList.isChecked?? && voList.isChecked == "true">checked</#if>>${voList.pfpCustomerInfoName}</td>
					</#if>					
			    </#list>
	    	</table>
	    	<br>
        </#list>
		</td>
    </tr>
	</#if>
	<#if managerPfbVOs??>
    <tr>
        <td width="15%" class="td06">負責聯播網</td>
        <td class="td02">	
        <table class="table01" >
        <#list managerPfbVOs as vo>
        		<#if (vo_index/4) = 0 >
				    <tr>
				        <td style="width:25%" class="td02"><input type="checkbox" name="pfbAccount" value="${vo.pfbCustomerInfoId}" <#if vo.isChecked?? && vo.isChecked == "true">checked</#if>> ${vo.pfbCustomerInfoName}</td>				    
				<#elseif (vo_index%4) = 3 >				    
				    <td style="width:25%" class="td02"><input type="checkbox" name="pfbAccount" value="${vo.pfbCustomerInfoId}" <#if vo.isChecked?? && vo.isChecked == "true">checked</#if>> ${vo.pfbCustomerInfoName}</td>
				    </tr>
				<#else>
				    <td style="width:25%" class="td02"><input type="checkbox" name="pfbAccount" value="${vo.pfbCustomerInfoId}" <#if vo.isChecked?? && vo.isChecked == "true">checked</#if>> ${vo.pfbCustomerInfoName}</td>
				</#if>
        </#list>
        </table>
		</td>
    </tr>
    </#if>
</table>
<br>
<table width="95%">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="更新">
            <input type="button" id="viewBtn" value="返回">
        </td>
    </tr>
</table>
<input type="hidden" name="system" value="${managerVO.systemId!}" />
<input type="hidden" name="memberId" value="${managerVO.memberId!}" />
</form>