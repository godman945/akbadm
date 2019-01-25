<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />PFB 無效流量扣款查詢</h2>

<form action="pfbxInvalidTrafficQuery.html" method="post">

<table width="750">
    <tr>
        <td>開始日期 : 
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly/>

		</td>
		<td>結束日期 : 
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly/>
		</td>
	</tr>
	<tr>
		<td>結算月份 :
			 <input type="text" id="closeDate" name="closeDate" value="${closeDate!}"  readonly/>
		</td>
		<td>PFB帳號 :
			<input type="text" id="pfbxCustomerInfoId" name="pfbxCustomerInfoId" value="${pfbxCustomerInfoId!}" />
		</td>
	</tr>
	<tr>
		<td>無效類別 :
			<select id="selectType" name="selectType" style="float:none;">
			<option value="" >全部類別</option> 
		        <#list selectTypeMap?keys as skey>
		  		    <option value="${skey}" <#if selectType == skey>selected</#if> >${selectTypeMap[skey]}</option>
		  	    </#list>
	      	</select>
		</td>
		<td>
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
            &nbsp;
            <input type="button" value="下載" onclick="doDownlaod()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if voList?exists && (voList?size>0)>

<div>
	<span class="pages">
		<@t.insertAttribute name="page" />
	</span>			
</div>
<table id="tableView" class="table01 tablesorter" style="width:1600px;word-wrap:break-word;word-break:break-all">
   <thead>
	    <tr>
	    	<th class="td09" style="width:100px">日期</td>
	        <th class="td09" style="width:150px">帳戶編號</td>
	        <th class="td09" style="width:200px">無效類別</td>
	        <th class="td09" style="width:500px">無效原因</td>
	        <th class="td09" style="width:100px">無效點擊費用</td>
	        <th class="td09" style="width:100px">無效分潤</td>
	        <th class="td09" style="width:100px">明細</td>
	        <th class="td09" style="width:100px">編輯</td>
	        <th class="td09" style="width:100px">結算日期</td>
	        <th class="td09" style="width:100px">新增日期</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    	<tr>
		        <td class="td09">${data.invDate!}</td>
		        <td class="td09">${data.pfbId!}</td>
		        <td class="td09">${data.invType!}</td>
		        <td class="td09">${data.invNote!}</td>
		        <td class="td10">$ ${data.invPrice!}</td>
		        <td class="td10">$ ${data.invPfbBonus!}</td>
		        <td class="td09"><a onclick="fundDetalView('${data.invId!}');">明細</a></td>
		        <td class="td09"><#if data.deleteFlag == 'Y'><input type="button" value="刪除" onclick="deleteTraffic('${data.invId!}')" /></#if></td>
		        <td class="td09">${data.closeDate!}</td>
		        <td class="td09">${data.updateDate!}</td>
		    </tr>
	    </#list>
	</tbody>
	<tr>
    	<td class="td09" colspan="4" style="background-color:#99FFFF;" >總計：${totalCount!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;">$ ${totalTrafficVO.invPrice!}</td>
    	<td class="td10" style="background-color:#99FFFF;">$ ${totalTrafficVO.invPfbBonus!}</td>
    	<td class="td09" style="background-color:#99FFFF;" ></td>
    	<td class="td09" style="background-color:#99FFFF;" ></td>
    	<td class="td09" style="background-color:#99FFFF;" ></td>
    	<td class="td09" style="background-color:#99FFFF;" ></td>
    </tr>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>
<div style="display:none;">
	<div id="invalidTrafficDetalDiv">
	
	</div>
</div>