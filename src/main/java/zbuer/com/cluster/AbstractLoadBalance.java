package zbuer.com.cluster;

import java.util.List;

/**
 * abstract
 *
 * @author buer
 * @since 16/5/24
 */
public abstract class AbstractLoadBalance implements LoadBalance {

	public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		if (invokers == null || invokers.size() == 0) {
			return null;
		}
		if (invokers.size() == 1) {
			return invokers.get(0);
		}
		return doSelect(invokers, url, invocation);
	}

	protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation);

	protected int getWeight(Invoker<?> invokers, Invocation invocation) {
		int weight = invokers.getUrl().getMethodParameter(invocation.getMethodName(), Constants.WEIGHT_KEY,
				Constants.DEFAULT_WEIGHT);
		if (weight > 0) {
			long timestamp = invokers.getUrl().getParameter(Constants.TIMESTAMP_KEY, 0L);
			if (timestamp > 0l) {
				int uptime = (int) (System.currentTimeMillis() - timestamp);
				int warmup = invokers.getUrl().getParameter(Constants.WARMUP_KEY, Constants.DEFAULT_WARMUP);
				if (uptime > 0 && uptime < warmup) {
					weight = calculateWarmupWeight(uptime, warmup, weight);
				}
			}
		}
		return weight;
	}

	static int calculateWarmupWeight(int uptime, int warmup, int weight) {
		int ww = (int) ((float) uptime / ((float) warmup / (float) weight));
		return ww < 1 ? 1 : (ww > weight ? weight : ww);
	}
}
