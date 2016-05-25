package zbuer.com.cluster;

/**
 * 执行器的抽象
 * @author buer
 * @since 16/5/24
 */
public interface Invoker<T> extends Node {

	/**
	 * get service interface
	 * @return
	 */

	Class<T> getInterface();

	/**
	 * invoke
	 */

	Result invoke(Invocation invocation) throws RpcException;



}
