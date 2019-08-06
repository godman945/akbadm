package com.pchome.akbadm.quartzs;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.pchome.akbadm.bean.AdBean;
import com.pchome.akbadm.bean.AdCategoryNewBean;
import com.pchome.akbadm.bean.AdDetailBean;
import com.pchome.akbadm.bean.AdmShowRuleBean;
import com.pchome.akbadm.bean.PfbxAreaBean;
import com.pchome.akbadm.bean.PfbxBlackUrlBean;
import com.pchome.akbadm.bean.PfbxPositionBean;
import com.pchome.akbadm.bean.PfbxSizeBean;
import com.pchome.akbadm.bean.PfbxUrlBean;
import com.pchome.akbadm.bean.PfbxUserGroupBean;
import com.pchome.akbadm.bean.PfbxUserOptionBean;
import com.pchome.akbadm.bean.PfbxUserSampleBean;
import com.pchome.akbadm.bean.PfbxWhiteUrlBean;
import com.pchome.akbadm.bean.StyleBean;
import com.pchome.akbadm.bean.TadBean;
import com.pchome.akbadm.bean.TadMapBean;
import com.pchome.akbadm.bean.TproBean;
import com.pchome.akbadm.bean.ValidKeywordBean;
import com.pchome.akbadm.db.pojo.AdmPfbxBlockUrl;
import com.pchome.akbadm.db.pojo.AdmShowRule;
import com.pchome.akbadm.db.pojo.PfbStyleInfo;
import com.pchome.akbadm.db.pojo.PfbxAllowCusurl;
import com.pchome.akbadm.db.pojo.PfbxAllowIndustry;
import com.pchome.akbadm.db.pojo.PfbxAllowUrl;
import com.pchome.akbadm.db.pojo.PfbxArea;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;
import com.pchome.akbadm.db.pojo.PfbxBlockIndustry;
import com.pchome.akbadm.db.pojo.PfbxGroupArea;
import com.pchome.akbadm.db.pojo.PfbxGroupCode;
import com.pchome.akbadm.db.pojo.PfbxGroupSize;
import com.pchome.akbadm.db.pojo.PfbxGroupUrl;
import com.pchome.akbadm.db.pojo.PfbxOptionArea;
import com.pchome.akbadm.db.pojo.PfbxOptionCode;
import com.pchome.akbadm.db.pojo.PfbxOptionSize;
import com.pchome.akbadm.db.pojo.PfbxOptionUrl;
import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.pojo.PfbxSampleArea;
import com.pchome.akbadm.db.pojo.PfbxSampleCode;
import com.pchome.akbadm.db.pojo.PfbxSampleSize;
import com.pchome.akbadm.db.pojo.PfbxSampleUrl;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.pojo.PfbxUrl;
import com.pchome.akbadm.db.pojo.PfbxUserGroup;
import com.pchome.akbadm.db.pojo.PfbxUserOption;
import com.pchome.akbadm.db.pojo.PfbxUserSample;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdSysprice;
import com.pchome.akbadm.db.pojo.PfpCodeAdactionMerge;
import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IAdmArwValueService;
import com.pchome.akbadm.db.service.ad.IAdmShowRuleService;
import com.pchome.akbadm.db.service.ad.IPfbStyleInfoService;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryMappingService;
import com.pchome.akbadm.db.service.ad.IPfpAdExcludeKeywordService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.ad.IPfpAdSpecificWebsiteService;
import com.pchome.akbadm.db.service.code.IPfpCodeAdactionMergeService;
import com.pchome.akbadm.db.service.code.IPfpCodeTrackingService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.IPfbxAreaService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionService;
import com.pchome.akbadm.db.service.pfbx.IPfbxSizeService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUrlService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUserGroupService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUserOptionService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUserSampleService;
import com.pchome.akbadm.db.service.pfbx.play.IAdmPfbxBlockUrlService;
import com.pchome.akbadm.db.service.pfbx.play.IPfbxAllowUrlService;
import com.pchome.akbadm.db.service.sysprice.IPfpAdSyspriceService;
import com.pchome.akbadm.db.service.sysprice.IPfpKeywordSyspriceService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.ad.EnumExcludeKeywordStatus;
import com.pchome.enumerate.ad.EnumIndexField;
import com.pchome.enumerate.ad.EnumPriceType;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.soft.util.HttpConnectionClient;
import com.pchome.soft.util.SpringSSHProcessUtil2;
import com.thoughtworks.xstream.XStream;

@Transactional
public class KernelJob {
    private static final String WRITE_FILE = "write file = ";
    private static final String[] EXTENSIONS = new String[]{"def"};

    private Logger log = LogManager.getRootLogger();
    private int reduceHour = 0;
    private int reduceMinuteDivisor = 15;
    private int reduceDivisor = 4;

    private IPfbStyleInfoService pfbStyleInfoService;
    private IPfpCustomerInfoService pfpCustomerInfoService;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpAdSyspriceService pfpAdSyspriceService;
    private IPfpKeywordSyspriceService pfpKeywordSyspriceService;
    private IPfpAdExcludeKeywordService pfpAdExcludeKeywordService;
    private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
    private IPfpAdCategoryMappingService pfpAdCategoryMappingService;
    private IPfpAdSpecificWebsiteService pfpAdSpecificWebsiteService;

    private IPfbxAreaService pfbxAreaService;
    private IPfbxPositionService pfbxPositionService;
    private IPfbxSizeService pfbxSizeService;
    private IPfbxUrlService pfbxUrlService;
    private IPfbxUserGroupService pfbxUserGroupService;
    private IPfbxUserOptionService pfbxUserOptionService;
    private IPfbxUserSampleService pfbxUserSampleService;

    private IAdmShowRuleService admShowRuleService;
    private IAdmArwValueService admArwValueService;
    private IAdmPfbxBlockUrlService admPfbxBlockUrlService;
    private IPfbxAllowUrlService pfbxAllowUrlService;

    private IPfpCodeAdactionMergeService pfpCodeAdactionMergeService;
    private IPfpCodeTrackingService pfpCodeTrackingService;

    private String admAddata;
    private String kernelAddata;
    private String kernelAddataDir;
    private String pfpProdGroupListApiUrl;
    private float adSysprice;
    private int makeNumber;
    private int serverNumber;
    private List<SpringSSHProcessUtil2> scpProcessList;

    private Map<String, Map<String, AdBean>> currentPoolMap = new HashMap<>();

    public void process() throws IOException {
        log.info("====KernelJob.process() start====");

        File lockFile = new File(kernelAddata + File.separator + "lock.txt");
        if (!lockFile.exists()) {
            try {
                log.info("touch " + lockFile.getPath());
                FileUtils.touch(lockFile);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                kernelAddataDir = kernelAddata + sdf.format(Calendar.getInstance().getTime()) + File.separator;

                // pfp
                style();
                tpro();
                tad();
                pool();
                keyword();
                prod();

                // pfb
                area();
                position();
                size();
                url();

                group();
                option();
                sample();

                showRule();
                blackUrl();
                whiteUrl();

                scp();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            } finally {
                log.info("delete " + kernelAddataDir);
                FileUtils.deleteQuietly(new File(kernelAddataDir));
            }
        }
        else {
            log.info(lockFile.getPath() + " exists");
        }

        log.info("delete " + lockFile.getPath());
        FileUtils.deleteQuietly(lockFile);

        log.info("====KernelJob.process() end====");
    }

