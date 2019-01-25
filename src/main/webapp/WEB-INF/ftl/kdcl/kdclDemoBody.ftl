<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<div class="cont">
    <h2>
        <img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告核心測試
    </h2>
    <div class="grtba borderbox" style="height: 80px;">
        <div style="clear:both;background:#e4e3e0" >
          <form id="templateProductSearchForm" action="templateProductSearch.html" method="post">
                <table width="75%">
                    <tr>
                        <td>請選擇帳戶 :
                        	<select id="customerInfoSelect" style="width:180px;" onChange="memberIdSelect();">
                        	 <option value="0"></option>
                        	 <#if pfbxCustomerInfoList?exists>
                        	 	<#list pfbxCustomerInfoList as pfbxCustomerInfoVO>
                        	 	   <option value=${pfbxCustomerInfoVO.customerInfoId!}>${pfbxCustomerInfoVO.memberId!}</option>
                        	 	</#list>
                        	 </#if>
                        	</select>
                        </td>
                        
                         <td>請選擇版位名稱 :
                        	<select id="positionNameSelect" style="width:130px;" onChange="positionNameTypeSelect();">
                        	 <option value="0"></option>
                        	 <#if pfbxPositionList?exists>
                        	 	<#list pfbxPositionList as pfbxPositionVO>
                        	 	   <option value=${pfbxPositionVO.PId!}>${pfbxPositionVO.PName!}</option>
                        	 	</#list>
                        	 </#if>
                        	</select>
                        </td>
                         <td>
                         	請輸入高: <input id="padHeightTextId" type="text" style="width:50px;"> 請輸入寬: <input id="padWidthTextId" type="text" style="width:50px;">
                         </td>
                         
                         <td>
                         	<input id="submitBtn" type="button" value="送出" onClick="submitSize();">
                         </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>  
 </br>
		<table border="1" style="width:auto;">
   				<tr border="1">
   					<td style="text-align:center;">
   					PreView
   					</td>
   				</tr>
   				<#if padHeight?exists>
   				<tr>
   					<td>
					<script language="javascript">
					pad_width=${padWidth};
					pad_height=${padHeight};
					pad_customerId="${pfbxPosition.pfbxCustomerInfo.customerInfoId!}";
					pad_positionId="${pfbxPosition.PId!}";
					</script>
   					<script id="pcadscript" language="javascript" src="${jsSource!}"></script>
   					</td>
   				</tr>
   				</#if>
   			<table>
   <form id="kdclAdFormId" action="kdclAd.html" method="post">
   <input type="hidden" value="" name="pfbxCustomerId" id="pfbxCustomerId" >
   <input type="hidden" value="" name="pid" id="pid">
   <input type="hidden" value="" id = "padHeight"  name="padHeight" id="padHeight" >
   <input type="hidden" value=""  id = "padWidth" name="padWidth" id="padWidth" >
   <#if pfbxPosition?exists >
  	<input type="hidden" value=${pfbxPosition.pfbxCustomerInfo.memberId!} name="defaultMemberId" id="defaultMemberId">
   	<input type="hidden" value=${pfbxPosition.pfbxCustomerInfo.customerInfoId!} name="defaultcustomerInfoId" id="defaultcustomerInfoId">
   	<input type="hidden" value=${pfbxPosition.PName!} name="defaultPName" id="defaultPName">
   	<input type="hidden" value=${pfbxPosition.PId!} name="defaultPId" id="defaultPId">
   </#if>
   <#if padHeight?exists >
   	<input type="hidden" value='${padHeight}' name="defaultPadHeight">
   	<input type="hidden" value='${padWidth}' name="defaultPadWidth">
   </#if>
</form>
<br>
