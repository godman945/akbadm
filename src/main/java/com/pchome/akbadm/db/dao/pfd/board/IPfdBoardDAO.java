package com.pchome.akbadm.db.dao.pfd.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdBoard;

public interface IPfdBoardDAO extends IBaseDAO<PfdBoard, Integer> {

	public List<PfdBoard> findPfdBoard(Map<String, String> conditionsMap) throws Exception;

    public Integer deletePfdBoardOvertime(Date overtime) throws Exception;

	public void deletePfdBoardById(String boardId) throws Exception;
	
	public void deletePfdBoardByDeleteId(String deleteId) throws Exception;
}
