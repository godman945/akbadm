<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="cont">
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告管理
	</h2>
	<div class="grtba borderbox">
		<div style="clear:both;background:#e4e3e0">
		    <form method="post" id="searchForm" name="searchForm">
				<table width="75%">
					<tr>
						<td>日期</td>
						<td>
							<select id="dateType" name="dateType" disabled>
								<option value="adActionDate">走期</option>
							</select>
				            <input id="IT_dateRange" readonly="true"/> 
				            <img src="<@s.url value="/" />html/img/icon_cal.gif" border="0" align="absmiddle" id="dateRangeSelect"/>
						</td>
						<td>會員帳號</td>
						<td>
				            <input type="text" id="userAccount" name="userAccount" /> 
						</td>
					</tr>
					<tr>
						<td>廣告狀態</td>
						<td>
		                    <select id="searchAdStatus" name="searchAdStatus">
		                		<option value="">全部</option>
		                		<option value="0">未完成</option>
		                		<option value="4">開啟</option>
		                		<option value="5">走期中</option>
		                		<option value="7">待播放</option>
		                		<option value="8">已結束</option>
		                		<option value="9">已暫停</option>
		                		<option value="10">已關閉</option>
		                    </select>
						</td>
						<td>廣告類型</td>
						<td>
		                    <select id="searchType" name="searchType">
		                    	<#list searchAdType as adType>
		                    		<option value="${adType.type!}">${adType.chName!}</option>
		                        </#list>
		                    </select>
						</td>
					</tr>
					<tr>
						<td>廣告名稱</td>
						<td>
				            <input type="text" id="adActionName" name="adActionName" /> 
						</td>
						<td>裝置</td>
						<td>
						    <select id="adPvclkDevice" name="adPvclkDevice"> 
						        <#list adPvclkDeviceMap?keys as skey>
						  		    <option value="${skey}">${adPvclkDeviceMap[skey]}</option>
						  	    </#list>
					      	</select>
						</td>
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

		<div id="tableList"></div>

	</div>

</div>	