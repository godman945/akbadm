package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdDetailService extends IBaseService<PfpAdDetail, String> {
	
	public PfpAdDetail findAdContent(String adSeq, String defineAdSeq) throws Exception;
	
	public float countAdSysprice(String adPoolSeq, float sysprice, String today) throws Exception;

	public List<PfpAdDetail> getPfpAdDetails(String adDetailSeq, String adSeq, String adPoolSeq, String defineAdSeq) throws Exception;
}