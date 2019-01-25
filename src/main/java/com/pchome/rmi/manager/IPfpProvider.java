package com.pchome.rmi.manager;

import java.util.List;

public interface IPfpProvider {

	// 列出 PFP 帳戶
	public List<PfpAccountVO> findPfpAccount(String memberId, String ip);
	// 是否帳戶管理者
	public boolean isManager(String memberId, String ip);
	
}
