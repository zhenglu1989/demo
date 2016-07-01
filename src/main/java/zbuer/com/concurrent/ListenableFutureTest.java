package zbuer.com.concurrent;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 *   ### 异步转同步的实现
 * <pre>
 *     1. 异步执行设置结果完成标记为true，轮询Context中的任务状态
 *     2. 利用ExecutorCompletionService 利用get阻塞返回结果
 *     3. 利用闭锁来实现
 *     4. 利用guava ListeningExecutorService 响应式编程 最优雅
 * </pre>
 *
 * @author buer
 * @since 16/7/1
 */
public class ListenableFutureTest {

	public static void main(String[] args) {
		ListeningExecutorService pool = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(20));
		ListenableFuture future = pool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				Thread.sleep(200);
				return "Task done.........";
			}
		});
		System.out.println("do other things..........");

		Futures.addCallback(future, new FutureCallback() {
			public void onSuccess(Object result) {
				System.out.println("result : " + result);

			}

			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		});
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("done .....");
	}
}
