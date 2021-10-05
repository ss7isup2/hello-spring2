package hello.hellospring.controller;


import hello.hellospring.members.Member;
import hello.hellospring.encryption.SHA;
import hello.hellospring.mail.MailSend;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class PostController {
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final MailSend mail;
    private final SHA sha;
    private final CookieController cookieController;
    private Member member;


    @PostMapping("loginCheck")
    public String loginCheck(HttpServletRequest request) {

        System.out.println(request.getRequestURL());
        Member member = (Member) httpSession.getAttribute("members");

        if (member != null) {
//            Map<String, Object> members = new HashMap<String, Object>();
//            members.put("member.id",member.getId());
//            System.out.println("member.id = "+members.get("member.id"));LLL
            return member.getId();
        } else {
            return null;
        }
    }

    @PostMapping("data")
    @ResponseBody
    public String test() {
        member = (Member) httpSession.getAttribute("members");
        if (member == null) {
            System.out.println("null");
            return "redirect:/";
        } else {
            return member.getId();
        }
    }
}