    private void style() throws IOException {
        Map<String, StyleBean> map = new HashMap<>();
        StyleBean styleBean = null;

        // style(Map) -> style(Bean)
        List<PfbStyleInfo> pfbStyleInfoList = pfbStyleInfoService.selectValidPfbStyleInfo();
        for (PfbStyleInfo pfbStyleInfo: pfbStyleInfoList) {
            try {
                styleBean = new StyleBean();
                styleBean.setStyleId(String.valueOf(pfbStyleInfo.getStyleNo()));
                for (String tproId: pfbStyleInfo.getStyle().split(",")) {
                    styleBean.getTproSet().add(tproId);
                }
                styleBean.setUrl(pfbStyleInfo.getPfbWebInfo().getUrl());
                styleBean.setFlag(pfbStyleInfo.getPfbWebInfo().getFlag());

                map.put(styleBean.getStyleId(), styleBean);
            } catch (Exception e) {
                log.error("styleNo=" + pfbStyleInfo.getStyleNo(), e);
            }
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("styleBean", StyleBean.class);
        String xml = xstream.toXML(map);

        // write file
        File file = new File(kernelAddataDir + "style.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("style: " + map.size());
    }

    private void tpro() throws IOException {
        File dir = new File(admAddata + EnumSequenceTableName.ADM_TEMPLATE_PRODUCT.getCharName());
        if (!dir.exists()) {
            log.info(dir.getPath() + " not exists");
            return;
        }
        if (!dir.isDirectory()) {
            log.info(dir.getPath() + " is not directory");
            return;
        }

        // tpro(Map) > tpro(Bean)
        Map<String, TproBean> map = new HashMap<>();
        TproBean tproBean = null;
        TadMapBean tadMapBean = null;
        String tproId = null;
        String backupTproId = null;
        String prodId = null;
        String tadId = null;
        String backupTadId = null;

        Iterator<File> iterator = FileUtils.iterateFiles(dir, EXTENSIONS, false);
        File file = null;
        List<String> lineList = null;
        boolean htmlFlag = false;

        while(iterator.hasNext()) {
            try {
                String value = null;
                htmlFlag = false;

                file = iterator.next();
                lineList = FileUtils.readLines(file, StandardCharsets.UTF_8);

                tproBean = new TproBean();
                tproId = file.getName();
                for (String extension: EXTENSIONS) {
                    tproId = tproId.replace("." + extension, "");
                }
                tproBean.setTproId(tproId);

                for (String line: lineList) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }

                    if (line.indexOf("backupTpro:") == 0) {
                        backupTproId = line.replace("backupTpro:", "");
                        if (StringUtils.isBlank(backupTproId)) {
                            continue;
                        }
                        tproBean.setBackupTproId(backupTproId);
                    }
                    else if (line.indexOf("ProdSeq:") == 0) {
                        prodId = line.replace("ProdSeq:", "");
                        if (StringUtils.isBlank(prodId)) {
                            continue;
                        }
                        tproBean.setProdId(prodId);
                    }
                    else if (line.indexOf("TemplateAdSeq:") == 0) {
                        tadId = line.replace("TemplateAdSeq:", "");
                        tadMapBean = tproBean.getTadMap().get(tadId);
                        if (tadMapBean == null) {
                            tadMapBean = new TadMapBean();
                        }
                        tadMapBean.setTadId(tadId);
                        tproBean.getTadMap().put(tadId, tadMapBean);
                    }
                    else if (line.indexOf("backupTad:") == 0) {
                        backupTadId = line.replace("backupTad:", "");
                        if (StringUtils.isBlank(backupTadId)) {
                            continue;
                        }
                        tproBean.setBackupTadId(backupTadId);
                    }
                    else if (line.indexOf("ProdTemplateAdSeq:") == 0) {
                        if (StringUtils.isBlank(tadId)) {
                            continue;
                        }

                        tadMapBean = tproBean.getTadMap().get(tadId);
                        if (tadMapBean == null) {
                            tadMapBean = new TadMapBean();
                        }
                        tadMapBean.setProdTadId(line.replace("ProdTemplateAdSeq:", ""));
                        tproBean.getTadMap().put(tadId, tadMapBean);
                    }
                    else if (line.indexOf("AdNum:") == 0) {
                        if (StringUtils.isBlank(tadId)) {
                            continue;
                        }

                        tadMapBean = tproBean.getTadMap().get(tadId);
                        if (tadMapBean == null) {
                            tadMapBean = new TadMapBean();
                        }
                        tadMapBean.setAdNum(Integer.valueOf(line.replace("AdNum:", "")));
                        tproBean.getTadMap().put(tadId, tadMapBean);
                    }
                    // TODO move to TadBean
                    else if (line.indexOf("ProdNum:") == 0) {
                        if (StringUtils.isBlank(tadId)) {
                            continue;
                        }

                        tadMapBean = tproBean.getTadMap().get(tadId);
                        if (tadMapBean == null) {
                            tadMapBean = new TadMapBean();
                        }
                        tadMapBean.setProdNum(Integer.valueOf(line.replace("ProdNum:", "")));
                        tproBean.getTadMap().put(tadId, tadMapBean);
                    }
                    else if (line.indexOf("Auto:") == 0) {
                        if ("Y".equals(line.replace("Auto:", ""))) {
                            tproBean.setAuto(true);
                        }
                    }
                    else if (line.indexOf("PageTotalNum:") == 0) {
                        tproBean.setPageSize(Integer.valueOf(line.replace("PageTotalNum:", "")));
                    }
                    else if (line.indexOf("StartNum:") == 0) {
                        tproBean.setStartNum(Integer.valueOf(line.replace("StartNum:", "")));
                    }
                    else if (line.indexOf("adType:") == 0) {
                        tproBean.setAdType(line.replace("adType:", ""));
                    }
                    else if (line.indexOf("templateAdType:") == 0) {
                        tproBean.setTemplateAdType(line.replace("templateAdType:", ""));
                    }
                    else if (line.indexOf("xType:") == 0) {
                        tproBean.setxType(line.replace("xType:", ""));
                    }
                    else if (line.indexOf("Iframe:") == 0) {
                        if ("Y".equals(line.replace("Iframe:", ""))) {
                            tproBean.setIframe(true);
                        }
                    }
                    else if (line.indexOf("Height:") == 0) {
                        value = line.replace("Height:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setIframeHeight(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("Weight:") == 0) {
                        value = line.replace("Weight:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setIframeWidth(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("PositionHeight:") == 0) {
                        value = line.replace("PositionHeight:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setPositionHeight(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("PositionWidth:") == 0) {
                        value = line.replace("PositionWidth:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setPositionWidth(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("LogoHeight:") == 0) {
                        value = line.replace("LogoHeight:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setLogoHeight(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("LogoWeight:") == 0) {
                        value = line.replace("LogoWeight:", "");
                        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
                            tproBean.setLogoWidth(Integer.valueOf(value));
                        }
                    }
                    else if (line.indexOf("html:") == 0) {
                        htmlFlag = true;
                    }
                    else if (htmlFlag) {
                        tproBean.setHtml(tproBean.getHtml() + line);
                    }
                }
                map.put(tproBean.getTproId(), tproBean);
            } catch (Exception e) {
                log.error(file.getPath(), e);
            }
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("tproBean", TproBean.class);
        xstream.alias("tadMapBean", TadMapBean.class);
        String xml = xstream.toXML(map);

        // write file
        file = new File(kernelAddataDir + "tpro.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("tpro: " + map.size());
    }

    private void tad() throws IOException {
        File dir = new File(admAddata + EnumSequenceTableName.ADM_TEMPLATE_AD.getCharName());
        if (!dir.exists()) {
            log.info(dir.getPath() + " not exists");
            return;
        }
        if (!dir.isDirectory()) {
            log.info(dir.getPath() + " is not directory");
            return;
        }

        // tad(Map) > tad(Bean)
        Map<String, TadBean> map = new HashMap<>();
        TadBean tadBean = null;
        String poolId = null;
        String tadId = null;

        Iterator<File> iterator = FileUtils.iterateFiles(dir, EXTENSIONS, false);
        File file = null;
        List<String> lineList = null;
        boolean contentFlag = false;

        while(iterator.hasNext()) {
            try {
                contentFlag = true;

                file = iterator.next();
                lineList = FileUtils.readLines(file, StandardCharsets.UTF_8);
                tadBean = new TadBean();

                for (String line: lineList) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }

                    tadId = file.getName();
                    for (String extension: EXTENSIONS) {
                        tadId = tadId.replace("." + extension, "");
                    }
                    tadBean.setTadId(tadId);

                    if (line.indexOf("PoolSeq:") == 0) {
                        poolId = line.replace("PoolSeq:", "");
                        tadBean.setPoolId(poolId);
                    }
                    else if (line.indexOf("DiffCompany:") == 0) {
                        if ("Y".equals(line.replace("DiffCompany:", ""))) {
                            tadBean.setDiffCompany(true);
                        }
                    }
                    else if (line.indexOf("html:") == 0) {
                        contentFlag = true;
                    }
                    else {
                        if (contentFlag) {
                            tadBean.setContent(tadBean.getContent() + line);
                        }
                    }
                }

                map.put(tadId, tadBean);
            } catch (Exception e) {
                log.error(file.getPath(), e);
            }
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("tadBean", TadBean.class);
        String xml = xstream.toXML(map);

        // write file
        file = new File(kernelAddataDir + "tad.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("tad: " + map.size());
    }

    private void pool() throws IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteReduceNumber = (calendar.get(Calendar.MINUTE) / reduceMinuteDivisor)+1;
        int reduceCount = 0;
        int splitAdSize = 0;

        Map<String, Map<String, AdBean>> poolMap = new HashMap<>();
        Map<String, AdBean> adMap = null;
        Set<String> allAdSet = new HashSet<>();
        Set<String> allowAdSet = new HashSet<>();
        AdBean adBean = null;
        AdDetailBean adDetailBean = null;
        PfpAd pfpAd = null;
        PfpAdGroup pfpAdGroup = null;
        PfpAdAction pfpAdAction = null;
        PfpCustomerInfo pfpCustomerInfo = null;
        PfpCodeAdactionMerge pfpCodeAdactionMerge = null;
        PfpCodeTracking pfpCodeTracking = null;
        String adPoolId = null;
        String adId = null;
        String actionId = null;
        String pfpCustomerInfoId = null;
        StringBuilder adClass = null;
        String priceType = null;
        String[] categoryCode = null;
        Float adSearchPrice = null;
        Float adChannelPrice = null;
        Float adBidPrice = null;
        int[] pvclkSums = null;
        int[] allPvclkSums = null;
        float cpmWeight = 0;
        float cpvWeight = 0;
        float ctr = 0;
        int qualityGrade = 0;
        PfdUserAdAccountRef ref = null;
        String adPvLimitStyle = null;
        String adPvLimitPeriod = null;
        int adPvLimitAmount = 0;
        Integer admArw = 1;
        String trackingSeq = null;
        int trackingRangeDate = 0;

        // get ad detail
        List<PfpAdDetail> pfpAdDetailList = pfpCustomerInfoService.selectValidAdDetail();
        log.info("pfpCustomerInfoService.selectValidAdDetail " + pfpAdDetailList.size());

        // get category mapping
        Map<String, StringBuilder> adClassCache = pfpAdCategoryMappingService.selectPfpAdCategoryMappingBufferMaps();
        log.info("pfpAdCategoryMappingService.selectPfpAdCategoryMappingBufferMaps " + adClassCache.size());

        // get category code
        Map<String, String[]> categoryCodeCache = pfpAdSpecificWebsiteService.selectCategoryCodeMap();
        log.info("pfpAdSpecificWebsiteService.selectCategoryCodeMap " + categoryCodeCache.size());

        // get adm arw value
        Map<String, Integer> admArwCache = admArwValueService.selectAdmArwMap();
        log.info("admArwValueService.selectAdmArwMap " + admArwCache.size());

        // sysprice (special rule)
        PfpAdSysprice pfpAdSysprice = pfpAdSyspriceService.selectAdSyspriceByPoolSeq("sysprice_001");
        Float adSystemPrice = adSysprice;
        if (pfpAdSysprice != null) {
            adSystemPrice = pfpAdSysprice.getSysprice();
        }
        log.info("pfpAdSyspriceService.selectAdSyspriceByPoolSeq " + adSystemPrice);

        // adActionTime
        Map<String, StringBuilder> adActionTimeCache = new HashMap<>();
        StringBuilder adActionTime = null;

        // yesterday
        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCalendar.set(Calendar.MINUTE, 0);
        yesterdayCalendar.set(Calendar.SECOND, 0);
        yesterdayCalendar.set(Calendar.MILLISECOND, 0);

        // adId, [sum(ad_pv), sum(ad_clk)]
        Map<String, int[]> pfpAdPvclkSumsCache = pfpAdPvclkService.selectPfpAdPvclkSums(yesterdayCalendar.getTime());
        log.info("pfpAdPvclkService.selectPfpAdPvclkSums " + pfpAdPvclkSumsCache.size());

        // weight
        for (int[] values: pfpAdPvclkSumsCache.values()) {
            if (allPvclkSums == null) {
                allPvclkSums = new int[values.length];
            }
            for (int i = 0; i < values.length; i++) {
                allPvclkSums[i] += values[i];
            }
        }

        // log by nico
        if (allPvclkSums != null) {
            log.info("======>allPvclkSums[CK]="+(float) allPvclkSums[1]+",[PV]="+allPvclkSums[0]+",[view]="+allPvclkSums[2]);


            if ((allPvclkSums[0] == 0) || (allPvclkSums[1] == 0)) {
                cpmWeight = 0.005f;
            }
            else {
                cpmWeight = (float) (Math.min((float) allPvclkSums[1] / allPvclkSums[0] , 0.02) * 1000);
            }

            if (allPvclkSums[1] == 0) {
                cpvWeight = 0.005f;
            }
            else {
                cpvWeight = Math.min((float) allPvclkSums[1] / allPvclkSums[0], 0.02f);
            }

        }else{

             cpmWeight = 0.005f;
             cpvWeight = 0.005f;
        }

        // log by nico
        log.info("======>cpmWeight="+cpmWeight+"cpvWeight="+cpvWeight);

        // get pfd ref
        Map<String, PfdUserAdAccountRef> pfpToPfdMap = pfdUserAdAccountRefService.selectPfdUserAdAccountRefMaps();
        log.info("pfdUserAdAccountRefService.findPfdUserAdAccountRefMaps " + pfpToPfdMap.size());

        // pfpCodeAdactionMerge
        Map<String, PfpCodeAdactionMerge> pfpCodeAdactionMergeMap = pfpCodeAdactionMergeService.selectPfpCodeAdactionMergeMap();
        log.info("pfpCodeAdactionMergeService.selectPfpCodeAdactionMergeMap " + pfpCodeAdactionMergeMap.size());

        // pfpCodeTracking
        Map<String, PfpCodeTracking> pfpCodeTrackingMap = pfpCodeTrackingService.selectPfpCodeTrackingMap();
        log.info("pfpCodeTrackingService.selectPfpCodeTrackingMap " + pfpCodeTrackingMap.size());

        // special rule: reduce
        for (PfpAdDetail pfpAdDetail: pfpAdDetailList) {
            allAdSet.add(pfpAdDetail.getPfpAd().getAdSeq());
        }

        // by nico
        splitAdSize=(allAdSet.size() / reduceDivisor) * minuteReduceNumber;
        log.info("splitAdSize="+splitAdSize);

        for (String adSeq: allAdSet) {
            if (reduceCount++ <= splitAdSize) {
                allowAdSet.add(adSeq);
            }
        }

        //add by nico
        log.info("reduceHour="+reduceHour+",minuteReduceNumber="+minuteReduceNumber+",allAdSetSize="+allAdSet.size()+",allowAdSetSize="+allowAdSet.size());

        // pool(Map) > ad(Map) > ad(Bean)
        for (PfpAdDetail pfpAdDetail: pfpAdDetailList) {
            try {
                adPoolId = pfpAdDetail.getAdPoolSeq();
                pfpAd = pfpAdDetail.getPfpAd();
                pfpAdGroup = pfpAd.getPfpAdGroup();
                pfpAdAction = pfpAdGroup.getPfpAdAction();
                pfpCustomerInfo = pfpAdAction.getPfpCustomerInfo();

                adId = pfpAd.getAdSeq();
                actionId = pfpAdAction.getAdActionSeq();
                pfpCustomerInfoId = pfpCustomerInfo.getCustomerInfoId();

                // special rule: reduce
                if ((hour == reduceHour) &&
                        !allowAdSet.contains(adId) &&
                        !"adp_201904090001".equals(adPoolId)) {

                    continue;
                }

                // get ad map
                adMap = poolMap.get(adPoolId);
                if (adMap == null) {
                    adMap = new HashMap<>();
                }

                // get ad bean
                adBean = adMap.get(adId);
                if (adBean == null) {
                    adBean = new AdBean();

                    // get adClass
                    adClass = adClassCache.get(pfpAd.getAdSeq());
                    if (adClass == null) {
                        adClass = new StringBuilder();
                    }

                    // get category code
                    categoryCode = categoryCodeCache.get(actionId);
                    if (categoryCode == null) {
                        categoryCode = new String[0];
                    }

                    adActionTime = adActionTimeCache.get(actionId);
                    if (adActionTime == null) {
                        adActionTime = new StringBuilder();
                        adActionTime.append(pfpAdAction.getAdActionSunTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionMonTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionTueTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionWedTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionThuTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionFriTime()).append(";");
                        adActionTime.append(pfpAdAction.getAdActionSatTime());

                        adActionTimeCache.put(actionId, adActionTime);
                    }

                    // get sums
                    pvclkSums = pfpAdPvclkSumsCache.get(adId);
                    if (pvclkSums == null) {
                        pvclkSums = new int[3];
                    }

                    //搜尋預設出價-使用群組的價格
                    //這沒用到才對
                    adSearchPrice = pfpAd.getPfpAdGroup().getAdGroupSearchPrice() > adSystemPrice ? adSystemPrice : pfpAd.getPfpAdGroup().getAdGroupSearchPrice();
                    //搜尋頻道出價-使用群組的價格
                    //20170330 nico 不再用系統價格了，直接用出價去比
                    adChannelPrice = pfpAd.getPfpAdGroup().getAdGroupChannelPrice();

                    // bidPrice
                    priceType = pfpAdGroup.getAdGroupPriceType().toUpperCase();
                    adBidPrice = 0f;
                    if (EnumPriceType.CPC.toString().equals(priceType)) {
                        adBidPrice = adChannelPrice;
                    }
                    else if (EnumPriceType.CPM.toString().equals(priceType)) {
                        adBidPrice = adChannelPrice / cpmWeight;
                    }
                    else if (EnumPriceType.CPV.toString().equals(priceType)) {
                        adBidPrice = adChannelPrice / cpvWeight;
                    }

                    if ("N".equals(pfpCustomerInfo.getRecognize())) {
                        adSearchPrice = 0f;
                        adChannelPrice = 0f;
                        adBidPrice = 0f;
                        pvclkSums = new int[3];
                    }

                    // quality grade
                    qualityGrade = 6;
                    if (pvclkSums[0] != 0) {
                        if (EnumPriceType.CPC.toString().equals(priceType)) {
                            if ((pvclkSums[0] == 0) || (pvclkSums[1] == 0)) {
                                ctr = 0.005f;
                            }
                            else {
                                ctr = pvclkSums[1] * 100f / pvclkSums[0];
                            }

                            if (ctr < 0.001f) {
                                qualityGrade = 1;
                            }
                            else if ((ctr >= 0.001f) && (ctr < 0.002f)) {
                                qualityGrade = 2;
                            }
                            else if ((ctr >= 0.002f) && (ctr < 0.003f)) {
                                qualityGrade = 3;
                            }
                            else if ((ctr >= 0.003f) && (ctr < 0.004f)) {
                                qualityGrade = 4;
                            }
                            else if ((ctr >= 0.004f) && (ctr < 0.005f)) {
                                qualityGrade = 5;
                            }
                            else if ((ctr >= 0.005f) && (ctr < 0.015f)) {
                                qualityGrade = 6;
                            }
                            else if ((ctr >= 0.015f) && (ctr < 0.045f)) {
                                qualityGrade = 7;
                            }
                            else if ((ctr >= 0.045f) && (ctr < 0.075f)) {
                                qualityGrade = 8;
                            }
                            else if ((ctr >= 0.075f) && (ctr < 0.2f)) {
                                qualityGrade = 9;
                            }
                            else if (ctr >= 0.2f) {
                                qualityGrade = 10;
                            }
                        }
                        else if (EnumPriceType.CPM.toString().equals(priceType) || EnumPriceType.CPV.toString().equals(priceType)) {
                            if ((pvclkSums[0] == 0) || (pvclkSums[2] == 0)) {
                                ctr = 1.6f;
                            }
                            else {
                                ctr = pvclkSums[2] * 100f / pvclkSums[0];
                            }

                            if (ctr < 0.05f) {
                                qualityGrade = 1;
                            }
                            else if ((ctr >= 0.05f) && (ctr < 0.1f)) {
                                qualityGrade = 2;
                            }
                            else if ((ctr >= 0.1f) && (ctr < 0.2f)) {
                                qualityGrade = 3;
                            }
                            else if ((ctr >= 0.2f) && (ctr < 0.4f)) {
                                qualityGrade = 4;
                            }
                            else if ((ctr >= 0.4f) && (ctr < 0.8f)) {
                                qualityGrade = 5;
                            }
                            else if ((ctr >= 0.8f) && (ctr < 1.6f)) {
                                qualityGrade = 6;
                            }
                            else if ((ctr >= 1.6f) && (ctr < 3.2f)) {
                                qualityGrade = 7;
                            }
                            else if ((ctr >= 3.2f) && (ctr < 6.4f)) {
                                qualityGrade = 8;
                            }
                            else if ((ctr >= 6.4f) && (ctr < 12.8f)) {
                                qualityGrade = 9;
                            }
                            else if (ctr >= 12.8f) {
                                qualityGrade = 10;
                            }
                        }
                    }

                    // ad pv limit
                    adPvLimitStyle = pfpAdAction.getAdPvLimitStyle();
                    adPvLimitPeriod = pfpAdAction.getAdPvLimitPeriod();
                    adPvLimitAmount = pfpAdAction.getAdPvLimitAmount();
                    if (EnumPriceType.CPM.toString().equals(priceType)) {
                        if ("0".equals(adPvLimitStyle)) {
                            adPvLimitStyle = "3";
                        }
                        if ("0".equals(adPvLimitPeriod)) {
                            adPvLimitPeriod = "D";
                        }
                        if (adPvLimitAmount == 0) {
                            adPvLimitAmount = 100;
                        }
                    }
                    else if (EnumPriceType.CPV.toString().equals(priceType)) {
                        if ("0".equals(adPvLimitStyle)) {
                            adPvLimitStyle = "3";
                        }
                        if ("0".equals(adPvLimitPeriod)) {
                            adPvLimitPeriod = "D";
                        }
                        if (adPvLimitAmount == 0) {
                            adPvLimitAmount = 10;
                        }
                    }

                    // adm arw
                    admArw = admArwCache.get(pfpCustomerInfoId);
                    if (admArw == null) {
                        admArw = 1;
                        admArwCache.put(pfpCustomerInfoId, admArw);
                    }

                    // code tracking
                    trackingSeq = "";
                    trackingRangeDate = 0;
                    pfpCodeAdactionMerge = pfpCodeAdactionMergeMap.get(actionId);
                    if (pfpCodeAdactionMerge != null) {
                        pfpCodeTracking = pfpCodeTrackingMap.get(pfpCodeAdactionMerge.getCodeId());
                        if (pfpCodeTracking != null) {
                            trackingSeq = pfpCodeTracking.getTrackingSeq();
                            trackingRangeDate = pfpCodeTracking.getTrackingRangeDate();
                        }
                    }

                    adBean.setCustomerInfoId(pfpCustomerInfoId);
                    if (pfpToPfdMap.containsKey(pfpCustomerInfoId)) {
                        ref = pfpToPfdMap.get(pfpCustomerInfoId);
                        adBean.setPfdCustomerInfoId(ref.getPfdCustomerInfo().getCustomerInfoId());
                        adBean.setPfdUserId(ref.getPfdUser().getUserId());
                        adBean.setPayType(Integer.parseInt(ref.getPfpPayType()));
                    } else {
                        adBean.setPfdCustomerInfoId("");
                        adBean.setPfdUserId("");
                        adBean.setPayType(1); //預付
                    }
                    adBean.setAdActionId(actionId);
                    adBean.setAdGroupId(pfpAdGroup.getAdGroupSeq());
                    adBean.setAdId(adId);
                    adBean.setAdClass(adClass.toString());
                    adBean.setAdClasses(adClass.toString().split(";"));
                    adBean.setAdStyle(pfpAd.getAdStyle());
                    adBean.setPriceType(priceType);
                    adBean.setAdType(pfpAdAction.getAdType());
                    adBean.setAdDevice(pfpAdAction.getAdDevice());
                    adBean.setRecognize(pfpCustomerInfo.getRecognize());
                    adBean.setAdActionStartDate(pfpAdAction.getAdActionStartDate());
                    adBean.setAdActionEndDate(pfpAdAction.getAdActionEndDate());
                    adBean.setAdAssignTadSeq(pfpAd.getAdAssignTadSeq());
                    adBean.setAdSpecificPlayType(pfpAdAction.getAdSpecificPlayType());
                    adBean.setAdPvLimitStyle(adPvLimitStyle);
                    adBean.setAdPvLimitPeriod(adPvLimitPeriod);
                    adBean.setAdPvLimitAmount(adPvLimitAmount);
                    adBean.setAdmArw(admArw);
                    adBean.setCategoryCodes(categoryCode);
                    adBean.setAdActionSex(StringUtils.defaultString(pfpAdAction.getAdActionSex(), ""));
                    adBean.setAdActionStartAge(pfpAdAction.getAdActionStartAge());
                    adBean.setAdActionEndAge(pfpAdAction.getAdActionEndAge());
                    adBean.setAdActionTime(adActionTime.toString());
                    adBean.setPfpCustomerInfoRemain(pfpCustomerInfo.getLaterRemain());
                    adBean.setAdActionMaxPrice(pfpAdAction.getAdActionMax());
                    adBean.setAdActionControlPrice(pfpAdAction.getAdActionControlPrice() / makeNumber / serverNumber);
                    adBean.setAdSearchPrice(adSearchPrice);
                    adBean.setAdChannelPrice(adChannelPrice);
                    adBean.setAdBidPrice(adBidPrice);
                    adBean.setQualityGrade(qualityGrade);
                    adBean.setAdSearchCount(adSearchPrice.intValue());
                    adBean.setAdChannelCount(adChannelPrice.intValue());
                    adBean.setAdBidCount(adBidPrice.intValue());
                    adBean.setAdPv(pvclkSums[0]);
                    adBean.setAdClk(pvclkSums[1]);
                    adBean.setTrackingSeq(trackingSeq);
                    adBean.setTrackingRangeDate(trackingRangeDate);
                    adBean.setAdActionCity(StringUtils.defaultString(pfpAdAction.getAdActionCity(), ""));
                    adBean.setAdActionCountry(StringUtils.defaultString(pfpAdAction.getAdActionCountry(), ""));
                }

                adDetailBean = adBean.getAdDetailMap().get(adId);
                if (adDetailBean == null) {
                    adDetailBean = new AdDetailBean();
                }

                adDetailBean.setAdDetailId(pfpAdDetail.getAdDetailSeq());
                adDetailBean.setAdDetailType(pfpAdDetail.getAdDetailId());
                if ("real_url".equals(pfpAdDetail.getAdDetailId())) {
                    adDetailBean.setAdDetailContent(this.encodeUrl(pfpAdDetail.getAdDetailContent()).trim());
                }
                // special rule
                else if ("mp4_url".equals(pfpAdDetail.getAdDetailId())) {
                    String youtubeMp4Url = this.getVideoUrl(pfpAdDetail.getAdDetailContent(), 22);
                    if(StringUtils.isBlank(youtubeMp4Url)){
                        youtubeMp4Url = this.getVideoUrl(pfpAdDetail.getAdDetailContent(), 18);
                    }
                    adDetailBean.setAdDetailContent(youtubeMp4Url.trim());
                }
                // special rule
                else if ("webm_url".equals(pfpAdDetail.getAdDetailId())) {
                    String youtubeWebmUrl = this.getVideoUrl(pfpAdDetail.getAdDetailContent(), 247);
                    if(StringUtils.isBlank(youtubeWebmUrl)){
                        youtubeWebmUrl = this.getVideoUrl(pfpAdDetail.getAdDetailContent(), 43);
                    }
                    adDetailBean.setAdDetailContent(youtubeWebmUrl.trim());
                }
                // refactor data structure
                else if ("prod_list".equals(pfpAdDetail.getAdDetailId())) {
                    String prodListId = pfpAdDetail.getAdDetailContent().trim();
                    adBean.setProdListId(prodListId);
                    adDetailBean.setAdDetailContent(prodListId);
                }
                // refactor data structure
                else if ("prod_group".equals(pfpAdDetail.getAdDetailId())) {
                    String prodGroupId = pfpAdDetail.getAdDetailContent().trim();
                    adBean.setProdGroupId(prodGroupId);
                    adDetailBean.setAdDetailContent(prodGroupId);
                }
                else {
                    adDetailBean.setAdDetailContent(pfpAdDetail.getAdDetailContent().trim());
                }
                adDetailBean.setDadId(pfpAdDetail.getDefineAdSeq());

                adBean.getAdDetailMap().put(pfpAdDetail.getAdDetailSeq(), adDetailBean);
                adMap.put(adId, adBean);
                poolMap.put(adPoolId, adMap);
            } catch (Exception e) {
                log.error("adDetailId=" + pfpAdDetail.getAdDetailSeq(), e);
            }
        }
        log.info("pool: " + poolMap.size());

        // to xml
        XStream xstream = new XStream();
        xstream.alias("adBean", AdBean.class);
        xstream.alias("adDetailBean", AdDetailBean.class);
        String xml = xstream.toXML(poolMap);

        // write file
        File file = new File(kernelAddataDir + "pool.xml");
        log.info(WRITE_FILE + file.getPath());
        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        // for prod
        this.currentPoolMap = poolMap;

        long endTime = Calendar.getInstance().getTimeInMillis();
        log.info("time: " + (endTime - startTime) / 1000 + "s");
    }

    private void keyword() throws IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();

        // load adGroup
        List<String> groupList = pfpCustomerInfoService.selectValidAdGroup("keyword");
        Map<String, Float> pfpKeywordSyspriceMap = pfpKeywordSyspriceService.getKeywordMap();
        List<PfpAdExcludeKeyword> pfpAdExcludeKeywordList = null;
        List<ValidKeywordBean> validKeywordList = null;

        // lucene
        File keywordDir = new File(kernelAddataDir + "keyword/");
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, new IKAnalyzer());
        Document doc = null;

        // yesterday
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String pvclkDate = df.format(calendar.getTime());

        log.info("writer index = " + keywordDir.getPath());

        // write index
        try (IndexWriter writer = new IndexWriter(FSDirectory.open(keywordDir), indexWriterConfig)) {
            for (String groupId: groupList) {
                pfpAdExcludeKeywordList = pfpAdExcludeKeywordService.selectPfpAdExcludeKeywords(groupId, EnumExcludeKeywordStatus.START.getStatusId());
                validKeywordList = pfpCustomerInfoService.selectValidAdKeyword(pfpAdExcludeKeywordList, pfpKeywordSyspriceMap, groupId, pvclkDate);

                for (ValidKeywordBean bean: validKeywordList) {
                    // write lucene
                    doc = new Document();
                    doc.add(new Field(EnumIndexField.AD_ACTION_ID.toString(), bean.getAdActionId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_GROUP_ID.toString(), bean.getAdGroupId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_ID.toString(), bean.getAdId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_ID.toString(), bean.getAdKeywordId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD.toString(), bean.getAdKeyword(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_EXCLUDE_KEYWORD.toString(), bean.getAdExcludeKeyword(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_ACTION_CONTROL_PRICE.toString(), String.valueOf(bean.getAdActionControlPrice() / makeNumber / serverNumber), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_SEARCH_PRICE.toString(), String.valueOf(bean.getAdKeywordSearchPrice()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_SEARCH_PHRASE_PRICE.toString(), String.valueOf(bean.getAdKeywordSearchPhrasePrice()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_SEARCH_PRECISION_PRICE.toString(), String.valueOf(bean.getAdKeywordSearchPrecisionPrice()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_CHANNEL_PRICE.toString(), String.valueOf(bean.getAdKeywordChannelPrice()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_TEMP_PRICE.toString(), String.valueOf(bean.getAdKeywordTempPrice()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_OPEN.toString(), String.valueOf(bean.getAdKeywordOpen()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_PHRASE_OPEN.toString(), String.valueOf(bean.getAdKeywordPhraseOpen()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_PRECISION_OPEN.toString(), String.valueOf(bean.getAdKeywordPrecisionOpen()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_PV.toString(), String.valueOf(bean.getAdKeywordPv()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.AD_KEYWORD_CLK.toString(), String.valueOf(bean.getAdKeywordClk()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(EnumIndexField.RECOGNIZE.toString(), bean.getRecognize(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    writer.addDocument(doc);
                }
            }

            log.info("maxDoc: " + writer.maxDoc());
        } catch (IOException e) {
            log.error(keywordDir.getPath(), e);
            throw e;
        }

        long endTime = Calendar.getInstance().getTimeInMillis();
        log.info("time: " + (endTime - startTime) / 1000 + "s");
    }

    private void area() throws IOException {
        Map<Integer, PfbxAreaBean> map = new HashMap<>();
        PfbxAreaBean pfbxAreaBean = null;

        List<PfbxArea> list = pfbxAreaService.loadAll();
        for (PfbxArea pfbxArea: list) {
            pfbxAreaBean = new PfbxAreaBean();
            pfbxAreaBean.setId(pfbxArea.getId());
            pfbxAreaBean.setArea(pfbxArea.getArea());

            map.put(pfbxAreaBean.getId(), pfbxAreaBean);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxAreaBean", PfbxAreaBean.class);
        String xml = xstream.toXML(map);

        // write file
        File file = new File(kernelAddataDir + "pfbxArea.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxArea: " + map.size());
    }

    private void position() throws IOException {
        Map<String, PfbxPositionBean> map = new HashMap<>();
        PfbxPositionBean pfbxPositionBean = null;

        List<PfbxPosition> list = pfbxPositionService.selectPfbxPositionByDeleteFlag(0);
        for (PfbxPosition pfbxPosition: list) {
            pfbxPositionBean = new PfbxPositionBean();
            pfbxPositionBean.setId(pfbxPosition.getPId());
            pfbxPositionBean.setPfbxCustomerInfoId(pfbxPosition.getPfbxCustomerInfo().getCustomerInfoId());
            pfbxPositionBean.setPfdCustomerInfoId(pfbxPosition.getPfdCustInfoId());
            pfbxPositionBean.setPfpCustomerInfoId(pfbxPosition.getPfpCustInfoId());
            pfbxPositionBean.setpName(pfbxPosition.getPName());
            pfbxPositionBean.setsId(pfbxPosition.getSId());
            pfbxPositionBean.setxType(pfbxPosition.getTemplateProductXType());
            pfbxPositionBean.setpType(pfbxPosition.getPType());
            pfbxPositionBean.setpPrice(pfbxPosition.getPPrice());

            map.put(pfbxPositionBean.getId(), pfbxPositionBean);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxPositionBean", PfbxPositionBean.class);
        String xml = xstream.toXML(map);

        // write file
        File file = new File(kernelAddataDir + "pfbxPosition.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxPosition: " + map.size());
    }

    private void size() throws IOException {
        Map<Integer, PfbxSizeBean> map = new HashMap<>();
        PfbxSizeBean pfbxSizeBean = null;

        List<PfbxSize> list = pfbxSizeService.loadAll();
        for (PfbxSize pfbxSize: list) {
            pfbxSizeBean = new PfbxSizeBean();
            pfbxSizeBean.setId(pfbxSize.getId());
            pfbxSizeBean.setHeight(pfbxSize.getHeight());
            pfbxSizeBean.setWidth(pfbxSize.getWidth());

            map.put(pfbxSizeBean.getId(), pfbxSizeBean);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxSizeBean", PfbxSizeBean.class);
        String xml = xstream.toXML(map);

        // write file
        File file = new File(kernelAddataDir + "pfbxSize.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxSize: " + map.size());
    }

    private void url() throws IOException {
        Map<Integer, PfbxUrlBean> map = new HashMap<>();
        PfbxUrlBean pfbxUrlBean = null;

        List<PfbxUrl> list = pfbxUrlService.loadAll();
        for (PfbxUrl pfbxUrl: list) {
            pfbxUrlBean = new PfbxUrlBean();
            pfbxUrlBean.setId(pfbxUrl.getUrlId());
            pfbxUrlBean.setPfbxCustomerInfoId(pfbxUrl.getPfbxCustomerInfo().getCustomerInfoId());
            pfbxUrlBean.setUrl(pfbxUrl.getUrl());

            map.put(pfbxUrlBean.getId(), pfbxUrlBean);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxUrlBean", PfbxUrlBean.class);
        String xml = xstream.toXML(map);

        // write file
        File file = new File(kernelAddataDir + "pfbxUrl.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxUrl: " + map.size());
    }

    private void group() throws IOException {
        // pfbxCustomerInfoId > PfbxUserGroupBean
        Map<String, Map<String, PfbxUserGroupBean>> pfbxCustomerInfoMap = new HashMap<>();
        Map<String, PfbxUserGroupBean> pfbxUserGroupMap = null;
        PfbxUserGroupBean pfbxUserGroupBean = null;

        List<PfbxUserGroup> list = pfbxUserGroupService.selectPfbxUserGroupByStatus("1");   // no enum
        for (PfbxUserGroup pfbxUserGroup: list) {
            pfbxUserGroupBean = new PfbxUserGroupBean();
            pfbxUserGroupBean.setId(pfbxUserGroup.getGId());
            pfbxUserGroupBean.setPfbxCustomerInfoId(pfbxUserGroup.getPfbxCustomerInfo().getCustomerInfoId());
            pfbxUserGroupBean.setGroupName(pfbxUserGroup.getGroupName());
            pfbxUserGroupBean.setPositionAllowType(pfbxUserGroup.getPiositionAllowType());
            pfbxUserGroupBean.setSizeAllowType(pfbxUserGroup.getSizeAllowType());
            pfbxUserGroupBean.setUrlAllowType(pfbxUserGroup.getUrlAllowType());

            // area
            for (PfbxGroupArea pfbxGroupArea: pfbxUserGroup.getPfbxGroupAreas()) {
                pfbxUserGroupBean.getAreas().add(pfbxGroupArea.getPfbxArea().getId());
            }

            // position
            for (PfbxGroupCode pfbxGroupCode: pfbxUserGroup.getPfbxGroupCodes()) {
                pfbxUserGroupBean.getPositions().add(pfbxGroupCode.getPfbxPosition().getPId());
            }

            // size
            for (PfbxGroupSize pfbxGroupSize: pfbxUserGroup.getPfbxGroupSizes()) {
                pfbxUserGroupBean.getSizes().add(pfbxGroupSize.getPfbxSize().getId());
            }

            // url
            for (PfbxGroupUrl pfbxGroupUrl: pfbxUserGroup.getPfbxGroupUrls()) {
                pfbxUserGroupBean.getUrls().add(pfbxGroupUrl.getPfbxUrl().getUrlId());
            }

            pfbxUserGroupMap = pfbxCustomerInfoMap.get(pfbxUserGroupBean.getPfbxCustomerInfoId());
            if (pfbxUserGroupMap == null) {
                pfbxUserGroupMap = new HashMap<>();
            }
            pfbxUserGroupMap.put(pfbxUserGroupBean.getId(), pfbxUserGroupBean);

            pfbxCustomerInfoMap.put(pfbxUserGroupBean.getPfbxCustomerInfoId(), pfbxUserGroupMap);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxUserGroupBean", PfbxUserGroupBean.class);
        String xml = xstream.toXML(pfbxCustomerInfoMap);

        // write file
        File file = new File(kernelAddataDir + "pfbxGroup.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxGroup: " + pfbxCustomerInfoMap.size());
    }

    private void option() throws IOException {
        // pfbxCustomerInfoId > PfbxUserOptionBean
        Map<String, Map<String, PfbxUserOptionBean>> pfbxCustomerInfoMap = new HashMap<>();
        Map<String, PfbxUserOptionBean> pfbxUserOptionMap = null;
        PfbxUserOptionBean pfbxUserOptionBean = null;
        AdCategoryNewBean adCategoryNewBean = null;

        List<PfbxUserOption> list = pfbxUserOptionService.selectPfbxUserOptionByStatus("1");   // no enum
        for (PfbxUserOption pfbxUserOption: list) {
            pfbxUserOptionBean = new PfbxUserOptionBean();
            pfbxUserOptionBean.setId(pfbxUserOption.getOId());
            pfbxUserOptionBean.setPfbxCustomerInfoId(pfbxUserOption.getPfbxCustomerInfo().getCustomerInfoId());
            pfbxUserOptionBean.setOptionName(pfbxUserOption.getOptionName());
            pfbxUserOptionBean.setPositionAllowType(pfbxUserOption.getPiositionAllowType());
            pfbxUserOptionBean.setSizeAllowType(pfbxUserOption.getSizeAllowType());
            pfbxUserOptionBean.setUrlAllowType(pfbxUserOption.getUrlAllowType());

            // allowIndustry
            for (PfbxAllowIndustry pfbxAllowIndustry: pfbxUserOption.getPfbxAllowIndustries()) {
                adCategoryNewBean = new AdCategoryNewBean();
                adCategoryNewBean.setId(pfbxAllowIndustry.getPfpAdCategoryNew().getId());
                adCategoryNewBean.setPartnerId(pfbxAllowIndustry.getPfpAdCategoryNew().getPartnerId());
                adCategoryNewBean.setParentId(pfbxAllowIndustry.getPfpAdCategoryNew().getParentId());
                adCategoryNewBean.setCode(pfbxAllowIndustry.getPfpAdCategoryNew().getCode());
                adCategoryNewBean.setName(pfbxAllowIndustry.getPfpAdCategoryNew().getName());
                adCategoryNewBean.setLevel(pfbxAllowIndustry.getPfpAdCategoryNew().getLevel());

                pfbxUserOptionBean.getAllowIndustries().put(adCategoryNewBean.getId(), adCategoryNewBean);
            }

            // allowCusurl
            for (PfbxAllowCusurl pfbxAllowCusurl: pfbxUserOption.getPfbxAllowCusurls()) {
                pfbxUserOptionBean.getAllowCusurls().put(pfbxAllowCusurl.getCuId(), this.encodeUrl(pfbxAllowCusurl.getUrl()));
            }

            // blockIndustry
            for (PfbxBlockIndustry pfbxBlockIndustry: pfbxUserOption.getPfbxBlockIndustries()) {
                adCategoryNewBean = new AdCategoryNewBean();
                adCategoryNewBean.setId(pfbxBlockIndustry.getPfpAdCategoryNew().getId());
                adCategoryNewBean.setPartnerId(pfbxBlockIndustry.getPfpAdCategoryNew().getPartnerId());
                adCategoryNewBean.setParentId(pfbxBlockIndustry.getPfpAdCategoryNew().getParentId());
                adCategoryNewBean.setCode(pfbxBlockIndustry.getPfpAdCategoryNew().getCode());
                adCategoryNewBean.setName(pfbxBlockIndustry.getPfpAdCategoryNew().getName());
                adCategoryNewBean.setLevel(pfbxBlockIndustry.getPfpAdCategoryNew().getLevel());

                pfbxUserOptionBean.getBlockIndustries().put(adCategoryNewBean.getId(), adCategoryNewBean);
            }

            // blockCusurl
            for (PfbxBlockCusurl pfbxBlockCusurl: pfbxUserOption.getPfbxBlockCusurls()) {
                pfbxUserOptionBean.getBlockCusurls().put(pfbxBlockCusurl.getCuId(), this.encodeUrl(pfbxBlockCusurl.getUrl()));
            }

            // area
            for (PfbxOptionArea pfbxOptionArea: pfbxUserOption.getPfbxOptionAreas()) {
                pfbxUserOptionBean.getAreas().add(pfbxOptionArea.getPfbxArea().getId());
            }

            // position
            for (PfbxOptionCode pfbxOptionCode: pfbxUserOption.getPfbxOptionCodes()) {
                pfbxUserOptionBean.getPositions().add(pfbxOptionCode.getPfbxPosition().getPId());
            }

            // size
            for (PfbxOptionSize pfbxOptionSize: pfbxUserOption.getPfbxOptionSizes()) {
                pfbxUserOptionBean.getSizes().add(pfbxOptionSize.getPfbxSize().getId());
            }

            // url
            for (PfbxOptionUrl pfbxOptionUrl: pfbxUserOption.getPfbxOptionUrls()) {
                pfbxUserOptionBean.getUrls().add(pfbxOptionUrl.getPfbxUrl().getUrlId());
            }

            pfbxUserOptionMap = pfbxCustomerInfoMap.get(pfbxUserOptionBean.getPfbxCustomerInfoId());
            if (pfbxUserOptionMap == null) {
                pfbxUserOptionMap = new HashMap<>();
            }
            pfbxUserOptionMap.put(pfbxUserOptionBean.getId(), pfbxUserOptionBean);

            pfbxCustomerInfoMap.put(pfbxUserOptionBean.getPfbxCustomerInfoId(), pfbxUserOptionMap);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxUserOptionBean", PfbxUserOptionBean.class);
        xstream.alias("adCategoryNewBean", AdCategoryNewBean.class);
        String xml = xstream.toXML(pfbxCustomerInfoMap);

        // write file
        File file = new File(kernelAddataDir + "pfbxOption.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxOption: " + pfbxCustomerInfoMap.size());
    }

    private void sample() throws IOException {
        // pfbxCustomerInfoId > PfbxUserSampleBean
        Map<String, Map<String, PfbxUserSampleBean>> pfbxCustomerInfoMap = new HashMap<>();
        Map<String, PfbxUserSampleBean> pfbxUserSampleMap = null;
        PfbxUserSampleBean pfbxUserSampleBean = null;

        List<PfbxUserSample> list = pfbxUserSampleService.selectPfbxUserSampleByStatus("1");   // no enum
        for (PfbxUserSample pfbxUserSample: list) {
            pfbxUserSampleBean = new PfbxUserSampleBean();
            pfbxUserSampleBean.setId(pfbxUserSample.getSId());
            pfbxUserSampleBean.setPfbxCustomerInfoId(pfbxUserSample.getPfbxCustomerInfo().getCustomerInfoId());
            pfbxUserSampleBean.setSampleName(pfbxUserSample.getSampleName());
            pfbxUserSampleBean.setAdType(pfbxUserSample.getAdType());
            pfbxUserSampleBean.setAdwordType(pfbxUserSample.getAdwordType());
            pfbxUserSampleBean.setBorderColor(pfbxUserSample.getBorderColor());
            pfbxUserSampleBean.setTitleColor(pfbxUserSample.getTittleColor());
            pfbxUserSampleBean.setBgColor(pfbxUserSample.getBgColor());
            pfbxUserSampleBean.setFontColor(pfbxUserSample.getFontColor());
            pfbxUserSampleBean.setUrlColor(pfbxUserSample.getUrlColor());
            pfbxUserSampleBean.setAdbackupType(pfbxUserSample.getAdbackupType());
            pfbxUserSampleBean.setAdbackupContent(pfbxUserSample.getAdbackupContent());
            pfbxUserSampleBean.setPositionAllowType(pfbxUserSample.getPiositionAllowType());
            pfbxUserSampleBean.setSizeAllowType(pfbxUserSample.getSizeAllowType());
            pfbxUserSampleBean.setUrlAllowType(pfbxUserSample.getUrlAllowType());
            pfbxUserSampleBean.setSort(pfbxUserSample.getSort());

            // area
            for (PfbxSampleArea pfbxSampleArea: pfbxUserSample.getPfbxSampleAreas()) {
                pfbxUserSampleBean.getAreas().add(pfbxSampleArea.getPfbxArea().getId());
            }

            // position
            for (PfbxSampleCode pfbxSampleCode: pfbxUserSample.getPfbxSampleCodes()) {
                pfbxUserSampleBean.getPositions().add(pfbxSampleCode.getPfbxPosition().getPId());
            }

            // size
            for (PfbxSampleSize pfbxSampleSize: pfbxUserSample.getPfbxSampleSizes()) {
                pfbxUserSampleBean.getSizes().add(pfbxSampleSize.getPfbxSize().getId());
            }

            // url
            for (PfbxSampleUrl pfbxSampleUrl: pfbxUserSample.getPfbxSampleUrls()) {
                pfbxUserSampleBean.getUrls().add(pfbxSampleUrl.getPfbxUrl().getUrlId());
            }

            pfbxUserSampleMap = pfbxCustomerInfoMap.get(pfbxUserSampleBean.getPfbxCustomerInfoId());
            if (pfbxUserSampleMap == null) {
                pfbxUserSampleMap = new LinkedHashMap<>();
            }
            pfbxUserSampleMap.put(pfbxUserSampleBean.getId(), pfbxUserSampleBean);

            pfbxCustomerInfoMap.put(pfbxUserSampleBean.getPfbxCustomerInfoId(), pfbxUserSampleMap);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxUserSampleBean", PfbxUserSampleBean.class);
        String xml = xstream.toXML(pfbxCustomerInfoMap);

        // write file
        File file = new File(kernelAddataDir + "pfbxSample.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxSample: " + pfbxCustomerInfoMap.size());
    }

    private void showRule() throws IOException {
        // pfbxCustomerInfoId > PfbxUserSampleBean
        List<AdmShowRuleBean> admShowRuleList = new ArrayList<>();
        AdmShowRuleBean admShowRuleBean = null;

        List<AdmShowRule> list = admShowRuleService.loadAll();
        for (AdmShowRule admShowRule: list) {
            admShowRuleBean = new AdmShowRuleBean();
            admShowRuleBean.setWayType(admShowRule.getWayType());
            admShowRuleBean.setPfdCustomerInfoId(admShowRule.getPfdCustomerInfoId());
            admShowRuleBean.setPfpCustomerInfoId(admShowRule.getPfpCustomerInfoId());
            admShowRuleBean.setPfbCustomerInfoId(admShowRule.getPfbCustomerInfoId());

            admShowRuleList.add(admShowRuleBean);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("admShowRuleBean", AdmShowRuleBean.class);
        String xml = xstream.toXML(admShowRuleList);

        // write file
        File file = new File(kernelAddataDir + "admShowRule.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("admShowRule: " + admShowRuleList.size());
    }

    private void blackUrl() throws IOException {
        Map<String, List<PfbxBlackUrlBean>> pfbxBlackUrlMap = new HashMap<>();
        List<PfbxBlackUrlBean> pfbxBlackUrlList = null;
        PfbxBlackUrlBean pfbxBlackUrlBean = null;
        String pfbxCustomerInfoId = null;

        List<AdmPfbxBlockUrl> admPfbxBlockUrlList = admPfbxBlockUrlService.loadAll();
        for (AdmPfbxBlockUrl admPfbxBlockUrl: admPfbxBlockUrlList) {
            pfbxCustomerInfoId = admPfbxBlockUrl.getPfbxCustomerInfo().getCustomerInfoId();
            pfbxBlackUrlList = pfbxBlackUrlMap.get(pfbxCustomerInfoId);
            if (pfbxBlackUrlList == null) {
                pfbxBlackUrlList = new ArrayList<>();
            }

            pfbxBlackUrlBean = new PfbxBlackUrlBean();
            pfbxBlackUrlBean.setPfbxCustomerInfoId(pfbxCustomerInfoId);
            pfbxBlackUrlBean.setUrl(admPfbxBlockUrl.getBlockUrl());
            pfbxBlackUrlList.add(pfbxBlackUrlBean);

            pfbxBlackUrlMap.put(pfbxCustomerInfoId, pfbxBlackUrlList);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxBlackUrlBean", PfbxBlackUrlBean.class);
        String xml = xstream.toXML(pfbxBlackUrlMap);

        // write file
        File file = new File(kernelAddataDir + "pfbxBlackUrl.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxBlackUrl: " + pfbxBlackUrlMap.size());
    }

    private void whiteUrl() throws IOException {
        Map<String, List<PfbxWhiteUrlBean>> pfbxWhiteUrlMap = new HashMap<>();
        List<PfbxWhiteUrlBean> pfbxWhiteUrlList = null;
        PfbxWhiteUrlBean pfbxWhiteUrlBean = null;
        String pfbxCustomerInfoId = null;

        List<PfbxAllowUrl> pfbxAllowUrlList = pfbxAllowUrlService.selectPfbxAllowUrl("0", "2");
        for (PfbxAllowUrl pfbxAllowUrl: pfbxAllowUrlList) {
            pfbxCustomerInfoId = pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId();
            pfbxWhiteUrlList = pfbxWhiteUrlMap.get(pfbxCustomerInfoId);
            if (pfbxWhiteUrlList == null) {
                pfbxWhiteUrlList = new ArrayList<>();
            }

            pfbxWhiteUrlBean = new PfbxWhiteUrlBean();
            pfbxWhiteUrlBean.setPfbxCustomerInfoId(pfbxCustomerInfoId);
            pfbxWhiteUrlBean.setPlayType(pfbxAllowUrl.getPfbxCustomerInfo().getPlayType());
            pfbxWhiteUrlBean.setDefaultType(pfbxAllowUrl.getDefaultType());
            pfbxWhiteUrlBean.setCategoryCode(pfbxAllowUrl.getCategoryCode());
            pfbxWhiteUrlBean.setUrl(pfbxAllowUrl.getRootDomain());
            pfbxWhiteUrlList.add(pfbxWhiteUrlBean);

            pfbxWhiteUrlMap.put(pfbxCustomerInfoId, pfbxWhiteUrlList);
        }

        // to xml
        XStream xstream = new XStream();
        xstream.alias("pfbxWhiteUrlBean", PfbxWhiteUrlBean.class);
        String xml = xstream.toXML(pfbxWhiteUrlMap);

        // write file
        File file = new File(kernelAddataDir + "pfbxWhiteUrl.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("pfbxWhiteUrl: " + pfbxWhiteUrlMap.size());
    }

    private void prod() throws IOException {
        Set<String> prodGroupSet = new HashSet<>();

        StringBuilder url = null;
        Map<String, List<Map<String, String>>> prodMap = new HashMap<>();
        List<Map<String, String>> prodList = null;
        Map<String, String> prodItemMap = null;
        String result = null;
        JSONObject jsonObject = null;
        JSONArray prodGroupArray = null;
        JSONObject prodGroupObject = null;
        Iterator<String> prodGroupIterator = null;
        String key = null;
        String value = null;

        for (Map<String, AdBean> adMap : this.currentPoolMap.values()) {
            for (AdBean adBean : adMap.values()) {
                if (StringUtils.isNotBlank(adBean.getProdGroupId())) {
                    prodGroupSet.add(adBean.getProdGroupId());
                }
            }
        }

        for (String prodGroupId: prodGroupSet) {
            url = new StringBuilder();
            url.append(pfpProdGroupListApiUrl);
            url.append("?groupId=").append(prodGroupId);
            url.append("&prodNum=11");

            result = HttpConnectionClient.getInstance().doGet(url.toString());
            jsonObject = new JSONObject(result);
            if (jsonObject.has("prodGroupList")) {
                prodList = new ArrayList<>();

                prodGroupArray = jsonObject.getJSONArray("prodGroupList");
                for (int i = 0; i < prodGroupArray.length(); i++) {
                    prodItemMap = new HashMap<>();

                    prodGroupObject = prodGroupArray.getJSONObject(i);
                    prodGroupIterator = prodGroupObject.keys();
                    while(prodGroupIterator.hasNext()) {
                        key = prodGroupIterator.next();
                        value = prodGroupObject.getString(key);

                        prodItemMap.put(key, value);
                    }

                    prodList.add(prodItemMap);
                }

                prodMap.put(prodGroupId, prodList);
            }
        }

        // to xml
        XStream xstream = new XStream();
        String xml = xstream.toXML(prodMap);

        // write file
        File file = new File(kernelAddataDir + "prod.xml");

        log.info(WRITE_FILE + file.getPath());

        FileUtils.writeStringToFile(file, xml, StandardCharsets.UTF_8);

        log.info("prodMap: " + prodMap.size());
    }

    private void scp() {
        for (SpringSSHProcessUtil2 scpProcess: scpProcessList) {
            scpProcess.sshExec("rm -rf " + kernelAddata);
            scpProcess.sshExec("mkdir -p " + kernelAddataDir);
            scpProcess.scpPutFolder(kernelAddataDir, kernelAddata, null, null, true);
            scpProcess.sshExec("touch " + kernelAddataDir + "ok.txt");

            log.info("scp put: " + kernelAddataDir + " " + kernelAddataDir);
        }

        log.info("scp ok");
    }

    private String encodeUrl(String url) {
        String[] urls = URLDecoder.decode(url, StandardCharsets.UTF_8).split("\\?");

        StringBuilder urlSb = new StringBuilder();
        if (urls.length == 1) {
            urlSb.append(encodePath(urls[0]));
        }
        else {
            urlSb.append(encodePath(urls[0]));
            urlSb.append("?").append(encodeParam(urls[1]));
        }

        // special rule
        String realUrl = urlSb.toString()
                                .replaceAll("\\+", "%20")
                                .replaceAll("\\%21", "!")
                                .replaceAll("\\%27", "'")
                                .replaceAll("\\%28", "(")
                                .replaceAll("\\%29", ")")
                                .replaceAll("\\%7E", "~")
                                .replaceAll("\\%2F", "/")
                                .replaceAll("\\%23", "#");

        return URLEncoder.encode(realUrl, StandardCharsets.UTF_8);
    }

    private String encodePath(String uri) {
        if (StringUtils.isBlank(uri)) {
            return uri;
        }

        String[] uris = uri.split("/");
        if (uris.length <= 2) {
            return uri;
        }

        StringBuilder uriSb = new StringBuilder();
        for (int i = 0; i < uris.length; i++) {
            if (i <= 2) {
                uriSb.append(uris[i]);
            }
            else {
                uriSb.append(URLEncoder.encode(uris[i], StandardCharsets.UTF_8));
            }

            if (i != uris.length - 1) {
                uriSb.append("/");
            }
        }

        return uriSb.toString();
    }

    private String encodeParam(String param) {
        if (StringUtils.isBlank(param)) {
            return param;
        }

        StringBuilder urlSb = new StringBuilder();

        String[] params = param.split("&");
        String[] keys = null;
        for (int i = 0; i < params.length; i++) {
            keys = params[i].split("=");
            if (keys.length == 2) {
                urlSb.append(keys[0]);
                urlSb.append("=");
                urlSb.append(URLEncoder.encode(keys[1], StandardCharsets.UTF_8));
            }
            else {
                urlSb.append(params[i]);
            }

            if (i != params.length - 1) {
                urlSb.append("&");
            }
        }

        return urlSb.toString();
    }

    private String getVideoUrl(String srcUrl, int fileType) {
        Process process = null;
        String descUrl = srcUrl;
        try {
            process = Runtime.getRuntime().exec(new String[] {"bash", "-c", "youtube-dl -f " + fileType + " -g " + srcUrl});
            descUrl = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            log.error(srcUrl + " " + fileType, e);
        }
        finally {
            if (process != null) {
                process.destroy();
            }
        }

        return descUrl;
    }

    public void setPfpAdExcludeKeywordService(IPfpAdExcludeKeywordService pfpAdExcludeKeywordService) {
        this.pfpAdExcludeKeywordService = pfpAdExcludeKeywordService;
    }

    public void setPfbStyleInfoService(IPfbStyleInfoService pfbStyleInfoService) {
        this.pfbStyleInfoService = pfbStyleInfoService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }

    public void setPfpAdSyspriceService(IPfpAdSyspriceService pfpAdSyspriceService) {
        this.pfpAdSyspriceService = pfpAdSyspriceService;
    }

    public void setPfpKeywordSyspriceService(IPfpKeywordSyspriceService pfpKeywordSyspriceService) {
        this.pfpKeywordSyspriceService = pfpKeywordSyspriceService;
    }

    public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
        this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
    }

    public void setPfpAdCategoryMappingService(IPfpAdCategoryMappingService pfpAdCategoryMappingService) {
        this.pfpAdCategoryMappingService = pfpAdCategoryMappingService;
    }

    public void setPfpAdSpecificWebsiteService(IPfpAdSpecificWebsiteService pfpAdSpecificWebsiteService) {
        this.pfpAdSpecificWebsiteService = pfpAdSpecificWebsiteService;
    }

    public void setPfbxAreaService(IPfbxAreaService pfbxAreaService) {
        this.pfbxAreaService = pfbxAreaService;
    }

    public void setPfbxPositionService(IPfbxPositionService pfbxPositionService) {
        this.pfbxPositionService = pfbxPositionService;
    }

    public void setPfbxSizeService(IPfbxSizeService pfbxSizeService) {
        this.pfbxSizeService = pfbxSizeService;
    }

    public void setPfbxUrlService(IPfbxUrlService pfbxUrlService) {
        this.pfbxUrlService = pfbxUrlService;
    }

    public void setPfbxUserGroupService(IPfbxUserGroupService pfbxUserGroupService) {
        this.pfbxUserGroupService = pfbxUserGroupService;
    }

    public void setPfbxUserOptionService(IPfbxUserOptionService pfbxUserOptionService) {
        this.pfbxUserOptionService = pfbxUserOptionService;
    }

    public void setPfbxUserSampleService(IPfbxUserSampleService pfbxUserSampleService) {
        this.pfbxUserSampleService = pfbxUserSampleService;
    }

    public void setAdmShowRuleService(IAdmShowRuleService admShowRuleService) {
        this.admShowRuleService = admShowRuleService;
    }

    public void setAdmArwValueService(IAdmArwValueService admArwValueService) {
        this.admArwValueService = admArwValueService;
    }

    public void setAdmPfbxBlockUrlService(IAdmPfbxBlockUrlService admPfbxBlockUrlService) {
        this.admPfbxBlockUrlService = admPfbxBlockUrlService;
    }

    public void setPfbxAllowUrlService(IPfbxAllowUrlService pfbxAllowUrlService) {
        this.pfbxAllowUrlService = pfbxAllowUrlService;
    }

    public void setPfpCodeAdactionMergeService(IPfpCodeAdactionMergeService pfpCodeAdactionMergeService) {
        this.pfpCodeAdactionMergeService = pfpCodeAdactionMergeService;
    }

    public void setPfpCodeTrackingService(IPfpCodeTrackingService pfpCodeTrackingService) {
        this.pfpCodeTrackingService = pfpCodeTrackingService;
    }

    public void setAdmAddata(String admAddata) {
        this.admAddata = admAddata;
    }

    public void setKernelAddata(String kernelAddata) {
        this.kernelAddata = kernelAddata;
    }

    public void setPfpProdGroupListApiUrl(String pfpProdGroupListApiUrl) {
        this.pfpProdGroupListApiUrl = pfpProdGroupListApiUrl;
    }

    public void setAdSysprice(float adSysprice) {
        this.adSysprice = adSysprice;
    }

    public void setMakeNumber(int makeNumber) {
        this.makeNumber = makeNumber;
    }

    public void setServerNumber(int serverNumber) {
        this.serverNumber = serverNumber;
    }

    public void setScpProcessList(List<SpringSSHProcessUtil2> scpProcessList) {
        this.scpProcessList = scpProcessList;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        KernelJob job = context.getBean(KernelJob.class);
        job.process();
        ((FileSystemXmlApplicationContext) context).close();
    }
}
