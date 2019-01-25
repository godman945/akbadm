package com.pchome.akbadm.db.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IBaseService<T, PK extends Serializable> {
	public T get(Serializable id);

    public long loadAllSize();

    public List<T> loadAll();

    public List<T> loadAll(int firstResult, int maxResults);

    public PK save(T entity);

    public void update(T entity);

    public void saveOrUpdate(T entity);

    public void saveOrUpdateAll(Collection<T> entities);

    public void delete(T entity);
}