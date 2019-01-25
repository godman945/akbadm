package com.pchome.akbadm.db.dao.mailbox;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpMailbox;

public interface IPfpMailboxDAO extends IBaseDAO<PfpMailbox, Integer> {
    public List<PfpMailbox> selectPfpMailbox(String customerInfoId, String category);

    public List<PfpMailbox> selectPfpMailboxBySend(String send);

    public int deletePfpMailbox(String customerInfoId, String category);
}