package com.pchome.soft.depot.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JredisUtil {
	private final Logger log = LogManager.getRootLogger();
	private static JedisCluster jedisCluster;

	public JredisUtil() {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("192.168.2.204", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.2.206", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.2.207", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.2.208", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.2.209", 6379));

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 最大连接数
		jedisPoolConfig.setMaxTotal(30);
		// 最大空闲连接数
		jedisPoolConfig.setMaxIdle(10);
		// 每次释放连接的最大数目
		jedisPoolConfig.setNumTestsPerEvictionRun(1024);
		// 释放连接的扫描间隔（毫秒）
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		// 连接最小空闲时间
		jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000);
		// 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10000);
		// 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1
		jedisPoolConfig.setMaxWaitMillis(1500);
		// 在获取连接的时候检查有效性, 默认false
		jedisPoolConfig.setTestOnBorrow(true);
		// 在空闲时检查有效性, 默认false
		jedisPoolConfig.setTestWhileIdle(true);
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		jedisPoolConfig.setBlockWhenExhausted(true);
		this.jedisCluster = new JedisCluster(jedisClusterNodes, 2000, 100, jedisPoolConfig);
	}

	public boolean setKeyAndExpire(String key, String value, int second) throws Exception {
		if (StringUtils.isBlank(key) && StringUtils.isBlank(value)) {
			return false;
		}
		jedisCluster.set(key, value);
		jedisCluster.expire(key, second);
		return true;
	}

	public String getKey(String key) throws Exception {
		if (StringUtils.isBlank(key)) {
			return "";
		}
		String value = "";
		value = jedisCluster.get(key) == null ? "" : jedisCluster.get(key);
		return value;
	}

	public String getKeyNew(String key) throws Exception {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		String value = null;
		value = jedisCluster.get(key) == null ? null : jedisCluster.get(key);
		return value;
	}

	public boolean delKey(String key) {
		log.info(">>>>>> key:" + key);
		if (StringUtils.isBlank(key)) {
			return false;
		}

		long status = 0;
		status = jedisCluster.del(key);
		return status == 0 ? false : true;
	}
}
