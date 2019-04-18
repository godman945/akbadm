package com.pchome.akbadm.db.dao.board;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpBoard;

public class PfpBoardDAO extends BaseDAO<PfpBoard, Integer> implements IPfpBoardDAO {
	@SuppressWarnings("unchecked")
	public List<Object> findSystemBoards(final int page, final int pageSize) throws Exception{

		List<Object> result = getHibernateTemplate().execute(

                new HibernateCallback<List<Object> >() {

					public List<Object>  doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();
						hql.append(" select b.boardId ");
                    	hql.append(" from PfpBoard as b ");
                    	hql.append(" where b.customerInfoId = 'ALL' ");
                    	hql.append(" order by b.boardId ");

						//log.info(">>  hql  = "+hql);

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

	@SuppressWarnings("unchecked")
	public List<PfpBoard> findSystemBoards() throws Exception{
		return (List<PfpBoard>) super.getHibernateTemplate().find("from PfpBoard where customerInfoId = 'ALL' ");
	}

	@SuppressWarnings("unchecked")
	public PfpBoard findPfpBoard(String boardId) throws Exception{
		List<PfpBoard> list = (List<PfpBoard>) super.getHibernateTemplate().find("from PfpBoard where boardId = '" + boardId + "' ");
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

    @SuppressWarnings("unchecked")
    public List<PfpBoard> selectPfpBoard(String customerInfoId, String category, String display) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpBoard ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");
        hql.append("    and display = ? ");

        return (List<PfpBoard>) this.getHibernateTemplate().find(hql.toString(), customerInfoId, category, display);
    }

    public Integer displayPfpBoard(String customerInfoId, String category, String display) {
        StringBuffer hql = new StringBuffer();
        hql.append("update PfpBoard ");
        hql.append("set display = ?, ");
        hql.append("    updateDate = ? ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), display, Calendar.getInstance().getTime(), customerInfoId, category);
    }

    public Integer deletePfpBoard(String customerInfoId, String category) {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfpBoard ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), customerInfoId, category);
    }

    public Integer deletePfpBoard(String customerInfoId, String category, String deleteId) {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfpBoard ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");
        hql.append("    and deleteId = ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), customerInfoId, category, deleteId);
    }

    public Integer deletePfpBoardOvertime(String overtime) {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfpBoard ");
        hql.append("where endDate < ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), overtime);
    }

	public void deletePfpBoard(String boardId) throws Exception{

		String hql = "delete from PfpBoard where boardId = '" + boardId + "'";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}
}