package com.pchome.akbadm.factory.notice;

import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeMoneyEntered extends ANotice {
    private EnumCategory enumCategory = EnumCategory.MONEY_ENTERED;

    @Override
    public void notice() {}

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }
}