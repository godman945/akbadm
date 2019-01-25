package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdPvclkDAO;
import com.pchome.akbadm.db.dao.recognize.IAdmRecognizeDetailDAO;
import com.pchome.akbadm.db.dao.recognize.IAdmRecognizeRecordDAO;
import com.pchome.akbadm.db.dao.trans.IAdmTransLossDAO;
import com.pchome.akbadm.db.dao.trans.IPfpTransDetailDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.AdPvclkVO;
import com.pchome.akbadm.db.vo.report.CheckBillReportVo;
import com.pchome.akbadm.utils.ConvertUtil;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.soft.util.DateValueUtil;

public class PfpAdPvclkService extends BaseService<PfpAdPvclk, String> implements IPfpAdPvclkService{

	private IAdmTransLossDAO admTransLossDAO;
	private IPfpTransDetailDAO pfpTransDetailDAO;
	private IAdmRecognizeDetailDAO admRecognizeDetailDAO;
	private IAdmRecognizeRecordDAO admRecognizeRecordDAO;

	@Override
	public List<Object[]> getListObjPFBDetailByReportDate(Date reportDate) throws Exception
	{
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> data = ((IPfpAdPvclkDAO)dao).getListObjPFBDetailByReportDate(reportDate);

		Object[] obj = null;
		float adClkPrice = 0;
		float adInvalidClkPrice = 0;
		float Subtotal = 0;
		float bonus = 0;
		for(Object[] o : data)
		{
			obj = new Object[5];
			obj[0] = o[0];

			float thisAdClkPrice = 0;
			if(o[1] != null)
			{
				thisAdClkPrice = Float.parseFloat(o[1].toString());
			}
			obj[1] = thisAdClkPrice;

			float thisAdInvalidClkPrice = 0;
			if(o[2] != null)
			{
				thisAdInvalidClkPrice = Float.parseFloat(o[2].toString());
			}
			obj[2] = thisAdInvalidClkPrice;

			float thisSubtotal = 0;
			if(o[3] != null)
			{
				thisSubtotal = Float.parseFloat(o[3].toString());
			}
			obj[3] = thisSubtotal;

			float thisbonus = 0;
			if(o[4] != null)
			{
				thisbonus = Float.parseFloat(o[4].toString());
			}
			obj[4] = thisbonus;

			adClkPrice += thisAdClkPrice;
			adInvalidClkPrice += thisAdInvalidClkPrice;
			Subtotal += thisSubtotal;
			bonus += thisbonus;
			list.add(obj);
		}

		obj = new Object[5];
		obj[0] = "TOTAL";
		obj[1] = adClkPrice;
		obj[2] = adInvalidClkPrice;
		obj[3] = Subtotal;
		obj[4] = bonus;
		list.add(obj);

		return list;
	}

	@Override
	public List<Object[]> getListObjPFPDetailByReportDate(Date reportDate) throws Exception
	{
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> data = ((IPfpAdPvclkDAO)dao).getListObjPFPDetailByReportDate(reportDate);

		Object[] obj = null;
		float adClkPrice = 0;
		float adInvalidClkPrice = 0;
		float costloss = 0;
		float Subtotal = 0;
		for(Object[] o : data)
		{
			obj = new Object[5];
			obj[0] = o[0];

			float thisAdClkPrice = 0;
			if(o[1] != null)
			{
				thisAdClkPrice = Float.parseFloat(o[1].toString());
			}
			obj[1] = thisAdClkPrice;

			float thisAdInvalidClkPrice = 0;
			if(o[2] != null)
			{
				thisAdInvalidClkPrice = Float.parseFloat(o[2].toString());
			}
			obj[2] = thisAdInvalidClkPrice;

			float thisCostloss = 0;
			if(o[3] != null)
			{
				thisCostloss = Float.parseFloat(o[3].toString());
			}
			obj[3] = thisCostloss;

			float thisSubtotal = 0;
			if(o[4] != null)
			{
				thisSubtotal = Float.parseFloat(o[4].toString());
			}
			obj[4] = thisSubtotal - thisCostloss;

			adClkPrice += thisAdClkPrice;
			adInvalidClkPrice += thisAdInvalidClkPrice;
			costloss += thisCostloss;
			Subtotal += thisSubtotal - thisCostloss;
			list.add(obj);
		}

		obj = new Object[5];
		obj[0] = "TOTAL";
		obj[1] = adClkPrice;
		obj[2] = adInvalidClkPrice;
		obj[3] = costloss;
		obj[4] = Subtotal;
		list.add(obj);

		return list;
	}

