package com.pchome.akbadm.db.service.pfd.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfd.board.IPfdBoardDAO;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.service.BaseService;

public class PfdBoardService extends BaseService<PfdBoard, Integer> implements IPfdBoardService {

	public List<PfdBoard> findPfdBoard(Map<String, String> conditionsMap) throws Exception {
		return ((IPfdBoardDAO) dao).findPfdBoard(conditionsMap);
	}

    public Integer deletePfdBoardOvertime(Date overtime) throws Exception {
        return ((IPfdBoardDAO)dao).deletePfdBoardOvertime(overtime);
    }

	public void deletePfdBoardById(String boardId) throws Exception {
		((IPfdBoardDAO) dao).deletePfdBoardById(boardId);
	}
	
	public void deletePfdBoardByDeleteId(String deleteId) throws Exception {
		((IPfdBoardDAO) dao).deletePfdBoardByDeleteId(deleteId);
	}
}
