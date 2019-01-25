package com.pchome.akbadm.db.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class BaseDAO<T, PK extends Serializable> extends HibernateDaoSupport implements IBaseDAO<T, PK> {
    protected Log log = LogFactory.getLog(this.getClass());

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    protected Class<T> getMyClass() {
        if (clazz == null) {
            clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return clazz;
    }

    @Override
    public T get(Serializable id) {
        return getHibernateTemplate().get(getMyClass(), id);
    }

    @Override
    public long loadAllSize() {
        return getHibernateTemplate().loadAll(getMyClass()).size();
    }

    @Override
    public List<T> loadAll() {
        return getHibernateTemplate().loadAll(getMyClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> loadAll(int firstResult, int maxResults) {
        return getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria(getMyClass()).setFirstResult(firstResult).setMaxResults(maxResults).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        return (PK) getHibernateTemplate().save(entity);
    }

    @Override
    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<T> entities) {
        getHibernateTemplate().saveOrUpdateAll(entities);
    }

    @Override
    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    protected int nextVal(final String sql) {
        BigInteger nextval = getHibernateTemplate().execute(
            new HibernateCallback<BigInteger>() {
                @Override
                public BigInteger doInHibernate(Session session) throws HibernateException, SQLException {
                    return (BigInteger) session.createSQLQuery(sql).uniqueResult();
                }
            });
        return nextval.intValue();
    }
}