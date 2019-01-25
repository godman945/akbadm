<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="lbpopa" id="dateSelectDialog"  style="display:none; cursor: default"> 

請選擇報表顯示的日期
<div class="calb">

	開始日期:<input value="${startDate}" id="startDate" readonly="true"/>
	<p> 
	結束日期:<input value="${endDate}" id="endDate" readonly="true"/>
	</div>
<p>
或者，請選擇以下的預設日期範圍
<p>
    
    <select  id="selectRange" name="selectRange" >      					
   		<#list dateSelectMap?keys as itemKey>
			<#assign item = dateSelectMap[itemKey]>
				<option value="${item}">${itemKey}</option>
	    </#list>      					
     </select>
	
<p>
	<input type="button" id="dateSelectOk" value="確 定" /> 
	&nbsp; 
	<input type="button" id="dateSelectCancel" value="取 消" /> 

</div> 