	@Override
	public List<Object[]> getListObjByDate1(Date startDate , Date endDate) throws Exception
	{
		return ((IPfpAdPvclkDAO)dao).getListObjByDate1(startDate, endDate);
	}

	@Override
	public List<Object[]> getListObjByDate(Date startDate , Date endDate) throws Exception
	{
		return ((IPfpAdPvclkDAO)dao).getListObjByDate(startDate, endDate);
	}

	@Override
	public List<Map> getListMapByDate(Date startDate , Date endDate) throws Exception
	{
		return ((IPfpAdPvclkDAO)dao).getListMapByDate(startDate, endDate);
	}

	@Override
	public List<PfpAdPvclk> getListByDate(Date startDate , Date endDate) throws Exception
	{
		return ((IPfpAdPvclkDAO)dao).getListByDate(startDate, endDate);
	}

	@Override
    public List<AdPvclkVO> findAdPvclkCostRank(String startDate, String endDate, int pageSize) throws Exception{

		List<Object> adPvClkCosts = ((IPfpAdPvclkDAO) dao).findAdPvclkTotalCost(startDate, endDate);

		List<AdPvclkVO> adPvclkVOs = null;
		//log.info("  adPvClkCosts  = "+adPvClkCosts.size());
		if(adPvClkCosts != null && adPvClkCosts.size() > 0){

			adPvclkVOs = new ArrayList<AdPvclkVO>();

			int i = 0;
			//log.info("  adPvClkCosts  = "+adPvClkCosts);

			for(Object object:adPvClkCosts){

				i++;

				if(i > pageSize)break;

				Object[] ob = (Object[])object;
				//log.info(ob[0]+" , "+ob[1]+" , "+ob[2]+" , "+ob[3]+" , "+ob[4]);
				float sortPrice = Float.parseFloat(ob[2].toString());

				AdPvclkVO vo = new AdPvclkVO(ob, sortPrice);

				//log.info("1. "+vo.getAdClickRate());
				//log.info("2. "+vo.getAverageCost());

				adPvclkVOs.add(vo);

			}

			Collections.sort(adPvclkVOs);

		}

		return adPvclkVOs;
	}

	@Override
    public List<AdPvclkVO> findAdPvclkCostShortRank(String startDate, String endDate, int pageSize) throws Exception{

		List<Object> adPvClkCosts = ((IPfpAdPvclkDAO) dao).findAdPvclkShortCost(startDate, endDate);

		List<AdPvclkVO> adPvclkVOs = null;
		//log.info("  adPvClkCosts  = "+adPvClkCosts.size());
		if(adPvClkCosts != null && adPvClkCosts.size() > 0){

			adPvclkVOs = new ArrayList<AdPvclkVO>();

			int i = 0;
			//log.info("  adPvClkCosts  = "+adPvClkCosts);

			for(Object object:adPvClkCosts){

				i++;

				if(i > pageSize)break;

				Object[] ob = (Object[])object;
				//log.info(ob[0]+" , "+ob[1]+" , "+ob[2]+" , "+ob[3]+" , "+ob[4]);
				float sortPrice = Float.parseFloat(ob[7].toString()) - Float.parseFloat(ob[2].toString());

				AdPvclkVO vo = new AdPvclkVO(ob, sortPrice);

				//log.info("1. "+vo.getAdClickRate());
				//log.info("2. "+vo.getAverageCost());

				adPvclkVOs.add(vo);

			}

			Collections.sort(adPvclkVOs);

		}

		return adPvclkVOs;
	}

