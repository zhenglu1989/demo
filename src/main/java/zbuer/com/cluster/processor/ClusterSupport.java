package zbuer.com.cluster.processor;

/**
 * 集群支持
 *
 * @author buer
 * @since 16/8/2
 */
public interface ClusterSupport {

	/**
	 * 集群通知
	 * @param channel
	 * @param body
	 * @return
	 */

	String notifyCluster(ClusterNotifyType channel, Object body);
}
