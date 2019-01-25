package com.pchome.akbadm.db.service.mailbox;

import java.util.List;

import com.pchome.akbadm.db.dao.mailbox.IPfpMailboxDAO;
import com.pchome.akbadm.db.pojo.PfpMailbox;
import com.pchome.akbadm.db.service.BaseService;

public class PfpMailboxService extends BaseService<PfpMailbox, Integer> implements IPfpMailboxService {
    public List<PfpMailbox> selectPfpMailbox(String customerInfoId, String category) {
        return ((IPfpMailboxDAO) dao).selectPfpMailbox(customerInfoId, category);
    }

    public List<PfpMailbox> selectPfpMailboxBySend(String send) {
        return ((IPfpMailboxDAO) dao).selectPfpMailboxBySend(send);
    }

    public int deletePfpMailbox(String customerInfoId, String category) {
        return ((IPfpMailboxDAO) dao).deletePfpMailbox(customerInfoId, category);
    }
}