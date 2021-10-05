package hello.hellospring.members;

import hello.hellospring.controller.CookieController;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SessionMember {
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }
    private Member member;
}
