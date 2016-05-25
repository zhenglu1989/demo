package zbuer.com.cluster;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author buer
 * @since 16/5/24
 */
public final class URL implements Serializable {

	private static final long serialVersionUID = -6244030073792771134L;

	private final String protocol;

	private final String username;

	private final String password;

	private final String host;

	private final int port;

	private final String path;

	private final Map<String, String> parameters;

	private volatile transient Map<String, Number> numbers;

	public URL(String protocol, String username, String password, String host, int port, String path,
			Map<String, String> parameters) {
		this.protocol = null;
		this.username = null;
		this.password = null;
		this.host = null;
		this.port = 0;
		this.path = null;
		this.parameters = null;
	}

	public URL(String protocol, String host, int port) {
		this(protocol, null, null, host, port, null, (Map<String, String>) null);
	}

	public URL(String protocol, String host, int port, Map<String, String> parameters) {
		this(protocol, null, null, host, port, null, parameters);
	}

	public URL(String protocol, String host, int port, String path) {
		this(protocol, null, null, host, port, path, (Map<String, String>) null);
	}

	public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
		this(protocol, null, null, host, port, path, parameters);
	}

	public URL(String protocol, String username, String password, String host, int port, String path) {
		this(protocol, username, password, host, port, path, (Map<String, String>) null);
	}

	public int getMethodParameter(String method, String key, int defaultValue) {
		String methodKey = method + "." + key;
		Number n = getNumbers().get(methodKey);
		if (n != null) {
			return n.intValue();
		}
		String value = getMethodParameter(method, key);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		int result = Integer.parseInt(value);
		getNumbers().put(methodKey, result);
		return result;

	}

	public String getMethodParameter(String method, String key) {
		String value = parameters.get(method + "." + key);
		if (value == null || value.length() == 0) {
			return getParameter(key);
		}
		return value;
	}

	public String getParameter(String key) {
		String value = parameters.get(key);
		if (value == null || value.length() == 0) {
			value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
		}
		return value;
	}

	public Map<String, Number> getNumbers() {
		if (numbers == null) {
			numbers = new ConcurrentHashMap<String, Number>();
		}
		return numbers;
	}

	public long getParameter(String key, long defaultValue) {
		Number n = getNumbers().get(key);
		if (n != null) {
			return n.intValue();
		}
		String value = getParameter(key);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		long result = Long.parseLong(value);
		getNumbers().put(key, result);
		return result;
	}

	public int getParameter(String key, int defaultValue) {
		Number n = getNumbers().get(key);
		if (n != null) {
			return n.intValue();
		}
		String value = getParameter(key);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		int result = Integer.parseInt(value);
		getNumbers().put(key, result);
		return result;
	}
}
