package com.pchome.akbadm.db.service.mailbox;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpMailbox;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpMailboxService extends IBaseService<PfpMailbox, Integer> {
    public List<PfpMailbox> selectPfpMailbox(String customerInfoId, String category);

    public List<PfpMailbox> selectPfpMailboxBySend(String send);

    public int deletePfpMailbox(String customerInfoId, String category);
}