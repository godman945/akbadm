<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />聯播網帳戶資料修改</h2>

<form name="updateForm" method="post">
<input type="hidden" name="customerInfoId" id="customerInfoId" value="${customerInfoId!}">
<input type="hidden" id="adType" name="adType" value="${adType!}">
聯播網帳戶資料：<br>
<table width="600" class="table01">
    <tr>
        <td class="td01">帳戶編號</td>
        <td class="td02">${customerInfoId!}</td>
        <td class="td01">帳戶類別</td>
        <td class="td02">${categoryName!}</td>
    </tr>
    <tr>
        <td class="td01">會員帳號</td>
        <td class="td02">
            ${memberId!}
            <input id="customerInfoId" type="hidden" value="${customerInfoId}">
        </td>
        <td class="td01">會員email</td>
        <td class="td02">${userEmail!}</td>
    </tr>
    <tr>
        <td class="td01">公司名稱</td>
        <td class="td02">${companyName!}</td>
        <td class="td01">統一編號</td>
        <td class="td02">${taxId!}</td>
    </tr>
    <tr>
        <td class="td01">聯絡人</td>
        <td class="td02">${contactName!}</td>
        <td class="td01">帳戶狀態</td>
        <td class="td02">
            <select name="status" id="status">
                <#list queryStatusOptionsMap?keys as key>
                    <#if key != "4">
		  		        <option value="${key}"
                            <#if status?exists && status = key> selected</#if>
                        >${queryStatusOptionsMap[key]}</option>
		  		    </#if>
		  	    </#list>
		  	</select>
            <input type="text" id="statusNote" name="statusNote" placeholder="說明原因"  value="${statusNote!}" style="display: <#if (status == "3" || status == "5")>inline<#else>none</#if>; " />
            <input id="accountStatus" type="hidden" value="${status!}">
        </td>
    </tr>
    <tr>
        <td class="td01">聯絡電話</td>
        <td class="td02">${contactPhone!}</td>
        <td class="td01">手機號碼</td>
        <td class="td02">${contactCell!}</td>
    </tr>
    <tr>
        <td class="td01">申請日期</td>
        <td class="td02">${applyDate!}</td>
        <td class="td01">開通日期</td>
        <td class="td02">${activeDate!}</td>
    </tr>
    <tr>
        <td class="td01">網站類型</td>
        <td class="td02">
        	<select name="categoryCode" id="categoryCode">
                <#list queryCategoryCodeMap?keys as key>
		  			<option value="${key}" <#if categoryCode?exists && categoryCode = key> selected</#if> >${queryCategoryCodeMap[key]}</option>
		  	    </#list>
		  	</select>
        </td>
        <td class="td01">結束日期</td>
        <td class="td02"><input type="text" id="closeDate" name="closeDate"
                                placeholder="結束日期"  value="${closeDate!}" readonly />
                        <input type="button" id="cleanCloseDate" name="cleanCloseDate" value="清除"/>
        </td>
    </tr>
     <tr>
        <td class="td01">撥放管理</td>
        <td colspan="3" class="td02">
            <input name="adTypeCheck"  type ="checkbox" id="searchAd"
        	    <#if '${adType}' == '1' || '${adType}' == '3'>checked</#if>>搜尋廣告
            <input name="adTypeCheck" type ="checkbox" id="contentAd"
        	    <#if '${adType}' == '2' || '${adType}' == '3'>checked</#if>>內容廣告
        </td>
    </tr>
    <#if pfbxCustomerInfoRefXTypeList??>
     <tr>
        <td class="td01">樣板屬性</td></td>
        <td class="td02" id="xTypeInfoTd" colspan="3">
			<ul>
			<#list pfbxCustomerInfoRefXTypeList ?sort_by("xtypeRefId") as pfbxCustomerInfoRefXTypeVO>
 				<li>
				  <input type="checkbox" id="${pfbxCustomerInfoRefXTypeVO.pfbxPositionMenu.id!}"
                         class="${pfbxCustomerInfoRefXTypeVO.pfbxPositionMenu.pfbxMenuProperties!}"
                         <#if "${pfbxCustomerInfoRefXTypeVO.status!}" == "1">checked</#if>
                         >${pfbxCustomerInfoRefXTypeVO.pfbxPositionMenu.pfbxMenuName!}</li>
 			</#list>
			</ul>
		</td>
        </td>
    </tr>
    </#if>
    <tr>
        <td class="td01">網站名稱</td>
        <td class="td02" colspan="3"><input type="text" id="websiteChineseName" name="websiteChineseName" value="${websiteChineseName!}" /></td>
    </tr>
    <tr>
        <td class="td01">廣告撥放網址</td>
        <td class="td02" colspan="3"><input type="text" id="websiteDisplayUrl" name="websiteDisplayUrl" value="${websiteDisplayUrl!}" /></td>
    </tr>
    <tr>
        <td class="td01">主網域</td>
        <td class="td02" colspan="3"><input type="text" id="rootDomain" name="rootDomain" value="${rootDomain!}" /></td>
    </tr>
    <tr>
        <td class="td01">廣告播放網域限制</td>
        <td class="td02" colspan="3">
        	<select name="playType" id="playType">
                <#list queryPlayTypeMap?keys as key>
		  			<option value="${key}" <#if playType?exists && playType = key> selected</#if> >${queryPlayTypeMap[key]}</option>
		  	    </#list>
		  	</select>
        </td>
    </tr>
