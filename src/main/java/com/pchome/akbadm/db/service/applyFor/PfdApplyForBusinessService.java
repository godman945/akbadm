package com.pchome.akbadm.db.service.applyFor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.applyFor.PfdApplyForBusinessDAO;
import com.pchome.akbadm.db.dao.pfd.account.IPfdAccountDAO;
import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.PfdApplyForBusinessVO;
import com.pchome.enumerate.applyFor.EnumPfdApplyForBusiness;

public class PfdApplyForBusinessService extends BaseService<PfdApplyForBusiness, String> implements IPfdApplyForBusinessService {

	private IPfdAccountDAO pfdAccountDAO;

	public void setPfdAccountDAO(IPfdAccountDAO pfdAccountDAO) {
		this.pfdAccountDAO = pfdAccountDAO;
	}

	public int findPfdApplyForBusinessCount(Map<String, String> conditionsMap) throws Exception {
		return ((PfdApplyForBusinessDAO) dao).findPfdApplyForBusinessCount(conditionsMap);
	}

	public List<PfdApplyForBusiness> findPfdApplyForBusiness(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception {
		return ((PfdApplyForBusinessDAO) dao).findPfdApplyForBusiness(conditionsMap, pageNo, pageSize);
	}

	public List<PfdApplyForBusinessVO> findPfdApplyForBusinessVO(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		List<PfdApplyForBusinessVO> voList = new ArrayList<PfdApplyForBusinessVO>();

		List<PfdApplyForBusiness> pojoList = ((PfdApplyForBusinessDAO) dao).findPfdApplyForBusiness(conditionsMap, pageNo, pageSize);

		Map<String, String> statusMap = new HashMap<String, String>();
		for (EnumPfdApplyForBusiness status : EnumPfdApplyForBusiness.values()) {
			statusMap.put(status.getType(), status.getChName());
		}

		List<PfdCustomerInfo> pfdCustomerInfoList = pfdAccountDAO.getPfdCustomerInfoByCondition(new HashMap<String, String>());
		Map<String, String> pfdCustomerInfoMap = new HashMap<String, String>();
		PfdCustomerInfo pfdCustomerInfo = null;
		for (int i=0; i<pfdCustomerInfoList.size(); i++) {
			pfdCustomerInfo = pfdCustomerInfoList.get(i);
			pfdCustomerInfoMap.put(pfdCustomerInfo.getCustomerInfoId(), pfdCustomerInfo.getCompanyName());
		}

		for (int i=0; i<pojoList.size(); i++) {

			PfdApplyForBusiness pojo = pojoList.get(i);

			PfdApplyForBusinessVO vo = new PfdApplyForBusinessVO();
			vo.setApplyForSeq(pojo.getSeq().toString());
			vo.setPfdCustomerInfoTitle(pfdCustomerInfoMap.get(pojo.getPfdCustomerInfoId()) + "<br>(" + pojo.getPfdCustomerInfoId() + ")");
			vo.setTaxId(pojo.getTargetTaxId());
			vo.setAdUrl(pojo.getTargetAdUrl());
			String strTmp = pojo.getIllegalReason();
			String[] array = strTmp.split(";");
			List<String> illegalReasonList = new ArrayList<String>();
			for (int k=0; k<array.length; k++) {
				String[] array2 = array[k].split(",");
				String strTmp2 = "";
				if (array2[0].equals("pfp")) { //pfp
					strTmp2 = "PFP廣告系統帳戶 - " + array2[1];
				} else { //enterprise
					strTmp2 = "PChome百大企業 - " + array2[1];
				}
				//超過10筆以上就不記了
				if (k == 10) {
					break;
				}
				illegalReasonList.add(strTmp2);
			}

			vo.setIllegalReasonList(illegalReasonList);
			vo.setStatus(statusMap.get(pojo.getStatus()));
			if (pojo.getApplyForTime()!=null) {
				vo.setApplyForTime(sdf.format(pojo.getApplyForTime()));				
			}

			voList.add(vo);
		}

		return voList;
	}

	/**
	 * 更新審核後的狀態
	 */
	public void updatePfdApplyForBusinessStatus(String status, String seq, String verifyUserId,
			String rejectReason) throws Exception {
		((PfdApplyForBusinessDAO) dao).updatePfdApplyForBusinessStatus(status, seq, verifyUserId, rejectReason);
	}
	
	public List<PfdApplyForBusiness> findPfdApplyForBusinessStatus(String status) throws Exception {
		return ((PfdApplyForBusinessDAO) dao).findPfdApplyForBusinessStatus(status);
	}
}
