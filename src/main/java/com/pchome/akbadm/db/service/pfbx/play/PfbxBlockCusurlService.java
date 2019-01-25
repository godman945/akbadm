package com.pchome.akbadm.db.service.pfbx.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.play.IPfbxBlockCusurlDAO;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public class PfbxBlockCusurlService extends BaseService<PfbxBlockCusurl,String> implements IPfbxBlockCusurlService {
	
	public List<PfbAdUrlListVO> getBlockurls(String pfbId , String searchUrl) throws Exception
	{
		List<PfbxBlockCusurl> list = ((IPfbxBlockCusurlDAO)dao).getSYSList_By_PfbId(pfbId, searchUrl);
		List<PfbAdUrlListVO> vos = new ArrayList<PfbAdUrlListVO>();
		
		for(PfbxBlockCusurl pfbbc : list)
		{
			PfbAdUrlListVO vo = new PfbAdUrlListVO();
			vo.setDetailurl(pfbbc.getUrl());
			vo.setDetailid(pfbbc.getCuId());
			vo.setDetaildesc(pfbbc.getBoardMesg());
			vos.add(vo);
		}
		
		return vos;
	}
	
	public List<PfbxBlockCusurl> getSYSList_By_PfbId(String pfbId) throws Exception
	{
		return ((IPfbxBlockCusurlDAO)dao).getSYSList_By_PfbId(pfbId, "");
	}
	
	public int chk_oid_url(String oid , String url)
	{
		return ((IPfbxBlockCusurlDAO)dao).chk_oid_url(oid, url);
	}
	
	public String insPfbxBlockCusurl(Map blockInfo)
	{
		blockInfo.put("optionName", "SYS");
		blockInfo.put("piositionType", "-1");
		blockInfo.put("urlType", "-1");
		blockInfo.put("sizeType", "-1");
		((IPfbxBlockCusurlDAO)dao).insPfbxBlockCusurl(blockInfo);
		
		return "success";
	}
	
	public int ins_oid_url(String cuid , String oid , String url , String bordMesg)
	{
		return ((IPfbxBlockCusurlDAO)dao).ins_oid_url(cuid, oid, url, bordMesg);
	}
}
