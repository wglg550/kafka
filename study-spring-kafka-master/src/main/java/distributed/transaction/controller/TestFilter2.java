package distributed.transaction.controller;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
public class TestFilter2 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("1111");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("2222");
        chain.doFilter(request, response);
        HttpServletRequest req =(HttpServletRequest) request;
        String path = req.getServletPath();
        System.out.println("TestFilter2 path:" + path);
        System.out.println("TestFilter2 session:" + req.getSession().getAttribute("aaa"));
    }

    @Override
    public void destroy() {
        System.out.println("3333");
    }
}
