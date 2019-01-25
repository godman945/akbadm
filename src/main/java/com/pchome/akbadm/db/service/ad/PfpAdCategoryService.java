package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.ad.PfpAdCategoryDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategory;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdCategoryService extends BaseService<PfpAdCategory, String> implements IPfpAdCategoryService {

	public List<PfpAdCategory> getAllPfpAdCategory() throws Exception {
		return ((PfpAdCategoryDAO) dao).getAllPfpAdCategory();
	}
}
