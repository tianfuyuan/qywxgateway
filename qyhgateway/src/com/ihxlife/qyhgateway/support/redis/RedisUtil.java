package com.ihxlife.qyhgateway.support.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ihxlife.qyhgateway.support.spring.SpringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis工具类
 * @author Administrator
 *
 */
public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	public static final JedisPool jedisPool = SpringUtils.getBean("jedisPool");

	/**
	 * Redis放值不递增
	 * @param key 键
	 * @param value 键值
	 * @return
	 */
	public static boolean insertResisInfo(String key, String value) {
		Jedis jedis = null;
		try {
			logger.info("Redis放值：key【{}】，value【{}】", key, value);
			jedis = jedisPool.getResource();
			if (jedis.exists(key)) {
				logger.error("key【{}】在redis中已存在，不重复插入");
				return false;
			}
			jedis.set(key, value);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Redis放置值异常【{}】", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * 向redis存储数据并设置有效时间
	 * @param key 键
	 * @param value 值
	 * @param seconds 有效时间
	 * @return
	 */
	public static boolean insertResisInfo(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			logger.info("Redis放值：key【{}】，value【{}】", key, value);
			jedis = jedisPool.getResource();
			if (jedis.exists(key)) {
				logger.error("key【{}】在redis中已存在，不重复插入");
				return false;
			}
			jedis.set(key, value);
			jedis.expire(key, seconds);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Redis放置值异常【{}】", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * Redis更新值
	 * @param key 键
	 * @param value 键值
	 * @return
	 */
	public static boolean updateResisInfo(String key, String value) {
		Jedis jedis = null;
		try {
			logger.info("Redis更值：key【{}】，value【{}】", key, value);
			jedis = jedisPool.getResource();
			if (!jedis.exists(key)) {
				logger.error("key【{}】在redis中不存在");
				return false;
			}
			jedis.set(key, value);
			return true;
		} catch (Exception ex) {
			logger.error("Redis更新值异常【{}】", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * Redis取值
	 * @param key 键
	 * @return
	 */
	public static String getResisInfo(String key) {
		Jedis jedis = null;
		try {
			logger.info("Redis取值：key【{}】", key);
			jedis = jedisPool.getResource();
			if (jedis.exists(key)) {
				return jedis.get(key);
			} else {
				logger.info("Redis取值：key【{}】,key不存在", key);
				return "";
			}
		} catch (Exception ex) {
			logger.error("Redis取值异常【{}】", ex.getMessage());
			ex.printStackTrace();
			return "";
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * Redis是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean isExists(String key) throws Exception {
		logger.info("Redis判断：key【{}】是否存在", key);
		boolean bool = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			bool = jedis.exists(key);
			logger.info("Redis判断：key【{}】是否存在, 结果【{}】", key, bool);
		} catch (Exception ex) {
			logger.error("Redis判断是否存在异常", ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
		return bool;
	}

	/**
	 * Redis删除
	 * @param key 键
	 * @return
	 */
	public static boolean deleteRedisInfo(String key) throws Exception {
		logger.info("Redis删除：key【{}】", key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis.exists(key)) {
				if (jedis.del(key) > 0) {
					return true;
				}
				return false;
			} else {
				logger.info("Redis删除：key【{}】,key不存在", key);
				return false;
			}
		} catch (Exception ex) {
			logger.error("Redis删除异常", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * 向redis中存入hash值
	 * @param hKey hash键
	 * @param key 键
	 * @param value 值
	 * @return
	 * @throws Exception
	 */
	public static boolean insertRedisHashInfo(String hKey, String key, String value) throws Exception {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis.hexists(hKey, key)) {
				logger.info("Redis hash中值存在：hKey【{}】,key【{}】,无需新增", hKey, key);
				return false;
			}
			jedis.hset(hKey, key, value);
			logger.info("Redis hash存值成功：hKey【{}】,key【{}】,value【{}】", hKey, key, value);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Redis放置hash值异常【{}】", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * 从redis中获取hash值
	 * @param hKey hash键
	 * @param key 键
	 * @return
	 * @throws Exception
	 */
	public static String getRedisHashInfo(String hKey, String key) throws Exception {
		Jedis jedis = null;
		try {
			logger.info("Redis Hash取值：hkey【{}】,key【{}】", hKey, key);
			jedis = jedisPool.getResource();
			if (jedis.hexists(hKey, key)) {
				return jedis.hget(hKey, key);
			} else {
				logger.info("Redis Hash取值：hkey【{}】,key【{}】 不存在", hKey, key);
				return "";
			}
		} catch (Exception ex) {
			logger.error("Redis Hash取值异常【{}】", ex.getMessage());
			ex.printStackTrace();
			return "";
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * 更新redis中的hash值
	 * @param hKey hash键
	 * @param key 键
	 * @param value 值
	 * @return
	 * @throws Exception
	 */
	public static boolean updateRedisHashInfo(String hKey, String key, String value) throws Exception {
		Jedis jedis = null;
		try {
			logger.info("Redis Hash更新值：hkey【{}】，key【{}】，value【{}】", hKey, key, value);
			jedis = jedisPool.getResource();
			if (!jedis.hexists(hKey, key)) {
				logger.error("hKey【{}】,key【{}】在redis Hash中不存在", hKey, key);
				return false;
			}
			jedis.hset(hKey, key, value);
			return true;
		} catch (Exception ex) {
			logger.error("Redis Hash更新值异常【{}】", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * 删除redis中的hash值
	 * @param hKey hash键
	 * @param key 键
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteRedisHashInfo(String hKey, String key) throws Exception {
		logger.info("Redis Hash删除值：hkey【{}】，key【{}】", hKey, key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis.hexists(hKey, key)) {
				if (jedis.hdel(hKey, key) > 0) {
					return true;
				}
				return false;
			} else {
				logger.info("Redis Hash删除：hkey【{}】,key【{}】,key不存在", hKey, key);
				return false;
			}
		} catch (Exception ex) {
			logger.error("Redis Hash删除异常", ex.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
	}

	/**
	 * redis中hash值是否存在
	 * @param hKey hash键
	 * @param key 键
	 * @return
	 * @throws Exception
	 */
	public static boolean isRedisHashExists(String hKey, String key) throws Exception {
		logger.info("Redis Hash判断：hkey【{}】,key【{}】是否存在", hKey, key);
		boolean bool = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			bool = jedis.hexists(hKey, key);
			logger.info("Redis Hash判断hkey【{}】,key【{}】是否存在", hKey, key, bool);
		} catch (Exception ex) {
			logger.error("Redis Hash判断是否存在异常", ex);
		} finally {
			if (jedis != null) {
				jedisPool.returnResourceObject(jedis);
			}
		}
		return bool;
	}
	
	/**
	 * 增量获取redis中的值,支持数字
	 * 
	 * @param key     存放key
	 * @param incrementValue   增量值
	 * @return
	 */
	public Long increment(String key, long incrementValue) {
		Long result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if(isExists(key)){
				result = jedis.incrBy(key, incrementValue);
				logger.info("获取的自增数值为：【{}】",result);
			}else{
				logger.info("redis不存在值 key为【{}】",key);
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		}
		
	}
}