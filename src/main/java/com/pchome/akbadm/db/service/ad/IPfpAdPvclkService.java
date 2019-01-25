package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.AdPvclkVO;
import com.pchome.akbadm.db.vo.report.CheckBillReportVo;

public interface IPfpAdPvclkService extends IBaseService<PfpAdPvclk, String> {

	public List<Object[]> getListObjPFBDetailByReportDate(Date reportDate) throws Exception;

	public List<Object[]> getListObjPFPDetailByReportDate(Date reportDate) throws Exception;

	public List<Object[]> getListObjByDate1(Date startDate , Date endDate) throws Exception;

	public List<Object[]> getListObjByDate(Date startDate , Date endDate) throws Exception;

	public List<Map> getListMapByDate(Date startDate , Date endDate) throws Exception;

	public List<PfpAdPvclk> getListByDate(Date startDate , Date endDate) throws Exception;

	public List<AdPvclkVO> findAdPvclkCostRank(String startDate, String endDate, int pageSize) throws Exception;

	public List<AdPvclkVO> findAdPvclkCostShortRank(String startDate, String endDate, int pageSize) throws Exception;

	public List<Object> accountPvclkSum(String customerInfoId, Date date);

	public List<Object> adActionPvclkSum(String customerInfoId, Date date);

    public float customerCost(String pfpCustomerId, Date pvclkDate);

	public float actionCost(String actionId, Date pvclkDate);

	public Float[] selectAdPvclkSumByPvclkDate(String pvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq, String adSeq);

	public Map<String, int[]> selectPfpAdPvclkSums(Date pvclkDate);

	public List<PfpAdPvclk> selectPfpAdPvclk(int firstResult, int maxResults);

	public float totalSysAdPvclk(Date startDate, Date endDate);

	public float totalPfbAdPvclk(String pfbId, Date startDate, Date endDate);

	public List<CheckBillReportVo> findCheckBillReportToVo(String searchDate);

	public Integer updateEmptyPfbxCustomerPostion(Date date,String pfbxCustomerID,String pfbxPostionID);

	public Integer updateEmptyUrl(Date date);

    public PfpAdPvclk findLastPfpAdPvclk();

    public int deleteMalice(Date recordDate, int recordTime);

    public Map<String,Boolean> checkPvClk(String date);

    public boolean insertSelect(Date date);
}
