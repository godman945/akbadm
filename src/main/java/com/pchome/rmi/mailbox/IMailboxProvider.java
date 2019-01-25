package com.pchome.rmi.mailbox;

public interface IMailboxProvider {
    public Integer add(String customerInfoId, String receiver, EnumCategory category) throws Exception;

    public Integer delete(String customerInfoId, EnumCategory category) throws Exception;
}