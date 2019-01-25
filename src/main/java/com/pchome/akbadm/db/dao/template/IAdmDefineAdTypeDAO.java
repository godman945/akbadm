package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAdType;

public interface IAdmDefineAdTypeDAO extends IBaseDAO<AdmDefineAdType, String> {

    public List<AdmDefineAdType> getDefineAdType() throws Exception;

	public List<AdmDefineAdType> getDefineAdTypeByCondition(String defineAdTypeId, String defineAdTypeName) throws Exception;

	public AdmDefineAdType getDefineAdTypeById(String defineAdTypeById) throws Exception;
}
