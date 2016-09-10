package zbuer.com.lock.template;

import org.springframework.stereotype.Component;

/**
 * Created by baisu on 16/9/6.
 */
@Component
public class ScheduleCallback implements Callback<Boolean> {

    public Boolean invoke() {
        //// TODO: 16/9/10
        return true;
    }
}
