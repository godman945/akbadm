package com.pchome.akbadm.factory.notice;

import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeAccountClosed extends ANotice {
    private EnumCategory enumCategory = EnumCategory.ACCOUNT_CLOSED;

    @Override
    public void notice() {}

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }
}