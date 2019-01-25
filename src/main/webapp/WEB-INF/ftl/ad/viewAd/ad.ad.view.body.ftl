<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="cont">
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告明細
	</h2>
	<div class="grtba borderbox">
		<div style="clear:both;background:#e4e3e0">
		    <form method="post" id="searchForm" name="searchForm">
				<table>
					<tr>
						<td>日期</td>
						<td>
							<select id="dateType" name="dateType" >
								<option value="adActionDate" <#if dateType?exists && dateType == "adActionDate">selected</#if>>走期</option>
								<option value="adResult" >成效</option>
							</select>
				            <input id="IT_dateRange" readonly="true"/> 
				            <img src="<@s.url value="/" />html/img/icon_cal.gif" border="0" align="absmiddle" id="dateRangeSelect"/>
						</td>
						<td>會員帳號</td>
						<td>
				            <input type="text" id="userAccount" name="userAccount" value="${userAccount!}"/> 
						</td>
					</tr>
					<tr>
						<td>廣告狀態</td>
						<td>
		                    <select id="searchAdStatus" name="searchAdStatus">
		                		<option value="" <#if searchAdStatus == "">selected</#if>>全部</option>
		                		<option value="1" <#if searchAdStatus == "1">selected</#if>>未審核</option>
		                		<option value="2" <#if searchAdStatus == "2">selected</#if>>審核中-pass</option>
		                		<option value="13" <#if searchAdStatus == "13">selected</#if>>審核中-reject</option>
		                		<option value="3" <#if searchAdStatus == "3">selected</#if>>已拒絕</option>
		                		<option value="4" <#if searchAdStatus == "4">selected</#if>>開啟</option>
		                		<option value="9" <#if searchAdStatus == "5">selected</#if>>已暫停</option>
		                		<option value="10" <#if searchAdStatus == "10">selected</#if>>已關閉</option>
		                		<option value="6" <#if searchAdStatus == "6">selected</#if>>違規下架</option>
		                    </select>
						</td>
						<td>廣告類型</td>
						<td>
		                    <select id="searchType" name="searchType">
		                    		<option value="" <#if searchType == "">selected</#if> >全部</option>
		                    	<#list searchAdType as adType>
		                    		<option value="${adType.type!}" <#if adType.type == searchType?number>selected</#if>>${adType.chName!}</option>
		                        </#list>
		                    </select>
						</td>
					</tr>
					<tr>
						<td>廣告明細</td>
						<td colspan=>
				            <input type="text" id="keyword" name="keyword" /> 
						</td>
						<td>廣告編號</td>
						<td>
							<div class="cala01">
					            <input type="text" id="adSeq" name="adSeq" /> 
					        </div>
						</td>
					</tr>
					<tr>
						<td valign="top"><div style="height:3px;"></div>裝置</td>
						<td valign="top">
						    <select id="adPvclkDevice" name="adPvclkDevice"> 
						        <#list adPvclkDeviceMap?keys as skey>
						  		    <option value="${skey}">${adPvclkDeviceMap[skey]}</option>
						  	    </#list>
					      	</select>
						</td>
						<td valign="top"><div style="height:3px;"></div>廣告類別</td>
						<td valign="top">
							<div id="divAdCategoryCode"  style="width:150px;">
								<select class="adCategory" id="adCategory" onchange ="changeCategoryItems('adCategory');">
	        		 				<option value="">全部廣告類別</option>
	        					</select>
	        					</div>
							<input type="hidden" id="adCategoryCode" name="adCategoryCode" >
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: center">
							<input type="button" id="search" name="search" value="查詢">
							<input type="reset" id="clean" name="clean" value="清除">
							<input type="button" value="下載" onclick="doDownlaod()" />
						</td>
					</tr>
				</table>
			</form>
		</div>
        
        <div style="clear:both;height:10px"></div>
        
        <@t.insertAttribute name="dateRangeSelect" />            

		<div style="clear:both;height:50%"></div>

		<div id="tableList"></div>

	</div>

	</div>
</div> 
<input type="hidden" id="adGroupSeq" name="adGroupSeq" value="${adGroupSeq!}" />
<input type="hidden" id="adclkDevice" name="adclkDevice" value="${adclkDevice!}" />
  