package zbuer.com.lock.template;


import zbuer.com.lock.domain.OneByOne;

/**
 * Created by baisu on 16/9/6.
 */
public interface OneByOneDao {

    /**
     * 增加一条记录
     *
     * @param oneByOne
     */
    public void addOneByOne(OneByOne oneByOne);

    /**
     * 删除一个记录
     *
     * @param oneByOne
     */
    public void delOneByOne(OneByOne oneByOne);
}
