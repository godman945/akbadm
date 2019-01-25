package com.pchome.akbadm.db.service.sequence;

import com.pchome.akbadm.db.pojo.Sequence;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.rmi.sequence.EnumSequenceTableName;

public interface ISequenceService extends IBaseService<Sequence, String> {

	//取得流水編號
	public String getId(EnumSequenceTableName enumSequenceTableName);

	//取得流水編號
	public String getId(EnumSequenceTableName enumSequenceTableName,String mid);

	public String getSerialNumber(EnumSequenceTableName enumSequenceTableName) throws Exception;

}
