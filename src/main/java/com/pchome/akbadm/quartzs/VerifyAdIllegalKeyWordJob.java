package com.pchome.akbadm.quartzs;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpCatalogLogo;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.pojo.PfpIllegalKeyword;
import com.pchome.akbadm.db.service.ad.IPfpAdDetailService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.catalog.IPfpCatalogLogoService;
import com.pchome.akbadm.db.service.catalog.IPfpCatalogProdEcService;
import com.pchome.akbadm.db.service.check.IPfpIllegalKeywordService;
import com.pchome.akbadm.db.vo.AdQueryConditionVO;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.catalog.EnumCatalogDeleteStatus;
import com.pchome.enumerate.catalog.EnumCatalogLogoStatus;
import com.pchome.enumerate.catalog.EnumCatalogProdEcCheckStatus;
import com.pchome.enumerate.catalog.EnumCatalogProdEcStatus;
import com.pchome.enumerate.catalog.EnumCatalogUploadStatus;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

/**
 * 審核廣告排程
 * @author johnwei
 *
 */
@Transactional
public class VerifyAdIllegalKeyWordJob{

	protected Logger log = LogManager.getRootLogger();

	private IPfpAdService pfpAdService;
	private IPfpAdDetailService pfpAdDetailService;
    private IPfpCatalogLogoService pfpCatalogLogoService;
    private IPfpCatalogProdEcService pfpCatalogProdEcService;
	private IPfpIllegalKeywordService pfpIllegalKeywordService;
	private SpringEmailUtil springEmailUtil;

    private int pageNo = 1;
    private int pageSize = 100000;

	//要檢核的屬性
	private String[] verifyAttributes = {"title", "content"};

	public static final String MAIL_API_NO = "P144";

