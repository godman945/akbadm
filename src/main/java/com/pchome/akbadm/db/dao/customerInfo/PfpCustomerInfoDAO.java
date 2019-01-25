package com.pchome.akbadm.db.dao.customerInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfpCustomerInfoDAO extends BaseDAO<PfpCustomerInfo,String> implements IPfpCustomerInfoDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findPfpCustomerInfo(final String keyword, final String customerInfoName, final int page, final int pageSize) throws Exception{

		List<Object> result = getHibernateTemplate().execute(

                new HibernateCallback<List<Object> >() {

					@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();
						hql.append(" select c.customerInfoId, c.customerInfoTitle, c.category, ");
                    	hql.append(" 		c.industry, c.status, c.createDate, c.activateDate, c.remain ");
                    	hql.append(" from PfpCustomerInfo as c ");
                    	hql.append(" where c.status != null ");

                    	if(StringUtils.isNotEmpty(keyword)){
                    		hql.append(" and ( c.customerInfoId like '%"+keyword+"%' ");
                    		hql.append(" or c.customerInfoTitle like '%"+keyword+"%') ");
                    	}

                    	hql.append(" order by c.customerInfoId  ");

						//log.info(" hql  = "+hql.toString());

						Query q;

						// page=-1  取得全部不分頁用於download

						if(page==-1){
							q = session.createQuery(hql.toString());
						}else{
							q = session.createQuery(hql.toString())
							.setFirstResult((page-1)*pageSize)
							.setMaxResults(pageSize);
						}

                        return q.list();
                    }
                }
        );

        return result;
	}

	@Override
    @SuppressWarnings("unchecked")
	public PfpCustomerInfo getCustomerInfo(String customerInfoId){

		StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo ");
        hql.append(" where customerInfoId = ? ");
        hql.append(" and status != '"+EnumAccountStatus.DELETE.getStatus()+"' ");

		List<PfpCustomerInfo> list = super.getHibernateTemplate().find(hql.toString(), customerInfoId);
		if(list.size()>0 && list != null){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpCustomerInfo> findPfpCustomerInfosByDate(String startDate, String endDate,
			String pfpCustInfoId, String status) throws Exception{

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpCustomerInfo where 1=1");
		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and activateDate >= '" + startDate + " 00:00:00'");
		}
		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and activateDate <= '" + endDate + " 23:59:59'");
		}
		if (StringUtils.isNotEmpty(pfpCustInfoId)) {
			hql.append(" and customerInfoId in (" + pfpCustInfoId + ")");
		}
		if (StringUtils.isNotEmpty(status)) {
			hql.append(" and status in (" + status + ")");
		}
		hql.append(" order by activateDate desc");

		log.info(">>> hql = " + hql.toString());

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> findPfpCustomerInfosByDate(String startDate, String endDate, List<String> pfpCustomerInfoIds, List<String> status) throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo where 1=1");
		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and activateDate >= :startDate");
			sqlParams.put("startDate", sdf.parse(startDate));
		}
		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and activateDate <= ?");
			sqlParams.put("endDate", sdf.parse(endDate));
		}
		if (pfpCustomerInfoIds != null  && pfpCustomerInfoIds.size() > 0) {
			hql.append(" and customerInfoId in (:pfpCustomerInfoIds)");
			sqlParams.put("pfpCustomerInfoIds", pfpCustomerInfoIds);
		}
		if (status != null && status.size() > 0) {
			hql.append(" and status in (:status)");
			sqlParams.put("status", status);
		}
		hql.append(" order by activateDate desc");

		Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {
    		query.setParameter(paramName, sqlParams.get(paramName));
        }

		List<PfpCustomerInfo> pfpCustomerInfos = query.list();


        return pfpCustomerInfos;
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> getAllCustomerInfo() throws Exception {

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo ");
        hql.append(" where status != '"+EnumAccountStatus.DELETE.getStatus()+"' ");

        return super.getHibernateTemplate().find(hql.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getCustomerInfos(final String keyword, final String userAccount, final String userEmail, final String customerInfoStatus, final int page, final int pageSize,final String pfdCustomerId) throws Exception{
    	List<Object> result = getHibernateTemplate().execute(

                new HibernateCallback<List<Object> >() {

					@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();
//						hql.append(" select c.customerInfoId, c.customerInfoTitle, c.category, ");
//                    	hql.append(" 		c.industry, c.status, c.createDate, c.activateDate, c.remain ");
//                    	hql.append(" from PfpCustomerInfo as c ");
//                    	hql.append(" where c.status != '"+EnumAccountStatus.DELETE.getStatus()+"' ");
//
//                    	if(StringUtils.isNotBlank(keyword)){
//                    		hql.append(" and ( c.customerInfoId like '%"+keyword+"%' ");
//                    		hql.append(" or c.customerInfoTitle like '%"+keyword+"%') ");
//                    	}
//
//                    	hql.append(" order by c.createDate  ");

						hql.append("select  u.pfpCustomerInfo.customerInfoId,");
						hql.append("		u.pfpCustomerInfo.memberId, u.pfpCustomerInfo.customerInfoTitle,");
						hql.append("		u.pfpCustomerInfo.category, u.pfpCustomerInfo.industry,");
						hql.append("		u.pfpCustomerInfo.status, u.pfpCustomerInfo.createDate,");
						hql.append("		u.pfpCustomerInfo.activateDate, u.pfpCustomerInfo.remain");
						hql.append(" from	PfpUser as u,PfdUserAdAccountRef as b");
                    	hql.append(" where 1=1");
                    	hql.append(" and u.pfpCustomerInfo.customerInfoId = b.pfpCustomerInfo.customerInfoId");
                    	
                    	if(StringUtils.isNotBlank(keyword)){
                    		hql.append(" and ( u.pfpCustomerInfo.customerInfoId like '%"+keyword+"%' ");
                    		hql.append(" or u.pfpCustomerInfo.customerInfoTitle like '%"+keyword+"%') ");
                    	}
                    	if(StringUtils.isNotBlank(userAccount)) {
                    		hql.append(" and ( u.userId like '%"+userAccount+"%' ");
                    		hql.append(" or u.pfpCustomerInfo.memberId like '%"+userAccount+"%') ");
                    	}
                    	if(StringUtils.isNotBlank(userEmail)) {
                    		hql.append(" and ( u.userEmail like '%"+userEmail+"%' ) ");
                    	}
                    	if(StringUtils.isNotBlank(customerInfoStatus)) {
                        	hql.append(" and u.pfpCustomerInfo.status = '"+customerInfoStatus+"' ");
                    	}
                    	if(StringUtils.isNotBlank(pfdCustomerId)) {
                    		System.out.println(pfdCustomerId);
                    		hql.append(" and b.pfdCustomerInfo.customerInfoId = '"+pfdCustomerId+"' ");
                    	}

                    	hql.append(" group by u.pfpCustomerInfo.customerInfoId");
                    	hql.append(" order by u.pfpCustomerInfo.createDate desc  ");
                    	//System.out.println(" hql  = "+hql.toString());

						//log.info(" hql  = "+hql.toString());

						Query q;

						// page=-1  取得全部不分頁用於download
						if(page==-1){
							q = session.createQuery(hql.toString());
						}else{
							q = session.createQuery(hql.toString())
							.setFirstResult((page-1)*pageSize)
							.setMaxResults(pageSize);
						}

                        return q.list();
                    }
                }
        );

        return result;

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> getExistentCustomerInfo() {

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo ");
        hql.append(" where status != ? ");

        return super.getHibernateTemplate().find(hql.toString(), EnumAccountStatus.DELETE.getStatus());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> findValidCustomerInfo(){

         StringBuffer hql = new StringBuffer();
         hql.append(" from PfpCustomerInfo ");
         hql.append(" where status = ? ");
         hql.append(" or status = ? ");
         hql.append(" or status = ?  ");
         hql.append(" and activateDate != null ");

         Object[] ob = new Object[]{EnumAccountStatus.CLOSE.getStatus(),
                                    EnumAccountStatus.START.getStatus(),
                                    EnumAccountStatus.STOP.getStatus()};

         return super.getHibernateTemplate().find(hql.toString(), ob);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> findValidCustomerInfo(String customerInfoId){

    	StringBuffer hql = new StringBuffer();
    	hql.append(" from PfpCustomerInfo ");
    	hql.append(" where customerInfoId = ? ");
        hql.append(" and ( status = ? ");
        hql.append(" or status = ? ");
        hql.append(" or status = ? ) ");
        hql.append(" and activateDate != null ");


        Object[] ob = new Object[]{customerInfoId,
        							EnumAccountStatus.CLOSE.getStatus(),
					                EnumAccountStatus.START.getStatus(),
					                EnumAccountStatus.STOP.getStatus()};

        return super.getHibernateTemplate().find(hql.toString(), ob);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> selectValidCustomerInfo(float remain) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpCustomerInfo ");
        hql.append("where status = ? ");
        hql.append("    and later_remain >= ? ");

        return super.getHibernateTemplate().find(hql.toString(), EnumAccountStatus.START.getStatus(), remain);
    }

    @Override
    public List<Object[]> selectValidAdGroup(String pvclkDate, float remain) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("    c.customer_info_id, ");
        sql.append("    a.ad_action_seq, ");
        sql.append("    g.ad_group_seq, ");
        sql.append("    c.later_remain, ");
        sql.append("    a.ad_action_control_price, ");
        sql.append("    ifnull( ");
        sql.append("        (select sum(p.ad_clk_price) ");
        sql.append("        from pfp_ad_pvclk as p ");
        sql.append("        where p.ad_pvclk_date = :pvclk_date1 ");
        sql.append("            and c.customer_info_id = p.customer_info_id ");
        sql.append("        group by p.customer_info_id), 0) as customer_cost, ");
        sql.append("    ifnull( ");
        sql.append("        (select sum(p.ad_clk_price) ");
        sql.append("        from pfp_ad_pvclk as p ");
        sql.append("        where p.ad_pvclk_date = :pvclk_date2 ");
        sql.append("            and a.ad_action_seq = p.ad_action_seq ");
        sql.append("        group by p.ad_action_seq), 0) as action_cost ");
        sql.append("from ");
        sql.append("    pfp_customer_info as c, ");
        sql.append("    pfp_ad_action as a, ");
        sql.append("    pfp_ad_group as g ");
        sql.append("where c.status = :customer_status ");
        sql.append("    and c.later_remain >= :remain ");
        sql.append("    and a.ad_action_status = :action_status ");
        sql.append("    and a.ad_action_start_date <= date_format(now(), '%Y-%m-%d') ");
        sql.append("    and a.ad_action_end_date >= date_format(now(), '%Y-%m-%d') ");
        sql.append("    and g.ad_group_status = :group_status ");
        sql.append("    and a.customer_info_id = c.customer_info_id ");
        sql.append("    and g.ad_action_seq = a.ad_action_seq ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("pvclk_date1", pvclkDate);
        query.setParameter("pvclk_date2", pvclkDate);
        query.setParameter("remain", remain);
        query.setParameter("customer_status", EnumAccountStatus.START.getStatus());
        query.setParameter("action_status", EnumAdStatus.Open.getStatusId());
        query.setParameter("group_status", EnumAdStatus.Open.getStatusId());

        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> selectValidAdKeyword(String groupId, String pvclkDate, float remain) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("    a.ad_action_seq, ");
        sql.append("    g.ad_group_seq, ");
        sql.append("    ad.ad_seq, ");
        sql.append("    k.ad_keyword_seq, ");
        sql.append("    k.ad_keyword, ");
        sql.append("    a.ad_action_control_price, ");
        sql.append("    k.ad_keyword_search_price, ");
        sql.append("    k.ad_keyword_search_phrase_price, ");
        sql.append("    k.ad_keyword_search_precision_price, ");
        sql.append("    k.ad_keyword_channel_price, ");
        sql.append("    k.ad_keyword_open, ");
        sql.append("    k.ad_keyword_phrase_open, ");
        sql.append("    k.ad_keyword_precision_open, ");
        sql.append("    g.ad_group_search_price_type, ");
        sql.append("    ifnull( ");
        sql.append("        (select sum(ad_keyword_pv) ");
        sql.append("        from pfp_ad_keyword_report ");
        sql.append("        where ad_keyword_pvclk_date = :pvclk_date1 ");
        sql.append("            and ad_keyword_seq = k.ad_keyword_seq ");
        sql.append("        group by ad_keyword_seq),0) pv, ");
        sql.append("    ifnull( ");
        sql.append("        (select sum(ad_keyword_clk-ad_keyword_invalid_clk) ");
        sql.append("        from pfp_ad_keyword_report ");
        sql.append("        where ad_keyword_pvclk_date = :pvclk_date2 ");
        sql.append("            and ad_keyword_seq = k.ad_keyword_seq ");
        sql.append("        group by ad_keyword_seq),0) clk, ");
        sql.append("    c.recognize, ");
        sql.append("    k.ad_keyword_update_time, ");
        sql.append("    k.ad_keyword_create_time ");
        sql.append("from ");
        sql.append("    pfp_ad_keyword as k, ");
        sql.append("    pfp_ad as ad, ");
        sql.append("    pfp_ad_group as g, ");
        sql.append("    pfp_ad_action as a, ");
        sql.append("    pfp_customer_info as c ");
        sql.append("where c.later_remain  >= :later_remain ");
        sql.append("    and g.ad_group_seq = :group_seq ");
        sql.append("    and k.ad_keyword_status = :keyword_status ");
        sql.append("    and ad.ad_status = :ad_status ");
        sql.append("    and a.customer_info_id = c.customer_info_id ");
        sql.append("    and g.ad_action_seq = a.ad_action_seq ");
        sql.append("    and k.ad_group_seq = g.ad_group_seq ");
        sql.append("    and ad.ad_group_seq = g.ad_group_seq ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("pvclk_date1", pvclkDate);
        query.setParameter("pvclk_date2", pvclkDate);
        query.setParameter("later_remain", remain);
        query.setParameter("group_seq", groupId);
        query.setParameter("keyword_status", EnumAdStatus.Open.getStatusId());
        query.setParameter("ad_status", EnumAdStatus.Open.getStatusId());

        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> selectCustomerInfo(EnumAccountStatus enumAccountStatus) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpCustomerInfo ");
        hql.append("where status = ? ");

        return super.getHibernateTemplate().find(hql.toString(), enumAccountStatus.getStatus());
    }

    @Override
    public float selectCustomerInfoRemain() {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(remain) ");
        hql.append("from PfpCustomerInfo ");
        hql.append("where recognize = 'Y' ");

        Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

    @Override
    public int selectCustomerInfoIdCountByActivateDate(String activateDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(distinct customerInfoId) ");
        hql.append("from PfpCustomerInfo ");
        hql.append("where recognize = 'Y' ");
        hql.append("    and activateDate >= :startDate ");
        hql.append("    and activateDate <= :endDate ");

        return ((Long) this.getSession()
                .createQuery(hql.toString())
                .setString("startDate", activateDate + " 00:00:00")
                .setString("endDate", activateDate + " 23:59:59")
                .uniqueResult())
                .intValue();
    }

    @Override
    public int selectCustomerInfoIdCountByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, String activateDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(distinct pci.customerInfoId) ");
        hql.append("from PfpCustomerInfo pci, ");
        hql.append("     PfdUserAdAccountRef puaar ");
        hql.append("where pci.recognize = 'Y' ");
        hql.append("    and pci.activateDate >= :startDate ");
        hql.append("    and pci.activateDate <= :endDate ");
        hql.append("    and puaar.pfpPayType = :pfpPayType ");
        hql.append("    and pci.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");

        return ((Long) this.getSession()
                .createQuery(hql.toString())
                .setString("startDate", activateDate + " 00:00:00")
                .setString("endDate", activateDate + " 23:59:59")
                .setString("pfpPayType", enumPfdAccountPayType.getPayType())
                .uniqueResult())
                .intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
	public List<PfpCustomerInfo> findValidCustomerInfoByMemberId(String memberId) {

    	StringBuffer hql = new StringBuffer();
    	hql.append(" from PfpCustomerInfo ");
    	hql.append(" where memberId = ? ");
        hql.append(" and ( status = ? ");
        hql.append(" or status = ? ");
        hql.append(" or status = ? ) ");
        hql.append(" and activateDate != null ");


        Object[] ob = new Object[]{memberId,
        							EnumAccountStatus.CLOSE.getStatus(),
					                EnumAccountStatus.START.getStatus(),
					                EnumAccountStatus.STOP.getStatus()};

        return super.getHibernateTemplate().find(hql.toString(), ob);


    }


	// 2014-04-24
	@Override
    @SuppressWarnings("unchecked")
	public HashMap<String, PfpCustomerInfo> getPfpCustomerInfoBySeqList(List<String> customerInfoIdList) throws Exception {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfpCustomerInfo where 1=1");
		if (customerInfoIdList != null) {
			sql.append(" and customerInfoId in (:customerInfoId)");
			sqlParams.put("customerInfoId", customerInfoIdList);
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("customerInfoId")) {
				q.setParameterList("customerInfoId", customerInfoIdList);
			}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 CustomerInfoId 抓取資料
		HashMap<String, PfpCustomerInfo> pfpCustomerInfoMap = new HashMap<String, PfpCustomerInfo>();
		List<PfpCustomerInfo> pfpCustomerInfos = q.list();

		for(PfpCustomerInfo pfpCustomerInfo:pfpCustomerInfos) {
			pfpCustomerInfoMap.put(pfpCustomerInfo.getCustomerInfoId(), pfpCustomerInfo);
		}

		return pfpCustomerInfoMap;
	}

	//2014-05-02
	@Override
    @SuppressWarnings("unchecked")
    public HashMap<String, PfpCustomerInfo> getPfpCustomerInfoBySeqList(String startDate, String endDate, List<String> pfpCustomerInfoIds, List<String> status) throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo where 1=1");
		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and activateDate >= :startDate");
			sqlParams.put("startDate", sdf.parse(startDate));
		}
		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and activateDate <= :endDate");
			sqlParams.put("endDate", sdf.parse(endDate));
		}
		if (pfpCustomerInfoIds != null  && pfpCustomerInfoIds.size() > 0) {
			hql.append(" and customerInfoId in (:pfpCustomerInfoIds)");
			sqlParams.put("pfpCustomerInfoIds", pfpCustomerInfoIds);
		}
		if (status != null && status.size() > 0) {
			hql.append(" and status in (:status)");
			sqlParams.put("status", status);
		}
		hql.append(" order by activateDate desc");

		Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {

			if(paramName.equals("pfpCustomerInfoIds") || paramName.equals("status")) {
				query.setParameterList(paramName, (List<String>)sqlParams.get(paramName));
			} else {
		  		query.setParameter(paramName, sqlParams.get(paramName));
			}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfpCustomerInfo> pfpCustomerInfoMap = new HashMap<String, PfpCustomerInfo>();
		List<PfpCustomerInfo> pfpCustomerInfos = query.list();

		for(PfpCustomerInfo pfpCustomerInfo:pfpCustomerInfos) {
			pfpCustomerInfoMap.put(pfpCustomerInfo.getCustomerInfoId(), pfpCustomerInfo);
		}

		return pfpCustomerInfoMap;

	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpCustomerInfo> findPfpCustomerInfo(String pcId) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfpCustomerInfo ");
		hql.append(" where memberId = ? ");
		hql.append(" order by customerInfoId desc ");

		list.add(pcId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    public Integer deletePfpCustomerInfo(String pfpCustomerInfoId) {

		StringBuffer hql = new StringBuffer();

	    hql.append("delete from PfpCustomerInfo ");
	    hql.append("where customerInfoId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), pfpCustomerInfoId);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findManagerPfpCustomerInfo(final String memberId) {

		List<Object> result = getHibernateTemplate().execute(

		        new HibernateCallback<List<Object> >() {

		        	@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {

		        		StringBuffer sql = new StringBuffer();
		        		Query q = null;

		        		sql.append(" select pfpci.customer_info_id, pfpci.customer_info_title, pfpci.status, ");
		        		sql.append("		pfpci.remain, ");
		        		sql.append("		coalesce(pfd_user_member_ref.member_id, ''), coalesce(pfd_user.user_name, ''), ");
//		        		sql.append("		coalesce(admmd.member_id, ''), coalesce(admmd.manager_name, ''), ");
		        		sql.append("		coalesce(pfpuser.member_id, '') ");
		        		sql.append(" from pfp_customer_info as pfpci ");
		        		sql.append("	left join pfd_user_ad_account_ref pfdadref");
		        		sql.append("		on pfpci.customer_info_id = pfdadref.pfp_customer_info_id ");
		        		sql.append("	left join pfd_user ");
		        		sql.append("		on pfdadref.pfd_user_id = pfd_user.user_id ");
		        		sql.append("	left join pfd_user_member_ref ");
		        		sql.append("		on pfd_user_member_ref.user_id = pfdadref.pfd_user_id ");

		        		sql.append("	left join adm_channel_account as admca ");
		        		sql.append("		on pfpci.customer_info_id = admca.account_id ");
		        		sql.append("	left join adm_manager_detail as admmd ");
		        		sql.append("		on admca.member_id = admmd.member_id ");
		        		sql.append("	left join (select pfp_user.customer_info_id, coalesce(member_id, '') member_id ");
		        		sql.append("	           from pfp_user_member_ref pfpumr ");
		        		sql.append("	           join pfp_user on pfpumr.user_id = pfp_user.user_id ");
		        		sql.append("	           where pfp_user.privilege_id = 0 and pfp_user.status != '3') pfpuser ");
		        		sql.append("		on pfpuser.customer_info_id = pfpci.customer_info_id ");
		        		sql.append(" where pfpci.status != :pfpCustomerInfoStatus  ");

		        		if(StringUtils.isNotBlank(memberId)){
		        			sql.append("	and admmd.member_id = :memberId ");
			        		sql.append("	and admmd.manager_status = :managerStatus ");
			        		sql.append("	and admca.channel_category = :channelCategory ");
		        		}

		        		sql.append(" group by pfpci.customer_info_id ");

		        		//log.info(">>> sql: "+sql);

	        			q = session.createSQLQuery(sql.toString())
		        				.setString("pfpCustomerInfoStatus", EnumAccountStatus.DELETE.getStatus());

	        			if(StringUtils.isNotBlank(memberId)){
	        				q.setString("memberId", memberId)
	        				.setString("managerStatus", EnumManagerStatus.START.getStatus())
	        				.setString("channelCategory", EnumChannelCategory.PFP.getCategory());
	        			}


		            	return q.list();
		        	}

		        }
		);

		return result;
	}

    @Override
    public int updateLaterRemainLessThanAdCost(String pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append("    pfp_customer_info as c, ");
        sql.append("    (select ");
        sql.append("        customer_info_id, ");
        sql.append("        ifnull(sum(ad_clk_price), 0) as clk_price ");
        sql.append("    from pfp_ad_pvclk ");
        sql.append("    where ad_pvclk_date = :pvclk_date ");
        sql.append("    group by customer_info_id) as p ");
        sql.append("set ");
        sql.append("    c.later_remain = 0, ");
        sql.append("    c.update_date = now() ");
        sql.append("where c.later_remain > 0 ");
        sql.append("    and c.customer_info_id = p.customer_info_id ");
        sql.append("    and c.later_remain <= p.clk_price ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("pvclk_date", pvclkDate);

        return query.executeUpdate();
    }

    @Override
    public int updateLaterRemainLessThanAdKeywordCost(String pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append("    pfp_customer_info as c, ");
        sql.append("    (select ");
        sql.append("        customer_info_id, ");
        sql.append("        ifnull(sum(ad_keyword_clk_price), 0) as clk_price ");
        sql.append("    from pfp_ad_keyword_pvclk ");
        sql.append("    where ad_keyword_pvclk_date = :pvclk_date ");
        sql.append("    group by customer_info_id) as p ");
        sql.append("set ");
        sql.append("    c.later_remain = 0, ");
        sql.append("    c.update_date = now() ");
        sql.append("where c.later_remain > 0 ");
        sql.append("    and c.customer_info_id = p.customer_info_id ");
        sql.append("    and c.later_remain <= p.clk_price ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("pvclk_date", pvclkDate);

        return query.executeUpdate();
    }
    
	public void saveOrUpdateWithCommit(PfpCustomerInfo pfpCustomerInfo) throws Exception{
		super.getSession().saveOrUpdate(pfpCustomerInfo);
		super.getSession().beginTransaction().commit();
	}
	
    @Override
    @SuppressWarnings("unchecked")
    public List<PfpCustomerInfo> findCustomerInfoIds(List<String> customerInfoList) throws Exception{

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpCustomerInfo ");
        hql.append(" where 1=1 ");
        hql.append(" and customerInfoId in (:customerInfoList) ");
         
     	Query q = this.getSession().createQuery(hql.toString());
    	q.setParameterList("customerInfoList", customerInfoList);

        return q.list();
    }
    
	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findTransDetailPfp(final String yesterday , final String today , final String tomorrow) throws Exception{

		List<Object> result = getHibernateTemplate().execute(

		        new HibernateCallback<List<Object> >() {

		        	@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {
		        		
		        		StringBuffer sql = new StringBuffer();
		        		Query q = null;

		        		sql.append(" select customer_info_id from pfp_order where (status = 'B301' or status = 'B302' ) ");
		        		sql.append(" and notify_date between :yesterdayTime and :tomorrowTime ");
		        		sql.append(" UNION ");
		        		sql.append(" select customer_info_id from pfp_refund_order where 1=1 and refund_date >= :yesterday and refund_date <= :today ");
		        		sql.append(" and ( (pay_type ='1' and refund_status = 'Y') or (pay_type = '2' and refund_status in ('Y','F')) ) ");
		        		sql.append(" UNION ");
		        		sql.append(" select customer_info_id from adm_feedback_record where 1=1 and status = '1'  and gift_date >= :yesterday and gift_date <= :today ");
		        		sql.append(" UNION ");
		        		sql.append(" select customer_info_id from adm_free_record  where 1=1 and record_date >= :yesterday and record_date <= :today ");
		        		sql.append(" UNION ");
		        		sql.append(" select pfp_customer_info_id from pfd_virtual_record where 1=1 and add_date >= :yesterday and add_date <= :today ");
		        		sql.append(" UNION ");
		        		sql.append(" select customer_info_id  from pfp_ad_action_report  where 1=1 and pay_type in ('1','2') ");
		        		sql.append(" and ad_pvclk_date >= :yesterday  and ad_pvclk_date <= :today ");

		        		
	        			q = session.createSQLQuery(sql.toString())
		        			.setString("yesterdayTime", yesterday + " 03:30:00 ")
	        				.setString("tomorrowTime", tomorrow + " 03:29:59 ")	        				
	        				.setString("yesterday",yesterday)
	        				.setString("today", today)	        				
	        				.setString("yesterday",yesterday)
	        				.setString("today", today)	        				
	        				.setString("yesterday",yesterday)
	        				.setString("today", today)	        				
	        				.setString("yesterday",yesterday)
	        				.setString("today", today)
	        				.setString("yesterday",yesterday)
	        				.setString("today", today);
	        			
		            	return q.list();
		        	}
		        }
		);
		return result;
	}
}
