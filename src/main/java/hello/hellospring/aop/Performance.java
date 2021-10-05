package hello.hellospring.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Aspect
@Component
@RequiredArgsConstructor
public class Performance {
    private final HttpSession httpSession;

    @Before("execution(* hello.hellospring.maching.MatchingController.*(..))")
    public Object beforeMethod(){
        System.out.println("Performance.beforeMethod");

        Object result = new ModelAndView("redirect:/members/new");
        return result;

    }



    // 종료 후에 실행될 AOP 메소드
    @After("within(hello.hellospring.controller.PostController)")
    public void afterMethod() {
        System.out.println("Performance.메소드 종료");
        return;
    }
}
