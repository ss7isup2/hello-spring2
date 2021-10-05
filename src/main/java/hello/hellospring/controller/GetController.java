package hello.hellospring.controller;


import hello.hellospring.members.Member;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor //
public class GetController {

    private final MemberService memberService;
    private final HttpSession httpSession;
    private final CookieController cookieController;
    private Member member;



    /* oauth check */
    @GetMapping("oauthCheck")
    public String oauthCheck(HttpServletRequest request){
        System.out.println("oauthCheck");
        try{
            String data = httpSession.getAttribute("members").getClass().getSimpleName();
            System.out.println("data = "+data);
            if(data.equals("Member")){
                return "redirect:/";
            }else{
                return "redirect:/members/new";
            }
        }catch (Exception e){
            return "redirect:/members/new";
        }
    }

    @GetMapping("test")
    public String test(){
        return "redirect:/members/new";
    }


    /* main */
    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            member = (Member) httpSession.getAttribute("members");
            if (member != null){
                model.addAttribute("members",member);
            }else{
                memberService.findByPublicKey(
                        cookieController.findCookie("autoLogin",request))
                        .ifPresent(value -> model.addAttribute("members", value));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        session.setAttribute("user_id", "test!!");


        System.out.println("home");
        return "home.html";
    }



}


