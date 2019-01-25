package com.pchome.akbadm.db.service.pfbx.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxBoardService extends IBaseService<PfbxBoard, Integer> {

	public List<PfbxBoard> findPfbxBoard(Map<String, String> conditionsMap) throws Exception;

    public Integer deletePfbxBoardOvertime(Date overtime) throws Exception;

	public void deletePfbxBoardById(String boardId) throws Exception;

	public void deleteBoard(String pfbCustomerInfoId, String deleteId) throws Exception;

	/**
	 * 依下面輸入參數值，刪除相對應PFB系統公告
	 * @param pfbxCustomerInfo 聯播網帳戶
	 * @param deleteId 刪除的依據
	 * @param createDate 建立日期
	 */
	public void delectBoardFindLatestContent(String pfbxCustomerInfo, String deleteId, String createDate);
}
