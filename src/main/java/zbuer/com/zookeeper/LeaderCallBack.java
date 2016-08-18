package zbuer.com.zookeeper;

/**
 * 选举callback
 *
 * @author buer
 * @since 16/8/18
 */
public interface LeaderCallBack {

	/**
	 * if this is a leader,do action
	 */
	void doLeaderAction();

	/**
	 * if this is not a leader,do action
	 */

	void doNoLeaderAction();

}
