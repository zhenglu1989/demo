package zbuer.com.lock.domain;

/**
 * 对应数据库表中的OneByOne记录
 * Created by baisu on 16/9/2.
 */
public class OneByOne {

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务Id 一般为帐号ID,和上面的bizType组成联合主键
     */
    private String bizId;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * @param bizType 业务类型
     * @param bizId   业务Id
     * @param method  方法名称
     */
    public OneByOne(String bizType, String bizId, String method) {
        this.bizType = bizType;
        this.bizId = bizId;
        this.method = method;
    }

    /**
     * @return the bizType
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * @param bizType the bizType to set
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * @return the bizId
     */
    public String getBizId() {
        return bizId;
    }

    /**
     * @param bizId the bizId to set
     */
    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}
