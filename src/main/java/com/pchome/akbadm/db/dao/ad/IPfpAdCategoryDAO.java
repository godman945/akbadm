package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategory;

public interface IPfpAdCategoryDAO extends IBaseDAO<PfpAdCategory,String> {

	public List<PfpAdCategory> getAllPfpAdCategory() throws Exception;
}
