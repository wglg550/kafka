package distributed.transaction.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class FirstInterceptor implements HandlerInterceptor {

    /**
     * 在DispatcherServlet之前执行
     */
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        System.out.println("************BaseInterceptor preHandle executed**********");
//        arg0.getSession().setAttribute("firstTest1", "firstTest1");
        return true;
    }

    /**
     * 在controller执行之后的DispatcherServlet之后执行
     */
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        System.out.println("************BaseInterceptor postHandle executed**********");
    }

    /**
     * 在页面渲染完成返回给客户端之前执行
     */
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        System.out.println("************BaseInterceptor afterCompletion executed**********");
//		Thread.sleep(10000);
    }
}
