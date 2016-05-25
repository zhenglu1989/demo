package zbuer.com.cluster;

import java.util.List;

/**
 * 负载均衡抽象接口
 * @author buer
 * @since 16/5/24
 */
public interface LoadBalance {

	/**
	 * select one invoker in list
	 * @param invokers
	 * @param url
	 * @param invocation
	 * @param <T>
	 * @return
	 */
	<T> Invoker<T> select(List<Invoker<T>> invokers,URL url,Invocation invocation);

}
