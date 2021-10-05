package hello.hellospring.redis;


import hello.hellospring.maching.MachingMembers;
import hello.hellospring.maching.MsMembers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisRepository {


    private final RedisTemplate<String, Object> redisTemplate; // redis object 용
    private HashOperations<String, String, MsMembers> MS_MEMBER_Template; // 매칭 대기자 전용
    private HashOperations<String, String, MachingMembers> MachingMember_template; //매칭 완료자 전용

    private final String MS_TOPIC = "MATCHING_MS_MEMBERS"; //  매칭 대기자 전용 토픽
    private final String MACHING_TOPIC = "MACHING_S_MEMBERS"; // 매칭 완료자 전용 토픽


    @PostConstruct
    private void init() {
        System.out.println("ChatRoomRepository.init");
        MS_MEMBER_Template = redisTemplate.opsForHash();
        MachingMember_template = redisTemplate.opsForHash();
    }



    public Set<String> getMsSubKey(){
        System.out.println("RedisRepository ==> getMsSubKey");
        return MS_MEMBER_Template.keys(MS_TOPIC);
    }


    // 현재 매칭 중인 사람 get
    public MachingMembers getMachingMembers(String subKey){
        System.out.println("RedisRepository ==> getMachingMembers");
        return MachingMember_template.get(MACHING_TOPIC, subKey);
    }


    // 현재 매칭 중인 사람 set //  아직 상요 안했음
    public void setMachingMembers(String subKey, MachingMembers value){
        System.out.println("RedisRepository ==> setMachingMembers");
        MachingMember_template.put(MACHING_TOPIC, subKey, value);
    }



    // 매칭 대기자 get
    public MsMembers getMS_MEMBER(String subKey){
        System.out.println("RedisRepository ==> getMS_MEMBER");
        System.out.println("machingMembers.getMsId() = "+subKey);
        return MS_MEMBER_Template.get(MS_TOPIC,subKey);
    }


    // 매칭 대기자 set
    public void setMS_MEMBER_Template(String subKey, MsMembers value){
        System.out.println("RedisRepository ==> setMS_MEMBER_Template");
        MS_MEMBER_Template.put(MS_TOPIC,subKey,value);
    }
}
