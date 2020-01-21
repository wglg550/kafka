package distributed.transaction.listener;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Description: session配置
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/2/12
 */
public class MySessionContext {
    private static HashMap mymap = new HashMap();

    public static synchronized void AddSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    public static synchronized void DelSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
        }
    }

    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null)
            return null;
        return (HttpSession) mymap.get(session_id);
    }
}