</table>

<br>

<table width="600">
    <tr>
        <td align="center">
            <input type="button" value="確定" onclick="doUpdate()">
            <input type="button" value="取消" onclick="backToList()">
        </td>
    </tr>
</table>
</form>


<#if pfbxBankVos??>
<br><br>
銀行資料:<br>
<table width="1100" class="table01">
    <tr>
        <th class="td01" style="width: 50px;">id</th>
        <th class="td01" style="width: 200px;">銀行名稱</th>
        <th class="td01" style="width: 200px;">銀行帳號</th>
        <th class="td01" style="width: 100px;">主要銀行</th>
        <th class="td01" style="width: 500px;">審核</th>
        <th class="td01" style="width: 50px;">附件歸擋</th>
    </tr>		
	<#list pfbxBankVos as vo>
	<tr>
        <td class="td02" style="text-align:center">${(vo_index+1)!}</td>
        <td class="td02" style="text-align:center">${vo.bankName!}</td>		            
        <td class="td02" style="text-align:center">${vo.accountNumber!}</td>
        <td class="td02" style="text-align:center">${vo.isMainBank!}</td>
        <td class="td02" style="text-align:left">
			<select id="doUpBSel_${vo.id}" voId="${vo.id}" class="doUpBstatusSEL">
				<#list enumCheckStatus as enumCheckStatus>
					<option value="${enumCheckStatus.status}" <#if (enumCheckStatus.status==vo.checkStatus)>selected="selected"</#if> >${enumCheckStatus.cName}</option>
				</#list>
			</select>
			<input type="text" name="" placeholder="說明原因" id="doUpBText_${vo.id}" value="${vo.checkNote!}" style="display: <#if (vo.checkStatus=="3")>inline<#else>none</#if>; " />
			<input type="button" id="doUpBBT_${vo.id}" voId="${vo.id}" class="doUpBstatusBT" value="修改"/>
        </td>
        <td class="td02" style="text-align:left; width:100px;">
        	<input type="file" name="" />
        </td>
  	</tr>	          	
    </#list>
</table>
</#if>


<#if pfbxPersonalVos??>
<br><br>
領款人資料:<br>
<table width="1100" class="table01">
    <tr>
        <th class="td01" style="width: 50px;">id</th>
        <th class="td01" style="width: 200px;">
            <#if category == "1">
                申請人姓名
            <#elseif category == "2">
                公司名稱
            <#else>
                姓名
            </#if>
        </th>
        <th class="td01" style="width: 300px;">
            <#if category == "1">
                身分證字號
            <#elseif category == "2">
                公司統編
            <#else>
                身分證字號
            </#if></th>
        <th class="td01" style="width: 500px;">審核</th>
        <th class="td01" style="width: 50px;">
            <#if category == "1">
                發票歸檔
            <#elseif category == "2">
                收據歸檔
            <#else>
                附件歸擋
            </#if></th>
    </tr>
  	<#list pfbxPersonalVos as vo>
	<tr>
        <td class="td02" style="text-align:center">${(vo_index+1)!}</td>
        <td class="td02" style="text-align:center">${vo.name!}</td>		            
        <td class="td02" style="text-align:center">${vo.idCard!}</td>
        <td class="td02" style="text-align:left">
			<select id="doUpPSel_${vo.id}" voId="${vo.id}" class="doUpPstatusSEL">
				<#list enumCheckStatus as enumCheckStatus>
					<option value="${enumCheckStatus.status}"
                            <#if (enumCheckStatus.status==vo.checkStatus)>selected="selected"</#if>
                            >${enumCheckStatus.cName}</option>
				</#list>
			</select>
			<input type="text" name="" placeholder="說明原因" id="doUpPText_${vo.id}" value="${vo.checkNote!}" style="display: <#if (vo.checkStatus=="3")>inline<#else>none</#if>; " />
			<input type="button" id="doUpPBT_${vo.id}" voId="${vo.id}" class="doUpPstatusBT" value="修改"/>
        </td>
        <td class="td02" style="text-align:left; width:100px;">
        	<input type="file" name="" />
        </td>
  	</tr>	          	
    </#list>
