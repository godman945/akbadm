package com.pchome.akbadm.db.service.pfbx.play;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.play.IPfbxPermissionDAO;
import com.pchome.akbadm.db.pojo.PfbxPermission;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxPermissionService extends BaseService<PfbxPermission,String> implements IPfbxPermissionService {
    //新增拒絕理由
    public void savePfbxPermissionList(List<PfbxPermission> PfbxPermissionList) throws Exception{
	 ((IPfbxPermissionDAO)dao).savePfbxPermissionList(PfbxPermissionList);
    }
    //查詢網址
    public List<PfbxPermission> findPfbxPermissionListByUrl(String pfbxApplyUrl) throws Exception{
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	List<Object> permissionObjList = ((IPfbxPermissionDAO)dao).findPfbxPermissionListByUrl(pfbxApplyUrl);
	
	System.out.println("AAAAAAAAAAAAA==="+permissionObjList.size());
	
	List<PfbxPermission> pfbxPermissionList = new ArrayList<PfbxPermission>();
	for (Object object : permissionObjList) {
	    Object[] obj = (Object[]) object;
	    PfbxPermission pfbxPermission = new PfbxPermission();
	    pfbxPermission.setId(Integer.parseInt(obj[0].toString()));
	    pfbxPermission.setType(Integer.parseInt(obj[1].toString()));
	    pfbxPermission.setPfbxApplyId(obj[2].toString());
	    pfbxPermission.setContent(obj[3].toString());
	    pfbxPermission.setStatus(Integer.parseInt(obj[4].toString()));
	    if(obj[6] == null){
		pfbxPermission.setRemark("");
	    }else{
		pfbxPermission.setRemark(obj[6].toString());
	    }
////	    format.parse(obj[7].toString())
////	    Date date = format.parse(obj[7].toString());
//	    pfbxPermission.setCreatDate(new Date());
	    pfbxPermissionList.add(pfbxPermission);
	}
	return pfbxPermissionList;
    }
}
