package com.pchome.akbadm.db.service.manager;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.manager.ManagerListVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.enumerate.manager.EnumChannelCategory;

public interface IAdmManagerDetailService extends IBaseService<AdmManagerDetail, Integer>{
	
	public AdmManagerDetail findAdmManagerDetail(String memberId, String channelCategory);

	public AdmManagerDetail findExistAdmManagerDetail(String memberId, String channelCategory);
	
	public AdmManagerDetail findLoginAdmManagerDetail(String memberId, String channelCategory);
	
	public List<ManagerListVO> findAdmManagerDetails(String system);
	
	public ManagerVO findAdmManagerDetail(int managerId);
	
	public Integer deleteAdmManagerDetail(String memberId, String channelCategory);
}
