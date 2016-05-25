package zbuer.com.cluster;

/**
 *  远程调用的一个抽象
 * @author buer
 * @since 16/5/24
 */
public interface Invocation {

	/**
	 * 获取method name
	 * @return
	 */
	String getMethodName();

	/**
	 * get parameter types
	 * @return
	 */
	Class<?> getParmeterTypes();

	/**
	 * get arguments
	 * @return
	 */
	Object[] getArguments();

}
