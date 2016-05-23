package zbuer.com.exception;

/**
 * 连接池超时异常
 * @author buer
 * @since 16/5/23
 */
public class PoolTimeoutException extends RuntimeException {

	private static final long serialVersionUID = 2473990261293651645L;

	public PoolTimeoutException(String message) {
		super(message);
	}
}