</table>
</#if>

<#if pfbxAllowUrlVOs?size != 0>
<br><br>
廣告播放網址資料:<br>
<table width="1100" class="table01">
	<tr height=35 >
		<th class="td01" style="width:70px;">申請日期</th>
		<th class="td01" style="width:190px;">網站類型</th>
		<th class="td01" style="width:150px;">網站名稱</th>
		<th class="td01" style="width:220px;">網站播放網址</th>
		<th class="td01" style="width:220px;">主網域</th>
		<th class="td01" style="width:170px;">網站狀態</th>
		<th class="td01" style="width:70px;">狀態日期</th>
	</tr>
	<#list pfbxAllowUrlVOs as vo>
	<tr height=35>
		<td class="td02" style="text-align:center">${vo.createDate!}</td>
		<td class="td02" style="text-align:center">
			<#if vo.defaultType?exists && vo.defaultType = 'Y'>
				<#if vo.categoryCode?exists>${queryCategoryCodeMap[vo.categoryCode]}<#else>尚未分類</#if>
			<#else>
				<select name="code_${vo.id!}" id="code_${vo.id!}">
                <#list queryCategoryCodeMap?keys as key>
		  			<option value="${key}" <#if vo.categoryCode?exists && vo.categoryCode = key> selected</#if> >${queryCategoryCodeMap[key]}</option>
		  	    </#list>
		  		</select>
		  		<input type="button" id="doUpUCode_${vo.id}" voId="${vo.id}" class="doUpUrlCodeBT" value="修改"/>
			</#if>
		</td>
		<td class="td02" style="text-align:center">${vo.urlName!}</td>
		<td class="td02" style="text-align:left"><a href="http://${vo.url!}" target="_blank" >${vo.url!}</td>
		<td class="td02" style="text-align:left">
			<#if vo.defaultType?exists && vo.defaultType = 'Y'>
				${vo.rootDomain!}
			<#else>
				<input type="text" id="rootDomain_${vo.id!}" name="rootDomain_${vo.id!}" value="${vo.rootDomain!}" />
				<input type="button" id="doUpUT_${vo.id}" voId="${vo.id}" class="doUpUrlBT" value="修改"/>
			</#if>
		</td>
		<td class="td02" style="text-align:center">
			<#if vo.defaultType?exists && vo.defaultType = 'Y'>
				預設
			<#else>
				<select name="urlStatus_${vo.id!}" id="urlStatus_${vo.id!}" voId="${vo.id}" class="doUpUrlStatusST" style="width:110px;" >
                <#list queryUrlStatusMap?keys as key>
		  			<option value="${key}" <#if vo.urlStatus?exists && vo.urlStatus = key> selected</#if> >${queryUrlStatusMap[key]}</option>
		  	    </#list>
		  		</select>
		  		<input type="button" id="doUpUST_${vo.id}" voId="${vo.id}" class="doUpUrlStatusBT" value="修改"/>
		  		<input type="text" name="doUpURN_${vo.id}" placeholder="需填寫原因" id="doUpURN_${vo.id}" value="${vo.refuseNote!}" style="display: <#if (vo.urlStatus=="1" || vo.urlStatus=="3")>inline<#else>none</#if>;width:150px; " />
			</#if>
		</td>
		<td class="td02" style="text-align:center">${vo.updateDate!}</td>
	</tr>
	</#list>
</table>
</#if>

<input type="hidden" id="messageId" value="${message!}">

<form name="queryForm" action="pfbAccountList.html" method="post">
    <input type="hidden" name="queryStartDate" value="${queryStartDate!}">
    <input type="hidden" name="queryEndDate" value="${queryEndDate!}">
    <input type="hidden" name="queryType" value="${queryType!}">
    <input type="hidden" name="queryText" value="${queryText!}">
    <input type="hidden" name="queryStatus" value="${queryStatus!}">
    <input type="hidden" name="queryCategory" value="${queryCategory!}">
</form>
