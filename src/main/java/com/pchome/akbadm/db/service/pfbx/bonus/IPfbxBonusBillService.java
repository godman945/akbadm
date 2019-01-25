package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.rmi.bonus.PfbxBonusVo;
import org.apache.lucene.index.SegmentInfos;

public interface IPfbxBonusBillService extends IBaseService<PfbxBonusBill,Integer>{
	
	public List<PfbxBonusVo> getPfbxBonusVoList(String keyword, String category, String status);
	
	public PfbxBonusVo findPfbxBonusVo(String pfbId);

	public PfbxBonusBill getpfbxBonusBillByPfbId(String pfbId);

	public PfbxBonusBill findPfbxBonusBill(String pfbId);
}
