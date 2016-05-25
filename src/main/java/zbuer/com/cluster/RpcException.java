package zbuer.com.cluster;

/**
 * 远程调用异常
 * @author buer
 * @since 16/5/24
 */
public class RpcException extends Exception {

	public RpcException(String message) {
		super(message);
	}
}
