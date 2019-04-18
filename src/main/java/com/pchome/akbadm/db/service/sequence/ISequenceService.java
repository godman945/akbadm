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

	/**
	 * 可輸入欄位總長度，補滿需要的位數
	 * ex:totalLength=20 結果:PCUL2018080800000001
	 * @param enumSequenceTableName
	 * @param mid 中間要輸入甚麼參數
	 * @param totalLength 總長度
	 * @return 
	 * @throws Exception
	 */
	public String getId(EnumSequenceTableName enumSequenceTableName, String mid, int totalLength) throws Exception;
}