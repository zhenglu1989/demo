package zbuer.com.exception;

/**
 * @author buer
 * @since 16/5/23
 */
public class PoolInterruptedException extends RuntimeException {
	
	public PoolInterruptedException(String message, Throwable cause) {
		super(message, cause);
	}
}
