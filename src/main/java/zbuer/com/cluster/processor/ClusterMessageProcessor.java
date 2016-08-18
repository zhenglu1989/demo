package zbuer.com.cluster.processor;

/**
 * 集群消息监听接口
 *
 * @author buer
 * @since 16/8/2
 */
public interface ClusterMessageProcessor<T> {

	/**
	 * 接收并处理消息
	 * @param channel
	 * @param message
	 */
	void onMessage(String channel, String message);

	/**
	 * 注册到处理器上
	 */

	void register();
}
