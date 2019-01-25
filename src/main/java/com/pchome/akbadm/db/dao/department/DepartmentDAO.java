package com.pchome.akbadm.db.dao.department;

import java.sql.SQLException;
import java.util.List;



import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.dao.department.IDepartmentDAO;
import com.pchome.akbadm.db.pojo.AdmDepartment;

public class DepartmentDAO extends BaseDAO<AdmDepartment, String> implements IDepartmentDAO {

	@SuppressWarnings("unchecked")
	public List<AdmDepartment> getParentDepartment() throws Exception {
		return super.getHibernateTemplate().find("from AdmDepartment where parentDepId is null or parentDepId = '' order by sort");
	}

	@SuppressWarnings("unchecked")
	public List<AdmDepartment> getChildDepartment(String parentId) throws Exception {
		return super.getHibernateTemplate().find("from AdmDepartment where parentDepId = '" + parentId + "' order by sort");
	}

	@SuppressWarnings("unchecked")
	public AdmDepartment getDeptById(String deptId) throws Exception {
		List<AdmDepartment> list = this.getHibernateTemplate().find("from AdmDepartment where depId = '" + deptId + "' order by sort");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmDepartment findParentDeptById(String deptId) throws Exception {
		List<AdmDepartment> list = this.getHibernateTemplate().find("from AdmDepartment where depId = '" + deptId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Integer saveDepartment(final AdmDepartment department) throws Exception {
		logger.info("++++++++++ DepId = " + department.getDepId());
		logger.info("DepName = " + department.getDepName());
		logger.info("ParentDepId = " + department.getParentDepId());
		logger.info("Sort = " + department.getSort());
		logger.info("CreateDate = " + department.getCreateDate());
		logger.info("UpdateDate = " + department.getUpdateDate());

		//this.getHibernateTemplate().clear();
		//this.getHibernateTemplate().saveOrUpdate(department);
		//this.getHibernateTemplate().flush();
		//this.getHibernateTemplate().getSessionFactory().getCurrentSession().beginTransaction().commit();
		//this.getHibernateTemplate().getSessionFactory().openSession().close();

		this.getHibernateTemplate().execute(new HibernateCallback(){

			@Override
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				try{

					session.doWork(
							new Work() {
								@Override
								public void execute(java.sql.Connection connection)
								throws SQLException {
									// TODO Auto-generated method stub
									connection.setAutoCommit(false);

								}
							}
					);

					session.saveOrUpdate(department);
					session.flush();

					session.doWork(
							new Work() {
								@Override
								public void execute(java.sql.Connection connection)
								throws SQLException {
									// TODO Auto-generated method stub
									connection.commit();

								}
							}
					);

					log.info(">>>save do commit >>>");

				} catch (HibernateException e1) { 
					log.error(e1.toString());
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.toString());
				} 
				finally {

					try {
						if (session != null) session.close();
					} catch (HibernateException e) {
						log.error(e.toString());
					}

				}

				return null;
			}

		});


		return department.getDepId();
	}




	public void deleteParentDept(String deptId) throws Exception {
		Session session = getSession();

		String sql = "delete from AdmDepartment where depId = '" + deptId + "'";
		session.createQuery(sql).executeUpdate();

		String sql2 = "delete from AdmDepartment where parentDepId = '" + deptId + "'";
		session.createQuery(sql2).executeUpdate();

		session.flush();
	}

	public void deleteChildDept(String deptId) throws Exception {
		String sql = "delete from AdmDepartment where depId = '" + deptId + "'";
		Session session = getSession();
		session.createQuery(sql).executeUpdate();
		session.flush();
	}
}
