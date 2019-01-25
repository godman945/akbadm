package com.pchome.akbadm.db.service.board;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpBoard;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpBoardService extends IBaseService<PfpBoard, Integer> {
	public List<PfpBoard> findSystemBoards(int page, int pageSize) throws Exception;

	public List<PfpBoard> findSystemBoards() throws Exception;

	public PfpBoard findPfpBoard(String boardId) throws Exception;

	public Integer addPfpBoard(PfpBoard board) throws Exception;

    public List<PfpBoard> selectPfpBoard(String customerInfoId, String category, String display);

    public Integer displayPfpBoard(String customerInfoId, String category, String display);

    public Integer deletePfpBoard(String customerInfoId, String category);

    public Integer deletePfpBoard(String customerInfoId, String category, String deleteId);

    public Integer deletePfpBoardOvertime(String overtime);

	public void updatePfpBoard(PfpBoard board) throws Exception;

	public void deletePfpBoard(String boardId) throws Exception;
}