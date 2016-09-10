package zbuer.com.lock.template;

import zbuer.com.lock.domain.OneByOne;

/**
 * 一个接一个执行模板接口<br>
 * 集群防并发
 *
 * @author baisu
 * @see OneByOne
 */
public interface OneByOneTemplate {

    /**
     * 一个接一个执行
     *
     * @param oneByOne 一个接一个实体
     * @param callback 回调,在这里写业务逻辑
     * @return 执行结果
     */
    <T> T execute(OneByOne oneByOne, Callback<T> callback);
}
