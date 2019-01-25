package com.pchome.akbadm.db.service.check;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpIllegalKeyword;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpIllegalKeywordService extends IBaseService<PfpIllegalKeyword,String>{

	public int getIllegalKeywordCountByCondition(String queryString) throws Exception;

	public List<PfpIllegalKeyword> getIllegalKeywordByCondition(String queryString, int pageNo, int pageSize) throws Exception;

	public void updateIllegalKeywordBySeq(String seq, String content) throws Exception;

	public void deleteIllegalKeywordBySeq(String seq) throws Exception;

	public int checkIllegalKeywordExists(String queryString) throws Exception;
}
