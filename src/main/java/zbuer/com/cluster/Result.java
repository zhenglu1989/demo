package zbuer.com.cluster;

/**
 * 返回结果抽象接口
 *
 * @author buer
 * @since 16/5/24
 */
public interface Result {

	/**
	 * get invoke result
	 *
	 * @return
	 */

	Object getValue();

	/**
	 * get exception
	 *
	 * @return
	 */

	Throwable getException();

	/**
	 * has exception
	 *
	 * @return
	 */

	boolean hasException();

	/**
	 * if(hasException()){
	 * throw getException();
	 * }
	 * return getValue();
	 *
	 * @return
	 * @throws Throwable
	 */

	Object recreate() throws Throwable;

}
