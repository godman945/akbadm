package com.pchome.akbadm.struts2.action.recognize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.vo.recognize.RecognizeDetailVo;
import com.pchome.akbadm.db.vo.recognize.RecognizeVo;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.recognize.EnumOrderType;

public class RecognizeReportAction extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IAdmRecognizeDetailService admRecognizeDetailService;
	private IPfdUserAdAccountRefService PfdUserAdAccountRefService;
	private IPfpRefundOrderService pfpRefundOrderService;

	//輸入參數
	private String date;
	
	private List<AdmRecognizeDetail> details;
	
	private int totalAmount = 0; 		//資料總比數
	private float totalRemain = 0; 		//總金額
	private float totalSave = 0; 		//總花費
	private float totalSpend = 0; 		//總剩餘
	private float totalTaxRemain = 0; 	//總金額
	private float totalTaxSave = 0; 	//總花費
	private float totalTaxSpend = 0; 	//總剩餘
	private float totalRefundPrice = 0; //總退款金額(未稅)
	private float totalRefundPriceTax = 0; //總退款金額(含稅)
	
	private List<RecognizeVo> vos;
	private List<RecognizeDetailVo> detailVos;
	
	public String execute(){
		try {
			log.info(">>> date = " + date);		
			date = date.replace("/", "-");
			
			List<AdmRecognizeDetail> recognizeDetails = admRecognizeDetailService.findAdmRecognizeDetails(date);
			
			vos = new ArrayList<RecognizeVo>();
			
			for(EnumOrderType orderType:EnumOrderType.values()){
				RecognizeVo vo = new RecognizeVo();
				
				vo.setOrderType(orderType.getTypeTag());
				vo.setTypeChName(orderType.getTypeName());
				vo.setAmount(0);
				vo.setTotalRemain(0);
				vo.setTotalSave(0);
				vo.setTotalSpend(0);
				vo.setTotalTaxRemain(0);
				vo.setTotalTaxSave(0);
				vo.setTotalTaxSpend(0);
				vo.setTotalRefundPrice(0);
				vo.setTotalRefundPriceTax(0);
							
				vos.add(vo);			
			}
			
			if(!recognizeDetails.isEmpty()){
				
				detailVos = new ArrayList<RecognizeDetailVo>();
				
				//取得當日全部預付退款資料
				Map<String,Object> advanceRefundMaps = findAllAdvanceRefundOrder(date);
				
				for(AdmRecognizeDetail detail:recognizeDetails){
					// 明細
					RecognizeDetailVo detailVo = new RecognizeDetailVo();
					
					detailVo.setOrderId(detail.getAdmRecognizeRecord().getRecognizeOrderId());
					detailVo.setOrderPrice(detail.getAdmRecognizeRecord().getOrderPrice());
					detailVo.setOrderTaxPrice(detail.getAdmRecognizeRecord().getOrderPrice() +  detail.getAdmRecognizeRecord().getTax());
					detailVo.setPfpId(detail.getCustomerInfoId());
					
					//預付退款(未稅)
					if (advanceRefundMaps != null){
						float refundPrice = 0;
						if (advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPrice") != null){
							refundPrice = (float)advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPrice");
							detailVo.setRefund(refundPrice);
						}else{
							detailVo.setRefund(0);
						}
					}else{
						detailVo.setRefund(0);
					}
					
					//總花費(未稅) = 當日花費(costPrice)減當日退款(refundPrice)
					float costPrice = 0;
					float refundPrice = 0;
					if (advanceRefundMaps != null){
						if (advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPrice") != null){
							refundPrice = (float)advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPrice");
							costPrice = detail.getCostPrice() - refundPrice;
							detailVo.setSpendCost(costPrice);
						}else{
							costPrice = detail.getCostPrice();
							detailVo.setSpendCost(costPrice);
						}
					}else{
						costPrice = detail.getCostPrice();
						detailVo.setSpendCost(costPrice);
					}
					
					
					//總花費(含稅) = 當日花費(costPrice)減當日退款(refundPriceTax)
					float costPriceTax = 0;
					float refundPriceTax = 0;
					if (advanceRefundMaps != null){
						if (advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPriceTax") != null){
							refundPriceTax = (float)advanceRefundMaps.get( detail.getCustomerInfoId() +"_"+ detail.getAdmRecognizeRecord().getRecognizeOrderId() +"_refundPriceTax");
							costPriceTax = (detail.getCostPrice() +  detail.getTax()) - refundPriceTax;
							detailVo.setSpendTaxCost(costPriceTax);
						}else{
							costPriceTax = detail.getCostPrice() +  detail.getTax();
							detailVo.setSpendTaxCost(costPriceTax);
						}
					}else{
						costPriceTax = detail.getCostPrice() +  detail.getTax();
						detailVo.setSpendTaxCost(costPriceTax);
					}
					
					
					detailVo.setRemain(detail.getRecordRemain());
					detailVo.setSpendDate(date);
					detailVo.setTaxRemain(detail.getRecordRemain() + detail.getTaxRemain());
					
					for(EnumOrderType orderType:EnumOrderType.values()){
						if(orderType.getTypeTag().equals(detail.getOrderType())){
							detailVo.setTypChName(orderType.getTypeName());
							break;
						}
					}
					
					detailVos.add(detailVo);
					
					// 加總
					totalAmount++;
					totalRemain += detail.getRecordRemain();
					totalSave += detail.getAdmRecognizeRecord().getOrderPrice();
					totalSpend += costPrice;
					totalTaxRemain += detail.getRecordRemain() + detail.getTaxRemain();
					totalTaxSave += detail.getAdmRecognizeRecord().getOrderPrice() +  detail.getAdmRecognizeRecord().getTax();
					totalTaxSpend += costPriceTax;	
					totalRefundPrice += refundPrice;
					totalRefundPriceTax += refundPriceTax;	
					
					// 分類
					for(RecognizeVo vo:vos){
						
						if(vo.getOrderType().equals(detail.getOrderType())){
							int amount = vo.getAmount() + 1;
							float totalRemain = vo.getTotalRemain() + detail.getRecordRemain();
							float totalSave = vo.getTotalSave() + detail.getAdmRecognizeRecord().getOrderPrice();
							float totalSpend = vo.getTotalSpend() + costPrice;
							float totalTaxRemain = vo.getTotalTaxRemain() + detail.getRecordRemain() + detail.getTaxRemain();
							float totalTaxSave = vo.getTotalTaxSave() + detail.getAdmRecognizeRecord().getOrderPrice() +  detail.getAdmRecognizeRecord().getTax();
							float totalTaxSpend = vo.getTotalTaxSpend() + costPriceTax;
							float totalRefundPrice =  vo.getTotalRefundPrice() + refundPrice;
							float totalRefundPriceTax =  vo.getTotalRefundPriceTax() + refundPriceTax;
							
							vo.setAmount(amount);
							vo.setTotalRemain(totalRemain);
							vo.setTotalSave(totalSave);
							vo.setTotalSpend(totalSpend);
							vo.setTotalTaxRemain(totalTaxRemain);
							vo.setTotalTaxSave(totalTaxSave);
							vo.setTotalTaxSpend(totalTaxSpend);
							vo.setTotalRefundPrice(totalRefundPrice);
							vo.setTotalRefundPriceTax(totalRefundPriceTax);
							
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
	}
	
	public Map<String, Object> findAllAdvanceRefundOrder(String date) throws Exception{
		List<Object> advanceRefundOrder = pfpRefundOrderService.findPfpRefundPrice(date);
		Map<String,Object> avanceRefundMap = null;	//預付退款金額(含稅與不含稅)
		
		if(!advanceRefundOrder.isEmpty()){
			avanceRefundMap = new HashMap<String,Object>();
			
			for (int i=0; i< advanceRefundOrder.size(); i++) {
				Object[] objArray = (Object[]) advanceRefundOrder.get(i);
				avanceRefundMap.put((String)objArray[0]+"_"+(String)objArray[1]+"_refundPrice", (float)objArray[2]);
				avanceRefundMap.put((String)objArray[0]+"_"+(String)objArray[1]+"_refundPriceTax", (float)objArray[3]);
			}
		}
		return avanceRefundMap;
	}
	
	
	public void setAdmRecognizeDetailService(IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		PfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}
	
	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<AdmRecognizeDetail> getDetails() {
		return details;
	}

	public String getDate() {
		return date;
	}

	public void setDetails(List<AdmRecognizeDetail> details) {
		this.details = details;
	}

	public List<RecognizeVo> getVos() {
		return vos;
	}

	public List<RecognizeDetailVo> getDetailVos() {
		return detailVos;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public float getTotalRemain() {
		return totalRemain;
	}

	public float getTotalSave() {
		return totalSave;
	}

	public float getTotalSpend() {
		return totalSpend;
	}

	public float getTotalTaxRemain() {
		return totalTaxRemain;
	}

	public float getTotalTaxSave() {
		return totalTaxSave;
	}

	public float getTotalTaxSpend() {
		return totalTaxSpend;
	}

	public float getTotalRefundPrice() {
		return totalRefundPrice;
	}

	public float getTotalRefundPriceTax() {
		return totalRefundPriceTax;
	}
}
