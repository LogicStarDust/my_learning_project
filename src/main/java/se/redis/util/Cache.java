package se.redis.util;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Set;

public class Cache extends RedisFactory{
	 
	 
	
	
	/**
	 * 添加缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putString(String key, String value) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			jedis.set(key, value);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return true;
	}
	/**
	 * 添加缓存,设置超时时间，单位为秒
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public static boolean putString(String key, String value,int seconds) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			jedis.setex(key, seconds, value);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return true;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			return jedis.get(key);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return null;
	}

	/**
	 * 获取一定位置字串
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getSubStr(String key,int start,int end) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			return jedis.substr(key, start, end); 
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return null;
	}


	/**
	 * 获取缓存列表
	 * 
	 * @param key
	 * @return
	 */
	public static List<String> getCacheList(String key) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			return jedis.lrange(key, 0, -1);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return null;
	}

	/**
	 * 添加缓存列表
	 * 
	 * @param key
	 * @param value
	 */
	public static void setCacheList(String key, String... value) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			if (null != value)
				jedis.rpush(key, value);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
	}

	/**
	 * 获取缓存列表
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> getCacheSet(String key) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			return jedis.smembers(key);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return null;
	}

	/**
	 * 添加缓存列表
	 * 
	 * @param key
	 * @param values
	 */
	public static void setCacheSet(String key, String... values) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			if (null != values)
				jedis.sadd(key, values);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
	}

	/**
	 * 通过key向zset中添加value,score,其中score就是用来排序的
	 * 如果该value已经存在则根据score更新元素,每次score自增加0.1；可以指定存放的个数
	 * @param key
	 * @param member
	 * @param n
	 * @return
	 */
	public static Long zaddInc(String key, String member, int n) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Long num = 0L;
		try {
			if (jedis != null) {
				Long length = zcard(key) - n;
				if (length >= 0) {
					zremrangeByRank(key, 0, length);
				}

				Set<Tuple> elements = Cache.zrevrangeWithScores(key,
						0, 0);
				double score = 1;
				for (Tuple tuple : elements) {
					score = tuple.getScore() + 0.1;
				}
				num = jedis.zadd(key, score, member);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}

		return num;
	}

	/**
	 * 通过key删除给定区间内的元素
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Long zremrangeByRank(String key, long start, long end) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Long res = 0L;
		try {
			if (jedis != null) {
				res = jedis.zremrangeByRank(key, start, end);
			}
		}catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return res;
	}

	public static Set<Tuple> zrevrangeWithScores(String key, long start,
			long end) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Set<Tuple> sets = null;
		try {
			if (jedis != null) {
				sets = jedis.zrevrangeWithScores(key, start, end);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return sets;
	}

	/**
	 * 通过key返回zset中的value个数
	 * @param key
	 * @return
	 */
	public static Long zcard(String key) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Long res = 0L;
		try {
			if (jedis != null) {
				res = jedis.zcard(key);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return res;
	}


	/**
	 * 通过key将获取score从start到end中zset的value socre从大到小排序;当start为0
	 * end为-1时返回全部
	 * @param key
	 * @return
	 */
	public static Set<String> zrevrange(String key, long start, long end) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Set<String> res = null;
		try {
			if (jedis != null) {
				res = jedis.zrevrange(key, start, end);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}

		return res;
	}

	/**
	 * 通过key删除在zset中指定的value
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public static Long zrem(String key, String... members) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		Long num = 0L;
		try {
			if (jedis != null) {
				num = jedis.zrem(key, members);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}

		return num;
	}
	/**
	 * 增加缓存 byte[]
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addCache(byte[] key, byte[] value) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			jedis.set(key, value);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return true;
	}
	/**
	 * 得到缓存
	 * @param key
	 * @return
	 */
	public static byte[] getCache(byte[] key) {
		ShardedJedis jedis = pool.getResource();
		boolean broken = false;
		try {
			return jedis.get(key);
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		return null;
	}
	
	/**
	 * 删除缓存
	 * @param key
	 * @return
	 */
	public static Long del(byte[] key){
		ShardedJedis jedis=pool.getResource();
		boolean broken = false;
		Long num = 0L;
		try {
			if(jedis != null) {
				num = jedis.del(key);
			}
		} catch (JedisException e) {
			 broken = handleJedisException(e);
		} finally {
			closeResource(jedis,broken);
		}
		
		return num;
	}

}
