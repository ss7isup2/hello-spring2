package hello.hellospring.members;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Member implements Serializable {

    @Id
    private String id;
    private String pass;
    private String name;
    private String email;
    private String role;
    private String oauthName;
    private String oauthKey;
    private String privateKey;
    private String publicKey;
}