<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<div  style="text-align:center;">
	<div style="font-size:24px;font-weight: bold;" >全部:${totalClick!}(PC:${tClick!}，手機:${fClick!}，無cookie:${nClick!})，邊緣無效:${iClick!}。</div>
	<div>
		<img src="pfbSizeReportImg.html?pfbxCustomerInfoId=${pfbxCustomerInfoId!}&adPvclkDate=${adPvclkDate!}&size=${size!}" >
	</div>
</div>
<br/>

