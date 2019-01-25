package com.pchome.akbadm.db.dao.manager;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmManagerDetail;

public interface IAdmManagerDetailDAO extends IBaseDAO<AdmManagerDetail, Integer>{

	public List<AdmManagerDetail> findAdmManagerDetail(String memberId, String channelCategory);
	
	public List<AdmManagerDetail> findExistAdmManagerDetail(String memberId, String channelCategory);
	
	public List<AdmManagerDetail> findLoginAdmManagerDetail(String memberId, String channelCategory);
	
	public List<AdmManagerDetail> findAdmManagerDetails(String system);
	
	public List<AdmManagerDetail> findAdmManagerDetail(int managerId);
	
	public Integer deleteAdmManagerDetail(String memberId, String channelCategory);
}
