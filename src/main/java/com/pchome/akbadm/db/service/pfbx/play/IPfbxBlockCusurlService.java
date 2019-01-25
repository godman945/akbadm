package com.pchome.akbadm.db.service.pfbx.play;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public interface IPfbxBlockCusurlService extends IBaseService<PfbxBlockCusurl,String> {
	
	public List<PfbAdUrlListVO> getBlockurls(String pfbId , String searchUrl) throws Exception;
	
	public List<PfbxBlockCusurl> getSYSList_By_PfbId(String pfbId) throws Exception;
	
	public int chk_oid_url(String oid , String url);
	
	public String insPfbxBlockCusurl(Map blockInfo);
	
	public int ins_oid_url(String cuid , String oid , String url , String bordMesg);
	
}
