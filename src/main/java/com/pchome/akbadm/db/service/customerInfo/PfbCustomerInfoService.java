package com.pchome.akbadm.db.service.customerInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.customerInfo.IPfbCustomerInfoDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxUser;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfbVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.rmi.manager.PfbAccountVO;

public class PfbCustomerInfoService extends BaseService<PfbxCustomerInfo, String> implements IPfbCustomerInfoService{

	public List<PfbxCustomerInfo> findPfbValidCustomerInfo() {
		
		return ((IPfbCustomerInfoDAO) dao).findPfbValidCustomerInfo();
	}
	
	public PfbxCustomerInfo findPfbCustomerInfo(String pfbCustomerInfoId) {
		List<PfbxCustomerInfo> list = ((IPfbCustomerInfoDAO) dao).findPfbCustomerInfo(pfbCustomerInfoId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	public List<PfbAccountVO> findManagerPfbAccount(String memberId, Date startDate, Date endDate) {
		
		List<PfbAccountVO> vos = null;
		
		List<Object> list = ((IPfbCustomerInfoDAO) dao).findManagerPfbAccount(memberId, startDate, endDate);
		
		if(!list.isEmpty()){
			
			vos = new ArrayList<PfbAccountVO>();
			
			for(Object object:list){

				Object[] ob = (Object[])object;

				if(ob[0] != null){
			
					PfbAccountVO vo = new PfbAccountVO();				
					String status = ob[2].toString();
					
					vo.setPfbCustomerInfoId(ob[0].toString());
					vo.setPfbCustomerInfoName(ob[1].toString());
					
					for(EnumPfbAccountStatus pfbStatus:EnumPfbAccountStatus.values()){						
						if(pfbStatus.getStatus().equals(status)){
							vo.setPfbCustomerInfoStatus(pfbStatus.getDescription());
							break;
						}
					}
					
					vos.add(vo);
				}				
			}
			
		}
		return vos;
	}
	
	public List<ManagerPfbVO> findManagerAccount(ManagerVO managerVO) {
		
		List<PfbxCustomerInfo> list = ((IPfbCustomerInfoDAO) dao).findPfbValidCustomerInfo();
		
		List<ManagerPfbVO> vos = null;
		
		if(!list.isEmpty()){
			
			vos = new ArrayList<ManagerPfbVO>();
			
			for(PfbxCustomerInfo customerInfo:list){
				ManagerPfbVO vo = new ManagerPfbVO();
				
				vo.setPfbCustomerInfoId(customerInfo.getCustomerInfoId());
				if(StringUtils.equals(EnumPfbxAccountCategory.PERSONAL.getCategory(), customerInfo.getCategory())){
					if(!customerInfo.getPfbxUsers().isEmpty()){
						for(PfbxUser pfbxUser:customerInfo.getPfbxUsers()){
							if(pfbxUser.getPrivilegeId() == 0){
								vo.setPfbCustomerInfoName(pfbxUser.getUserName());
								break;
							}
						}
					} else {
						vo.setPfbCustomerInfoName(customerInfo.getContactName());
					}
				} else {
					vo.setPfbCustomerInfoName(customerInfo.getCompanyName());	
				}
				
				if(managerVO.getCustomerInfoIds() != null){
					
					for(String id:managerVO.getCustomerInfoIds()){
						if(id.equals(vo.getPfbCustomerInfoId())){
							vo.setIsChecked("true");
							break;
						}
					}
				}
				
				vos.add(vo);
			}
		}
		
		return vos;
	}
}
