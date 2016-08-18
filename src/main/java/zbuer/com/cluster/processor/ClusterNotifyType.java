package zbuer.com.cluster.processor;

import org.springframework.util.StringUtils;

/**
 * @author buer
 * @since 16/8/2
 */
public enum ClusterNotifyType {

	/**
	 * 规则变更
	 */
	spider_engine_rule_detail_alter,

	/**
	 * 用户规则变更
	 */
	spider_engine_rule_auth_alter,
	/**
	 * 恢复
	 */
	spider_engine_rule_restore;

	public static String[] all() {
		String[] all = new String[ClusterNotifyType.values().length];
		int i = 0;
		for (ClusterNotifyType v : ClusterNotifyType.values()) {
			all[i++] = v.name();
		}
		return all;
	}

	public static ClusterNotifyType parse(String type) {
		if (StringUtils.isEmpty(type)) {
			return null;
		}
		for (ClusterNotifyType v : ClusterNotifyType.values()) {
			if (v.name().equals(type)) {
				return v;
			}
		}
		return null;
	}
}
