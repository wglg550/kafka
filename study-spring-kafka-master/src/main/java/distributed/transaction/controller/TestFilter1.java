package distributed.transaction.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TestFilter1 extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //在DispatcherServlet之前执行
        System.out.println("############TestFilter1 doFilterInternal executed############");
        String path = request.getServletPath();
        if (path.indexOf("courseware") != -1) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                String json = "{123}";
                writer.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(writer);
            }
        }
        filterChain.doFilter(request, response);
        System.out.println("TestFilter1 path:" + request.getServletPath());
//        System.out.println("TestFilter1 session:" + request.getSession(false).getAttribute("aaa"));
        //在视图页面返回给客户端之前执行，但是执行顺序在Interceptor之后
        System.out.println("############TestFilter1 doFilter after############");
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
    }
}
