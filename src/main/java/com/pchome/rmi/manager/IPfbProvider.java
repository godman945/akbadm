package com.pchome.rmi.manager;

import java.util.List;

public interface IPfbProvider {

	// 列出 PFb 帳戶
	public List<PfbAccountVO> findPfbAccount(String memberId, String ip);

	// 是否帳戶管理者
	public boolean isManager(String memberId, String ip);
}
