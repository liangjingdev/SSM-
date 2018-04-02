package cn.jing.campusShop.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * function:强指定redis的JedisPool接口构造函数，这样才能在centos中成功创建JedisPool
 * 
 * @author liangjing
 *
 */
public class JedisPoolWriper {

	/** Redis连接池对象 */
	private JedisPool jedisPool;

	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * function:获取Redis连接池对象
	 * 
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * function:注入Redis连接池对象(利用getter和setter方法将Redis连接池对象注入到Spring IOC容器中)
	 * 
	 * @param jedisPool
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
