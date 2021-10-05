package hello.hellospring.oauth;

import hello.hellospring.members.Member;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;


@RequiredArgsConstructor
@Service
@SuppressWarnings("unchecked") // 자바 5부터 컬렉션 사용시 타입을 지정하지 않으면 uses unchecked or unsafe operations. 에러 발생
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final HttpSession httpSession;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("=============CustomOAuth2UserService.loadUser");
        httpSession.removeAttribute("oauthMember");
        OAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        String oauthName = attributes.getOauthName();
        String oauthKey = attributes.getOauthKey();
        Optional<Member> members = memberService.findByOauth(oauthName,oauthKey);

        if(members.isPresent()){
            System.out.println("member is not null");
        }else{
            System.out.println("member is null");
        }

        httpSession.setAttribute("members",members.isPresent()?members.get():attributes);
        System.out.println("CustomOAuth2UserService.end");
        return oAuth2User;
    }


}