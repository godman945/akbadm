package com.pchome.akbadm.db.service.board;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.board.IPfpBoardDAO;
import com.pchome.akbadm.db.pojo.PfpBoard;
import com.pchome.akbadm.db.service.BaseService;

public class PfpBoardService extends BaseService<PfpBoard, Integer> implements IPfpBoardService {
	public List<PfpBoard> findSystemBoards(int page, int pageSize) throws Exception{

		List<Object> objects = ((IPfpBoardDAO)dao).findSystemBoards(page, pageSize);

		List<PfpBoard> boards = null;

		for(Object object:objects){

			if(StringUtils.isNotEmpty(object.toString())){

				if(boards == null){
					boards = new ArrayList<PfpBoard>();
				}

				PfpBoard board = ((IPfpBoardDAO)dao).findPfpBoard(object.toString());
				boards.add(board);
			}
		}

		return boards;
	}

	public List<PfpBoard> findSystemBoards() throws Exception{
		return ((IPfpBoardDAO)dao).findSystemBoards();
	}

	public PfpBoard findPfpBoard(String boardId) throws Exception{
		return ((IPfpBoardDAO)dao).findPfpBoard(boardId);
	}

	public Integer addPfpBoard(PfpBoard board) throws Exception{
		((IPfpBoardDAO)dao).saveOrUpdate(board);
		return board.getBoardId();
	}

    public List<PfpBoard> selectPfpBoard(String customerInfoId, String category, String display) {
        return ((IPfpBoardDAO) dao).selectPfpBoard(customerInfoId, category, display);
    }

    public Integer displayPfpBoard(String customerInfoId, String category, String display) {
        return ((IPfpBoardDAO)dao).displayPfpBoard(customerInfoId, category, display);
    }

    public Integer deletePfpBoard(String customerInfoId, String category) {
        return ((IPfpBoardDAO)dao).deletePfpBoard(customerInfoId, category);
    }

    public Integer deletePfpBoard(String customerInfoId, String category, String deleteId) {
        return ((IPfpBoardDAO)dao).deletePfpBoard(customerInfoId, category, deleteId);
    }

    public Integer deletePfpBoardOvertime(String overtime) {
        return ((IPfpBoardDAO)dao).deletePfpBoardOvertime(overtime);
    }

	public void updatePfpBoard(PfpBoard board) throws Exception{
		((IPfpBoardDAO)dao).saveOrUpdate(board);
	}

	public void deletePfpBoard(String boardId) throws Exception{
		((IPfpBoardDAO)dao).deletePfpBoard(boardId);
	}
}