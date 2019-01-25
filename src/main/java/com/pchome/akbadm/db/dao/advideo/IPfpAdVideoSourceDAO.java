package com.pchome.akbadm.db.dao.advideo;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoSource;

public interface IPfpAdVideoSourceDAO extends IBaseDAO<PfpAdVideoSource,String>{
	

	public PfpAdVideoSource getVideoUrl(String videoUrl) throws Exception;
	
}
