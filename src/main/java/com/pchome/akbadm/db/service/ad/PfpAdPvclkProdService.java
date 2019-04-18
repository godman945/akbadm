package com.pchome.akbadm.db.service.ad;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdPvclkProdDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkProd;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ProdAdReportVO;

public class PfpAdPvclkProdService extends BaseService<PfpAdPvclkProd, String> implements IPfpAdPvclkProdService{

	public List<Object> getProdAdDetailReport(Map<String, String> conditionMap) throws Exception{
		List<Map<String,Object>> prodAdDetailReport = ((IPfpAdPvclkProdDAO)dao).getProdAdDetailReport(conditionMap);
		List<Object> prodAdReportVOBeanList = new ArrayList<Object>();
		ProdAdReportVO prodAdReportVOBean = null;
		DecimalFormat df = new DecimalFormat("0.00");
		
		if( (!prodAdDetailReport.isEmpty()) && (prodAdDetailReport.size()>0) ){
			for (Object object : prodAdDetailReport) {
				prodAdReportVOBean = new ProdAdReportVO();
				Map obj = (Map) object;
				
				double clk = Double.parseDouble(obj.get("clk").toString());
				double pv = Double.parseDouble(obj.get("pv").toString());
				prodAdReportVOBean.setPfpCustomerInfoId(obj.get("pfp_customer_info_id").toString());//客戶帳號
				prodAdReportVOBean.setProdName(obj.get("ec_name").toString());//商品名稱
				prodAdReportVOBean.setProdImg(obj.get("ec_img").toString());//商品圖
				prodAdReportVOBean.setAdSeq(obj.get("ad_seq").toString());//廣告序號
				prodAdReportVOBean.setCatalogSeq(obj.get("catalog_seq").toString());//商品目錄ID
				prodAdReportVOBean.setCatalogProdSeq(obj.get("catalog_prod_seq").toString());//商品ID
				prodAdReportVOBean.setClk(obj.get("clk").toString());//商品點選數
				prodAdReportVOBean.setPv(obj.get("pv").toString());//曝光次數
				prodAdReportVOBean.setClkRate(df.format((clk / pv)*100) + "%");//(商品點選數/曝光次數)=商品點選率
				
				
				prodAdReportVOBeanList.add(prodAdReportVOBean);
			}
		}
		return prodAdReportVOBeanList;
	}
	
	
	public ProdAdReportVO getSumProdAdDetailReport(Map<String, String> conditionMap) throws Exception{
		List<Map<String,Object>> prodAdDetailReport = ((IPfpAdPvclkProdDAO)dao).getSumProdAdDetailReport(conditionMap);
		ProdAdReportVO prodAdReportVOBean = new ProdAdReportVO();
		DecimalFormat df = new DecimalFormat("0.00");
		double sumClk = 0; 
		double sumPv = 0;
		double sumClkRate= 0;
		int count = 0;
		
		if( (!prodAdDetailReport.isEmpty()) && (prodAdDetailReport.size()>0) ){
			for (Object object : prodAdDetailReport) {
				Map obj = (Map) object;
				
				double clk = Double.parseDouble(obj.get("clk").toString());
				double pv = Double.parseDouble(obj.get("pv").toString());
				
				sumClk = sumClk+clk;
				sumPv = sumPv+pv;
				count= count+1;
			}
		}
		
		prodAdReportVOBean.setClk(String.valueOf(sumClk));
		prodAdReportVOBean.setPv(String.valueOf(sumPv));
		sumClkRate = (sumClk / sumPv)*100;
		prodAdReportVOBean.setClkRate(df.format(sumClkRate) + "%");
		prodAdReportVOBean.setRowTotal(Integer.toString(count));
		
		return prodAdReportVOBean;
	}

	
}
