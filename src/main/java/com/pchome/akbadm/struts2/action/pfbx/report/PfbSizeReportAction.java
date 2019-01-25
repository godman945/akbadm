package com.pchome.akbadm.struts2.action.pfbx.report;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.IPfbxSizeService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.report.IPfbxSizeGetAdClickService;
import com.pchome.akbadm.db.service.pfbx.report.IPfbxSizeReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;
import com.pchome.akbadm.struts2.BaseAction;

import org.apache.commons.lang.StringUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;

public class PfbSizeReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private IPfbxSizeReportService pfbxSizeReportService;
	private IPfbxSizeService pfbxSizeService;
	private IPfbxSizeGetAdClickService pfbxSizeGetAdClickService;
	private Map<String, String> sizeMap;

    //輸入參數
	private String startDate;
	private String endDate;
	private String size;
	private String searchCategory;
	private String searchOption;
	private String searchText;
	private String pfbxCustomerInfoId;
	private String adPvclkDate;
	
	private PfbxCustomerReportVo pfbxCustomerReportVo;
	
	private List<PfbxCustomerInfo> list = new ArrayList<PfbxCustomerInfo>();
	private List<PfbxReportVO> voList = new ArrayList<PfbxReportVO>();
	//訊息
	private String message = "";
	
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 50;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private long totalCount = 0;   				// 初始化共幾筆
	
	private long totalSize = 0;					//總比數						
	private long totalPv = 0;					//曝光數	
	private long totalClk = 0;					//點擊數	
	private float totalClkRate = 0;				//點擊率
	private float totalAdClkAvgPrice = 0;		//單次點擊收益
	private float totalAdPvRate = 0;			//千次曝光收益
	private float totalAdClkPriceSum = 0;		//總廣告花費
	private float totalBonus = 0;				//預估收益

	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	//點擊分佈圖
	private Integer totalClick = 0;
	private Integer tClick = 0;
	private Integer fClick = 0;
	private Integer nClick = 0;
	private Integer iClick = 0;
	private InputStream imageStream;
	
    public String execute() throws Exception {
    	
    	sizeMap();
    	
    	return SUCCESS;
    }	
    public String sizeSearch() throws Exception {
    	sizeMap();
    	log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> size = " + size);
		log.info(">>> searchText = " + searchText);
    	
    	Map<String, String> conditionMap = new HashMap<String, String>();
    	List<PfbxReportVO> viewList = new ArrayList<PfbxReportVO>();
    	
    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			list = new ArrayList<PfbxCustomerInfo>();
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);
	    	conditionMap.put("size", size);
	    	
	    	if(StringUtils.isNotBlank(searchCategory)){
	    		conditionMap.put("searchCategory", searchCategory);
	    	}
	    	
	    	if(StringUtils.isNotBlank(searchOption)){
	    		if (StringUtils.isNotBlank(searchText)){
	    			log.info("searchOption: "+searchOption);
	    			log.info("searchText: "+searchText);
	    			conditionMap.put(searchOption, searchText);
	    		}		
	    	} else {
	    		if (StringUtils.isNotBlank(searchText)){
	    			conditionMap.put("searchAll", searchText);
	    		}
	    	}
	    	
	    	voList = new ArrayList<PfbxReportVO>();
	    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	    	voList = pfbxSizeReportService.getPfbxSizeReportByCondition(conditionMap);
	    	log.info("voList: "+voList.size());
	    	
	    	if(voList.size()==0){
	    		
	    		this.message = "查無資料！";
	    	}else{
	    		
	    		totalCount = voList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;
	    		
		    	for (int i=0; i<voList.size(); i++) {
		    		PfbxReportVO vo = new PfbxReportVO();
	    			vo = voList.get(i);
	    			log.info("getAdPvSum: "+vo.getAdPvSum());
	    			
	    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
	    				viewList.add(vo);
	    			}
	    			
	    			if(vo.getAdPvSum() != null){
	    				totalPv += Integer.parseInt(vo.getAdPvSum().replaceAll(",", ""));		
	    			}
	    			if(vo.getAdClkSum() != null){
	    				totalClk += Integer.parseInt(vo.getAdClkSum().replaceAll(",", "")); 				
	    			}
	    			if(vo.getTotalBonus() != null){
	    				totalBonus += Double.parseDouble(vo.getTotalBonus().replaceAll(",", ""));	
	    				vo.setTotalBonus(df3.format(Double.parseDouble(vo.getTotalBonus())));
	    			}
	    			if(vo.getAdClkPriceSum() != null){
	    				totalAdClkPriceSum += Double.parseDouble(vo.getAdClkPriceSum().replaceAll(",", ""));	
	    			}
					
					if (totalClk==0 || totalPv==0) {
						totalClkRate = 0;
					} else {
						totalClkRate = (float) ((double) totalClk*100/totalPv);
					}
					
					if (totalBonus==0 || totalClk==0) {
						totalAdClkAvgPrice = 0;
					} else {
						totalAdClkAvgPrice = (float) totalBonus/totalClk;
					}
					
					if(totalBonus==0 || totalPv==0){
						totalAdPvRate = 0;
					} else {
						totalAdPvRate = (float) totalBonus*1000/totalPv;
					}	
				}
		    	
	    	}
    	
		}
    	
    	if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
    	
    	voList = viewList;
    	
		return SUCCESS;
	}
    
    private void makeDownloadReportData() throws Exception {
    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告尺寸成效報表_" + dformat.format(new Date()) + ".csv";
    	DecimalFormat df = new DecimalFormat("0.00");
    	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    	String[] tableHeadArray = {"期間","帳戶編號","帳戶類別","網站名稱","廣告尺寸","廣告曝光數","廣告點擊數","廣告點擊率","單次點擊收益","千次曝光收益","預估收益"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,廣告尺寸成效");
		content.append("\n\n");
		content.append("廣告尺寸:,");
		if (StringUtils.isNotBlank(size)){
			content.append(size);
		} else {
			content.append("全部");
		}
		content.append("\n\n");
		
		content.append("帳戶類別:,");
		if(StringUtils.isNotBlank(searchCategory)){
			if("1".equals(searchCategory)){
				content.append("個人戶");
			} else {
				content.append("公司戶");
			}
		} else {
			content.append("全部");
		}
		content.append("\n\n");	
		
		if(StringUtils.isNotBlank(searchOption)){
    		if (StringUtils.isNotBlank(searchText)){
    			if("customerInfoId".equals(searchOption)){
    				content.append("帳戶編號:,");
    			} else {
    				content.append("網站名稱:,");
    			}
    			content.append(searchText);
    			content.append("\n\n");
    		}		
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			content.append("帳戶編號/網站名稱:,");
    			content.append(searchText);
    			content.append("\n\n");
    		}
    	}
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<voList.size(); i++) {
    		PfbxReportVO vo = new PfbxReportVO();
			vo = voList.get(i);
			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getCustomerInfoId() + "\",");
			content.append("\"" + vo.getWebsiteCategory() + "\",");
			content.append("\"" + vo.getWebsiteChineseName() + "\",");
			content.append("\"" + vo.getAdPvclkSize() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvRate() + "\",");
			content.append("\"$ " + vo.getTotalBonus() + "\"");
			content.append("\n");
		}
		
		//總計
		content.append("\"總計:" + totalSize + "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" + df2.format(totalPv) + "\",");
		content.append("\"" + df2.format(totalClk) + "\",");
		content.append("\"" + df.format(totalClkRate) + "%\",");
		content.append("\"$ " + df3.format(totalAdClkAvgPrice) + "\",");
		content.append("\"$ " + df3.format(totalAdPvRate) + "\",");
		content.append("\"$ " + df3.format(totalBonus) + "\"");
		
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
    }
    
    public void sizeMap() {
		List<PfbxSize> sizeList = pfbxSizeService.loadAll();
		sizeMap = new TreeMap<String, String>();
		for(PfbxSize size : sizeList){
			String sizeName = String.valueOf(size.getWidth())+ " x "+String.valueOf(size.getHeight());
			sizeMap.put(sizeName, sizeName);
		}
	}

    public String getClick() throws Exception {
    	log.info(">>> pfbxCustomerInfoId = " + pfbxCustomerInfoId);
		log.info(">>> adPvclkDate = " + adPvclkDate);
		log.info(">>> size = " + size);
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		if(StringUtils.isNotBlank(pfbxCustomerInfoId)){
    		conditionMap.put("pfbxCustomerInfoId", pfbxCustomerInfoId);
    	}
		
		if(StringUtils.isNotBlank(adPvclkDate)){
    		conditionMap.put("adPvclkDate", adPvclkDate);
    	}
		
		String width = "0";
		String height = "0";
		if(StringUtils.isNotBlank(size)){
			String[] searchSize = size.split("x");
			width = searchSize[0].trim();
			height = searchSize[1].trim();
    		conditionMap.put("width", width);
    		conditionMap.put("height", height);
    	}
		
		List<PfpAdClickVO> adClickList = new ArrayList<PfpAdClickVO>();
		adClickList = pfbxSizeGetAdClickService.getAdClickMouseDownList(conditionMap);
		
		List<PfpAdClickVO> adClickForTList = new ArrayList<PfpAdClickVO>();
		List<PfpAdClickVO> adClickForFList = new ArrayList<PfpAdClickVO>();
		List<PfpAdClickVO> adClickForNList = new ArrayList<PfpAdClickVO>();
		
		for(PfpAdClickVO vo:adClickList){
			String mouseMoveFlag = vo.getMouseMoveFlag();
			if(StringUtils.equals("T", mouseMoveFlag)){
				adClickForTList.add(vo);
			} else if(StringUtils.equals("F", mouseMoveFlag)){
				adClickForFList.add(vo);
			} else if(StringUtils.equals("N", mouseMoveFlag)){
				adClickForNList.add(vo);
			}
		}
    	
		Integer paddingWidth = Integer.parseInt(width) - 10;
		Integer paddingHeight = Integer.parseInt(height) - 10;
		
		tClick = adClickForTList.size();
		for(PfpAdClickVO tVO: adClickForTList){
			Integer x = Integer.parseInt(tVO.getMouseDownX());
			Integer y = Integer.parseInt(tVO.getMouseDownY());
			if(x<10 || x>paddingWidth || y<10 || y>paddingHeight){
				iClick ++;
			}
		}
		fClick = adClickForFList.size();
		for(PfpAdClickVO fVO: adClickForFList){
			Integer x = Integer.parseInt(fVO.getMouseDownX());
			Integer y = Integer.parseInt(fVO.getMouseDownY());
			if(x<10 || x>paddingWidth || y<10 || y>paddingHeight){
				iClick ++;
			}
		}
		nClick = adClickForNList.size();
		totalClick = tClick + fClick + nClick;
		
    	return SUCCESS;
    }
    
	public String getClickImg() throws Exception {
    	
    	log.info(">>> pfbxCustomerInfoId = " + pfbxCustomerInfoId);
		log.info(">>> adPvclkDate = " + adPvclkDate);
		log.info(">>> size = " + size);
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		if(StringUtils.isNotBlank(pfbxCustomerInfoId)){
    		conditionMap.put("pfbxCustomerInfoId", pfbxCustomerInfoId);
    	}
		
		if(StringUtils.isNotBlank(adPvclkDate)){
    		conditionMap.put("adPvclkDate", adPvclkDate);
    	}
		
		String width = "0";
		String height = "0";
		if(StringUtils.isNotBlank(size)){
			String[] searchSize = size.split("x");
			width = searchSize[0].trim();
			height = searchSize[1].trim();
    		conditionMap.put("width", width);
    		conditionMap.put("height", height);
    	}
		
		List<PfpAdClickVO> adClickList = new ArrayList<PfpAdClickVO>();
		adClickList = pfbxSizeGetAdClickService.getAdClickMouseDownList(conditionMap);
		
		int w = Integer.parseInt(width);
		int h = Integer.parseInt(height);
		
		BufferedImage bImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g1=(Graphics2D) bImage.getGraphics();;
	    g1.setColor(new Color(197,228,251));
	    g1.fillRect(0, 0, w, h);
	    
	    Rectangle rectangle = new Rectangle(10, 10, w -20, h-20);
	    g1.setColor(new Color(255,255,255));
	    g1.fillRect(10, 10, w -20, h-20);
		
		for(PfpAdClickVO vo:adClickList){
			String mouseMoveFlag = vo.getMouseMoveFlag();
			int mouseDownX = Integer.parseInt(vo.getMouseDownX());
			int mouseDownY = Integer.parseInt(vo.getMouseDownY());
			
			if(StringUtils.equals("T", mouseMoveFlag)){
				if(rectangle.contains(mouseDownX, mouseDownY)){
					g1.setColor(new Color(0,255,0));
				} else {
					g1.setColor(new Color(0,0,0));
				}
				g1.fillArc(mouseDownX -3,mouseDownY-3,6,6,0,360);
			} else if(StringUtils.equals("F", mouseMoveFlag)){
				if(rectangle.contains(mouseDownX, mouseDownY)){
					g1.setColor(new Color(255,0,0));
				} else {
					g1.setColor(new Color(0,0,0));
				}
				g1.fillArc(mouseDownX-3,mouseDownY-3,6,6,0,360);
			}
		}
		g1.dispose();	    
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(bImage, "png", output);
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>imgWidth=" + bImage.getWidth());
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>imgWidthHeight=" + bImage.getHeight());
		
		this.imageStream = new ByteArrayInputStream(output.toByteArray());  
        output.close();
		
        return SUCCESS;
    }
    
	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchCategory() {
		return searchCategory;
	}
	
	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}
	
	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public PfbxCustomerReportVo getPfbxCustomerReportVo() {
		return pfbxCustomerReportVo;
	}

	public void setPfbxCustomerReportVo(PfbxCustomerReportVo pfbxCustomerReportVo) {
		this.pfbxCustomerReportVo = pfbxCustomerReportVo;
	}

	public List<PfbxCustomerInfo> getList() {
		return list;
	}

	public void setList(List<PfbxCustomerInfo> list) {
		this.list = list;
	}

	public IPfbxCustomerInfoService getPfbxCustomerInfoService() {
		return pfbxCustomerInfoService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PfbxReportVO> getVoList() {
		return voList;
	}

	public void setVoList(List<PfbxReportVO> voList) {
		this.voList = voList;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	public IPfbxSizeService getPfbxSizeService() {
		return pfbxSizeService;
	}
	public void setPfbxSizeService(IPfbxSizeService pfbxSizeService) {
		this.pfbxSizeService = pfbxSizeService;
	}
	public Map<String, String> getSizeMap() {
		return sizeMap;
	}
	public void setSizeMap(Map<String, String> sizeMap) {
		this.sizeMap = sizeMap;
	}
	public IPfbxSizeReportService getPfbxSizeReportService() {
		return pfbxSizeReportService;
	}
	public void setPfbxSizeReportService(
			IPfbxSizeReportService pfbxSizeReportService) {
		this.pfbxSizeReportService = pfbxSizeReportService;
	}
	public void setPfbxSizeGetAdClickService(
			IPfbxSizeGetAdClickService pfbxSizeGetAdClickService) {
		this.pfbxSizeGetAdClickService = pfbxSizeGetAdClickService;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public long getTotalPv() {
		return totalPv;
	}
	public long getTotalClk() {
		return totalClk;
	}
	public float getTotalClkRate() {
		return totalClkRate;
	}
	public float getTotalAdClkAvgPrice() {
		return totalAdClkAvgPrice;
	}
	public float getTotalAdPvRate() {
		return totalAdPvRate;
	}
	public float getTotalAdClkPriceSum() {
		return totalAdClkPriceSum;
	}
	public float getTotalBonus() {
		return totalBonus;
	}
	public String getDownloadFlag() {
		return downloadFlag;
	}
	public void setDownloadFlag(String downloadFlag) {
		this.downloadFlag = downloadFlag;
	}
	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}
	public String getDownloadFileName() {
		return downloadFileName;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public void setAdPvclkDate(String adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
	}
	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}
	public String getAdPvclkDate() {
		return adPvclkDate;
	}
	public Integer getTotalClick() {
		return totalClick;
	}
	public Integer getTClick() {
		return tClick;
	}
	public Integer getFClick() {
		return fClick;
	}
	public Integer getNClick() {
		return nClick;
	}
	public Integer getIClick() {
		return iClick;
	}
	public InputStream getImageStream() {
		return imageStream;
	}
	
}
