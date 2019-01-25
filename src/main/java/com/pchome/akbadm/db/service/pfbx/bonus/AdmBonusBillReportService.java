package com.pchome.akbadm.db.service.pfbx.bonus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.bonus.IAdmBonusBillReportDAO;
import com.pchome.akbadm.db.pojo.AdmBonusBillReport;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;

public class AdmBonusBillReportService extends BaseService<AdmBonusBillReport, Integer> implements IAdmBonusBillReportService
{
	private IPfpAdPvclkService pfpAdPvclkService;
	private IAdmTransLossService admTransLossService;
	
	/**
	 * 查詢功能與每日排程共用
	 * @param sdate
	 * @param edate
	 * @return List<PfbxInComeReportVo>
	 * @throws Exception
	 */
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList5(String sdate, String edate) throws Exception
	{
		log.info("...use getPfbxInComeReportVoList5");
//		log.info("...reportDate=" + sdate + "~" + edate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(sdate);
		Date endDate = sdf.parse(edate);
		
//		long time1 , time2 ;
//		time1 = System.currentTimeMillis();
		
		List<PfbxInComeReportVo> vos = ((IAdmBonusBillReportDAO) dao).getListBySEDate2(startDate, endDate);
		
//		time2 = System.currentTimeMillis();
//		log.info("...5 use--" + (time2-time1));
		
		return vos;
	}
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList4(String sdate, String edate) throws Exception
	{
		log.info("...use getPfbxInComeReportVoList4");
//		log.info("...reportDate=" + sdate + "~" + edate);
		
//		long time1, time2;
//        time1 = System.currentTimeMillis();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(sdate);
		Date endDate = sdf.parse(edate);
		
		//VO
		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();
		
		//日期區間adm bonus bill report
		List<AdmBonusBillReport> list = this.getListBySEDate(startDate, endDate);
		
		//日期區間總點擊 pfp ad pvclk
		List<Object[]> adClkList = pfpAdPvclkService.getListObjByDate1(startDate, endDate);
		
		//日期區間超撥cost loss
		List<Object[]> costLossList = admTransLossService.getTransLossSumByTransDate(startDate, endDate);
		
		
		PfbxInComeReportVo vo = null;
		for (AdmBonusBillReport admBill : list)
		{
			vo = new PfbxInComeReportVo();
			vo.setReportdate(admBill.getReportDate());
			vo.setId(admBill.getId());
			
			//計算每日點擊price
			float fadclkprice = 0;
			float fSysclkPrice = (float)Math.round(admBill.getSysClkPrice());
			Date reportDate = admBill.getReportDate();
			
			
			
			//取得當日點擊price原始資料
			for(Object[] adclk : adClkList)
			{
				if((adclk[0].toString()).equals(reportDate.toString()))
				{
					fadclkprice = Float.parseFloat(adclk[1].toString());
					break;
				}
			}
			
			//先把有無超撥註記放進去
			if(fadclkprice != fSysclkPrice)
			{
				vo.setNotMatchFlag("Y");
			}
			else
			{
				vo.setNotMatchFlag("N");
			}
			
			//扣除超撥
			for(Object[] costLoss : costLossList)
			{
				if(StringUtils.equals(costLoss[0].toString() , reportDate.toString()))
				{
					fadclkprice -= Float.parseFloat(costLoss[1].toString());
					break;
				}
			}
			//最終顯示數字
			vo.setAdclkprice((float)Math.round(fadclkprice));
			
			//這個方法可以選擇取到小數第幾位
			//vo.setSysclkprice(new BigDecimal(admBill.getSysClkPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue());
			
			vo.setSysclkprice(fSysclkPrice);
			vo.setIncome(admBill.getIncome());
			vo.setExpense(admBill.getExpense());
			vo.setTotal(admBill.getTotal());
			voList.add(vo);
		}
		
//		time2 = System.currentTimeMillis();
//		log.info("...4 use--" + (time2-time1));
		
		return voList;
	}

	public List<PfbxInComeReportVo> getPfbxInComeReportVoList3(String sdate, String edate) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(sdate);
		Date endDate = sdf.parse(edate);
		
		//VO
		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();
		
		//日期區間adm bonus bill report
		List<AdmBonusBillReport> list = this.getListBySEDate(startDate, endDate);
		
		//日期區間總點擊 pfp ad pvclk
		List<Object[]> adClkList = pfpAdPvclkService.getListObjByDate(startDate, endDate);
		
		PfbxInComeReportVo vo = null;
		for (AdmBonusBillReport admBill : list)
		{
			vo = new PfbxInComeReportVo();
			vo.setReportdate(admBill.getReportDate());
			vo.setId(admBill.getId());
			
			//計算每日點擊price
			float fadclkprice = 0;
			Date reportDate = admBill.getReportDate();
			for(Object[] adclk : adClkList)
			{
				if((adclk[2].toString()).equals(reportDate.toString()))
				{
					fadclkprice += Float.parseFloat(adclk[0].toString()) - Float.parseFloat(adclk[1].toString());
				}
			}

			float todayCostloss = admTransLossService.selectTransLossSumByTransDate(reportDate.toString());
			
			vo.setAdclkprice(fadclkprice - todayCostloss);
			
			vo.setSysclkprice(admBill.getSysClkPrice());
			vo.setIncome(admBill.getIncome());
			vo.setExpense(admBill.getExpense());
			vo.setTotal(admBill.getTotal());
			voList.add(vo);
		}
		
