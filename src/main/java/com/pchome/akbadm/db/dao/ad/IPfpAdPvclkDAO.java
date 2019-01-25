package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;

public interface IPfpAdPvclkDAO extends IBaseDAO<PfpAdPvclk, String> {

	public List<Object[]> getListObjPFBDetailByReportDate(Date reportDate) throws Exception;

	public List<Object[]> getListObjPFPDetailByReportDate(Date reportDate) throws Exception;

	public List<Object[]> getListObjByDate1(Date startDate, Date endDate) throws Exception;

	public List<Object[]> getListObjByDate(Date startDate , Date endDate) throws Exception;

	public List<Map> getListMapByDate(Date startDate , Date endDate) throws Exception ;

	public List<PfpAdPvclk> getListByDate(Date startDate , Date endDate) throws Exception ;

	public List<Object> findAdPvclkTotalCost(String startDate, String endDate) throws Exception;

	public List<Object> findAdPvclkShortCost(String startDate, String endDate) throws Exception;

	public List<Object> accountPvclkSum(String customerInfoId, Date date);

	public List<Object> adActionPvclkSum(String customerInfoId, Date date);

    public float customerCost(String pfpCustomerId, Date pvclkDate);

	public float actionCost(String actionId, Date pvclkDate);

    public Float[] selectAdPvclkSumByPvclkDate(String pvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq, String adSeq);

    public List<Object[]> selectPfpAdPvclkSums(Date pvclkDate);

	public List<PfpAdPvclk> selectPfpAdPvclk(int firstResult, int maxResults);

//	public float selectAdClkPrice(String pvclkDate, String customerInfoId, String actionId);

	public List<Object[]> selectAdClkPriceByPfpCustomerInfoId(Date pvclkDate);

    public Map<String, Float> selectAdClkPriceMapByPfpCustomerInfoId(Date pvclkDate);

	public List<Object[]> selectAdClkPriceByPfpAdActionId(Date pvclkDate);

    public Map<String, Float> selectAdClkPriceMapByPfpAdActionId(Date pvclkDate);

	public float totalSysAdPvclk(Date startDate, Date endDate);

	public float totalPfbAdPvclk(String pfbId, Date startDate, Date endDate);

	public List<Object[]> findPfpAdPvclks(Date date);

	public Integer updateEmptyPfbxCustomerPostion(Date date,String pfbxCustomerID,String pfbxPostionID);

	public Integer updateEmptyUrl(Date date);

    public List<PfpAdPvclk> findLastPfpAdPvclk();

    public int deleteMalice(Date recordDate, int recordTime);

    public Map<String,Boolean> checkPvClk(String date);

    public PfpAdPvclk selectOneBeforeDate(Date pvclkDate);

    public PfpAdPvclk selectOneByDate(Date pvclkDate);

    public PfpAdPvclk selectBackupByDate(Date pvclkDate);

    public int selectCountByDate(Date pvclkDate);

    public int replaceSelectByDate(Date pvclkDate);

    public int deleteByDate(Date pvclkDate);
}
