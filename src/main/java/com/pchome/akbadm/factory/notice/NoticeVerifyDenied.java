package com.pchome.akbadm.factory.notice;

import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeVerifyDenied extends ANotice {
    private EnumCategory enumCategory = EnumCategory.VERIFY_DENIED;

    @Override
    public void notice() {}

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }
}