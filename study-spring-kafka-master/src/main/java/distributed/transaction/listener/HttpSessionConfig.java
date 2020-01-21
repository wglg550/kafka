package distributed.transaction.listener;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Description: redis-session设置
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/2/12
 */
@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)//单位为秒
@EnableRedisHttpSession
public class HttpSessionConfig {

}
