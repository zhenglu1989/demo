package zbuer.com.zookeeper;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author buer
 * @since 16/8/18
 */
@Component
public class ZkUtil {

	private Logger logger = LoggerFactory.getLogger(ZkUtil.class);

	@Resource
	private LeaderLatch leaderLatch;

	private ExecutorService leaderEventExecutor = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("leaderEventExecutor").build());

	public void selectLeaderAndDoAction(final LeaderCallBack leaderCallBack) throws Exception {

		LeaderLatchListener leaderLatchListener = new LeaderLatchListener() {
			public void isLeader() {
				leaderCallBack.doLeaderAction();
			}

			public void notLeader() {
				logger.info("Node-{} release leader", leaderLatch.getId());
				leaderCallBack.doNoLeaderAction();

			}
		};
		leaderLatch.addListener(leaderLatchListener, leaderEventExecutor);
		leaderLatch.start();
		if (!leaderLatch.hasLeadership()) {
			logger.info("leadership is not currently held by instance {}", leaderLatch.getId());
			leaderLatch.await();
		}

	}

}
