package zbuer.com.pool;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author buer
 * @since 16/5/20
 */
public interface Pool<T> {

	T get();

	T get(long timeout, TimeUnit timeUnit);

	void release(T t);

	void close();

	void release(T t, boolean discard);
}
