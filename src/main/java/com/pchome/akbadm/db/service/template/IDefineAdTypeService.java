package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmDefineAdType;
import com.pchome.akbadm.db.service.IBaseService;

public interface IDefineAdTypeService extends IBaseService<AdmDefineAdType, String> {

    public List<AdmDefineAdType> getAllDefineAdType() throws Exception;

	public List<AdmDefineAdType> getDefineAdTypeByCondition(String defineAdTypeId, String defineAdTypeName) throws Exception;

	public AdmDefineAdType getDefineAdTypeById(String defineAdTypeId) throws Exception;
}