	private List<AdPvclkVO> sortAdPvclkVOs(List<Object> adPvClkCosts, int pageSize, float sortPrice) throws Exception{

		List<AdPvclkVO> adPvclkVOs = null;
		//log.info("  adPvClkCosts  = "+adPvClkCosts.size());
		if(adPvClkCosts != null && adPvClkCosts.size() > 0){

			adPvclkVOs = new ArrayList<AdPvclkVO>();

			int i = 0;
			//log.info("  adPvClkCosts  = "+adPvClkCosts);

			for(Object object:adPvClkCosts){

				i++;

				if(i > pageSize)break;

				Object[] ob = (Object[])object;
				//log.info(ob[0]+" , "+ob[1]+" , "+ob[2]+" , "+ob[3]+" , "+ob[4]);
				//float sortPrice = Float.parseFloat(ob[2].toString());

				AdPvclkVO vo = new AdPvclkVO(ob, sortPrice);

				//log.info("1. "+vo.getAdClickRate());
				//log.info("2. "+vo.getAverageCost());

				adPvclkVOs.add(vo);

			}

			Collections.sort(adPvclkVOs);

		}

		return adPvclkVOs;
	}

	@Override
    public List<Object> accountPvclkSum(String customerInfoId, Date date){

		return ((IPfpAdPvclkDAO)dao).accountPvclkSum(customerInfoId, date);
	}

	@Override
    public List<Object> adActionPvclkSum(String customerInfoId, Date date) {

		return ((IPfpAdPvclkDAO)dao).adActionPvclkSum(customerInfoId, date);
	}

    @Override
    public float customerCost(String pfpCustomerId, Date pvclkDate) {
        return ((IPfpAdPvclkDAO)dao).customerCost(pfpCustomerId, pvclkDate);
    }

    @Override
	public float actionCost(String actionId, Date pvclkDate) {
	    return ((IPfpAdPvclkDAO)dao).actionCost(actionId, pvclkDate);
	}

	@Override
    public Float[] selectAdPvclkSumByPvclkDate(String pvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq, String adSeq) {
        return ((IPfpAdPvclkDAO)dao).selectAdPvclkSumByPvclkDate(pvclkDate, customerInfoId, adActionSeq, adGroupSeq, adSeq);
    }

    @Override
    public Map<String, int[]> selectPfpAdPvclkSums(Date pvclkDate) {
        Map<String, int[]> map = new HashMap<String, int[]>();

        List<Object[]> list = ((IPfpAdPvclkDAO) dao).selectPfpAdPvclkSums(pvclkDate);
        String id = null;
        int[] sums = null;

        for (Object[] objs: list) {
            // ad_seq
            id = (String) objs[0];

            // sum(ad_pv), sum(ad_clk)
            sums = new int[objs.length-1];
            for (int j = 1; j < objs.length; j++) {
                sums[j-1] = ConvertUtil.convertInteger(objs[j]);
            }

//            log.info(id + " " + sums[0] + " " + sums[1]);

            map.put(id, sums);
        }

        return map;
    }

	@Override
    public List<PfpAdPvclk> selectPfpAdPvclk(int firstResult, int maxResults) {
	    return ((IPfpAdPvclkDAO)dao).selectPfpAdPvclk(firstResult, maxResults);
	}

	@Override
    public float totalSysAdPvclk(Date startDate, Date endDate) {
		 return ((IPfpAdPvclkDAO)dao).totalSysAdPvclk(startDate, endDate);
	}

	@Override
    public float totalPfbAdPvclk(String pfbId, Date startDate, Date endDate) {
		 return ((IPfpAdPvclkDAO)dao).totalPfbAdPvclk(pfbId, startDate, endDate);
	}

