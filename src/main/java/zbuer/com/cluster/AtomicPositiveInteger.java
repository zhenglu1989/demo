package zbuer.com.cluster;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author buer
 * @since 16/5/25
 */
public class AtomicPositiveInteger extends Number{
	private static final long serialVersionUID = 5945455783761121554L;

	private final AtomicInteger i;

	public AtomicPositiveInteger(int initialValue) {
		i = new AtomicInteger(initialValue);
	}

	public AtomicPositiveInteger() {
		i = new AtomicInteger();
	}

	public final int getAndIncrement(){
		for(;;){
			int current = i.get();
			int next = current >= Integer.MAX_VALUE ? 0 : current + 1;
			if(i.compareAndSet(current,next)){
				return current;
			}
		}
	}

	public final int getAndDecrement(){
		for(;;){
			int current = i.get();
			int next = (current <= 0 ? Integer.MAX_VALUE : current -1);
			if(i.compareAndSet(current,next)){
				return current;
			}
		}
	}

	public final int get(){
		return i.get();
	}

	public final void set(int newValue){
		if(newValue < 0){
			throw new IllegalArgumentException("new value "+ newValue + " < 0");
		}
		i.set(newValue);
	}


	@Override public int intValue() {
		return i.intValue();
	}

	@Override public long longValue() {
		return i.longValue();
	}

	@Override public float floatValue() {
		return i.floatValue();
	}

	@Override public double doubleValue() {
		return i.doubleValue();
	}

	public static void main(String[] args) {
		AtomicInteger	i = new AtomicInteger(2);
		int current = i.get();
		System.out.println(current);
		int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
		System.out.println(next);
		i.compareAndSet(current, next);
		System.out.println(i.get());
	}
}
