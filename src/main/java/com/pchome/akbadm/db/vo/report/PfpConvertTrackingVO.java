package com.pchome.akbadm.db.vo.report;

public class PfpConvertTrackingVO {

	int pageNo;	
	int pageSize;
	String status;
	String pfdCustomerInfoId;
	String pfdCustomerInfoName;
	String pfpCustomerInfoId;
	String pfpCustomerInfoName;
	
	String convertSeq;
	String convertName;
	String paId;
	String convertType;
	String clickRangeDate;
	String impRangeDate;
	String convertClass;	
	String convertPriceType;
	String convertPrice;
	String convertStatus;
	String convertStatusDesc;
	String convertBelong;
	String convertNumType;
	String convertRuleNum;
	String verifyStatus;
	String convertTypeDesc;
	String convertClassDesc;	
	String transConvertPrice;		//pfp_code_convert_trans的 convert_price
	String transCKConvertCount;		//pfp_code_convert_trans的 ck convert_count
	String transPVConvertCount;		//pfp_code_convert_trans的 pv convert_count
	String transAllConvertCount;	//pfp_code_convert_trans的 ck+pv convert_count
	String transSumCKConvertCount;	//加總全部 pfp_code_convert_trans的 ck convert_count
	String transSumPVConvertCount;	//加總全部 pfp_code_convert_trans的 pv convert_count
	String transSumAllConvertCount;	//加總全部 pfp_code_convert_trans的 ck+pv convert_count
	String ckStartDate;
	String ckEndDate;
	String pvStartDate;
	String pvEndDate;
	
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	public String getPfdCustomerInfoName() {
		return pfdCustomerInfoName;
	}
	public void setPfdCustomerInfoName(String pfdCustomerInfoName) {
		this.pfdCustomerInfoName = pfdCustomerInfoName;
	}
	public String getPfpCustomerInfoName() {
		return pfpCustomerInfoName;
	}
	public void setPfpCustomerInfoName(String pfpCustomerInfoName) {
		this.pfpCustomerInfoName = pfpCustomerInfoName;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getConvertSeq() {
		return convertSeq;
	}
	public void setConvertSeq(String convertSeq) {
		this.convertSeq = convertSeq;
	}
	public String getConvertName() {
		return convertName;
	}
	public void setConvertName(String convertName) {
		this.convertName = convertName;
	}
	public String getPaId() {
		return paId;
	}
	public void setPaId(String paId) {
		this.paId = paId;
	}
	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}
	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}
	public String getConvertType() {
		return convertType;
	}
	public void setConvertType(String convertType) {
		this.convertType = convertType;
	}
	public String getClickRangeDate() {
		return clickRangeDate;
	}
	public void setClickRangeDate(String clickRangeDate) {
		this.clickRangeDate = clickRangeDate;
	}
	public String getImpRangeDate() {
		return impRangeDate;
	}
	public void setImpRangeDate(String impRangeDate) {
		this.impRangeDate = impRangeDate;
	}
	public String getConvertClass() {
		return convertClass;
	}
	public void setConvertClass(String convertClass) {
		this.convertClass = convertClass;
	}
	public String getConvertPriceType() {
		return convertPriceType;
	}
	public void setConvertPriceType(String convertPriceType) {
		this.convertPriceType = convertPriceType;
	}
	public String getConvertPrice() {
		return convertPrice;
	}
	public void setConvertPrice(String convertPrice) {
		this.convertPrice = convertPrice;
	}
	public String getConvertStatus() {
		return convertStatus;
	}
	public void setConvertStatus(String convertStatus) {
		this.convertStatus = convertStatus;
	}
	public String getConvertStatusDesc() {
		return convertStatusDesc;
	}
	public void setConvertStatusDesc(String convertStatusDesc) {
		this.convertStatusDesc = convertStatusDesc;
	}
	public String getConvertBelong() {
		return convertBelong;
	}
	public void setConvertBelong(String convertBelong) {
		this.convertBelong = convertBelong;
	}
	public String getConvertNumType() {
		return convertNumType;
	}
	public void setConvertNumType(String convertNumType) {
		this.convertNumType = convertNumType;
	}
	public String getConvertRuleNum() {
		return convertRuleNum;
	}
	public void setConvertRuleNum(String convertRuleNum) {
		this.convertRuleNum = convertRuleNum;
	}
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	public String getConvertTypeDesc() {
		return convertTypeDesc;
	}
	public void setConvertTypeDesc(String convertTypeDesc) {
		this.convertTypeDesc = convertTypeDesc;
	}
	public String getConvertClassDesc() {
		return convertClassDesc;
	}
	public void setConvertClassDesc(String convertClassDesc) {
		this.convertClassDesc = convertClassDesc;
	}
	public String getTransConvertPrice() {
		return transConvertPrice;
	}
	public void setTransConvertPrice(String transConvertPrice) {
		this.transConvertPrice = transConvertPrice;
	}
	public String getTransCKConvertCount() {
		return transCKConvertCount;
	}
	public void setTransCKConvertCount(String transCKConvertCount) {
		this.transCKConvertCount = transCKConvertCount;
	}
	public String getTransPVConvertCount() {
		return transPVConvertCount;
	}
	public void setTransPVConvertCount(String transPVConvertCount) {
		this.transPVConvertCount = transPVConvertCount;
	}
	public String getTransAllConvertCount() {
		return transAllConvertCount;
	}
	public void setTransAllConvertCount(String transAllConvertCount) {
		this.transAllConvertCount = transAllConvertCount;
	}
	public String getTransSumCKConvertCount() {
		return transSumCKConvertCount;
	}
	public void setTransSumCKConvertCount(String transSumCKConvertCount) {
		this.transSumCKConvertCount = transSumCKConvertCount;
	}
	public String getTransSumPVConvertCount() {
		return transSumPVConvertCount;
	}
	public void setTransSumPVConvertCount(String transSumPVConvertCount) {
		this.transSumPVConvertCount = transSumPVConvertCount;
	}
	public String getTransSumAllConvertCount() {
		return transSumAllConvertCount;
	}
	public void setTransSumAllConvertCount(String transSumAllConvertCount) {
		this.transSumAllConvertCount = transSumAllConvertCount;
	}
	public String getCkStartDate() {
		return ckStartDate;
	}
	public void setCkStartDate(String ckStartDate) {
		this.ckStartDate = ckStartDate;
	}
	public String getCkEndDate() {
		return ckEndDate;
	}
	public void setCkEndDate(String ckEndDate) {
		this.ckEndDate = ckEndDate;
	}
	public String getPvStartDate() {
		return pvStartDate;
	}
	public void setPvStartDate(String pvStartDate) {
		this.pvStartDate = pvStartDate;
	}
	public String getPvEndDate() {
		return pvEndDate;
	}
	public void setPvEndDate(String pvEndDate) {
		this.pvEndDate = pvEndDate;
	}

	
	
}