	@Override
    public List<CheckBillReportVo> findCheckBillReportToVo(String searchDate) {

		List<CheckBillReportVo> vos = null;

		List<Object[]> objects = ((IPfpAdPvclkDAO)dao).findPfpAdPvclks(DateValueUtil.getInstance().stringToDate(searchDate));

		if(!objects.isEmpty()){

			vos = new ArrayList<CheckBillReportVo>();

			for (Object[] objs: objects) {

				String pfpId = (String) objs[0];

				if(pfpId != null){

					CheckBillReportVo vo = new CheckBillReportVo();

					float pvClkPrice = Float.valueOf(objs[1].toString());
					pvClkPrice = (float)Math.floor(pvClkPrice);
					float invalidClkPrice = Float.valueOf(objs[2].toString());

					if(pvClkPrice > 0){

						vo.setDate(searchDate);
						vo.setPfpId(pfpId);
						vo.setPvClkPrice(pvClkPrice);
						vo.setInvalidClkPrice(invalidClkPrice);

						vos.add(vo);
					}

				}

			}

			Date date = DateValueUtil.getInstance().stringToDate(searchDate);
			List<Object[]> admTransLosses = admTransLossDAO.findTransLosses(date);
			// 交易明細 trans_detail
			List<PfpTransDetail> transDetails = pfpTransDetailDAO.findPfpLastTransDetails(date);
			//log.info(" transDetails: "+transDetails.size());
			// 攤提明細 recognize_detail
			List<Object[]> recognizePrices = admRecognizeDetailDAO.findRecognizePrice(date);
			//log.info(" recognizePrices: "+recognizePrices);
			// 攤提記錄 recognize_record
			List<Object[]> recognizeRemains = admRecognizeRecordDAO.findPfpRemain(date);
			//List<Object[]> recognizeRemains = admRecognizeRecordDAO.findRecognizeRemain(date);
			//log.info(" recognizeRemains: "+recognizeRemains);
			// 攤提明細 recognize_detail
			List<Object[]> recognizeTotalPrices = admRecognizeDetailDAO.findRecognizePriceBeforeDate(date);
			//log.info(" recognizeTotalPrices: "+recognizeTotalPrices);

			if(vos != null && !vos.isEmpty()){

				for(CheckBillReportVo vo:vos){

					// 超播金額
					for (Object[] objs: admTransLosses) {

						String pfpId = (String) objs[0];

						if(pfpId != null && vo.getPfpId().equals(pfpId)){

							float transLoss = Float.valueOf(objs[1].toString());
							vo.setTransLoss(transLoss);

							break;
						}
					}

					// 前端顯示當天交易金額和餘額
					for(PfpTransDetail transDetail:transDetails){

						if(vo.getPfpId().equals(transDetail.getPfpCustomerInfo().getCustomerInfoId())){

							// 花費金額
							if(transDetail.getTransType().equals(EnumTransType.SPEND_COST.getTypeId())){
								vo.setTransPrice(transDetail.getTransPrice());
							}

							// 所剩餘額
							vo.setTransRemain(transDetail.getRemain());
						}
					}

					// 當天攤提費用
					for (Object[] objs: recognizePrices) {

						String pfpId = (String) objs[0];

						if(pfpId != null && vo.getPfpId().equals(pfpId)){

							float orderPrice = Float.valueOf(objs[1].toString());
							vo.setRecognizePrice(orderPrice);

							break;
						}
					}
					// 當天攤提餘額
					for (Object[] objs: recognizeRemains) {

						String pfpId = (String) objs[0];

						if(pfpId != null && vo.getPfpId().equals(pfpId)){

							float orderRemain = Float.valueOf(objs[1].toString());

							vo.setRecognizeRemain(orderRemain);

							break;
						}
					}


					for (Object[] objs: recognizeTotalPrices) {


						String pfpId = (String) objs[0];

						if(pfpId != null && vo.getPfpId().equals(pfpId)){

							float totalPrice = Float.valueOf(objs[1].toString());
							float orderRemain = vo.getRecognizeRemain() - totalPrice;

							vo.setRecognizeRemain(orderRemain);

							break;
						}
					}

				}
			}

		}

		return vos;
	}

