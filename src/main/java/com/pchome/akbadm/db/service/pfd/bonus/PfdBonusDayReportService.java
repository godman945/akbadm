package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.pfd.bonus.IPfdBonusDayReportDAO;
import com.pchome.akbadm.db.pojo.PfdBonusDayReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdBonusDayReportService extends BaseService<PfdBonusDayReport, Integer> implements IPfdBonusDayReportService{
	
	public List<Object[]> getListObjPFDDetailByReportDate(Date reportDate) {
		
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> data = ((IPfdBonusDayReportDAO)dao).getListObjPFDDetailByReportDate(reportDate);
		
		Object[] obj = null;
		float savePrice = 0;
		float freePrice = 0;
		float postpaidPrice = 0;
		float subTotal = 0;
		for(Object[] o : data)
		{
			obj = new Object[5];
			obj[0] = o[0];
			
			float thisSaveprice = 0;
			if(o[1] != null)
			{
				thisSaveprice = Float.parseFloat(o[1].toString());
			}
			obj[1] = thisSaveprice;
			
			float thisFreePrice = 0;
			if(o[2] != null)
			{
				thisFreePrice = Float.parseFloat(o[2].toString());
			}
			obj[2] = thisFreePrice;
			
			float thisPostpaidPrice = 0;
			if(o[3] != null)
			{
				thisPostpaidPrice = Float.parseFloat(o[3].toString());
			}
			obj[3] = thisPostpaidPrice;
			
			float thisSubTotal = thisSaveprice + thisFreePrice + thisPostpaidPrice;
			obj[4] = thisSubTotal;
			
			savePrice += thisSaveprice;
			freePrice += thisFreePrice;
			postpaidPrice += thisPostpaidPrice;
			subTotal += thisSubTotal;
			list.add(obj);
		}
		
		obj = new Object[5];
		obj[0] = "Total";
		obj[1] = savePrice;
		obj[2] = freePrice;
		obj[3] = postpaidPrice;
		obj[4] = subTotal;
		list.add(obj);
		
		return list;
	}

	public float findPfdSaveMonthBonus(Date reportDate) {
		return ((IPfdBonusDayReportDAO)dao).findPfdSaveMonthBonus(reportDate);
	}
	
	public float findPfdPaidMonthBonus(Date reportDate) {
		return ((IPfdBonusDayReportDAO)dao).findPfdPaidMonthBonus(reportDate);
	}
	
	public Integer deletePfdBonusDayReport(Date deleteDate) {
		return ((IPfdBonusDayReportDAO)dao).deletePfdBonusDayReport(deleteDate);
	}

	@Override
	public float getPfdSavePrice(String pfdId, String startDate, String endDate) {
		return ((IPfdBonusDayReportDAO)dao).getPfdSavePrice(pfdId, startDate, endDate);
	}

	@Override
	public float getPfdPaidPrice(String pfdId, String startDate, String endDate) {
		return ((IPfdBonusDayReportDAO)dao).getPfdPaidPrice(pfdId, startDate, endDate);
	}
}
