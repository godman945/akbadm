package com.pchome.akbadm.db.service.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.pchome.akbadm.db.dao.report.ITrackingReportDAO;
import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpCodeTrackingVO;
import com.pchome.enumerate.report.EnumRetargetingCodeType;
import com.pchome.enumerate.report.EnumTrackingStatusType;
import com.pchome.enumerate.report.EnumVerifyStatusType;
import com.pchome.soft.depot.utils.JredisUtil;

public class TrackingReportService extends BaseService<PfpCodeTracking, String> implements ITrackingReportService {

	private String codeManageRediskey;
	
	/**
	 * 再行銷追蹤查詢明細
	 * @param pfpCodeTrackingVO
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<PfpCodeTrackingVO> getTrackingReportDetail(PfpCodeTrackingVO pfpCodeTrackingVO) throws Exception {
		List<Map<String, Object>> trackingList = ((ITrackingReportDAO) dao).getTrackingReportDetail(pfpCodeTrackingVO);
		
		JredisUtil jredisUtil = new JredisUtil();
		List<PfpCodeTrackingVO> trackingVOList = new ArrayList<>();
		for (Map<String, Object> dataMap : trackingList) {
			PfpCodeTrackingVO trackingVO = new PfpCodeTrackingVO();
			trackingVO.setPfdCustomerInfoName((String) dataMap.get("company_name"));
			trackingVO.setPfpCustomerInfoId((String) dataMap.get("pfp_customer_info_id"));
			trackingVO.setPfpCustomerInfoName((String) dataMap.get("customer_info_title"));
			trackingVO.setTrackingName((String) dataMap.get("tracking_name"));
			String TrackingSeq = (String) dataMap.get("tracking_seq");
			trackingVO.setTrackingSeq(TrackingSeq);
			
			// 狀態
			String status = (String) dataMap.get("tracking_status");
			if (status.equals(EnumTrackingStatusType.Close.getType())) {
				trackingVO.setStatus(EnumTrackingStatusType.Close.getChName());
			} else if (status.equals(EnumTrackingStatusType.Open.getType())) {
				trackingVO.setStatus(EnumTrackingStatusType.Open.getChName());
			} else if (status.equals(EnumTrackingStatusType.Delete.getType())) {
				trackingVO.setStatus(EnumTrackingStatusType.Delete.getChName());
			}
			
			// 驗證狀態
			//stg:pa:codecheck:traceId002
			String redisKey = codeManageRediskey + TrackingSeq;
			String redisData = jredisUtil.getKeyNew(redisKey); // 查詢此客戶redis是否有資料
			
			if (StringUtils.isBlank(redisData)) {
				trackingVO.setCertificationStatus(EnumVerifyStatusType.Unverified.getChName());
			} else {
				trackingVO.setCertificationStatus(EnumVerifyStatusType.Verified.getChName());
			}
			
			// 代碼類型
			String codeType = (String) dataMap.get("code_type");
			if (codeType.equals(EnumRetargetingCodeType.GeneralWebTracking.getType())) {
				trackingVO.setCodeType(EnumRetargetingCodeType.GeneralWebTracking.getChName());
			}else if (codeType.equals(EnumRetargetingCodeType.DynamicProductAdTracking.getType())) {
				trackingVO.setCodeType(EnumRetargetingCodeType.DynamicProductAdTracking.getChName());
			}
			
			trackingVO.setTrackingRangeDate((Integer) dataMap.get("tracking_range_date"));
			
			trackingVOList.add(trackingVO);
		}
		return trackingVOList;
	}

	public void setCodeManageRediskey(String codeManageRediskey) {
		this.codeManageRediskey = codeManageRediskey;
	}

}