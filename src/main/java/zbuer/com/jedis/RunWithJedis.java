package zbuer.com.jedis;

import redis.clients.jedis.Jedis;

/**
 * reids的一个回调
 *
 * @author buer
 * @since 16/6/13
 */
public interface RunWithJedis<T> {

	T run(Jedis jedis);
}
