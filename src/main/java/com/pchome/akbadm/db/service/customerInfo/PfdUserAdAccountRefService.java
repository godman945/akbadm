package com.pchome.akbadm.db.service.customerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.customerInfo.IPfdCustomerInfoDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfpVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.soft.util.DateValueUtil;

public class PfdUserAdAccountRefService extends BaseService<PfdUserAdAccountRef, String> implements IPfdUserAdAccountRefService{

	private IPfdCustomerInfoDAO pfdCustomerInfoDAO;

	@Override
    public int findPfpCustomerInfoAmount(String pfdCustomerInfoId, String startDate, String endDate) {

		List<PfdUserAdAccountRef> list = ((IPfdUserAdAccountRefDAO)dao).findPfpCustomerInfo(pfdCustomerInfoId,
																								DateValueUtil.getInstance().stringToDate(startDate),
																								DateValueUtil.getInstance().stringToDate(endDate));
																								//DateValueUtil.getInstance().getDateForStartDateAddDay(endDate, 1));

		if(list.isEmpty()){
			return 0;
		}else{
			return list.size();
		}
	}

	@Override
    public PfdUserAdAccountRef findPfdUserAdAccountRef(String pfdCustomerInfoId) {

		List<PfdUserAdAccountRef> list = ((IPfdUserAdAccountRefDAO)dao).findPfdUserAdAccountRef(pfdCustomerInfoId);

		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Override
    public List<PfdUserAdAccountRef> findPfdUserAdAccountRefs(String pfdCustomerInfoId) {
		return ((IPfdUserAdAccountRefDAO)dao).findPfdUserAdAccountRef(pfdCustomerInfoId);
	}

	@Override
    public List<PfdUserAdAccountRef> findPfdUserIdByPfpCustomerInfoId(String pfpCustomerInfoId)	throws Exception {
		return ((IPfdUserAdAccountRefDAO)dao).findPfdUserIdByPfpCustomerInfoId(pfpCustomerInfoId);
	}

	@Override
    public Integer deletePfdUserAdAccountRef(String pfpCustomerInfoId){
		return ((IPfdUserAdAccountRefDAO)dao).deletePfdUserAdAccountRef(pfpCustomerInfoId);
	}

	@Override
    public List<ManagerPfpVO> findManagerAccount(ManagerVO managerVO) {

		List<ManagerPfpVO> vos = null;

		List<PfdCustomerInfo> pfds = pfdCustomerInfoDAO.findPfdValidCustomerInfo();

		if(!pfds.isEmpty()){

			vos = new ArrayList<ManagerPfpVO>();

			for(PfdCustomerInfo pfd:pfds){

				ManagerPfpVO vo = new ManagerPfpVO();
				List<Map<String,String>> pfpList = new ArrayList<Map<String,String>>();

				vo.setPfdCustomerInfoId(pfd.getCustomerInfoId());
				vo.setPfdCustomerInfoName(pfd.getCompanyName());
				vo.setList(pfpList);

				vos.add(vo);
			}
		}

		List<PfdUserAdAccountRef> refs = ((IPfdUserAdAccountRefDAO)dao).findPfdUserAdAccountRefs();


		if(!refs.isEmpty()){

			for(PfdUserAdAccountRef ref:refs){

				for(ManagerPfpVO vo:vos){

					if(vo.getPfdCustomerInfoId().equals(ref.getPfdCustomerInfo().getCustomerInfoId())){

						Map<String,String> map = new HashMap<String,String>();
						map.put("pfpCustomerInfoId", ref.getPfpCustomerInfo().getCustomerInfoId());
						map.put("pfpCustomerInfoName", ref.getPfpCustomerInfo().getCustomerInfoTitle());

						if(managerVO.getCustomerInfoIds() != null){
							for(String id:managerVO.getCustomerInfoIds()){
								if(id.equals(ref.getPfpCustomerInfo().getCustomerInfoId())){
									map.put("isChecked", "true");
									break;
								}
							}
						}

						vo.getList().add(map);

						break;
					}

				}
			}
		}

		return vos;
	}

	@Override
    public PfdUserAdAccountRef findPfdUserAdAccountRefByPfpId(String pfpCustomerInfoId) {

		List<PfdUserAdAccountRef> list = ((IPfdUserAdAccountRefDAO)dao).findPfdUserAdAccountRefByPfpId(pfpCustomerInfoId);

		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Override
    public List<PfpCustomerInfo> findPfdUserAdAccountRefByPfdId(List<String> customerInfoIdList){
		return ((IPfdUserAdAccountRefDAO)dao).findPfdUserAdAccountRefByPfdId(customerInfoIdList);
	}

	@Override
    public Map<String, PfdUserAdAccountRef> selectPfdUserAdAccountRefMaps() {
        Map<String, PfdUserAdAccountRef> map = new HashMap<String, PfdUserAdAccountRef>();

        List<PfdUserAdAccountRef> pfdRefList = ((IPfdUserAdAccountRefDAO)dao).loadAll();
        PfdUserAdAccountRef ref = null;
        for (int i = 0; i < pfdRefList.size(); i++) {
            ref = pfdRefList.get(i);
            map.put(ref.getPfpCustomerInfo().getCustomerInfoId(), ref);
        }

        return map;
	}

    public void setPfdCustomerInfoDAO(IPfdCustomerInfoDAO pfdCustomerInfoDAO) {
        this.pfdCustomerInfoDAO = pfdCustomerInfoDAO;
    }
}
