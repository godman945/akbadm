package com.pchome.akbadm.db.service.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.order.PfpRefundOrderReleaseDAO;
import com.pchome.akbadm.db.pojo.PfpRefundOrderRelease;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.PfpRefundOrderReleaseVo;

public class PfpRefundOrderReleaseService extends BaseService<PfpRefundOrderRelease, String> implements IPfpRefundOrderReleaseService {
	
	public int findPfpRefundOrderReleaseCount(Map<String, String> conditionsMap) throws Exception {
		return ((PfpRefundOrderReleaseDAO) dao).findPfpRefundOrderRelease(conditionsMap).size();
	}
	
	public List<PfpRefundOrderReleaseVo> findPfpRefundOrderRelease(Map<String, String> conditionsMap) throws Exception{
		List<Object> list = ((PfpRefundOrderReleaseDAO) dao).findPfpRefundOrderRelease(conditionsMap);
		
		List<PfpRefundOrderReleaseVo> resultData = new ArrayList<PfpRefundOrderReleaseVo>();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date current = new Date();
		String today = sdFormat.format(current);
		
		for (int i=0; i< list.size(); i++) {

			Object[] objArray = (Object[]) list.get(i);
			
			PfpRefundOrderReleaseVo queryDataVo = new PfpRefundOrderReleaseVo();
			queryDataVo.setReleaseSeq(Integer.toString((Integer)objArray[12]));		//退款審核序號
			queryDataVo.setCustomerInfoId((String) objArray[0]);					//「帳戶編號」
			queryDataVo.setCustomerInfoTitle((String) objArray[11]);				//「帳戶名稱」
			queryDataVo.setBillingId((String) objArray[1]);							//金流「訂單編號」
			queryDataVo.setNotifyDate(objArray[2].toString());						//金流「訂單日期」
			queryDataVo.setOrderPriceTax((float)objArray[3]+(float)objArray[4]);	//「訂單金額」
			queryDataVo.setOrderRemainTax((float)objArray[6]+(float)objArray[7]);	//「可退款金額」
			queryDataVo.setBillingPayStatus((String)objArray[5]);					//「付款方式」
			queryDataVo.setRefundStatus((String)objArray[8]);						//「退款狀態」
			queryDataVo.setBillingStatus((String)objArray[5]);						//「金流狀態」
			queryDataVo.setApplyTime(objArray[9].toString());						//申請退款日
			queryDataVo.setRejectReason((String)objArray[13]);						//拒絕理由
			queryDataVo.setRefundPriceTax((float)objArray[14]);						//退款金額(含稅)
			
			long diffDay =  getDateIntervalCount( today, objArray[9].toString().substring(0, 10) ); //計算申請退款日是否超過1天
			queryDataVo.setOverOneDay(diffDay > 0 ? "Y" : "N");						//申請退款日是否超過1天
			
			resultData.add(queryDataVo);
		}
		
		return resultData;
	}

	//更新審核後的狀態
	public void updatePfpRefundOrderReleaseStatus(String status, String seq, String verifyUserId, String rejectReason) throws Exception {
		((PfpRefundOrderReleaseDAO) dao).updatePfpRefundOrderReleaseStatus(status, seq, verifyUserId, rejectReason);
	}
	
	public long getDateIntervalCount(String endDate, String startDate) throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
		return ((dformat.parse(endDate).getTime() - dformat.parse(startDate).getTime()) / (1000*60*60*24));
	}
	
}
