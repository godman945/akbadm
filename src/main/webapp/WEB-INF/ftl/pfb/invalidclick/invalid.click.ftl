<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.tablesorter.js"></script>
<script language="JavaScript" src="${request.contextPath}/html/js/common/reportCommon.js" ></script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />PFB 流量檢測查詢</h2>

<form action="pfbxInvalidClickQuery.html" method="post">
<!--20190513-->
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
			&nbsp;&nbsp;
			<input type="checkbox" id="groupPositionIdBox" <#if groupPositionId != "" >checked</#if> />不對版位分組
			<input type="hidden" id="groupPositionId" name="groupPositionId" value="${groupPositionId!}"  />
		</td>
	</tr>
	<tr>
		<td>查詢類別 :
			<select id="selectType" name="selectType" style="float:none;"> 
		        <#list selectTypeMap?keys as skey>
		  		    <option value="${skey}" <#if selectType == skey>selected</#if> >${selectTypeMap[skey]}</option>
		  	    </#list>
	      	</select>
		</td>
		<td>數量限制 :
			<input type="text" id="mount" name="mount" value="${mount!}" />
		</td>
	</tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
            &nbsp;
            <input type="button" value="清除" onclick="doClear()" />
        </td>
    </tr>
</table>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if tableDataList?exists && (tableDataList?size>0)>

<div>
	<span class="pages">
		<span style="float:left;">&nbsp;<input type="button" value="無效流量送出" onclick="doAdd()"></span>
		<@t.insertAttribute name="page" />
	</span>			
</div>
<table id="tableView" class="table01 tablesorter" style="width:100%;word-wrap:break-word;word-break:break-all">
   <thead>
    	<tr height="35"> 
    		<th style="border:solid;border-width:1px;">全選<input type="checkbox" onclick="selectAll_new(this)" /></th>
    		<#list tableHeadList as th>
				<th style="border:solid;border-width:1px;">${th}</th>
    		</#list>
    	</tr> 
	</thead>
    <tbody>
    	<#assign index2 = 0>
	    <#list tableDataList as td>
         <#assign index = 0>
		 <tr height="30">
		 	<td style="border:solid;border-width:1px;width:50px">
		 		<input type="checkbox" class="selectbox" onclick="select_one('${index2}',['${selectType}','${mount}','${groupPositionId}'<#list td as tdin2>,'${tdin2}'</#list>])" />
		 	</td>
			<#list td as tdin>
				<td align="${align_data[index]}" style="border:solid;border-width:1px;min-width:100px">
				<#if (selectType =='5' && index == 3 && groupPositionId =='') || (selectType =='5' && index == 2 && groupPositionId =='N')>
					<a href='${tdin}' target='_blank' >${tdin}</a>
				<#else>
					<#if tdin?length == 1 >
					  null
					<#else>
					  ${tdin}
					</#if>
				</#if>
				</td>
			<#assign index = index + 1>
 			</#list>
 	    </tr>
 	    <#assign index2 = index2 + 1>
 		</#list>
    </tbody>
</table>

<#else>

	<#if message?exists && message!="">
	
	${message!}
	
	</#if>

</#if>
<input type="hidden" id="downloadFlag" name="downloadFlag" />

</form>