package zbuer.com.cluster.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import zbuer.com.jedis.RedisUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zbuer.com.jedis.RunWithJedis;
import com.alibaba.fastjson.JSON;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author buer
 * @since 16/8/2
 */
@Component
public class ClusterSupportWithRedis extends JedisPubSub implements Runnable, ClusterSupport {

	private static final Logger logger = LoggerFactory.getLogger(ClusterSupportWithRedis.class);

	private final ExecutorService listenerThread = Executors.newSingleThreadExecutor();

	private Map<ClusterNotifyType, ClusterMessageProcessor<?>> processors = new ConcurrentHashMap<ClusterNotifyType, ClusterMessageProcessor<?>>();

	public void register(ClusterNotifyType type, ClusterMessageProcessor<?> processor) {
		processors.put(type, processor);
	}

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void onMessage(String channel, String message) {
		super.onMessage(channel, message);
		logger.info("recieve msg channel : " + channel + " detail : " + message);
		ClusterNotifyType type = ClusterNotifyType.parse(channel);
		if (type == null) {
			logger.error("can not find channel type for channel : " + String.valueOf(channel));
		} else {
			ClusterMessageProcessor<?> proc = processors.get(ClusterNotifyType.parse(channel));
			if (proc == null) {
				logger.error("can not find channel processor for channel:" + String.valueOf(channel));
			} else {
				proc.onMessage(channel, message);

			}

		}
	}

	public String notifyCluster(final ClusterNotifyType channel, final Object body) {
		try {
			logger.info("notify cluster app :");
			Long result = redisUtil.execute(this.getClass().getSimpleName(), new RunWithJedis<Long>() {
				public Long run(Jedis jedis) {
					Long result = jedis.publish(channel.name(), JSON.toJSONString(body));
					return result;
				}
			});
			logger.info("notify cluster app : " + String.valueOf(result));

			return String.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostConstruct
	public void monitor() {
		listenerThread.execute(this);
	}

	public void run() {
		logger.info("listen start.....");
		try {
			redisUtil.execute(this.getClass().getSimpleName(), new RunWithJedis<String>() {
				public String run(Jedis jedis) {
					Client client = jedis.getClient();
					client.setTimeoutInfinite();
					proceed(client, ClusterNotifyType.all());
					return null;

				}
			});

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	@PreDestroy
	public void destroy() {
		listenerThread.shutdown();
	}

}
