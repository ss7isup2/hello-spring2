package hello.hellospring.members;

import hello.hellospring.encryption.RSA;
import hello.hellospring.members.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MemberEncryption {
    public  Member setRSA(Member member){
        HashMap<String, String> rsaKeyPair = RSA.createKeypairAsString();
        member.setPrivateKey(rsaKeyPair.get("privateKey"));
        member.setPublicKey(rsaKeyPair.get("publicKey"));
        return member;
    }
}
