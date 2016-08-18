package zbuer.com.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Leader factorybean
 * @author buer
 * @since 16/8/18
 */
public class LeaderLatchFactoryBean implements FactoryBean<LeaderLatch>, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(LeaderLatchFactoryBean.class);

	private CuratorFramework curatorFramework;

	private String latchPth;

	private String connectionStr;

	private LeaderLatch leaderLatch;

	public LeaderLatch getObject() throws Exception {
		return leaderLatch;
	}

	public Class<?> getObjectType() {
		return LeaderLatch.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		curatorFramework = CuratorFrameworkFactory
				.newClient(connectionStr, new ExponentialBackoffRetry(1000, 3));
		leaderLatch = new LeaderLatch(curatorFramework, latchPth);
	}

	public void destroy() throws Exception {
		logger.info("remove this instance from the leadership election.");
		leaderLatch.close();

	}

	public void setLatchPth(String latchPth) {
		this.latchPth = latchPth;
	}

	public void setConnectionStr(String connectionStr) {
		this.connectionStr = connectionStr;
	}
}
