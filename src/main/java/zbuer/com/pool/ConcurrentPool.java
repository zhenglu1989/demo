package zbuer.com.pool;

import zbuer.com.exception.PoolInternalException;
import zbuer.com.exception.PoolInterruptedException;
import zbuer.com.exception.PoolTimeoutException;

import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 并发连接池的实现
 *
 * @author buer
 * @since 16/5/20
 */
public class ConcurrentPool<T> implements Pool<T> {

	private final int maxSize;

	private final ItemFactory<T> itemFactory;

	private final Deque<T> available = new ConcurrentLinkedDeque<T>();

	private final Semaphore permits;

	private volatile boolean closed;

	public ConcurrentPool(int maxSize, ItemFactory<T> itemFactory) {
		this.maxSize = maxSize;
		this.itemFactory = itemFactory;
		permits = new Semaphore(maxSize, true);
	}

	public interface ItemFactory<T> {

		T create(final boolean initialize);

		void close();

		boolean shouldPrune(T t);
	}

	public T get() {
		return get(-1, TimeUnit.MILLISECONDS);
	}

	public T get(final long timeout, final TimeUnit timeUnit) {
		if (closed) {
			throw new IllegalStateException("the pool is closed");
		}
		if (!acquirePermit(timeout, timeUnit)) {
			throw new PoolTimeoutException(
					String.format("Timeout waiting for a pooled item after %d %s", timeout, timeUnit));
		}
		T t = available.pollLast();
		if (t == null) {
			t = createNewAndReleasePermitIfFailure(false);
		}
		return t;
	}

	protected T createNewAndReleasePermitIfFailure(final boolean initialize) {
		try {
			T newMember = itemFactory.create(initialize);
			if (newMember == null) {
				throw new PoolInternalException("The factory for the pool created a null item");
			}
			return newMember;
		} catch (PoolInternalException e) {
			permits.release();
			throw e;
		}
	}

	protected boolean acquirePermit(final long timeout, final TimeUnit timeUnit) {
		try {
			if (closed) {
				return false;
			} else if (timeout >= 0) {
				return permits.tryAcquire(timeout, timeUnit);
			}else {
				permits.acquire();
				return true;
			}
		} catch (InterruptedException e) {
			throw new PoolInterruptedException("Interrupted acquiring a permit to retrieve an item from the pool ", e);

		}
	}

	public void release(T t) {
		release(t, false);
	}

	public int getMaxSize(){
		return maxSize;
	}

	public int getInUserCount(){
		return maxSize - permits.availablePermits();
	}

	public int getAvailableCount(){
		return available.size();
	}

	/**
	 * 关闭所有连接池中可用连接
	 */
	public void close() {
		closed = true;
		Iterator<T> iterator = available.iterator();
		while (iterator.hasNext()) {
			T t = iterator.next();
			close(t);
			iterator.remove();
		}

	}

	public void release(T t, boolean discard) {
		if (t == null) {
			throw new IllegalArgumentException("can not return a null item to the pool");
		}
		if (closed) {
			close(t);
			return;
		}
		if (discard) {
			close(t);
		} else {
			available.addLast(t);
		}
		releasePermit();

	}

	protected void releasePermit() {
		permits.release();
	}

	private void close(final T t) {
		try {
			itemFactory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
