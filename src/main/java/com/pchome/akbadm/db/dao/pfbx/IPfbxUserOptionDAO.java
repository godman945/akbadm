package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserOption;

public interface IPfbxUserOptionDAO extends IBaseDAO<PfbxUserOption, String> {
	
	public List<PfbxUserOption> getSYSBypfbId(String pfbId);
	
    public List<PfbxUserOption> selectPfbxUserOptionByStatus(String status);
}
