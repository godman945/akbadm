package com.pchome.akbadm.bean;

import java.util.HashSet;
import java.util.Set;

public class StyleBean {
    private String styleId;
    private Set<String> tproSet = new HashSet<String>();
    private String url;
    private int flag;

    public String getStyleId() {
        return styleId;
    }
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    public Set<String> getTproSet() {
        return tproSet;
    }
    public void setTproSet(Set<String> tproSet) {
        this.tproSet = tproSet;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}