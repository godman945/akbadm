package com.pchome.akbadm.db.service.order;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.order.IPfpOrderDAO;
import com.pchome.akbadm.db.dao.order.PfpOrderDAO;
import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.pojo.PfpUserMemberRef;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.OrderReportVO;
import com.pchome.config.TestConfig;
import com.pchome.soft.util.DateValueUtil;



public class PfpOrderService extends BaseService<PfpOrder,String> implements IPfpOrderService{

	public List<PfpOrder> findPfpOrder(String orderId, String account, String sDate, String eDate, String status) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date startDate = sdf.parse(sDate + " 00:00:00");
		
		Date endDate = sdf.parse(eDate + " 23:59:59");
		
		return ((IPfpOrderDAO) dao).findPfpOrder(orderId, account, startDate, endDate, status);
	}
	
	// @Transactional
	public List<OrderReportVO> findPfpOrderByDate(String startDate, String endDate) throws Exception{
		
		List<OrderReportVO> orderReportVOs = null;
		List<PfpOrder> orders = ((PfpOrderDAO) dao).findPfpOrderByDate(startDate, endDate);
		
		if(orders != null && orders.size() > 0){
			orderReportVOs = new ArrayList<OrderReportVO>();
			
			for(PfpOrder order:orders){
				OrderReportVO vo = new OrderReportVO();
				vo.setOrderDate(DateValueUtil.getInstance().dateToString(order.getUpdateDate()));
				
				for(PfpUserMemberRef ref:order.getPfpUser().getPfpUserMemberRefs()){
					vo.setMemberId(ref.getId().getMemberId());
				}
				
				vo.setCustomerInfoName(order.getPfpCustomerInfo().getCustomerInfoTitle());
				vo.setAddMoney((int)order.getOrderPrice());
				
				orderReportVOs.add(vo);
			}
		}
		
		return orderReportVOs;
	}
	
	public List<PfpOrder> successOrder(String customerInfoId, Date date) {
		// 時間設定今天03:30:00 ~ 隔天 03:29:59 依排程時間設定
		Calendar cal = Calendar.getInstance();		
		
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 3, 30, 0);
		Date startDate = cal.getTime();
		
		cal.add(Calendar.DATE, 1);		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 3, 29, 59);
		Date endDate = cal.getTime();
		
		//SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//log.info(">>> startDate: "+sf.format(startDate));
		//log.info(">>> endDate: "+sf.format(endDate));
		
		return ((PfpOrderDAO) dao).successOrder(customerInfoId, startDate, endDate);
	}
	
	public List<PfpOrder> successOrder(String customerInfoId, Date date, String OrderBy) {
		return ((PfpOrderDAO) dao).successOrder(customerInfoId, date, OrderBy);
	}
	
	public PfpOrder findSuccessOrder(String orderId){
		List<PfpOrder> orders = ((PfpOrderDAO) dao).findSuccessOrder(orderId);
		if(orders.isEmpty()){
			return null;
		}else{
			return orders.get(0);
		}
	}
	
	public List<PfpOrder> findRefundOrder(String customerInfoId, Date date) {
		return ((IPfpOrderDAO) dao).findRefundOrder(customerInfoId, date);
	}
	
	public List<PfpOrder> findPfpOrder(String pfpCustomerInfoId) {
		return ((IPfpOrderDAO) dao).findPfpOrder(pfpCustomerInfoId);
	}
	
	public Integer deletePfpOrder(String orderId){
		return ((IPfpOrderDAO) dao).deletePfpOrder(orderId);
	}
	
	public PfpOrder findOrderInfo(String orderId) throws Exception {
		
		List<PfpOrder> orders = ((IPfpOrderDAO)dao).findOrder(orderId);
		
		if(orders.isEmpty()){
			return null;
		}else{
			return orders.get(0);
		}
	}
	
	public static void main(String args[]) throws Exception{

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		Logger log = LogManager.getRootLogger();

		PfpOrderService service = (PfpOrderService) context.getBean("PfpOrderService");
		
		String date = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		
	
		//AccountVO vo = service .findRegisterDataById("reantoilpc");
		//log.info("\n   size  =  "+service.checkOrderDetail("AC201303060000000023", date).size());
	}
	

}
