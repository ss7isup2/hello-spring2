package hello.hellospring.controller;

import hello.hellospring.members.Member;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;


@Component
@Transactional
public class CookieController {
    public String findCookie(String name, HttpServletRequest request){
        Cookie[] getCookie = request.getCookies(); // 모든 쿠키 가져오기
        if(getCookie != null) { // 만약 쿠키가 없으면 쿠키 생성
            for (Cookie cookie : getCookie) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Cookie setCookie(String name, String value, int time){
        Cookie cookie = new Cookie(name, value); // 쿠키 이름을 name으로 생성
        cookie.setMaxAge(time); // 기간을 하루로 지정(60초 * 60분 * 24시간)
        cookie.setPath("/");
        return cookie;
    }
}

