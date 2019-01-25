package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxUserOption;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxUserOptionService extends IBaseService<PfbxUserOption, String> {
	
	public List<PfbxUserOption> getSYSBypfbId(String pfbId);
	
    public List<PfbxUserOption> selectPfbxUserOptionByStatus(String status);
}
