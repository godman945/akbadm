package com.pchome.akbadm.db.service.advideo;

import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.service.IBaseService;


public interface IPfpAdVideoSourceService extends IBaseService<PfpAdVideoSource,String>{
	
	public PfpAdVideoSource getVideoUrl(String videoUrl) throws Exception;

	
}
