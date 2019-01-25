package com.pchome.akbadm.db.service.advideo;

import com.pchome.akbadm.db.dao.advideo.PfpAdVideoSourceDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdVideoSourceService extends BaseService<PfpAdVideoSource,String> implements IPfpAdVideoSourceService{

    public PfpAdVideoSource getVideoUrl(String videoUrl) throws Exception {
		return ((PfpAdVideoSourceDAO)dao).getVideoUrl(videoUrl);
	}
}
