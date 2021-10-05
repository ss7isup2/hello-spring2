package hello.hellospring.repository;

import hello.hellospring.members.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository {

    // save, findById 한세트
    Member save(Member member); // 회원 가입
    Optional<Member> findById(String id); // 중복 ID 확인



    // findPass , findByPublicKey 한세트
    // 패스워드 찾기
    Optional<Member> findPass(String id, String email); // id,이메일로 패스워드 찾기
    // 공개키로 유저 정보 확인( password 찾기 용)
    Optional<Member> findByPublicKey(String publicKey); // 공개키로 유저 정보 확인


    // 패스워드 변경
    int changPass(String id, String pass, String publicKey);


    // oauth key check
    Optional<Member> findByOauth(String oauthName, String oauthKey); // oauth 확인
    // login
    Optional<Member> loginCheckInfo(String id, String pass); // 사용자 확인




    List<Member> findAll();
}
