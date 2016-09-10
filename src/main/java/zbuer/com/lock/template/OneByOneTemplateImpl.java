package zbuer.com.lock.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import zbuer.com.lock.domain.OneByOne;

/**
 * 一个接着一个处理实现<br>
 * 集群防并发
 *
 * @author baisu
 * @see OneByOneTemplate, OneByOne
 */
@Component
public class OneByOneTemplateImpl implements OneByOneTemplate {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(OneByOneTemplateImpl.class);

    /**
     * 插入结果
     */
    private ThreadLocal<Boolean> insertResult = new ThreadLocal<Boolean>();

    /**
     * 业务描述
     */
    private ThreadLocal<String> description = new ThreadLocal<String>();

    @Autowired
    private OneByOneDao oneByOneDao;

    public <T> T execute(OneByOne oneByOne, Callback<T> callback) {
        StringBuilder builder = new StringBuilder(64);
        builder.append(oneByOne.getBizType()).append("-").append(oneByOne.getBizId()).append("-")
                .append(oneByOne.getMethod());
        this.description.set(builder.toString());

        try {
            this.beforeInvoke(oneByOne); // 前处理

            return callback.invoke(); // 业务逻辑
        } finally {
            this.afterInvoke(oneByOne); // 后处理
        }
    }

    /**
     * 前处理<br>
     * 尝试插入处理记录:加锁
     *
     * @param oneByOne 一个接一个业务实体
     */
    private void beforeInvoke(final OneByOne oneByOne) {
        try {
            insertResult.set(Boolean.TRUE);
            // 插入处理记录
            oneByOneDao.addOneByOne(oneByOne);
        } catch (BadSqlGrammarException e) { // SQL语法错误或表不存在: 直接执行业务逻辑,不抛异常
            insertResult.set(Boolean.FALSE);

            if (logger.isWarnEnabled()) {
                logger.warn(description.get() + "插处理记录失败", e);
            }
        } catch (Exception e) {
            insertResult.set(Boolean.FALSE);

            if (logger.isWarnEnabled()) {
                logger.warn(description.get() + "插处理记录失败", e);
            }
            logger.info(description.get() + "业务正在处理中");

        }

    }

    /**
     * 后处理<br>
     * 删除处理记录:释放锁
     *
     * @param oneByOne 一个接一个业务实体
     */
    private void afterInvoke(final OneByOne oneByOne) {
        // beforeInvoke时插入失败,不需删除处理记录
        Boolean bInserted = insertResult.get();
        if (bInserted != null && !bInserted) { // 嵌套OneByOne,内层会清空线程变量,所以为null时也要删处理记录
            logger.warn("{},bInserted:{}", description.get(), bInserted);
        }

        try {
            // 删除处理记录
            oneByOneDao.delOneByOne(oneByOne);
        } catch (Exception t) {
            logger.error(description.get() + "删处理记录失败", t);
        } finally {
            // 清理
            description.set(null);
            insertResult.set(null);
        }

    }

}
