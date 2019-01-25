package com.pchome.akbadm.db.service.customerInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.customerInfo.IPfdCustomerInfoDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfdVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.manager.PfdAccountVO;

public class PfdCustomerInfoService extends BaseService <PfdCustomerInfo, String> implements IPfdCustomerInfoService{

	private IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO;
	
	public List<PfdCustomerInfo> findPfdValidCustomerInfo() {
		
		return ((IPfdCustomerInfoDAO)dao).findPfdValidCustomerInfo();
	}
	
	public PfdCustomerInfo findPfdCustomerInfo(String pfdCustomerInfoId) {
		List<PfdCustomerInfo> list = ((IPfdCustomerInfoDAO)dao).findPfdCustomerInfo(pfdCustomerInfoId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Transactional
	public List<PfdAccountVO> findManagerPfdAccount(String memberId, Date startDate, Date endDate) {
		
		List<PfdAccountVO> vos = null;
		
		List<Object> list = ((IPfdCustomerInfoDAO)dao).findManagerPfdAccount(memberId, startDate, endDate);
		
		//log.info(" list: "+list);
		
		if(!list.isEmpty()){
			
			vos = new ArrayList<PfdAccountVO>();
			
			for(Object object:list){

				Object[] ob = (Object[])object;

				if(ob[0] != null){
			
					PfdAccountVO vo = new PfdAccountVO();				
					String status = ob[2].toString();
					
					vo.setPfdCustomerInfoId(ob[0].toString());
					vo.setPfdCustomerInfoName(ob[1].toString());
					
					for(EnumPfdAccountStatus pfdStatus:EnumPfdAccountStatus.values()){						
						if(pfdStatus.getStatus().equals(status)){
							vo.setPfdCustomerInfoStatus(pfdStatus.getChName());
							break;
						}
					}
					
					vo.setOneWeekAdCost(Float.parseFloat(ob[3].toString()));
					vo.setAdvanceTotalRemain(0);
					vo.setLaterTotalRemain(0);
					vo.setDevelopAmount(Integer.parseInt(ob[4].toString()));					
					vo.setLaterRemainQuota(Float.parseFloat(ob[5].toString()));
					
					//vo.setAdvanceTotalRemain(Float.parseFloat(ob[4].toString()));
					//vo.setLaterTotalRemain(Float.parseFloat(ob[5].toString()));
					//log.info(Float.parseFloat(ob[3].toString()));
					//log.info(Float.parseFloat(ob[4].toString()));
					//log.info(Float.parseFloat(ob[5].toString()));
					//log.info(Integer.parseInt(ob[6].toString()));
					
					vos.add(vo);
				}				
			}
			
		}
		
		// 取所有PFD 管理 PFP 資料, 用比對方式計算餘額
		List<PfdUserAdAccountRef> refs = pfdUserAdAccountRefDAO.findPfdUserAdAccountRefs();
		
		//log.info(">>> refs: "+refs);
		float advanceRemain = 0;
		float laterRemain = 0;
		
		if(!refs.isEmpty() && vos != null){
			
			for(PfdUserAdAccountRef ref:refs){
				
				//log.info(">>> remain: "+ref.getPfpCustomerInfo().getRemain());
				
				for(PfdAccountVO vo:vos){
					
					if(ref.getPfdCustomerInfo().getCustomerInfoId().equals(vo.getPfdCustomerInfoId())){
						
						
						if(ref.getPfpPayType().equals(EnumPfdAccountPayType.ADVANCE.getPayType())){
							advanceRemain = vo.getAdvanceTotalRemain();							
							advanceRemain += ref.getPfpCustomerInfo().getRemain();
							vo.setAdvanceTotalRemain(advanceRemain);
						}else if(ref.getPfpPayType().equals(EnumPfdAccountPayType.LATER.getPayType())){
							laterRemain = vo.getLaterTotalRemain();
							laterRemain += ref.getPfpCustomerInfo().getRemain();
							vo.setLaterTotalRemain(laterRemain);
						}
						break;
					}
				}
			}
		}
		
		return vos;
	}
	
	public List<ManagerPfdVO> findManagerAccount(ManagerVO managerVO) {
		
		List<PfdCustomerInfo> list = ((IPfdCustomerInfoDAO)dao).findPfdValidCustomerInfo();
		
		List<ManagerPfdVO> vos = null;
		
		if(!list.isEmpty()){
			
			vos = new ArrayList<ManagerPfdVO>();
			
			for(PfdCustomerInfo customerInfo:list){
				ManagerPfdVO vo = new ManagerPfdVO();
				
				vo.setCustomerInfoId(customerInfo.getCustomerInfoId());
				vo.setCustomerInfoName(customerInfo.getCompanyName());
				
				if(managerVO.getCustomerInfoIds() != null){
					
					for(String id:managerVO.getCustomerInfoIds()){
						if(id.equals(vo.getCustomerInfoId())){
							vo.setIsChecked("true");
							break;
						}
					}
				}
				
				vos.add(vo);
			}
		}
		
		return vos;
	}


	public void setPfdUserAdAccountRefDAO(
			IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO) {
		this.pfdUserAdAccountRefDAO = pfdUserAdAccountRefDAO;
	}

}
