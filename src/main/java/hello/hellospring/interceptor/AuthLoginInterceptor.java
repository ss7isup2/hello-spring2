package hello.hellospring.interceptor;

import hello.hellospring.members.Member;
import hello.hellospring.members.SessionMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthLoginInterceptor extends HandlerInterceptorAdapter {






    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor ==> preHandle");
        System.out.println(request.getRequestURL());
        if (request.getSession().getAttribute("members") == null) {
            System.out.println("members is null");
           ModelAndView mav = new ModelAndView("redirect:/members/login");
           throw new ModelAndViewDefiningException(mav);

        }else{
            return true;
        }



    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("Interceptor > postHandle");
//        log.info("Interceptor > postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
//        log.info("Interceptor > afterCompletion" );
    }
}