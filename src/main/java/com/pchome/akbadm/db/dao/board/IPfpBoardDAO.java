package com.pchome.akbadm.db.dao.board;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpBoard;

public interface IPfpBoardDAO extends IBaseDAO<PfpBoard, Integer> {
	public List<Object> findSystemBoards(final int page, final int pageSize) throws Exception;

	public List<PfpBoard> findSystemBoards() throws Exception;

	public PfpBoard findPfpBoard(String boardId) throws Exception;

    public List<PfpBoard> selectPfpBoard(String customerInfoId, String category, String display);

	public Integer displayPfpBoard(String customerInfoId, String category, String display);

	public Integer deletePfpBoard(String customerInfoId, String category);

    public Integer deletePfpBoard(String customerInfoId, String category, String deleteId);

    public Integer deletePfpBoardOvertime(String overtime);

	public void deletePfpBoard(String boardId) throws Exception;
}