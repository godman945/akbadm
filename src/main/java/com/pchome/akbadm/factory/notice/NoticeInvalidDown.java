package com.pchome.akbadm.factory.notice;

import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeInvalidDown extends ANotice {
    private EnumCategory enumCategory = EnumCategory.INVALID_DOWN;

    @Override
    public void notice() {}

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }
}