package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang3.StringUtils;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.report.IPfpAdVideoReportService;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class AdVideoReportAction extends BaseAction {
    private static final long serialVersionUID = 2733622960742078562L;

    private IPfdCustomerInfoService pfdCustomerInfoService;
    private IPfpAdVideoReportService pfpAdVideoReportService;
    private IPfpAdService pfpAdService;

    private String akbPfpServer;

    private Map<String, String> pfdCustomerInfoIdMap;
    private Set<String> adPriceTypeSet;
    private Map<String, String> adDeviceMap;
    private Set<String> adSizeSet;

    private String startDate;
    private String endDate;
    private String adVideoDate;
    private String pfdCustomerInfoId;
    private String pfpCustomerInfoId;
    private String memberId;
    private String adId;
    private String adPriceType;
    private String adDevice;
    private String adSize;

    private List<PfpAdVideoReportVO> voList = new ArrayList<>();
    private PfpAdVideoReportVO vo = new PfpAdVideoReportVO();

    private int pageNo = 1;     // 初始化目前頁數
    private int pageSize = 50;  // 初始化每頁幾筆
    private int pageCount = 0;  // 初始化共幾頁
    private int totalCount = 0; // 初始化共幾筆

    private int totalPv;
    private int totalVpv;
    private int totalAdView;
    private int totalClk;
    private float totalPrice;
    private float totalVpvRate;
    private float totalVpvPrice;
    private float totalAdViewRate;
    private float totalAdViewPrice;
    private float totalPvPriceAvg;
    private float totalClkRate;
    private int totalVideoIdc;

    private String message = "";

    private String downloadFileName;
    private InputStream downloadFileStream;

    @Override
    public String execute() throws Exception {
        try {
            if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
                this.message = "請選擇日期開始查詢！";
                return SUCCESS;
            }

            Map<String, Object> conditionMap = this.getConditionMap();

            voList = pfpAdVideoReportService.selectByCondition(conditionMap, Math.max(0, (pageNo-1)*pageSize), pageSize);
            voList = this.getAdDetail(voList);
            if (voList.size() == 0) {
                this.message = "查無資料！";
                return SUCCESS;
            }

            totalCount = pfpAdVideoReportService.selectCountByCondition(conditionMap);
            pageCount = (int) Math.ceil((float)totalCount / pageSize);

            for (PfpAdVideoReportVO vo: voList) {
                totalPv += vo.getAdPv();
                totalClk += vo.getAdClk();
                totalVpv += vo.getAdVpv();
                totalAdView += vo.getAdView();
                totalPrice += vo.getAdPrice();
                totalVideoIdc += vo.getAdVideoIdc();
            }
            if (totalPv > 0) {
                totalPvPriceAvg = totalPrice*1000f / totalPv;
                totalClkRate = totalClk*100f / totalPv;
                totalVpvRate = totalVpv*100f / totalPv;
                totalAdViewRate = totalAdView*100f / totalPv;
            }
            if (totalVpv > 0) {
                totalVpvPrice = totalPrice / totalVpv;
            }
            if (totalAdView > 0) {
                totalAdViewPrice = totalPrice / totalAdView;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return SUCCESS;
    }

    public String adVideoReportDetail() throws Exception {
        try {
            List<PfpAdVideoReportVO> voList = pfpAdVideoReportService.selectByCondition(this.getConditionMap(), Math.max(0, (pageNo-1)*pageSize), pageSize);
            if (voList.size() == 0) {
                this.message = "查無資料！";
                return SUCCESS;
            }

            PfpAd pfpAd = null;
            for (PfpAdVideoReportVO vo: voList) {
                pfpAd = pfpAdService.get(vo.getAdId());
                if (pfpAd != null) {
                    vo.setAdActionName(pfpAd.getPfpAdGroup().getPfpAdAction().getAdActionName());
                    vo.setAdGroupName(pfpAd.getPfpAdGroup().getAdGroupName());
                    this.vo = vo;
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return SUCCESS;
    }

    public String adVideoReportDownload() throws Exception {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Format numberformat = NumberFormat.getInstance();
            Format decimalFormat = new DecimalFormat("#,##0.00");
            Format priceFormat = new DecimalFormat("#,##0.000");

            String pfdCustomerInfoStr = "全部經銷商";
            PfdCustomerInfo pfd = null;
            if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
                pfd = pfdCustomerInfoService.get(pfdCustomerInfoId);
            }
            if (pfd != null) {
                pfdCustomerInfoStr = pfd.getCompanyName();
            }

            String adPriceTypeStr = "全部";
            if (StringUtils.isNotBlank(adPriceType)) {
                adPriceTypeStr = adPriceType;
            }

            String adDeviceStr = getAdDeviceMap().get(adDevice);
            if (StringUtils.isBlank(adDeviceStr)) {
                adDeviceStr = "全部";
            }

            String adSizeStr = "全部";
            if (StringUtils.isNotBlank(adSize)) {
                adSizeStr = adSize;
            }

            List<PfpAdVideoReportVO> voList = pfpAdVideoReportService.selectByCondition(this.getConditionMap(), Math.max(0, (pageNo-1)*pageSize), pageSize);
            voList = this.getAdDetail(voList);

            // excel
            downloadFileName = URLEncoder.encode("影音廣告成效_" + dateFormat.format(new Date()) + ".xls", "UTF-8");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            WritableWorkbook book = Workbook.createWorkbook(out);
            WritableSheet sheet = book.createSheet("Sheet1", 0);

            sheet.addCell(new Label(0, 0, "報表類型：影音廣告成效"));
            sheet.addCell(new Label(0, 2, "查詢日期：" + startDate + " 到 " + endDate));
            sheet.addCell(new Label(0, 3, "查詢條件：" + pfdCustomerInfoStr));
            sheet.addCell(new Label(0, 4, "計價方式：" + adPriceTypeStr));
            sheet.addCell(new Label(0, 5, "播放裝置：" + adDeviceStr));
            sheet.addCell(new Label(0, 6, "廣告尺寸：" + adSizeStr));

            sheet.addCell(new Label(0, 8, "日期"));
            sheet.addCell(new Label(1, 8, "經銷商"));
            sheet.addCell(new Label(2, 8, "廣告客戶"));
            sheet.addCell(new Label(3, 8, "影片明細"));
            sheet.addCell(new Label(4, 8, "影片長度"));
            sheet.addCell(new Label(5, 8, "計價方式"));
            sheet.addCell(new Label(6, 8, "播放裝置"));
            sheet.addCell(new Label(7, 8, "廣告尺寸"));
            sheet.addCell(new Label(8, 8, "曝光數"));
            sheet.addCell(new Label(9, 8, "收視數"));
            sheet.addCell(new Label(10, 8, "收視率"));
            sheet.addCell(new Label(11, 8, "完整播放率"));
            sheet.addCell(new Label(12, 8, "CPM"));
            sheet.addCell(new Label(13, 8, "CPV"));
            sheet.addCell(new Label(14, 8, "費用"));
            sheet.addCell(new Label(15, 8, "點擊數"));
            sheet.addCell(new Label(16, 8, "點擊率"));
            sheet.addCell(new Label(17, 8, "收視人數(不重複)"));
            sheet.addCell(new Label(18, 8, "重播次數"));
            sheet.addCell(new Label(19, 8, "聲音開啟次數"));
            sheet.addCell(new Label(20, 8, "影片播放進度 (25%)"));
            sheet.addCell(new Label(21, 8, "影片播放進度 (50%)"));
            sheet.addCell(new Label(22, 8, "影片播放進度 (75%)"));
            sheet.addCell(new Label(23, 8, "影片播放進度 (100%)"));

            int lineNo = 9;
            for (PfpAdVideoReportVO vo: voList) {
                sheet.addCell(new Label(0, lineNo, vo.getAdVideoDate().toString()));
                sheet.addCell(new Label(1, lineNo, vo.getPfdCustomerInfoName()));
                sheet.addCell(new Label(2, lineNo, vo.getPfpCustomerInfoName() + " " + vo.getPfpCustomerInfoId()));
                sheet.addCell(new Label(3, lineNo, vo.getAdDetailContent()));
                sheet.addCell(new Label(4, lineNo, vo.getAdDetailVideoSeconds()));
                sheet.addCell(new Label(5, lineNo, vo.getAdPriceType()));
                sheet.addCell(new Label(6, lineNo, getAdDeviceMap().get(vo.getAdDevice())));
                sheet.addCell(new Label(7, lineNo, vo.getTproWidth() + "x" + vo.getTproHeight()));
                sheet.addCell(new Label(8, lineNo, numberformat.format(vo.getAdPv())));
                sheet.addCell(new Label(9, lineNo, numberformat.format(vo.getAdVpv())));
                sheet.addCell(new Label(10, lineNo, decimalFormat.format(vo.getAdVpvRate()) + "%"));
                sheet.addCell(new Label(11, lineNo, decimalFormat.format(vo.getAdVideoProcessRate()) + "%"));
                sheet.addCell(new Label(12, lineNo, "$ " + decimalFormat.format(vo.getAdPrice() / vo.getAdView())));
                sheet.addCell(new Label(13, lineNo, "$ " + decimalFormat.format(vo.getAdPvPriceAvg())));
                sheet.addCell(new Label(14, lineNo, "$ " + priceFormat.format(vo.getAdPrice())));
                sheet.addCell(new Label(15, lineNo, numberformat.format(vo.getAdClk())));
                sheet.addCell(new Label(16, lineNo, decimalFormat.format(vo.getAdClkRate()) + "%"));
                sheet.addCell(new Label(17, lineNo, numberformat.format(vo.getAdVideoUniq())));
                sheet.addCell(new Label(18, lineNo, numberformat.format(vo.getAdVideoReplay())));
                sheet.addCell(new Label(19, lineNo, numberformat.format(vo.getAdVideoMusic())));
                sheet.addCell(new Label(20, lineNo, numberformat.format(vo.getAdVideoProcess25())));
                sheet.addCell(new Label(21, lineNo, numberformat.format(vo.getAdVideoProcess50())));
                sheet.addCell(new Label(22, lineNo, numberformat.format(vo.getAdVideoProcess75())));
                sheet.addCell(new Label(23, lineNo, numberformat.format(vo.getAdVideoProcess100())));

                lineNo++;
            }

            book.write();
            book.close();
            downloadFileStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        return SUCCESS;
    }

    private Map<String, Object> getConditionMap() {
        String tproWidth = null;
        String tproHeight = null;
        if (StringUtils.isNotBlank(adSize)) {
            String[] adSizes = adSize.split("x");
            if (adSizes.length == 2) {
                tproWidth = adSizes[0];
                tproHeight = adSizes[1];
            }
        }

        Map<String, Object> conditionMap = new HashMap<>();
        if (StringUtils.isNotBlank(startDate)) {
            conditionMap.put("startDate", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            conditionMap.put("endDate", endDate);
        }
        if (StringUtils.isNotBlank(adVideoDate)) {
            conditionMap.put("adVideoDate", adVideoDate);
        }
        if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
            conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
        }
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            conditionMap.put("pfpCustomerInfoId", pfpCustomerInfoId);
        }
        if (StringUtils.isNotBlank(memberId)) {
            conditionMap.put("memberId", memberId);
        }
        if (StringUtils.isNotBlank(adId)) {
            conditionMap.put("adId", adId);
        }
        if (StringUtils.isNotBlank(adPriceType)) {
            conditionMap.put("adPriceType", adPriceType);
        }
        if (StringUtils.isNotBlank(adDevice)) {
            conditionMap.put("adDevice", adDevice);
        }
        if (StringUtils.isNotBlank(tproWidth)) {
            conditionMap.put("tproWidth", tproWidth);
        }
        if (StringUtils.isNotBlank(tproHeight)) {
            conditionMap.put("tproHeight", tproHeight);
        }
        return conditionMap;
    }

    private List<PfpAdVideoReportVO> getAdDetail(List<PfpAdVideoReportVO> voList) throws Exception {
        Set<PfpAdDetail> pfpAdDetailSet = null;
        for (PfpAdVideoReportVO vo: voList) {
            pfpAdDetailSet = pfpAdService.get(vo.getAdId()).getPfpAdDetails();
            for (PfpAdDetail pfpAdDetail: pfpAdDetailSet) {
                if ("title".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailTitle(pfpAdDetail.getAdDetailContent());
                }
                else if ("content".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailContent(pfpAdDetail.getAdDetailContent());
                }
                else if ("img".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailImg(pfpAdDetail.getAdDetailContent());
                }
                else if ("mp4_path".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailMp4Path(pfpAdDetail.getAdDetailContent());
                }
                else if ("video_seconds".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailVideoSeconds(pfpAdDetail.getAdDetailContent());
                }
                else if ("video_url".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailVideoUrl(pfpAdDetail.getAdDetailContent());
                }
                else if ("real_url".equals(pfpAdDetail.getAdDetailId())) {
                    vo.setAdDetailRealUrl(pfpAdDetail.getAdDetailContent());
                }
            }
        }

        return voList;
    }

    public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService) {
        this.pfdCustomerInfoService = pfdCustomerInfoService;
    }

    public void setPfpAdVideoReportService(IPfpAdVideoReportService pfpAdVideoReportService) {
        this.pfpAdVideoReportService = pfpAdVideoReportService;
    }

    public void setPfpAdService(IPfpAdService pfpAdService) {
        this.pfpAdService = pfpAdService;
    }

    public String getAkbPfpServer() {
        return akbPfpServer;
    }

    public void setAkbPfpServer(String akbPfpServer) {
        this.akbPfpServer = akbPfpServer;
    }

    public Map<String, String> getPfdCustomerInfoIdMap() {
        List<PfdCustomerInfo> list = pfdCustomerInfoService.loadAll();

        pfdCustomerInfoIdMap = new TreeMap<String, String>();
        for (PfdCustomerInfo data: list) {
            pfdCustomerInfoIdMap.put(data.getCustomerInfoId(), data.getCompanyName());
        }

        return pfdCustomerInfoIdMap;
    }

    public Set<String> getAdPriceTypeSet() {
        adPriceTypeSet = new TreeSet<String>();
        adPriceTypeSet.add("CPM");
        adPriceTypeSet.add("CPV");
        return adPriceTypeSet;
    }

    public Map<String, String> getAdDeviceMap() {
        adDeviceMap = new LinkedHashMap<String,String>();
        adDeviceMap.put("PC", "電腦");
        adDeviceMap.put("mobile", "行動裝置");
        return adDeviceMap;
    }

    public Set<String> getAdSizeSet() {
        adSizeSet = new TreeSet<String>();
        adSizeSet.add("300x250");
        adSizeSet.add("300x600");
        adSizeSet.add("320x480");
        adSizeSet.add("336x280");
        adSizeSet.add("640x390");
        adSizeSet.add("950x390");
        adSizeSet.add("970x250");
        return adSizeSet;
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

    public void setAdVideoDate(String adVideoDate) {
        this.adVideoDate = adVideoDate;
    }

    public String getPfdCustomerInfoId() {
        return pfdCustomerInfoId;
    }

    public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
        this.pfdCustomerInfoId = pfdCustomerInfoId;
    }

    public String getPfpCustomerInfoId() {
        return pfpCustomerInfoId;
    }

    public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
        this.pfpCustomerInfoId = pfpCustomerInfoId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdPriceType() {
        return adPriceType;
    }

    public void setAdPriceType(String adPriceType) {
        this.adPriceType = adPriceType;
    }

    public String getAdDevice() {
        return adDevice;
    }

    public void setAdDevice(String adDevice) {
        this.adDevice = adDevice;
    }

    public String getAdSize() {
        return adSize;
    }

    public void setAdSize(String adSize) {
        this.adSize = adSize;
    }

    public List<PfpAdVideoReportVO> getVoList() {
        return voList;
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

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPv() {
        return totalPv;
    }

    public int getTotalVpv() {
        return totalVpv;
    }

    public int getTotalAdView() {
        return totalAdView;
    }

    public float getTotalVpvRate() {
        return totalVpvRate;
    }

    public float getTotalAdViewRate() {
        return totalAdViewRate;
    }

    public float getTotalAdViewPrice() {
        return totalAdViewPrice;
    }

    public float getTotalVpvPrice() {
        return totalVpvPrice;
    }

    public float getTotalPvPriceAvg() {
        return totalPvPriceAvg;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getTotalClk() {
        return totalClk;
    }

    public float getTotalClkRate() {
        return totalClkRate;
    }

    public int getTotalVideoIdc() {
        return totalVideoIdc;
    }

    public String getMessage() {
        return message;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public InputStream getDownloadFileStream() {
        return downloadFileStream;
    }

    public PfpAdVideoReportVO getVo() {
        return vo;
    }
}
