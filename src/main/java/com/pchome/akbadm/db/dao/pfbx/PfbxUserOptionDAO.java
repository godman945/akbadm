package com.pchome.akbadm.db.dao.pfbx;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserOption;

public class PfbxUserOptionDAO extends BaseDAO<PfbxUserOption, String> implements IPfbxUserOptionDAO {
	
	@SuppressWarnings("unchecked")
    public List<PfbxUserOption> getSYSBypfbId(String pfbId)
    {
        StringBuffer hql = new StringBuffer();
        List<Object> con = new ArrayList<Object>();
        
        hql.append("from PfbxUserOption where 1=1 ");
        hql.append("and pfbxCustomerInfo.customerInfoId = ? ");
        hql.append("and optionName = ? ");
        
        con.add(pfbId);
        con.add("SYS");
        
        return (List<PfbxUserOption>) this.getHibernateTemplate().find(hql.toString(), con.toArray());
    }
	
    @SuppressWarnings("unchecked")
    public List<PfbxUserOption> selectPfbxUserOptionByStatus(String status) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxUserOption ");
        hql.append("where pfbxCustomerInfo.status = ? ");

        return (List<PfbxUserOption>) this.getHibernateTemplate().find(hql.toString(), status);
    }
}
