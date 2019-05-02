package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.ad.IPfpAdExcludeKeywordDAO;
import com.pchome.akbadm.db.dao.ad.PfpAdExcludeKeywordDAO;
import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdExcludeKeywordService extends BaseService<PfpAdExcludeKeyword,String> implements IPfpAdExcludeKeywordService{
	@Override
    public List<PfpAdExcludeKeyword> getAllPfpAdExcludeKeywords() throws Exception{
		return ((PfpAdExcludeKeywordDAO)dao).loadAll();
	}

	@Override
    public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adExcludeKeywordSeq, String adGroupSeq, String adExcludeKeyword) throws Exception{
		return ((PfpAdExcludeKeywordDAO)dao).getPfpAdExcludeKeywords(adExcludeKeywordSeq, adGroupSeq, adExcludeKeyword);
	}

	@Override
    public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adGroupSeq, String customerInfoId) throws Exception{
		return ((PfpAdExcludeKeywordDAO)dao).getPfpAdExcludeKeywords(adGroupSeq,customerInfoId);
	}

    @Override
    public List<PfpAdExcludeKeyword> selectPfpAdExcludeKeywords(String adGroupSeq, int status) {
        return ((IPfpAdExcludeKeywordDAO)dao).selectPfpAdExcludeKeywords(adGroupSeq, status);
    }

	@Override
    public PfpAdExcludeKeyword getPfpAdExcludeKeywordBySeq(String adExcludeKeywordSeq) throws Exception {
		return ((PfpAdExcludeKeywordDAO)dao).getPfpAdExcludeKeywordBySeq(adExcludeKeywordSeq);
	}

	@Override
    public void insertPfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception {
		((PfpAdExcludeKeywordDAO)dao).insertPfpAdExcludeKeyword(pfpAdExcludeKeyword);
	}

	@Override
    public void updatePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception {
		((PfpAdExcludeKeywordDAO)dao).updatePfpAdExcludeKeyword(pfpAdExcludeKeyword);
	}

	@Override
    public void deletePfpAdExcludeKeyword(String adExcludeKeywordSeq) throws Exception {
		((PfpAdExcludeKeywordDAO)dao).deletePfpAdExcludeKeyword(adExcludeKeywordSeq);
	}

	@Override
    public void savePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception {
		((PfpAdExcludeKeywordDAO)dao).saveOrUpdatePfpAdExcludeKeyword(pfpAdExcludeKeyword);
	}
}