	public void setAdmTransLossDAO(IAdmTransLossDAO admTransLossDAO) {
		this.admTransLossDAO = admTransLossDAO;
	}

	public void setPfpTransDetailDAO(IPfpTransDetailDAO pfpTransDetailDAO) {
		this.pfpTransDetailDAO = pfpTransDetailDAO;
	}

	public void setAdmRecognizeDetailDAO(
			IAdmRecognizeDetailDAO admRecognizeDetailDAO) {
		this.admRecognizeDetailDAO = admRecognizeDetailDAO;
	}

	public void setAdmRecognizeRecordDAO(
			IAdmRecognizeRecordDAO admRecognizeRecordDAO) {
		this.admRecognizeRecordDAO = admRecognizeRecordDAO;
	}

	@Override
	public Integer updateEmptyPfbxCustomerPostion(Date date,
			String pfbxCustomerID, String pfbxPostionID) {
		return ((IPfpAdPvclkDAO)dao).updateEmptyPfbxCustomerPostion(date, pfbxCustomerID, pfbxPostionID);
	}

	@Override
	public Integer updateEmptyUrl(Date date) {
		return ((IPfpAdPvclkDAO)dao).updateEmptyUrl(date);
	}

    @Override
    public PfpAdPvclk findLastPfpAdPvclk() {
        PfpAdPvclk pfpAdPvclk = null;

        List<PfpAdPvclk> list = ((IPfpAdPvclkDAO) dao).findLastPfpAdPvclk();
        if (list.size() > 0) {
            pfpAdPvclk = list.get(0);
        }

        return pfpAdPvclk;
    }

    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        return ((IPfpAdPvclkDAO)dao).deleteMalice(recordDate, recordTime);
    }

    @Override
    public Map<String,Boolean> checkPvClk(String date) {
    	return ((IPfpAdPvclkDAO)dao).checkPvClk(date);
    }

    @Override
    public boolean insertSelect(Date date) {
        int selectCount = 0;
        int replaceCount = 0;
        int deleteCount = 0;

        // select by date
        PfpAdPvclk pfpAdPvclk = null;
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            date = calendar.getTime();

            pfpAdPvclk = ((IPfpAdPvclkDAO)dao).selectOneBeforeDate(date);
        }
        else {
            pfpAdPvclk = ((IPfpAdPvclkDAO)dao).selectOneByDate(date);
        }

        if (pfpAdPvclk == null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            log.info("date " + df.format(date));
            log.info("pfpAdPvclk = null");
            return true;
        }

        Date pvclkDate = pfpAdPvclk.getAdPvclkDate();
        log.info("pvclkDate " + pvclkDate);

        // select backup by date
        pfpAdPvclk = ((IPfpAdPvclkDAO)dao).selectBackupByDate(pvclkDate);
        if (pfpAdPvclk != null) {
            log.info("pfpAdPvclk backup != null");
            return false;
        }

        // select count
        selectCount = ((IPfpAdPvclkDAO)dao).selectCountByDate(pvclkDate);
        log.info("select " + selectCount);

        // replace
        replaceCount = ((IPfpAdPvclkDAO)dao).replaceSelectByDate(pvclkDate);
        log.info("replace " + replaceCount);

        if (selectCount != replaceCount) {
            return false;
        }

        // delete
        deleteCount = ((IPfpAdPvclkDAO)dao).deleteByDate(pvclkDate);
        log.info("delete " + deleteCount);

        if (replaceCount != deleteCount) {
            return false;
        }

        return true;
    }
}
