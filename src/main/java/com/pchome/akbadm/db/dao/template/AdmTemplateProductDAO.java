package com.pchome.akbadm.db.dao.template;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;

public class AdmTemplateProductDAO extends BaseDAO<AdmTemplateProduct, String> implements IAdmTemplateProductDAO {

    
    	//依據寬高過濾樣版
    	@SuppressWarnings("unchecked")
	public List<AdmTemplateProduct> getTemplateProductBySize(Map<String,String> condition) throws Exception{
	    StringBuffer sql = new StringBuffer();
	    sql.append("  from AdmTemplateProduct a where a.templateProductWidth='"+condition.get("width")+"' and a.templateProductHeight = '"+condition.get("height")+"'");
	    return super.getHibernateTemplate().find(sql.toString());
	}
    
    
	@SuppressWarnings("unchecked")
	public List<AdmTemplateProduct> getTemplateProductByCondition(String templateProductSeq, String templateProductName, String templateProductType, String templateProductWidth, String templateProductHeight) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmTemplateProduct where 1=1");
		if (StringUtils.isNotEmpty(templateProductSeq)) {
			sql.append(" and templateProductSeq like '%" + templateProductSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(templateProductName)) {
			sql.append(" and templateProductName like '%" + templateProductName.trim() + "%'");
		}
		
		if (StringUtils.isNotEmpty(templateProductType)) {
			sql.append(" and templateProductXtype = '" + templateProductType.trim() + "'");
		}
		
		if (StringUtils.isNotEmpty(templateProductWidth)) {
			sql.append(" and templateProductWidth = '" + templateProductWidth.trim() + "'");
		}
		
		if (StringUtils.isNotEmpty(templateProductHeight)) {
			sql.append(" and templateProductHeight = '" + templateProductHeight.trim() + "'");
		}
		
		return super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmTemplateProduct getTemplateProductById(String templateProductId) throws Exception {
		List<AdmTemplateProduct> list = super.getHibernateTemplate().find("from AdmTemplateProduct where templateProductId = '" + templateProductId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmTemplateProduct getTemplateProductBySeq(String templateProductSeq) throws Exception {
		List<AdmTemplateProduct> list = super.getHibernateTemplate().find("from AdmTemplateProduct where templateProductSeq = '" + templateProductSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void insertTemplateProduct(AdmTemplateProduct templateproduct) throws Exception {
		this.save(templateproduct);
	}

	public void updateTemplateProduct(AdmTemplateProduct templateproduct) throws Exception {
		this.update(templateproduct);
	}

	public void deleteTemplateProduct(String templateProductSeq) throws Exception {
		String sql = "delete from AdmTemplateProduct where templateProductSeq = '" + templateProductSeq + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public String saveTemplateProduct(AdmTemplateProduct templateproduct) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(templateproduct);
		return templateproduct.getTemplateProductSeq();
	}
}
