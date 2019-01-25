<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />PFB 惡意點擊查詢</h2>

<form action="pfbxMaliceCheckQuery.html" method="post">

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
		<td>PFB帳號 :
			<input type="text" id="pfbxCustomerInfoId" name="pfbxCustomerInfoId" value="${pfbxCustomerInfoId!}" />
		</td>
		<td>PFB版位 :
			<input type="text" id="pfbxPositionId" name="pfbxPositionId" value="${pfbxPositionId!}" />
		</td>
	</tr>
	<tr>
		<td>違規類別 :
			<select id="maliceType" name="maliceType" style="float:none;"> 
					<option value="" />全部類別</option>
		        <#list maliceTypeMap?keys as skey>
		  		    <option value="${skey}" <#if maliceType == skey>selected</#if> >${maliceTypeMap[skey]}</option>
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
<table id="tableView" class="table01 tablesorter" style="width:1925px;word-wrap:break-word;word-break:break-all">
   <thead>
	    <tr>
	    	<th class="td09" style="width:100px">日期</td>
	        <th class="td09" style="width:50px">時間</td>
	        <th class="td09" style="width:150px">類別</td>
	        <th class="td09" style="width:100px">帳戶編號</td>
	        <th class="td09" style="width:100px">版位</td>
	        <th class="td09" style="width:100px">會員帳號</td>
	        <th class="td09" style="width:200px">uuid</td>
	        <th class="td09" style="width:100px">ip</td>
	        <th class="td09" style="width:200px">referer</td>
	        <th class="td09" style="width:200px">user_agent</td>
	        <th class="td09" style="width:125px">滑鼠移動判斷</td>
	        <th class="td09" style="width:125px">滑鼠移動區域:寬</td>
	        <th class="td09" style="width:125px">滑鼠移動區域:高</td>
	        <th class="td09" style="width:125px">滑鼠點擊位置:X</td>
	        <th class="td09" style="width:125px">滑鼠點擊位置:Y</td>
	        <th class="td09" style="width:125px">無效點擊費用</td>
	    </tr>
    </thead>
    <tbody>
	    <#list voList as data>
	    	<tr>
		        <td class="td09">${data.recordDate!}</td>
		        <td class="td09">${data.recordTime!}</td>
		        <td class="td09">${data.maliceType!}</td>
		        <td class="td09">${data.pfbxCustomerInfoId!}</td>
		        <td class="td09">${data.pfbxPositionId!}</td>
		        <td class="td09">${data.memId!}</td>
		        <td class="td09">${data.uuid!}</td>
		        <td class="td09">${data.remoteIp!}</td>
		        <td class="td09">${data.referer!}</td>
		        <td class="td09">${data.userAgent!}</td>
		        <td class="td09">${data.mouseMoveFlag!}</td>
		        <td class="td09">${data.mouseAreaWidth!}</td>
		        <td class="td09">${data.mouseAreaHeight!}</td>
		        <td class="td09">${data.mouseDownX!}</td>
		        <td class="td09">${data.mouseDownY!}</td>
		        <td class="td10">$ ${data.adPrice!}</td>
		    </tr>
	    </#list>
	</tbody>
	<tr>
    	<td class="td09" colspan="15" style="background-color:#99FFFF;" >總計：${totalCount!}筆</td>
    	<td class="td10" style="background-color:#99FFFF;">$ ${totalVO.adPrice!}</td>
    </tr>	
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>
