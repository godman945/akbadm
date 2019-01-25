package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pchome.akbadm.db.dao.pfbx.IPfbxUserOptionDAO;
import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdUrlReportDAO;
import com.pchome.akbadm.db.pojo.AdmPfbxBlockUrl;
import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxUserOption;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public class PfbxAdUrlReportService extends BaseService<PfbxAdUrlReport, Integer> implements IPfbxAdUrlReportService {
	
	private IPfbxUserOptionDAO pfbxUserOptionDAO;
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId_V20170307(PfbxCustomerInfo pfbInfo , String startDate , String endDate , String searchUrl , String domain) throws Exception
	{
		List<PfbAdUrlListVO> listRS = new ArrayList<PfbAdUrlListVO>();
		
		//符合
		List<String> list = ((IPfbAdUrlReportDAO) dao).getErrorUrlByPfbId2(pfbInfo.getCustomerInfoId(), startDate, endDate, searchUrl, domain);
		log.info("...listS=" + list.size());
		
		//已被封鎖的不顯示
		List<String> urls = new ArrayList<String>();
		
		Set<AdmPfbxBlockUrl> blockeds = pfbInfo.getAdmPfbxBlockUrls();
		for(AdmPfbxBlockUrl blocked : blockeds)
		{
			urls.add(blocked.getBlockUrl());
		}
//		List<PfbxUserOption> pfbxuos = pfbxUserOptionDAO.getSYSBypfbId(pfbId);
//		if(pfbxuos.size() == 1)
//		{
//			PfbxUserOption pfbxuo = pfbxuos.get(0);
//			List<PfbxBlockCusurl> Pfbxbcs = new ArrayList<PfbxBlockCusurl>(pfbxuo.getPfbxBlockCusurls());
//			
//			for(PfbxBlockCusurl pfbxbc : Pfbxbcs)
//			{
//				urls.add(pfbxbc.getUrl());
//			}
//		}
		//已被封鎖的不顯示 end
		
		for(String url : list)
		{
			if(!urls.contains(url))
			{
				PfbAdUrlListVO vo = new PfbAdUrlListVO();
				vo.setDetailurl(url);
				listRS.add(vo);
			}
		}
		
		return listRS;
	}
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId2(String pfbId , String startDate , String endDate , String searchUrl , String domain) throws Exception
	{
		List<PfbAdUrlListVO> listRS = new ArrayList<PfbAdUrlListVO>();
		
		//符合
		List<String> list = ((IPfbAdUrlReportDAO) dao).getErrorUrlByPfbId2(pfbId, startDate, endDate, searchUrl, domain);
		log.info("...listS=" + list.size());
		
		//已被封鎖的不顯示
		List<String> urls = new ArrayList<String>();
		List<PfbxUserOption> pfbxuos = pfbxUserOptionDAO.getSYSBypfbId(pfbId);
		if(pfbxuos.size() == 1)
		{
			PfbxUserOption pfbxuo = pfbxuos.get(0);
			List<PfbxBlockCusurl> Pfbxbcs = new ArrayList<PfbxBlockCusurl>(pfbxuo.getPfbxBlockCusurls());
			
			for(PfbxBlockCusurl pfbxbc : Pfbxbcs)
			{
				urls.add(pfbxbc.getUrl());
			}
		}
		//已被封鎖的不顯示 end
		
		for(String url : list)
		{
			if(!urls.contains(url))
			{
				PfbAdUrlListVO vo = new PfbAdUrlListVO();
				vo.setDetailurl(url);
				listRS.add(vo);
			}
		}
		
		return listRS;
	}
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId(String pfbId , String startDate , String endDate , String searchUrl) throws Exception
	{
		//符合
		List<PfbAdUrlListVO> list= ((IPfbAdUrlReportDAO) dao).getErrorUrlByPfbId(pfbId, startDate, endDate , searchUrl);
		List<PfbAdUrlListVO> listRS = new ArrayList<PfbAdUrlListVO>();
		
		log.info("...listS=" + list.size());
		log.info("...listRS=" + listRS.size());
		
		//已被封鎖的不顯示
		List<String> urls = new ArrayList<String>();
		List<PfbxUserOption> pfbxuos = pfbxUserOptionDAO.getSYSBypfbId(pfbId);
		if(pfbxuos.size() == 1)
		{
			PfbxUserOption pfbxuo = pfbxuos.get(0);
			List<PfbxBlockCusurl> Pfbxbcs = new ArrayList<PfbxBlockCusurl>(pfbxuo.getPfbxBlockCusurls());
			
			for(PfbxBlockCusurl pfbxbc : Pfbxbcs)
			{
				urls.add(pfbxbc.getUrl());
			}
		}
		//已被封鎖的不顯示 end
		
		for(PfbAdUrlListVO vo : list)
		{
			if(!urls.contains(vo.getDetailurl()))
			{
				listRS.add(vo);
			}
		}
		
		
		return listRS;
	}
	
	public List<PfbxAdUrlReport> getListByPfbId(String pfbId) throws Exception
	{
		return ((IPfbAdUrlReportDAO) dao).getListByPfbId(pfbId);
	}
	
	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdUrlReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdUrlReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdUrlReport> dataList) throws Exception {
		((IPfbAdUrlReportDAO) dao).insertReportData(dataList);
	}

	public void setPfbxUserOptionDAO(IPfbxUserOptionDAO pfbxUserOptionDAO)
	{
		this.pfbxUserOptionDAO = pfbxUserOptionDAO;
	}
	
}
