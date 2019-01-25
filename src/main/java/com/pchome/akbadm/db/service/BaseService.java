package com.pchome.akbadm.db.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.dao.IBaseDAO;

public abstract class BaseService<T, PK extends Serializable> implements IBaseService<T, PK> {
    protected Log log = LogFactory.getLog(this.getClass());

    protected IBaseDAO<T, PK> dao;

    @Override
    public T get(Serializable id) {
    	log.info(id);
        return dao.get(id);
    }

    @Override
    public long loadAllSize() {
        return dao.loadAllSize();
    }

    @Override
    public List<T> loadAll() {
        return dao.loadAll();
    }

    @Override
    public List<T> loadAll(int firstResult, int maxResults) {
        return dao.loadAll(firstResult, maxResults);
    }

    @Override
    public PK save(T entity) {
        return dao.save(entity);
    }

    @Override
    public void update(T entity) {
        dao.update(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<T> entities) {
        dao.saveOrUpdateAll(entities);
    }

    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }

    public IBaseDAO<T, PK> getDao() {
        return dao;
    }

    public void setDao(IBaseDAO<T, PK> dao) {
        this.dao = dao;
    }
}