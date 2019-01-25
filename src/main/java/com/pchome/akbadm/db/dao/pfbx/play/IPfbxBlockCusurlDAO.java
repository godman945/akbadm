package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;

public interface IPfbxBlockCusurlDAO extends IBaseDAO<PfbxBlockCusurl, String> {
	
	public List<PfbxBlockCusurl> getSYSList_By_PfbId(String pfbId , String searchurl) throws Exception;
	
	public int chk_oid_url(String oid , String url);
	
	public int insPfbxBlockCusurl(Map blockInfo);
	
	public int ins_oid_url(String cuid , String oid , String url , String bordMesg);
    
}
