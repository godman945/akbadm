package com.pchome.rmi.sequence;

import com.pchome.akbadm.db.service.sequence.ISequenceService;

public class SequenceProviderImp implements ISequenceProvider{

	ISequenceService sequenceService;
	
	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	@Override
	public String getSequenceID(EnumSequenceTableName enumSequenceTableName) {
		// TODO Auto-generated method stub
		return sequenceService.getId(enumSequenceTableName);
	}

	@Override
	public String getSequenceID(EnumSequenceTableName enumSequenceTableName, String mid) {
		// TODO Auto-generated method stub
		return sequenceService.getId(enumSequenceTableName, mid);
	}
}
