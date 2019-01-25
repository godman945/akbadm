package com.pchome.akbadm.db.service.report;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.report.IAdKeywordReportDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdKeywordDAO;
import com.pchome.akbadm.db.pojo.PfdKeywordReport;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.KeywordReportVO;
import com.pchome.config.TestConfig;

public class AdKeywordReportService extends BaseService<PfdKeywordReport, String> implements IAdKeywordReportService {

	private IAdKeywordReportDAO adKeywordReportDAO;
	private IPfpAdKeywordDAO pfpAdKeywordDAO;
	
	public void setPfpAdKeywordDAO(IPfpAdKeywordDAO pfpAdKeywordDAO) {
		this.pfpAdKeywordDAO = pfpAdKeywordDAO;
	}

	public void setAdKeywordReportDAO(IAdKeywordReportDAO adKeywordReportDAO) {
		this.adKeywordReportDAO = adKeywordReportDAO;
	}

	public List<KeywordReportVO> getAdKeywordReportList(String startDate, String endDate,
			String adKeywordType, String sortMode, int displayCount,
			String pfdCustomerInfoId) throws Exception {

		return adKeywordReportDAO.getAdKeywordReportList(startDate, endDate, adKeywordType,
				sortMode, displayCount, pfdCustomerInfoId);
	}

	public List<KeywordReportVO> getAdKeywordReportDetail(String startDate, String endDate,
			String adKeywordType, String sortMode, int displayCount, String keyword,
			String pfdCustomerInfoId) throws Exception {

		List<KeywordReportVO> list = adKeywordReportDAO.getAdKeywordReportDetail(startDate, endDate,
				adKeywordType, sortMode, displayCount, keyword, pfdCustomerInfoId);

		KeywordReportVO vo = null;
		String adKeywordSeq = null;
		PfpAdKeyword pojo = null;
		for (int i=0; i<list.size(); i++) {
			vo = list.get(i);
			adKeywordSeq = vo.getKeywordSeq();
			pojo = pfpAdKeywordDAO.findPfpAdKeywordBySeq(adKeywordSeq);
			vo.setAdAction(pojo.getPfpAdGroup().getPfpAdAction().getAdActionName());
			vo.setAdGroup(pojo.getPfpAdGroup().getAdGroupName());
		}

		return list;
	}

	public List<KeywordReportVO> getAdKeywordOfferPriceReportList(String startDate, String endDate,
			String adKeywordType, String searchText, int displayCount,
			String pfdCustomerInfoId) throws Exception {

		DecimalFormat numberFormat = new DecimalFormat("###,###,###,###");

		List<KeywordReportVO> list = adKeywordReportDAO.getAdKeywordOfferPriceReportList(startDate, endDate,
				adKeywordType, searchText, displayCount, pfdCustomerInfoId);

		KeywordReportVO vo = null;
		String adKeywordSeq = null;
		PfpAdKeyword pojo = null;
		for (int i=0; i<list.size(); i++) {
			vo = list.get(i);
			adKeywordSeq = vo.getKeywordSeq();
			pojo = pfpAdKeywordDAO.findPfpAdKeywordBySeq(adKeywordSeq);
			vo.setAdAction(pojo.getPfpAdGroup().getPfpAdAction().getAdActionName());
			vo.setAdGroup(pojo.getPfpAdGroup().getAdGroupName());
			vo.setKeyword(pojo.getAdKeyword());
			vo.setOfferPrice(numberFormat.format(pojo.getAdKeywordSearchPrice()));
		}

		return list;
	}
	
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        IAdKeywordReportService service = (IAdKeywordReportService) context.getBean("AdKeywordReportService");

        System.out.println(">>> service start");

        //List<KeywordReportVO> voList = service.getAdKeywordReportList("2013-03-11", "2013-03-14", "1", "pv_sum");
        //List<KeywordReportVO> voList = service.getAdKeywordReportList("2013-03-11", "2013-03-14", "1", "clk_sum");
        //List<KeywordReportVO> voList = service.getAdKeywordReportList("2013-03-11", "2013-03-14", "1", "price_sum");
        //List<KeywordReportVO> voList = service.getAdKeywordReportList("2013-03-11", "2013-03-14", "2", "price_sum");
        //List<KeywordReportVO> voList = service.getAdKeywordReportList("2013-03-11", "2013-03-14", "", "price_sum", 1000);
        //System.out.println(">>> voList.size() = " + voList.size());

//        for (int i=0; i<voList.size(); i++) {
//        	KeywordReportVO vo = voList.get(i);
//            System.out.println("----------" + i + "----------");
//            System.out.println(">>> vo.getKeyword() = " + vo.getKeyword());
//            System.out.println(">>> vo.getKwPvSum() = " + vo.getKwPvSum());
//            System.out.println(">>> vo.getKwClkSum() = " + vo.getKwClkSum());
//            System.out.println(">>> vo.getKwPriceSum() = " + vo.getKwPriceSum());
//            System.out.println(">>> vo.getKwClkRate() = " + vo.getKwClkRate());
//            System.out.println(">>> vo.getClkPriceAvg() = " + vo.getClkPriceAvg());
//        }
    }
}
