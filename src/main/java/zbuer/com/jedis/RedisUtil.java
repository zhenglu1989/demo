package zbuer.com.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisClusterConnectionHandler;

/**
 * redis工具,可以执行一个callback
 * @author buer
 * @since 16/6/13
 */
@Component
public class RedisUtil {

  @Autowired
	private JedisClusterConnectionHandler jedisClusterConnectionHandler;

	public <K> K execute(String key, final RunWithJedis<K> runWithJedis){
		JedisClusterCommand<K> cmd = new JedisClusterCommand<K>(jedisClusterConnectionHandler,2) {
			@Override public K execute(Jedis connection) {
				return runWithJedis.run(connection);
			}
		};
		return cmd.run(key);

	}

}
