package com.pchome.akbadm.db.service.manager;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.manager.IAdmChannelAccountDAO;
import com.pchome.akbadm.db.dao.manager.IAdmManagerDetailDAO;
import com.pchome.akbadm.db.pojo.AdmChannelAccount;
import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.manager.ManagerListVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerPrivilege;
import com.pchome.enumerate.manager.EnumManagerStatus;

public class AdmManagerDetailService extends BaseService<AdmManagerDetail, Integer> implements IAdmManagerDetailService{

	private IAdmChannelAccountDAO admChannelAccountDAO;
	
	public AdmManagerDetail findAdmManagerDetail(String memberId, String channelCategory) {
		List<AdmManagerDetail> list = ((IAdmManagerDetailDAO)dao).findAdmManagerDetail(memberId, channelCategory);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public AdmManagerDetail findExistAdmManagerDetail(String memberId, String channelCategory) {
		
		List<AdmManagerDetail> list = ((IAdmManagerDetailDAO)dao).findExistAdmManagerDetail(memberId, channelCategory);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public AdmManagerDetail findLoginAdmManagerDetail(String memberId, String channelCategory) {
		
		List<AdmManagerDetail> list = ((IAdmManagerDetailDAO)dao).findLoginAdmManagerDetail(memberId, channelCategory);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public List<ManagerListVO> findAdmManagerDetails(String system) {
		
		List<ManagerListVO> vos = null;
		List<AdmManagerDetail> list = ((IAdmManagerDetailDAO)dao).findAdmManagerDetails(system);
		
		if(!list.isEmpty()){
			
			vos = new ArrayList<ManagerListVO>();
			
			for(AdmManagerDetail managerDetail:list){
				
				ManagerListVO vo = new ManagerListVO();
				
				vo.setId(String.valueOf(managerDetail.getId()));
				
				for(EnumChannelCategory channel:EnumChannelCategory.values()){
					if(channel.getCategory().equals(managerDetail.getManagerChannel())){
						vo.setSystem(channel.getChName());
					}
				}
				
				vo.setMemberId(managerDetail.getMemberId());
				vo.setChName(managerDetail.getManagerName());
				
				for(EnumManagerPrivilege manager:EnumManagerPrivilege.values()){
					if(manager.getPrivilege().equals(managerDetail.getManagerPrivilege())){
						vo.setPrivilegeName(manager.getChName());
						vo.setPrivilegeId(manager.getPrivilege());
					}
				}
				
				for(EnumManagerStatus manager:EnumManagerStatus.values()){
					
					if(manager.getStatus().equals(managerDetail.getManagerStatus())){
						vo.setStatus(manager.getChName());
					}
				}
								
				vo.setUpdateDate(managerDetail.getUpdateDate());
				vo.setCreateDate(managerDetail.getCreateDate());
				
				vos.add(vo);
			}
		}
		return vos;
	}
	
	public ManagerVO findAdmManagerDetail(int managerId) {
		
		List<AdmManagerDetail> list = ((IAdmManagerDetailDAO)dao).findAdmManagerDetail(managerId);
		ManagerVO vo = null;
		
		if(!list.isEmpty()){
			
			vo = new ManagerVO();
			
			for(EnumChannelCategory channel:EnumChannelCategory.values()){
				if(channel.getCategory().equals(list.get(0).getManagerChannel())){
					vo.setSystem(channel.getChName());
					vo.setSystemId(channel.getCategory());
				}
			}
			
			vo.setMemberId(list.get(0).getMemberId());
			vo.setName(list.get(0).getManagerName());			
			vo.setPrivilege(list.get(0).getManagerPrivilege());			
			vo.setStatus(list.get(0).getManagerStatus());
			
			List<AdmChannelAccount> admChannelAccounts = admChannelAccountDAO.findAdmChannelAccount(vo.getMemberId(), vo.getSystemId());
			
			if(!admChannelAccounts.isEmpty()){
				
				List<String> customerInfoIds = new ArrayList<String>();
				
				for(AdmChannelAccount account:admChannelAccounts){
					
					customerInfoIds.add(account.getAccountId());
				}
				
				vo.setCustomerInfoIds(customerInfoIds);
			}
			
		}
		
		return vo;
	}

	
	public Integer deleteAdmManagerDetail(String memberId, String channelCategory) {
		return ((IAdmManagerDetailDAO)dao).deleteAdmManagerDetail(memberId, channelCategory);
	}
	
	public void setAdmChannelAccountDAO(IAdmChannelAccountDAO admChannelAccountDAO) {
		this.admChannelAccountDAO = admChannelAccountDAO;
	}
	
	
}
