package zbuer.com.limiter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 应用级限流
 *
 * @author buer
 * @since 16/6/13
 */
public class ApplicationLimiter {

	private AtomicLong atomicLong = new AtomicLong(0);

	private static final int LIMIT = 1000;

	/**
	 * <pre>
	 *     限制某个接口的总并发数/请求数（伪代码）
	 * </pre>
	 */
	public void handleByLimiter() {
		try {
			if (atomicLong.incrementAndGet() > LIMIT) {
				// 拒绝请求
			}
			//处理请求

		} finally {
			atomicLong.decrementAndGet();

		}
	}

	/**
	 * 时间窗口内限制并发数(限制某个接口/服务每秒/每分钟/每天的请求数/调用量)
	 */

	public void handleByLimiterAtFiXedTime() throws Exception {

		LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS)
		                                                     .build(new CacheLoader<Long, AtomicLong>() {

			                                                     @Override
			                                                     public AtomicLong load(Long key) throws Exception {
				                                                     return new AtomicLong(0);
			                                                     }
		                                                     });

		long limit = 1000;
		while (true) {
			//得到当前的秒数
			long currentSeconds = System.currentTimeMillis() / 1000;
			if (counter.get(currentSeconds).incrementAndGet() > limit) {

				System.out.println("限流了：" + currentSeconds);
				continue;
			}
			//处理业务逻辑
		}

	}
}
