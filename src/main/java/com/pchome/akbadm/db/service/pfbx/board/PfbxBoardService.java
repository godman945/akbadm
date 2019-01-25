package com.pchome.akbadm.db.service.pfbx.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.board.IPfbxBoardDAO;
import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxBoardService extends BaseService<PfbxBoard, Integer> implements IPfbxBoardService {

	public List<PfbxBoard> findPfbxBoard(Map<String, String> conditionsMap) throws Exception {
		return ((IPfbxBoardDAO) dao).findPfbxBoard(conditionsMap);
	}

    public Integer deletePfbxBoardOvertime(Date overtime) throws Exception {
        return ((IPfbxBoardDAO)dao).deletePfbxBoardOvertime(overtime);
    }

	public void deletePfbxBoardById(String boardId) throws Exception {
		((IPfbxBoardDAO) dao).deletePfbxBoardById(boardId);
	}

	public void deleteBoard(String pfbCustomerInfoId, String deleteId) throws Exception {		
		((IPfbxBoardDAO) dao).deleteBoard(pfbCustomerInfoId, deleteId);
	}

	/**
	 * 依下面輸入參數值，刪除相對應PFB系統公告
	 * @param pfbxCustomerInfo 聯播網帳戶
	 * @param deleteId 刪除的依據
	 * @param createDate 建立日期
	 */
	@Override
	public void delectBoardFindLatestContent(String pfbxCustomerInfo, String deleteId, String createDate) {
		((IPfbxBoardDAO) dao).delectBoardFindLatestContent(pfbxCustomerInfo, deleteId, createDate);
	}
}
