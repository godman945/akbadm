package com.pchome.akbadm.db.dao.advideo;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoSource;

public class PfpAdVideoSourceDAO extends BaseDAO<PfpAdVideoSource, String> implements IPfpAdVideoSourceDAO {

	@SuppressWarnings("unchecked")
	public PfpAdVideoSource getVideoUrl(String videoUrl) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAdVideoSource ");
		hql.append(" where adVideoUrl = ? ");
		Object[] conditionArray = new Object[] { videoUrl };
		List<PfpAdVideoSource> list = (List<PfpAdVideoSource>) super.getHibernateTemplate().find(hql.toString(), conditionArray);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
