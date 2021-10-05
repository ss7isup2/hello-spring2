package hello.hellospring.service;

import hello.hellospring.members.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    public MemberService(MemberRepository memberRepository, HttpSession httpSession) {
        this.memberRepository = memberRepository;
        this.httpSession = httpSession;
    }


    public Boolean join(Member member) { // JOIN
        memberRepository.save(member);
        return true;
    }


    public Optional<Member> findPass(Member member){ // 패스워드 찾기
        return memberRepository.findPass(member.getId(), member.getEmail());
    }


    public Optional<Member> checkLogin(Member member){ // 로그인 체크
        return memberRepository.loginCheckInfo(member.getId(),member.getPass());
    }



    public Optional<Member> findByOauth(String oauthName, String oauthKey){
        return memberRepository.findByOauth(oauthName, oauthKey);
    }



    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findByPublicKey(String publicKey){
        return memberRepository.findByPublicKey(publicKey);
    }


    public int changByPass(String id, String pass, String publicKey){ // PASS CHECK
        return memberRepository.changPass(id,pass, publicKey);
    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        Member member = (Member) httpSession.getAttribute("members");
//
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//
//            authorities.add(new SimpleGrantedAuthority("MEMBER");
//
//        return new User(member.getId(),member.getPass() ,authorities);
//    }
}
