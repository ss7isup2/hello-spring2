package hello.hellospring.repository;

import hello.hellospring.members.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Repository
public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override // 회원가입
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        List<Member> result = em.createQuery("select m from Member m where m.id = :id",
                Member.class).setParameter("id",id).getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findPass(String id, String email) {
        List<Member> result =
                em.createQuery("select m from Member m where m.email = :email and m.id = :id",Member.class)
                        .setParameter("email",email)
                        .setParameter("id",id)
                        .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByPublicKey(String publicKey) {
        List<Member> result =
                em.createQuery("select m from Member m where m.publicKey = :publicKey",Member.class)
                        .setParameter("publicKey",publicKey)
                        .getResultList();
        return result.stream().findAny();
    }


    @Override
    public Optional<Member> findByOauth(String oauthName, String oauthKey) {
        System.out.println("findByOauth.oauthName "+oauthName);
        List<Member> result =
                em.createQuery("select m from Member m where m.oauthName = :oauthName and m.oauthKey = :oauthKey",Member.class)
                        .setParameter("oauthName",oauthName)
                        .setParameter("oauthKey",oauthKey)
                        .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> loginCheckInfo(String id, String pass) {
        List<Member> result =
                em.createQuery("select m from Member m where m.id = :id and m.pass = :pass",Member.class)
                        .setParameter("id",id)
                        .setParameter("pass",pass)
                        .getResultList();
        return result.stream().findAny();
    }

    @Override
    public int changPass(String id, String pass, String publicKey) {
        return 0;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
