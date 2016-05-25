package zbuer.com.cluster;

/**
 * 对应节点的抽象
 *
 * @author buer
 * @since 16/5/24
 */
public interface Node {

	/**
	 * get url
	 *
	 * @return
	 */
	URL getUrl();

	/**
	 * 节点是否可用
	 *
	 * @return
	 */
	boolean isAvailable();

	/**
	 * destory
	 */

	void destory();
}
