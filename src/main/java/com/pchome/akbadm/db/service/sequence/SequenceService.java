package com.pchome.akbadm.db.service.sequence;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.sequence.ISequenceDAO;
import com.pchome.akbadm.db.dao.template.AdmTemplateProductDAO;
import com.pchome.akbadm.db.pojo.Sequence;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.config.TestConfig;
import com.pchome.rmi.sequence.EnumSequenceTableName;


public class SequenceService extends BaseService<Sequence, String> implements ISequenceService {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private int orderDayNum;
	

	public void setOrderDayNum(int orderDayNum) {
		this.orderDayNum = orderDayNum;
	}

	private Sequence getSequence(EnumSequenceTableName enumSequenceTableName) {
		Sequence sequence = ((ISequenceDAO) dao).get(enumSequenceTableName.getSnoName());
		Date date = new Date();
		String tableDate = sdf.format(date);

		// no data
		if (sequence == null) {
			sequence = new Sequence();
			sequence.setTableName(enumSequenceTableName.getSnoName());
			sequence.setTableChar(enumSequenceTableName.getCharName().toUpperCase());
			sequence.setTableDate(tableDate);
			sequence.setTableNo(1);
			sequence.setCreateDate(date);
		} else {
			// today
			if (tableDate.equals(sequence.getTableDate())) {
				int num = sequence.getTableNo();
				sequence.setTableNo(++num);
			}
			// yesterday
			else {
				
				if(EnumSequenceTableName.ADM_TEMPLATE_AD==enumSequenceTableName || EnumSequenceTableName.ADM_TEMPLATE_PRODUCT==enumSequenceTableName){
				
					int num = sequence.getTableNo();
					sequence.setTableNo(++num);
				
				}else{
				
					sequence.setTableDate(tableDate);
					sequence.setTableNo(1);
				}
			}
		}

		sequence.setUpdateDate(date);
		((ISequenceDAO) dao).saveOrUpdate(sequence);

		return sequence;
	}

    /**
     * 補數字字串
     * @param str 原始字串
     * @param limit 限定長度
     * @return 補數字字串
     */
    private String getZeroSeq(String str, int limit) {
        if ((str.length() - limit) >= -2) {
            return StringUtils.leftPad(str, limit, '0');
        }

        StringBuffer sb = new StringBuffer();
        sb.append("0");
        for (int i = 0, length = limit - str.length() - 2; i < length; i++) {
            sb.append(limit - i);
        }
        sb.append("0").append(str);

//        log.info("zeroSeq = " + sb);

        return sb.toString();
    }

    /**
     * 補數字字串
     * @param str 原始字串
     * @param limit 限定長度
     * @return 補數字字串
     */
    private String getAddZeroSeq(String str, int limit) {
        return StringUtils.leftPad(str, limit, '0');
    }

	private String getIDForTable(EnumSequenceTableName enumSequenceTableName, String mid){
		Sequence sequence = getSequence(enumSequenceTableName);

		int limit=0;
		String tableDate="";
		String checkCode="";
		StringBuffer tableNo = new StringBuffer();
		int no = sequence.getTableNo();

		limit = orderDayNum;
		tableDate=sequence.getTableDate();
		checkCode="";

		for (int i = 0, length = limit - String.valueOf(no).length(); i < length; i++) {
			tableNo.append("0");
		}

		tableNo.append(no);

		StringBuffer id = new StringBuffer();


		id.append(sequence.getTableChar())
		.append(mid)
		.append(tableDate)
		.append(tableNo)
		.append(checkCode);

		return id.toString();
	}

	@Transactional
	public synchronized String getId(EnumSequenceTableName enumSequenceTableName) {
		return this.getId(enumSequenceTableName, "");
	}

	@Transactional
	public synchronized String getId(EnumSequenceTableName enumSequenceTableName, String mid) {
		String id = null;
		id = this.getIDForTable(enumSequenceTableName, mid);

		return id;
	}

	public String getSerialNumber(EnumSequenceTableName enumSequenceTableName) throws Exception {
		
		int limit = 15;
		Sequence sequence = this.getSequence(enumSequenceTableName);
		
		StringBuffer id = new StringBuffer();
		id.append(sequence.getTableChar())
			.append(sequence.getTableDate());
		String num = this.getZeroSeq(String.valueOf(sequence.getTableNo()), (limit - id.length()));
		id.append(num);
		
		return id.toString();
	}

	/**
	 * 可輸入欄位總長度，補滿需要的位數
	 * ex:totalLength=20 結果:PCUL2018080800000001
	 * @param enumSequenceTableName
	 * @param mid 中間要輸入甚麼參數
	 * @param totalLength 總長度
	 * @return 
	 * @throws Exception
	 */
	@Override
	synchronized public String getId(EnumSequenceTableName enumSequenceTableName, String mid, int totalLength) throws Exception {
		Sequence sequence = getSequence(enumSequenceTableName);
		String tableDate = sequence.getTableDate();
		int no = sequence.getTableNo();
		
		// 輸入總欄位數 - 序號前面的代碼長度 - 底線等等的長度 - 日期長度 - DB內目前序號長度
		totalLength = totalLength - sequence.getTableChar().length() - mid.length() - tableDate.length() - String.valueOf(no).length();

		StringBuffer tableNo = new StringBuffer();
		for (int i = 0; i < totalLength; i++) {
			tableNo.append("0");
		}
		tableNo.append(no);

		StringBuffer id = new StringBuffer();
		id.append(sequence.getTableChar())
		  .append(mid)
		  .append(tableDate)
		  .append(tableNo);
		
		return id.toString();
	}
	
	public static void main(String args[]) {
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		Logger log = LogManager.getRootLogger();

		SequenceService sequenceService = (SequenceService) context.getBean("SequenceService");
		for (int i = 0; i < 5; i++) {
			// Product po=productDAO.get("P0010001");
			// log.info("po="+po.getProdName());

			log.info(sequenceService.getId(EnumSequenceTableName.ADM_TEMPLATE_AD));
			log.info(sequenceService.getId(EnumSequenceTableName.ADM_TEMPLATE_PRODUCT));
			log.info(sequenceService.getId(EnumSequenceTableName.PFBX_APPLY_ORDER));
			// log.info(sequenceService.getId(EnumSequenceTableName.ORDER_DETAIL));
			// log.info(sequenceService.getId(EnumSequenceTableName.INVOICE));
			// log.info(sequenceService.getId(EnumSequenceTableName.RECOGNIZE));
			// log.info(sequenceService.getId(EnumSequenceTableName.REFUND));
			// log.info(sequenceService.getId(EnumSequenceTableName.ATM,
			// String.valueOf(RandomUtils.nextInt(9999))));
			// log.info(sequenceService.getId(EnumSequenceTableName.ESANATM,
			// String.valueOf(RandomUtils.nextInt(9999))));
			// log.info(sequenceService.getId(EnumSequenceTableName.INVOICENO));

			// log.info(sequenceService.getNo(EnumSequenceTableName.ORDER));

			// log.info(sequenceService.getInvoiceCheckCode("PW52639707"));
			// log.info(sequenceService.getEatmShaCode("8314030508415205","1000"));
		}
	}

}