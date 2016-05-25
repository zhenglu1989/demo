package zbuer.com.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 轮询负载均衡
 * <pre>
 *     筛选出比当前权重大的invokers,对length取模，获取最终的invoker
 *     算法步奏:
 *     1）获取轮询key  服务名+方法名
 *          获取可供调用的invokers个数length
 *          设置最大权重的默认值maxWeight=0
 *          设置最小权重的默认值minWeight=Integer.MAX_VALUE
 *     2）遍历所有Inokers，比较出得出maxWeight和minWeight
 *     3）如果权重是不一样的
 *          根据key获取自增序列
 *          自增序列加一与最大权重取模默认得到currentWeigth
 *          遍历所有invokers筛选出大于currentWeight的invokers
 *          设置可供调用的invokers的个数length
 *     4）自增序列加一并与length取模，从invokers获取invoke
 *
 * </pre>
 *
 * @author buer
 * @since 16/5/25
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

	public static final String NAME = "roundrobin";

	private final ConcurrentMap<String, AtomicPositiveInteger> sequences = new ConcurrentHashMap<String, AtomicPositiveInteger>();

	private final ConcurrentMap<String, AtomicPositiveInteger> weightSequences = new ConcurrentHashMap<String, AtomicPositiveInteger>();

	@Override protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		String key = invokers.get(0).getUrl().getServiceKey() + "." + invocation.getMethodName();
		int length = invokers.size();
		int maxWeight = 0;
		int minWeight = Integer.MAX_VALUE; // 最小权重
		for (int i = 0; i < length; i++) {
			int weight = getWeight(invokers.get(i), invocation);
			maxWeight = Math.max(maxWeight, weight);
			minWeight = Math.min(minWeight, weight);
		}
		if (maxWeight > 0 && minWeight < maxWeight) {
			AtomicPositiveInteger weightSequence = weightSequences.get(key);
			if (weightSequence == null) {
				weightSequences.putIfAbsent(key, new AtomicPositiveInteger());
				weightSequence = weightSequences.get(key);
			}
			int currentWeight = weightSequence.getAndIncrement() % maxWeight;
			List<Invoker<T>> weightInvokers = new ArrayList<Invoker<T>>();
			for (Invoker<T> invoker : invokers) {
				if (getWeight(invoker, invocation) > currentWeight) {
					weightInvokers.add(invoker);
				}
			}
			int weightLength = weightInvokers.size();
			if (weightLength == 1) {
				return weightInvokers.get(0);
			} else if (weightLength > 1) {
				invokers = weightInvokers;
				length = invokers.size();
			}
		}
		AtomicPositiveInteger sequence = sequences.get(key);
		if (sequence == null) {
			sequences.putIfAbsent(key, new AtomicPositiveInteger());
			sequence = sequences.get(key);
		}

		return invokers.get(sequence.getAndIncrement() % length);
	}
}
