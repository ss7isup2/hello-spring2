package hello.hellospring.members;


import hello.hellospring.controller.CookieController;
import hello.hellospring.encryption.SHA;
import hello.hellospring.mail.MailSend;
import hello.hellospring.oauth.OAuthAttributes;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("members")
@SessionAttributes("testSessionData")
public class MemberController {
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final MailSend mail;
    private final SHA sha;
    private final CookieController cookieController;
    @PostMapping("createMember")
    @ResponseBody
    public Object create(Member member) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("MemberController ==> create");
        System.out.println("member = "+member.getId());

        try{
            if(httpSession.getAttribute("members").getClass().getSimpleName().equals("OAuthAttributes")){
                OAuthAttributes oauthData = (OAuthAttributes) httpSession.getAttribute("members");
                member.setOauthName(oauthData.getOauthName());
                member.setOauthKey(oauthData.getOauthKey());
            }

        }catch (Exception e){
            System.out.println(e);
        }


        // member Setting
        MemberEncryption memberEncryption = new MemberEncryption();
        member = memberEncryption.setRSA(member);  // RSA Setting
        member.setPass(sha.encoding(member.getPass())); // password encoding
        member.setRole("ROLE_USER");

        System.out.println("member id = "+member.getId());
        System.out.println("member pass = "+ member.getPass());
        System.out.println("member email = "+member.getEmail());
        System.out.println("member publicKey = "+member.getPublicKey());
        System.out.println("member privateKey =" +member.getPrivateKey());
        System.out.println("member setOauthName =" +member.getOauthName());
        System.out.println("member setOauthKey =" +member.getOauthKey());

        Boolean returnData = false;

        Map<String, Object> data = new HashMap<String, Object>();


        try{
            returnData =  memberService.join(member);
            data.put("function", "susses");

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            data.put("function", "error");

            if(e.getMessage().contains("constraint [email]")){
                data.put("value","email");

            }else if(e.getMessage().contains("constraint [id]")) {
                data.put("value","id");
            }
        }
        System.out.println("end");
        return data;
    }


    // login
    @PostMapping("login")
    public String login(Member member, Model model, HttpServletRequest request, HttpServletResponse response, String id) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try{
            System.out.println("member id = "+member.getId());
            System.out.println("member pass = "+member.getPass());
            // pass encryption
            member.setPass(sha.encoding(member.getPass()));
            Optional<Member> result = memberService.checkLogin(member);

            if(result.isPresent()){
                httpSession.setMaxInactiveInterval(3600);
                httpSession.setAttribute("members", member);

                // AutoLogin Check
                if (request.getParameter("autoLogin") != null){
                    System.out.println("autoLogin");
                    response.addCookie(cookieController.setCookie("autoLogin",result.get().getPublicKey(),60*60*24*365));
                }else{
                    response.addCookie(cookieController.setCookie("autoLogin",null,0)) ;
                }

                response.addCookie(cookieController.setCookie("members",result.get().getPublicKey(),60*60*24*365));



                model.addAttribute("testSessionData", member);
                System.out.println("success");
                return "redirect:/";

            }else{
                System.out.println("else");
                model.addAttribute("value","아이디 혹은 패스워드가 틀렸습니다.");
                return "redirect:/members/login";
            }

        }catch (Exception e){
            System.out.println(e);
            model.addAttribute("error","예상치 못한 오류 발생");
            return "redirect:/members/login";
        }

    }

    @PostMapping("findPass")
    public String findPass(Member member) throws MessagingException {
        Optional<Member> result = memberService.findPass(member);

        try {
            mail.findPass(member.getEmail(),member.getId(), result.get().getPublicKey());
            System.out.println("메일 패스워드 발송 되었습니다.");
            return "redirect:/";

        }catch (Exception e){
            System.out.println("아이디 또는 이메일이 잘 못 되었습니다.");
            return "members/findPass";
        }
    }

    // mail call back
    @PostMapping("resultPass")
    public String findPassForm(String key){
        System.out.println("key = "+key);
        httpSession.setAttribute("key",key);
        return "members/changPass";
    }



    @PostMapping("changPass")
    public String changPass(String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("changPass");
        Member member = new Member();
        member.setPublicKey((String) httpSession.getAttribute("key"));

        System.out.println(member.getPass());
        System.out.println(member.getPublicKey());
        Optional<Member> result = memberService.findByPublicKey(member.getPublicKey());
        System.out.println("result id = "+result.get().getId());
        if(result.isPresent()){
            member.setId(result.get().getId());
            MemberEncryption memberEncryption = new MemberEncryption();
            member = memberEncryption.setRSA(member);



            System.out.println(member.getId());
            System.out.println(member.getPass());

            memberService.changByPass(member.getId(), member.getPass(), member.getPublicKey());
            return "redirect:/";

        }else{
            return "error";
        }
    }


    @GetMapping("logout")
    public String logout(HttpServletResponse response){
        System.out.println("logout");
        httpSession.invalidate();
        Cookie cookie = cookieController.setCookie("autoLogin",null,0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    /* find pass */
    @GetMapping("findPass")
    public String findPassForm(){
        System.out.println("findpass");
        return "members/findPass";
    }


    /* create user */
    @GetMapping("new")
    public String createForm(Model model) {
        return "members/createMemberForm";
    }


    // member List
    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }


    /* login */
    @GetMapping("login")
    public String loginForm(Model model, HttpServletRequest request){
        if (httpSession.getAttribute("autoLogin") != null){
            httpSession.setAttribute("members",httpSession.getAttribute("autoLogin"));
            return "redirect:"+(String) request.getHeader("REFERER");
        }else{
            return "members/memberLogin";
        }
    }

    @PostMapping("mailCheck")
    @ResponseBody
    public String sendPinNum(String email) throws MessagingException {
        return mail.sendPin(email); // pin 전달
    }
}
