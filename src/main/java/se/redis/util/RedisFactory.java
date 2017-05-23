package se.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import se.redis.util.help.SerializeTranscoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RedisFactory {
	protected static Logger logger = LoggerFactory.getLogger(SerializeTranscoder.class);
	// 缓存池
	protected static ShardedJedisPool pool;
	static {
		init();
	}

	/**
	 * 初始化缓存操作
	 */
	protected static void init() {
		Properties prop = new Properties();

		try {
			InputStream is = Cache.class.getResourceAsStream("/redis.properties");
			prop.load(is);
		} catch (IOException e) {
			logger.error("conn redis config file fail....", e);
		}
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		pool = new ShardedJedisPool(poolConfig, shards);
		String[] nodes = prop.getProperty("nodes").split(",");

		for (String node : nodes) {

			shards.add(new JedisShardInfo(node.split(":")[0], Integer.valueOf(node.split(":")[1])));
		}
		Runtime.getRuntime().addShutdownHook(new CloseJedisConn());
	}

	/**
	 * 
	 * @author 刘宪领
	 * @data 2016-12-16 下午2:43:00
	 */
	protected static class CloseJedisConn extends Thread {

		@Override
		public void run() {
			try {
				if (null != pool && !pool.isClosed()) {
					pool.destroy();
				}
			} catch (Exception e) {
				logger.error("jedis destroy error....", e);
			}
		}

	}

	/**
	 * 日志处理
	 * 
	 * @param jedisException
	 * @return
	 */
	protected static boolean handleJedisException(JedisException jedisException) {
		if (jedisException instanceof JedisConnectionException) {
			logger.error("Redis connection  lost.", jedisException);
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null)
					&& (jedisException.getMessage().indexOf("READONLY") != -1)) {
				logger.error("Redis connection are read-only slave.",
						jedisException);
			} else {
				// dataException, isBroken=false
				return false;
			}
		} else {
			logger.error("Jedis exception happen.", jedisException);
		}
		return true;
	}

	/**
	 * 根据不同的状态释放
	 * 
	 * @param jedis
	 * @param conectionBroken
	 */
	protected static void closeResource(ShardedJedis jedis, boolean conectionBroken) {
		try {
			if (conectionBroken) {
				pool.returnBrokenResource(jedis);
			} else {
				pool.returnResource(jedis);
			}
		} catch (Exception e) {
			logger.error("return back jedis failed, will fore close the jedis.", e);
			jedis.close();
		}
	}
}
