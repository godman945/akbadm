package com.pchome.akbadm.db.pojo;

// Generated 2018/10/22 下午 04:50:20 by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PfpCodeConvertTrans generated by hbm2java
 */
@Entity
@Table(name = "pfp_code_convert_trans")
public class PfpCodeConvertTrans implements java.io.Serializable {

    private String id;
    private Date convertDate;
    private String convertCkPv;
    private String convertSeq;
    private String convertNumType;
    private String convertBelong;
    private Date convertBelongDate;
    private String adSeq;
    private String uuid;
    private int convertCount;
    private float convertPrice;
    private Date updateDate;
    private Date createDate;

    public PfpCodeConvertTrans() {
    }

    public PfpCodeConvertTrans(String id, Date convertDate, String convertCkPv,
            String convertSeq, String convertNumType, String convertBelong,
            Date convertBelongDate, String adSeq, String uuid,
            int convertCount, float convertPrice, Date updateDate,
            Date createDate) {
        this.id = id;
        this.convertDate = convertDate;
        this.convertCkPv = convertCkPv;
        this.convertSeq = convertSeq;
        this.convertNumType = convertNumType;
        this.convertBelong = convertBelong;
        this.convertBelongDate = convertBelongDate;
        this.adSeq = adSeq;
        this.uuid = uuid;
        this.convertCount = convertCount;
        this.convertPrice = convertPrice;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 20)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "convert_date", nullable = false, length = 19)
    public Date getConvertDate() {
        return this.convertDate;
    }

    public void setConvertDate(Date convertDate) {
        this.convertDate = convertDate;
    }

    @Column(name = "convert_ck_pv", nullable = false, length = 2)
    public String getConvertCkPv() {
        return this.convertCkPv;
    }

    public void setConvertCkPv(String convertCkPv) {
        this.convertCkPv = convertCkPv;
    }

    @Column(name = "convert_seq", nullable = false, length = 20)
    public String getConvertSeq() {
        return this.convertSeq;
    }

    public void setConvertSeq(String convertSeq) {
        this.convertSeq = convertSeq;
    }

    @Column(name = "convert_num_type", nullable = false, length = 1)
    public String getConvertNumType() {
        return this.convertNumType;
    }

    public void setConvertNumType(String convertNumType) {
        this.convertNumType = convertNumType;
    }

    @Column(name = "convert_belong", nullable = false, length = 2)
    public String getConvertBelong() {
        return this.convertBelong;
    }

    public void setConvertBelong(String convertBelong) {
        this.convertBelong = convertBelong;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "convert_belong_date", nullable = false, length = 19)
    public Date getConvertBelongDate() {
        return this.convertBelongDate;
    }

    public void setConvertBelongDate(Date convertBelongDate) {
        this.convertBelongDate = convertBelongDate;
    }

    @Column(name = "ad_seq", nullable = false, length = 20)
    public String getAdSeq() {
        return this.adSeq;
    }

    public void setAdSeq(String adSeq) {
        this.adSeq = adSeq;
    }

    @Column(name = "uuid", nullable = false, length = 50)
    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "convert_count", nullable = false)
    public int getConvertCount() {
        return this.convertCount;
    }

    public void setConvertCount(int convertCount) {
        this.convertCount = convertCount;
    }

    @Column(name = "convert_price", nullable = false, precision = 12, scale = 0)
    public float getConvertPrice() {
        return this.convertPrice;
    }

    public void setConvertPrice(float convertPrice) {
        this.convertPrice = convertPrice;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false, length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
