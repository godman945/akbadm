package com.pchome.enumerate.factory;

public enum EnumStore {
    ALL_LOG(false);

    private boolean put;

    private EnumStore(boolean put) {
        this.put = put;
    }

    public boolean isPut() {
        return put;
    }
}