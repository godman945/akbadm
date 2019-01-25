package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmPfbxBlockUrl;

public interface IAdmPfbxBlockUrlDAO extends IBaseDAO<AdmPfbxBlockUrl, String> {
	
	public List<AdmPfbxBlockUrl> getListByPfbId(String pfbId) throws Exception;
	
}
