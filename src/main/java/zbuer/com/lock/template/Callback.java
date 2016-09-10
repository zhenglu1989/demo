package zbuer.com.lock.template;

/**
 * 回调
 * Created by baisu on 16/9/2.
 */
public interface Callback<T> {
    /**
     * 业务调用
     *
     * @return 结果
     */
    T invoke();
}
