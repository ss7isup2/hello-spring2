package hello.hellospring.maching;



import hello.hellospring.members.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;



@Controller
@RequiredArgsConstructor
@RequestMapping("matching")
public class MatchingController {




    @GetMapping("enter/locationMatching")
    public String locationMatching(Model model, HttpServletRequest req, HttpServletResponse res){
        System.out.println("MatchingController ==> locationMatching");

        return "matching/locationMatching";
    }
}
