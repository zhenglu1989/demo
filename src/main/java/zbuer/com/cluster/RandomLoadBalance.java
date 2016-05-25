package zbuer.com.cluster;

import java.util.List;
import java.util.Random;

/**
 * random load balance
 *
 * @author buer
 * @since 16/5/25
 */
public class RandomLoadBalance extends AbstractLoadBalance {

	public static final String NAME = "random";

	private final Random random = new Random();

	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		int length = invokers.size(); //总个数
		int totalWeight = 0;
		boolean sameWeight = true;

		for (int i = 0; i < length; i++) {
			int weight = getWeight(invokers.get(i), invocation);
			totalWeight += weight;
			if (sameWeight && i > 0 && weight != getWeight(invokers.get(i - 1), invocation)) {
				sameWeight = false;
			}
		}
		if (totalWeight > 0 && !sameWeight) {
			int offset = random.nextInt(totalWeight);
			for (int i = 0; i < length; i++) {
				offset -= getWeight(invokers.get(i), invocation);
				if (offset < 0) {
					return invokers.get(i);
				}
			}
		}
		return invokers.get(random.nextInt(length));
	}
}
