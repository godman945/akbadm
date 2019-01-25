package com.pchome.soft.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedUtil {
    private Log log = LogFactory.getLog(getClass().getName());
    private SockIOPool pool = SockIOPool.getInstance();
    private MemCachedClient client = new MemCachedClient();

    private String[] addr;
    private Integer[] weights = {1};
    private int initConn = 5;
    private int minConn = 5;
    private int maxConn = 250;
    private int maxIdle = 1000 * 60 * 60 * 6;
    private int maintSleep = 30;
    private boolean nagle = false;
    private int socketTo = 30;
    private int socketConnectTo = 0;

    public MemcachedUtil() {}

    public MemcachedUtil(String...addr) {
        this.addr = addr;
        init();
    }

    public MemcachedUtil(String[] addr, Integer[] weights, int initConn,
            int minConn, int maxConn, int maxIdle, int maintSleep,
            boolean nagle, int socketTo, int socketConnectTo) {
        this.addr = addr;
        this.weights = weights;
        this.initConn = initConn;
        this.minConn = minConn;
        this.maxConn = maxConn;
        this.maxIdle = maxIdle;
        this.maintSleep = maintSleep;
        this.nagle = nagle;
        this.socketTo = socketTo;
        this.socketConnectTo = socketConnectTo;
        init();
    }

    public void init() {
        pool.setServers(addr);
        pool.setWeights(weights);
        pool.setInitConn(initConn);
        pool.setMinConn(minConn);
        pool.setMaxConn(maxConn);
        pool.setMaxIdle(maxIdle);
        pool.setMaintSleep(maintSleep);
        pool.setNagle(nagle);
        pool.setSocketTO(socketTo);
        pool.setSocketConnectTO(socketConnectTo);
        pool.initialize();
    }

    public void addCache(String key, Object value, Date expiry) {
        log.debug(key + " " + value);
        client.add(key, value, expiry);
    }

    public void deleteCache(String key) {
        log.debug(key);
        client.delete(key);
    }

    public void replaceCache(String key, Object value, Date expiry) {
        log.debug(key + " " + value);
        client.replace(key, value, expiry);
    }

    public void setCache(String key, Object value, Date expiry) {
        log.debug(key + " " + value);
        client.set(key, value, expiry);
    }

    public Object getCache(String key) {
        Object value = client.get(key);
        log.debug(key + " " + value);
        return value;
    }

    public void setAddr(String...addr) {
        this.addr = addr;
    }

    public void setWeights(Integer...weights) {
        this.weights = weights;
    }

    public void setInitConn(int initConn) {
        this.initConn = initConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaintSleep(int maintSleep) {
        this.maintSleep = maintSleep;
    }

    public void setNagle(boolean nagle) {
        this.nagle = nagle;
    }

    public void setSocketTo(int socketTo) {
        this.socketTo = socketTo;
    }

    public void setSocketConnectTo(int socketConnectTo) {
        this.socketConnectTo = socketConnectTo;
    }
}
