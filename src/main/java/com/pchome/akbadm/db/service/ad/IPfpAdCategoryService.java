package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdCategory;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdCategoryService extends IBaseService<PfpAdCategory, String> {

	public List<PfpAdCategory> getAllPfpAdCategory() throws Exception;
}
