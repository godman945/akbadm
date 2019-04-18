package com.pchome.akbadm.db.service.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.report.IPfpCodeConvertDAO;
import com.pchome.akbadm.db.pojo.PfpCodeConvert;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingRuleVO;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;
import com.pchome.enumerate.convert.EnumConvertClassType;
import com.pchome.enumerate.convert.EnumConvertStatusType;
import com.pchome.enumerate.convert.EnumConvertType;
import com.pchome.enumerate.report.EnumVerifyStatusType;
import com.pchome.soft.depot.utils.JredisUtil;

public class PfpCodeConvertService extends BaseService<PfpCodeConvert, String> implements IPfpCodeConvertService {

	private String codeManageRediskey;
	private IPfpCodeConvertRuleService pfpCodeConvertRuleService;
	
	
	public List<PfpConvertTrackingVO> findPfpCodeConvertList(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		List<Map<String,Object>> convertTrackingLists = ((IPfpCodeConvertDAO)dao).findPfpCodeConvertList(convertTrackingVO);
		List<PfpConvertTrackingVO> dataList = new ArrayList<>();
		
		if( (!convertTrackingLists.isEmpty()) && (convertTrackingLists.size()>0) ){
			for (Object object : convertTrackingLists) {
				PfpConvertTrackingVO convertTrackingBean = new PfpConvertTrackingVO();
				Map obj = (Map) object;
				convertTrackingBean.setConvertSeq(obj.get("convert_seq").toString());	
				convertTrackingBean.setClickRangeDate(obj.get("click_range_date").toString());
				convertTrackingBean.setImpRangeDate(obj.get("imp_range_date").toString());
				
				dataList.add(convertTrackingBean);
			}
		}
		return dataList;
	}
	
	
	public PfpConvertTrackingVO getConvertTrackingList(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		List<Map<String,Object>> convertTrackingLists = ((IPfpCodeConvertDAO)dao).getConvertTrackingList(convertTrackingVO);
		PfpConvertTrackingVO convertTrackingBean = new PfpConvertTrackingVO();
		
		if( (!convertTrackingLists.isEmpty()) && (convertTrackingLists.size()>0) ){
			for (Object object : convertTrackingLists) {
				Map obj = (Map) object;
				
				convertTrackingBean.setPfdCustomerInfoName(obj.get("company_name").toString());
				convertTrackingBean.setPfpCustomerInfoId(obj.get("pfp_customer_info_id").toString());
				convertTrackingBean.setPfpCustomerInfoName(obj.get("customer_info_title").toString());
				convertTrackingBean.setConvertName(obj.get("convert_name").toString());
				convertTrackingBean.setConvertSeq(obj.get("convert_seq").toString());	
				convertTrackingBean.setConvertStatus(obj.get("convert_status").toString());	//轉換狀態(0:關閉;1:開啟;2:刪除)
				convertTrackingBean.setConvertClass(obj.get("convert_class").toString());	//轉換分類(1.查看內容 2.搜尋 3.加到購物車 4.加到購物清單 5.開始結帳 6.新增付款資料 7.購買 8.完成註冊)
				convertTrackingBean.setClickRangeDate(obj.get("click_range_date").toString());	//點擊追蹤天數
				convertTrackingBean.setImpRangeDate(obj.get("imp_range_date").toString());		//曝光追蹤天數
				convertTrackingBean.setTransConvertPrice(obj.get("trans_convert_price").toString());		//轉換價值
				convertTrackingBean.setTransCKConvertCount(obj.get("trans_ck_convert_count").toString());	//點擊後轉換數
				convertTrackingBean.setTransPVConvertCount(obj.get("trans_pv_convert_count").toString());	//瀏覽後轉換數
				convertTrackingBean.setTransAllConvertCount( Integer.toString(Integer.parseInt(obj.get("trans_ck_convert_count").toString())+Integer.parseInt(obj.get("trans_pv_convert_count").toString())) );//所有轉換(點擊後轉換數+瀏覽後轉換數)
				convertTrackingBean.setConvertRuleNum(obj.get("convert_rule_num").toString());//轉換條件數量(0 沒有條件;有條件是 count(rule 數量))
				convertTrackingBean.setConvertType(obj.get("convert_type").toString());	//convert_type 轉換類型(1.標準轉換追蹤(預設)2.自訂轉換追蹤條件)
				
				
				JredisUtil jredisUtil = new JredisUtil();
				int convertRuleNum = Integer.parseInt(convertTrackingBean.getConvertRuleNum());
				if ( convertRuleNum > 0){
					//自訂轉換追蹤
					//認證狀態(已認證、未認證)prd:pa:codecheck:CAC20181112000000001:RLE20180724000000001
					int count =0;
					List<PfpConvertTrackingRuleVO>convertTrackingRuleList = pfpCodeConvertRuleService.getPfpCodeConvertRuleByCondition(convertTrackingVO);
					for (PfpConvertTrackingRuleVO convertTrackingRuleVO : convertTrackingRuleList) {
						String redisKey =codeManageRediskey+convertTrackingBean.getConvertSeq()+":"+convertTrackingRuleVO.getConvertRuleId();
						String redisData = jredisUtil.getKeyNew(redisKey); // 查詢此客戶redis是否有資料
						if (redisData!=null){
							count = count+1;
						}
					}
					
					if (convertRuleNum == count){
						convertTrackingBean.setVerifyStatus(EnumVerifyStatusType.Verified.getChName());
					}else{
						convertTrackingBean.setVerifyStatus(EnumVerifyStatusType.Unverified.getChName());						
					}
				}else{
					//標準轉換追蹤
					//認證狀態(已認證、未認證)prd:pa:codecheck:CAC20181112000000001
					String redisKey =codeManageRediskey+convertTrackingBean.getConvertSeq();
					String redisData = jredisUtil.getKeyNew(redisKey); // 查詢此客戶redis是否有資料
					if ( redisData == null ){
						convertTrackingBean.setVerifyStatus(EnumVerifyStatusType.Unverified.getChName());
					}else{
						convertTrackingBean.setVerifyStatus(EnumVerifyStatusType.Verified.getChName());
					}
				}
				
				
				//轉換狀態(0:關閉;1:開啟;2:刪除)
				for(EnumConvertStatusType convertStatusType:EnumConvertStatusType.values()){
					if( StringUtils.equals(convertStatusType.getType(), obj.get("convert_status").toString()) ){
						convertTrackingBean.setConvertStatusDesc(convertStatusType.getChName());	
						break;
					}
				}
				
				//轉換類型中文(1.標準轉換追蹤(預設)2.自訂轉換追蹤條件)
				for(EnumConvertType convertType:EnumConvertType.values()){
					if( StringUtils.equals(convertType.getType(), obj.get("convert_type").toString()) ){
						convertTrackingBean.setConvertTypeDesc(convertType.getChName());	
						break;
					}
				}
				
				//轉換分類(1.查看內容 2.搜尋 3.加到購物車 4.加到購物清單 5.開始結帳 6.新增付款資料 7.購買 8.完成註冊)
				for(EnumConvertClassType convertClassType:EnumConvertClassType.values()){
					if( StringUtils.equals(convertClassType.getType(), obj.get("convert_class").toString()) ){
						convertTrackingBean.setConvertClassDesc(convertClassType.getChName());	
						break;
					}
				}
			}
		}
		return convertTrackingBean;
	}
	
	
	public String getConvertTrackingCount(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		return ((IPfpCodeConvertDAO)dao).getConvertTrackingCount(convertTrackingVO);
	}

	
	
	
	public void setCodeManageRediskey(String codeManageRediskey) {
		this.codeManageRediskey = codeManageRediskey;
	}

	public void setPfpCodeConvertRuleService(IPfpCodeConvertRuleService pfpCodeConvertRuleService) {
		this.pfpCodeConvertRuleService = pfpCodeConvertRuleService;
	}
	
	

}