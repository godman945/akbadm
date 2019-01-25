<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="noticepop" style="width:520px;display:block" >
	<h4><strong>排除關鍵字</strong></h4>
	
	<div class="popcont" style="height:300px;overflow:auto;">
		<b>排除關鍵字</b>：
		系統比對到這些排除關鍵字時，不會播出您的廣告。<br>
		<em>»</em>廣告:${adGroup.pfpAdAction.adActionName!}<em>›</em>分類：${adGroup.adGroupName!}<br/>
		<br/>
		<br/>
		
		<table id="keywordTableView" width="100%" border="0" cellpadding="0" cellspacing="1" class="tb01" style="margin-top:5px">
			<tr>
				<th width="73%" height="30">排除關鍵字</th>
				<th width="17%" height="30">狀態</th>
			</tr>
			<#if adExcludeKeywords?exists>
				<#list adExcludeKeywords as keywords>
				<tr>
	    			<td>${keywords.adExcludeKeyword!}</td>
	    			<td>
	    			<#if keywords.adExcludeKeywordStatus == 1>
	    				開啟
	    			<#elseif keywords.adExcludeKeywordStatus == 0>
	    				關閉
	    			<#else>
	    				暫停
	    			</#if>		
					</td>
    			</tr>
				</#list>
			</#if>
			<#if (adExcludeKeywords?size<=0)>
				<tr>
					<td colspan="3">
						此分類中尚未設定排除關鍵字。
					</td>
				</tr>
			</#if>
		</table>
	</div> 
	<a href="#" id="exit" class="popclose">關閉</a>
</div>
