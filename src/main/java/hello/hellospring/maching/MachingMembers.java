package hello.hellospring.maching;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


// 매칭 수신자 용
@Getter
@Setter
public class MachingMembers implements Serializable {
    private String function = "MatchingCompleted";  //매칭 확인 function
    private String myId;
    private String myHardness;
    private String myLatitude;

    private String msId;
    private String msHardness;
    private String msLatitude;

    //상대방과의 거리
    private String DFO;
}


