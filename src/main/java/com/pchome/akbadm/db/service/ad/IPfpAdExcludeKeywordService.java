package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdExcludeKeywordService extends IBaseService<PfpAdExcludeKeyword,String>{

	public List<PfpAdExcludeKeyword> getAllPfpAdExcludeKeywords() throws Exception;

	public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adExcludeKeywordSeq, String adGroupSeq, String adExcludeKeyword) throws Exception;

	public PfpAdExcludeKeyword getPfpAdExcludeKeywordBySeq(String adExcludeKeywordSeq) throws Exception;

	public void insertPfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception;

	public void updatePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception;

	public void deletePfpAdExcludeKeyword(String adExcludeKeywordSeq) throws Exception;

	public void savePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception;

	public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adGroupSeq, String customerInfoId) throws Exception;

    public List<PfpAdExcludeKeyword> selectPfpAdExcludeKeywords(String adGroupSeq, int status) throws Exception;

}