	public void process() throws Exception {
        log.info("====VerifyAdIllegalKeyWordJob.process() start====");

		Date now = new Date();

		//全部禁用字
		List<PfpIllegalKeyword> illegalKeywordList = pfpIllegalKeywordService.loadAll();

		//撈出所有未審核的 ad
		AdQueryConditionVO queryVO = new AdQueryConditionVO();
		queryVO.setStatus(new String[]{Integer.toString(EnumAdStatus.NoVerify.getStatusId())}); //未審核
		List<PfpAd> adList = pfpAdService.getPfpAdByConditions(queryVO, -1, 0);

		log.info(">>> adList.size() = " + adList.size());

		//有新廣告要通知管理者進來審
		if (adList.size()>0) {

			try {

				Mail mail = null;

		        try {

		        	mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		        	if (mail == null) {
		        		throw new Exception("Mail Object is null.");
		        	}

		        } catch (Exception e) {
		        	log.error(e.getMessage(), e);
		        	throw e;
		        }

	            mail.setMsg("<html><body>您有 " + adList.size() + " 件新廣告待審</body></html>");

	            springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		for (int w=0; w<adList.size(); w++) {

			PfpAd ad = adList.get(w);
			log.info(">>> -------------------- ad_seq = " + ad.getAdSeq());

			Set<PfpAdDetail> adDetailSet = ad.getPfpAdDetails();

			log.info(">>> adDetailSet.size() = " + adDetailSet.size());

			if (adDetailSet.size()>0) {

				boolean[] detailVerifyResults = new boolean[adDetailSet.size()];

				int index = 0;
				Iterator<PfpAdDetail> it = adDetailSet.iterator();
				while (it.hasNext()) {

					PfpAdDetail adDetail = it.next();
					log.info(">>> -------------------- ad_detail_seq = " + adDetail.getAdDetailSeq());

					String attribute = adDetail.getAdDetailId();
					log.info(">>> attribute = " + attribute);

					//判斷那些屬性要驗證，目前只有 title 和 content
					boolean needVerify = false;
					for (int k=0; k<verifyAttributes.length; k++) {
						if (attribute.equals(verifyAttributes[k])) {
							needVerify = true;
							break;
						}
					}

					log.info(">>> needVerify = " + needVerify);
					if (needVerify) {

						boolean detailVerifyResult = true; //審查結果, true: 通過, false: 失敗

						String bingoKeyWord = "";

						String content = adDetail.getAdDetailContent();
						log.info(">>> content = " + content);

						String[] illegalKeywordArray = null;
						for (int y=0; y<illegalKeywordList.size(); y++) {

							boolean pass = false; //檢核成果(true: 通過, false: 失敗)

							String illegalKeyword = illegalKeywordList.get(y).getContent();

							if (illegalKeyword.indexOf("%")!=-1) {
								illegalKeywordArray = illegalKeyword.split("%");
							} else {
								illegalKeywordArray = new String[]{illegalKeyword};
							}

							int illegalKeywordAmount = illegalKeywordArray.length;
							boolean[] bingo = new boolean[illegalKeywordAmount];
							for (int z=0; z<illegalKeywordAmount; z++) {
								if (content.indexOf(illegalKeywordArray[z])!=-1) {
									bingo[z] = true;
								}
							}

							for (int x=0; x<bingo.length; x++) {
								if (!bingo[x]) {
									pass = true;
									break;
								}
							}

							if (!pass) {
								detailVerifyResult = false;
								bingoKeyWord += illegalKeyword + ",";
							}
						}

						if (StringUtils.isNotEmpty(bingoKeyWord)) {
							if (bingoKeyWord.lastIndexOf(",") == bingoKeyWord.length()-1) {
								bingoKeyWord = bingoKeyWord.substring(0, bingoKeyWord.length()-1);
							}
						}

						if (detailVerifyResult) {
							adDetail.setVerifyStatus("y");
						} else {
							adDetail.setVerifyStatus("n");
							adDetail.setIllegalKeyword(bingoKeyWord);
						}
						adDetail.setSysVerifyTime(now);

						log.info(">>> VerifyStatus = " + adDetail.getVerifyStatus());
						log.info(">>> IllegalKeyword = " + adDetail.getIllegalKeyword());
						log.info(">>> VerifyTime = " + adDetail.getSysVerifyTime());

						pfpAdDetailService.saveOrUpdate(adDetail);

						detailVerifyResults[index] = detailVerifyResult;

					} else {
						detailVerifyResults[index] = true; //不需要審的直接通過
					}

					index++;
				}

				boolean adVerifyResult = true;
				for (int i=0; i<detailVerifyResults.length; i++) {
					if (!detailVerifyResults[i]) {
						adVerifyResult = false;
						break;
					}
				}
				log.info(">>> adVerifyResult = " + adVerifyResult);

				if (adVerifyResult) {
					ad.setAdStatus(EnumAdStatus.Verify_sys_pass.getStatusId()); //系統審核通過
				} else {
					ad.setAdStatus(EnumAdStatus.Verify_sys_regect.getStatusId()); //系統審核不過
				}
				ad.setAdSysVerifyTime(now);

				pfpAdService.saveOrUpdate(ad);
				log.info(">>> save ad");

			} else {
				log.info(">>> no ad details");
			}
		}

        // prod
		List<Map<String, Object>> catalogProdEcList = pfpCatalogProdEcService.selectPfpCatalogProdEc(null, null, EnumCatalogUploadStatus.COMPLETE.getStatus(), EnumCatalogDeleteStatus.UNDELETE.getStatus(), EnumCatalogProdEcStatus.OPEN.getStatus(), EnumCatalogProdEcCheckStatus.VERIFY.getStatus(), pageNo, pageSize);
		log.info("catalogProdEcList.size() = " + catalogProdEcList.size());
        if (!catalogProdEcList.isEmpty()) {
            try {
                Mail mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
                if (mail != null) {
                    mail.setMsg("<html><body>您有 " + catalogProdEcList.size() + " 件新商品待審</body></html>");
                    springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
                }

                PfpCatalogProdEc pfpCatalogProdEc = null;
                for (Map<String, Object> map: catalogProdEcList) {
                    pfpCatalogProdEc = pfpCatalogProdEcService.get((Integer) map.get("id"));
                    pfpCatalogProdEc.setEcCheckStatus(EnumCatalogLogoStatus.VERIFYING.getStatus());
                    pfpCatalogProdEc.setUpdateDate(now);
                    pfpCatalogProdEcService.update(pfpCatalogProdEc);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        // logo
        List<Map<String, Object>> catalogLogoList = pfpCatalogLogoService.selectCatalogLogo(null, EnumCatalogLogoStatus.VERIFY.getStatus(), pageNo, pageSize);
        log.info("catalogLogoList.size() = " + catalogLogoList.size());
        if (!catalogLogoList.isEmpty()) {
            try {
                Mail mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
                if (mail != null) {
                    mail.setMsg("<html><body>您有 " + catalogLogoList.size() + " 件新LOGO待審</body></html>");
                    springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
                }

                PfpCatalogLogo pfpCatalogLogo = null;
                for (Map<String, Object> map: catalogLogoList) {
                    pfpCatalogLogo = pfpCatalogLogoService.get((String) map.get("catalog_logo_seq"));
                    pfpCatalogLogo.setStatus(EnumCatalogLogoStatus.VERIFYING.getStatus());
                    pfpCatalogLogo.setUpdateDate(now);
                    pfpCatalogLogoService.update(pfpCatalogLogo);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        log.info("====VerifyAdIllegalKeyWordJob.process() end====");
	}

	public void setPfpAdService(IPfpAdService pfpAdService) {
		this.pfpAdService = pfpAdService;
	}

	public void setPfpAdDetailService(IPfpAdDetailService pfpAdDetailService) {
		this.pfpAdDetailService = pfpAdDetailService;
	}

    public void setPfpCatalogLogoService(IPfpCatalogLogoService pfpCatalogLogoService) {
        this.pfpCatalogLogoService = pfpCatalogLogoService;
    }

    public void setPfpCatalogProdEcService(IPfpCatalogProdEcService pfpCatalogProdEcService) {
        this.pfpCatalogProdEcService = pfpCatalogProdEcService;
    }

	public void setPfpIllegalKeywordService(IPfpIllegalKeywordService pfpIllegalKeywordService) {
		this.pfpIllegalKeywordService = pfpIllegalKeywordService;
	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		VerifyAdIllegalKeyWordJob job = context.getBean(VerifyAdIllegalKeyWordJob.class);

		job.process();
	}
}
