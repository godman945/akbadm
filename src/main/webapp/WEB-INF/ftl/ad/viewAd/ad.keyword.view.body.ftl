<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="cont">
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />關鍵字廣告
	</h2>
	<div class="grtba borderbox">
		<div style="clear:both;background:#e4e3e0">
		    <form method="post" id="searchForm" name="searchForm">
				<table width="75%">
					<tr>
						<td>日期</td>
						<td>
							<select id="dateType" name="dateType" disabled>
								<option value="adActionDate" <#if dateType?exists && dateType == "adActionDate">selected</#if>>走期</option>
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
						<td>關鍵字狀態</td>
						<td>
		                    <select id="searchAdStatus" name="searchAdStatus">
		                		<option value="" <#if searchAdStatus == "">selected</#if>>全部</option>
		                		<option value="4" <#if searchAdStatus?exists && searchAdStatus == "4">selected</#if>>開啟</option>
		                		<option value="9" <#if searchAdStatus?exists && searchAdStatus == "9">selected</#if>>已暫停</option>
		                		<option value="10" <#if searchAdStatus?exists && searchAdStatus == "10">selected</#if>>已關閉</option>
		                    </select>
						</td>
						<td>裝置</td>
						<td>
						    <select id="adKeywordPvclkDevice" name="adKeywordPvclkDevice"> 
						        <#list adKeywordPvclkDeviceMap?keys as skey>
						  		    <option value="${skey}">${adKeywordPvclkDeviceMap[skey]}</option>
						  	    </#list>
					      	</select>
						</td>
					</tr>
					<tr>
						<td>關鍵字名稱</td>
						<td>
				            <input type="text" id="keyword" name="keyword" /> 
						</td>
						<!-- <td>廣告類型</td>
						<td>
		                    <select id="searchType" name="searchType">
		                    	<#list searchAdType as adType>
		                    		<option value="${adType.type!}" <#if adType.type == searchType?number>selected</#if>>${adType.chName!}</option>
		                        </#list>
		                    </select>
						</td> -->
					</tr>
					<tr>
						<td colspan="4" style="text-align: center">
							<input type="button" id="search" name="search" value="查詢">
							<input type="reset" id="clean" name="clean" value="清除">
						</td>
					</tr>
				</table>
			</form>
		</div>
        
        <div style="clear:both;height:10px"></div>
        
        <@t.insertAttribute name="dateRangeSelect" />            

		<div style="clear:both;height:50%"></div>

		<div id="tableList" style="max-width:1000px;"></div>

	</div>
</div>
 
<input type="hidden" id="adGroupSeq" name="adGroupSeq" value="${adGroupSeq!}" />
<input type="hidden" id="adclkDevice" name="adclkDevice" value="${adclkDevice!}" />
<input type="hidden" id="adExcludeKeywordSeq" name="adExcludeKeywordSeq" />
<input type="hidden" id="adExcludeKeywordStatus" name="adExcludeKeywordStatus" />