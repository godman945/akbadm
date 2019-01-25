package com.pchome.rmi.sequence;

public interface ISequenceProvider {
		
	//取得流水編號
	public String getSequenceID(EnumSequenceTableName enumSequenceTableName);

	//取得流水編號，但有中介符號
	public String getSequenceID(EnumSequenceTableName enumSequenceTableName, String mid);
}
