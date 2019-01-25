<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="cont">
	<h2>
		<img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />廣告禮金管理
	</h2>
	<div class="grtba borderbox">
		<div style="clear:both;background:#e4e3e0">
		    <form method="post" id="searchForm" name="searchForm">
				<table width="75%">
					<tr>
						<td>活動開始日期</td>
						<td>
				            <input type="text" id="startDate" name="startDate" value="${searchStartDate!}"  readonly/>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~</td>
						<td>
				            <input type="text" id="endDate" name="endDate" value="${searchEndDate!}"  readonly/>
						</td>
					</tr>
					<tr>
						<td>禮金失效日期</td>
						<td>
				            <input type="text" id="startDate2" name="startDate2" value="${inviledStartDate!}"  readonly/>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~</td>
						<td>
				            <input type="text" id="endDate2" name="endDate2" value="${inviledEndDate!}"  readonly/>
						</td>
					</tr>
					<tr>
						<td>禮金動作</td>
						<td>
		                    <select id="payment" name="payment">
		                    	<option value="" <#if payment == "">selected</#if>>全部</option>
		                    	<#list searchPayment as payType>
		                    		<option value="${payType.status!}" <#if payType.type == payment>selected</#if>>${payType.chName!}</option>
		                        </#list>
		                    </select>
						</td>
						<td>禮金序號輸入頁</td>
						<td>
		                    <select id="giftStyle" name="giftStyle">
		                    	<option value="" <#if giftStyle == "">selected</#if>>全部</option>
		                    	<#list searchGiftStyle as gifType>
		                    		<option value="${gifType.status!}" <#if gifType.type == giftStyle>selected</#if>>${gifType.chName!}</option>
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
		<input type="button" value="新增" onclick="addFreeAction()" >
		<div id="tableList"></div>

	</div>

</div>	