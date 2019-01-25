<#assign s=JspTaglibs["/struts-tags"]>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/html/css/angularJs/bootstrap.min.css" />
<div class="container-fluid">
	<h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>修改優惠方案</strong></h3>
	
	<div class="container-fluid">
		<form method="post" id="modifyForm" name="modifyForm" action="updSpecial.html">
		
			<div class="form-group">
				<div class="container col-lg-6"">
					<table class="table table-striped table-bordered">
				        <tr>
							<td class="text-center" style="width:250px;"><strong>帳戶編號</strong></td>
							<td class="text-left col-lg-4">
								<input type="text" id="pfbId"  name="pfbId" value="${pfbxBonusSetSpecialVO.pfbId!}" readonly >								
							</td>
						</tr>
						<tr>
							<td class="text-center" style="width:250px;"><strong>活動名稱</strong></td>
							<td class="text-left col-lg-4">
								<input type="text" id="specialName"  name="specialName" value="${pfbxBonusSetSpecialVO.specialName!}" />	
								<span id="chkSpecialName" name="chkSpecialName" style="color:red;size:5"></span>							
							</td>
						</tr>
				        <tr>
							<td class="text-center" style="width:250px;"><strong>開始日期</strong></td>
							<td class="text-left col-lg-4">
								<input id="startDate" name="startDate" value="${pfbxBonusSetSpecialVO.startDate!}" style="cursor:default;" readonly="true"/>
								<span id="chkStartDate" name="chkStartDate" style="color:red;size:5"></span>
							</td>
						</tr>
						<tr>
							<td class="text-center" style="width:250px;"><strong>結束日期</strong></td>
							<td class="text-left col-lg-4">
								<input id="endDate" name="endDate" value="${pfbxBonusSetSpecialVO.endDate!}" style="cursor:default;" readonly="true"/>
								<span id="chkEndDate" name="chkEndDate" style="color:red;size:5"></span>
							</td>
						</tr>
						<tr>
							<td class="text-center" style="width:250px;"><strong>客戶分潤 %</strong></td>
							<td class="text-left col-lg-4">
								<input type="text" id="pfbPercent" value="${pfbxBonusSetSpecialVO.pfbPercent!}" name="pfbPercent" MaxLength="3">
								<span id="chkPfbPercent" name="chkPfbPercent" style="color:red;size:5"></span>
							</td>
						</tr>
				    </table>
				</div>				
			</div>
			<input type="hidden" id="id" name="id" value="${pfbxBonusSetSpecialVO.id!}" />
			<input type="hidden" id="pchomeChargePercent" name="pchomeChargePercent" value="${pfbxBonusSetSpecialVO.pchomeChargePercent!}" />
			<input type="hidden" id="deleteFlag" name="deleteFlag" value="${pfbxBonusSetSpecialVO.deleteFlag!}" />
			<input type="hidden" id="updateDate" name="updateDate" value="${pfbxBonusSetSpecialVO.updateDate!}" />
			<input type="hidden" id="createDate" name="createDate" value="${pfbxBonusSetSpecialVO.createDate!}" />
			<div class="container-fluid">        
		  		<label class="control-label col-lg-3">
		    		<input type="button" class="btn btn-default" style="font-weight:bold;" value="儲存" onClick="save()" />
		    		&nbsp;
		    		<input type="button" class="btn btn-default" style="font-weight:bold;" value="返回" onClick="cancerSubmit()" />
		  		</label>
			</div>
			
		</form>	
	</div>
</div>

