package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.IPfbxUserOptionDAO;
import com.pchome.akbadm.db.pojo.PfbxUserOption;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxUserOptionService extends BaseService<PfbxUserOption, String> implements IPfbxUserOptionService {
	
	public List<PfbxUserOption> getSYSBypfbId(String pfbId)
	{
		return ((IPfbxUserOptionDAO) dao).getSYSBypfbId(pfbId);
	}
	
    public List<PfbxUserOption> selectPfbxUserOptionByStatus(String status) {
        return ((IPfbxUserOptionDAO) dao).selectPfbxUserOptionByStatus(status);
    }
}
