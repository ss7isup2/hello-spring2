package hello.hellospring.maching;


import hello.hellospring.members.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;



//매칭 요청자 member
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MsMembers implements Serializable {
    public static final long serialVersionUID = -4404892818759343708L;
    private String function = "MatchWaitInformation"; // 매칭 요청  function
    private String myId; // id
    private String myHardness; // 경도
    private String myLatitude; // 위도
    private String myWantStreet; // 원하는 거리
    private String machingYn = "N";

    // 거리
    private String DFO;

}
