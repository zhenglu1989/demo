package zbuer.com.cluster;

import java.util.List;
import java.util.Random;

/**
 * random load balance
 * <pre>
 *     1）获取所有invokers的个数
 *     2）遍历所有Invokers, 获取计算每个invokers的权重，并把权重累计加起来
 *          每相邻的两个invoker比较他们的权重是否一样，有一个不一样说明权重不均等
 *     3）总权重大于零且权重不均等的情况下
 *          按总权重获取随机数offset = random.netx(totalWeight)
 *          遍历invokers确定随机数offset落在哪个片段(invoker上)
 *     4）权重相同或者总权重为0， 根据invokers个数均等选择
 * </pre>
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