		return voList;
	}

	public List<PfbxInComeReportVo> getPfbxInComeReportVoList2(String sdate, String edate) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(sdate);
		Date endDate = sdf.parse(edate);

		// VO
		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();

		// 日期區間adm bonus bill report
		List<AdmBonusBillReport> list = this.getListBySEDate(startDate, endDate);

		// 日期區間總點擊 pfp ad pvclk
		List<PfpAdPvclk> adClkList2 = pfpAdPvclkService.getListByDate(startDate, endDate);

		PfbxInComeReportVo vo = null;
		for (AdmBonusBillReport admBill : list)
		{
			vo = new PfbxInComeReportVo();
			vo.setReportdate(admBill.getReportDate());

			// 計算每日點擊price
			float fadclkprice = 0;
			Date reportDate = admBill.getReportDate();
			for (Object adclk : adClkList2)
			{
				if ((((PfpAdPvclk) adclk).getAdPvclkDate().toString()).equals(reportDate.toString()))
				{
					fadclkprice += ((PfpAdPvclk) adclk).getAdClkPrice() - ((PfpAdPvclk) adclk).getAdInvalidClkPrice();
				}
			}
			vo.setAdclkprice(fadclkprice);

			vo.setSysclkprice(admBill.getSysClkPrice());
			vo.setIncome(admBill.getIncome());
			vo.setExpense(admBill.getExpense());
			vo.setTotal(admBill.getTotal());
			voList.add(vo);
		}

		log.info("voList Size===" + voList.size());

		return voList;
	}

	public List<PfbxInComeReportVo> getPfbxInComeReportVoList1(String sdate, String edate) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(sdate);
		Date endDate = sdf.parse(edate);

		// VO
		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();

		// 日期區間adm bonus bill report
		List<AdmBonusBillReport> list = this.getListBySEDate(startDate, endDate);

		// 日期區間總點擊 pfp ad pvclk
		List<Map> adClkList = pfpAdPvclkService.getListMapByDate(startDate, endDate);

		PfbxInComeReportVo vo = null;
		for (AdmBonusBillReport admBill : list)
		{
			vo = new PfbxInComeReportVo();
			vo.setReportdate(admBill.getReportDate());

			// 計算每日點擊price
			float fadclkprice = 0;
			Date reportDate = admBill.getReportDate();
			for (Map adclk : adClkList)
			{
				if (adclk.get("ad_pvclk_date").toString().equals(reportDate.toString()))
				{
					fadclkprice += Float.parseFloat(adclk.get("ad_clk_price").toString()) - Float.parseFloat(adclk.get("ad_invalid_clk_price").toString());
				}
			}
			vo.setAdclkprice(fadclkprice);

			vo.setSysclkprice(admBill.getSysClkPrice());
			vo.setIncome(admBill.getIncome());
			vo.setExpense(admBill.getExpense());
			vo.setTotal(admBill.getTotal());
			voList.add(vo);
		}

		log.info("voList Size===" + voList.size());

		return voList;
	}

//	public List<PfbxInComeReportVo> getPfbxInComeReportVoList(String sdate, String edate) throws Exception
//	{
//		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();
//		List<Map> maps = this.getMapsBySEDate(sdate, edate);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//		PfbxInComeReportVo vo = null;
//		for (Map map : maps)
//		{
//			vo = new PfbxInComeReportVo();
//			vo.setReportdate(map.get("report_date"));
//
//			float fadclkprice = 0;
//			Date dReportDate = sdf.parse(map.get("report_date").toString());
//			fadclkprice = pfpAdPvclkService.totalSysAdPvclk(dReportDate, dReportDate);
//			vo.setAdclkprice(fadclkprice);
//
//			vo.setSysclkprice(Float.parseFloat(map.get("sys_clk_price").toString()));
//			vo.setIncome(Float.parseFloat(map.get("income").toString()));
//			vo.setExpense(Float.parseFloat(map.get("expense").toString().toString()));
//			vo.setTotal(Float.parseFloat(map.get("total").toString()));
//			voList.add(vo);
//		}
//
//		return voList;
//	}

	public List<AdmBonusBillReport> getListBySEDate(Date sdate, Date edate) throws Exception
	{
		return ((IAdmBonusBillReportDAO) dao).getListBySEDate(sdate, edate);
	}

	public List<Map> getMapsBySEDate(String sdate, String edate)
	{
		return ((IAdmBonusBillReportDAO) dao).getMapsBySEDate(sdate, edate);
	}

	public AdmBonusBillReport getOneBySEDate(Date sdate, Date edate) throws Exception
	{
		List<AdmBonusBillReport> list = ((IAdmBonusBillReportDAO) dao).getListBySEDate(sdate, edate);

		if (list.size() > 0)
		{
			return list.get(0);
		}

		return null;
	}

	public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService)
	{
		this.pfpAdPvclkService = pfpAdPvclkService;
	}

	public Integer deleteAdmBonusBillReport(Date deleteDate)
	{
		return ((IAdmBonusBillReportDAO) dao).deleteAdmBonusBillReport(deleteDate);
	}

	public void setAdmTransLossService(IAdmTransLossService admTransLossService)
	{
		this.admTransLossService = admTransLossService;
	}

	
}
