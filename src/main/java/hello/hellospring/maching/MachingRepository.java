package hello.hellospring.maching;


import com.fasterxml.jackson.core.JsonProcessingException;
import hello.hellospring.kafka.Producer;
import hello.hellospring.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class MachingRepository {


    private final RedisRepository redisRepository;
    private final DistanceCalculation distanceCalculation;
    private final SimpMessagingTemplate messagingTemplate;
    private final Producer producer;
    private final static String MATCHING_MEMBERS = "MATCHING_MEMBERS";








    public Boolean MachingData(MachingMembers machingMembers) throws JsonProcessingException {
        System.out.println("MachingRepository ==> MachingData");
        MsMembers msMembers = redisRepository.getMS_MEMBER(machingMembers.getMsId());

        if(msMembers.getMachingYn().equals("N") || msMembers.getMachingYn().equals("n")){
            System.out.println("true");
            msMembers.setMachingYn("Y");
            redisRepository.setMS_MEMBER_Template(machingMembers.getMsId(), msMembers); // 레디스에 저장
            producer.sendMessage(MATCHING_MEMBERS, machingMembers); // maching 내용
//            messagingTemplate.convertAndSend("/sub/matching/location/"+machingMembers.getMyId(), msMembers);
            return true;
        }else{
            System.out.println("false");;
            machingMembers.setFunction("MatchingFail");
            producer.sendMessage(MATCHING_MEMBERS, machingMembers); // maching 내용
//            messagingTemplate.convertAndSend("/sub/matching/location/"+machingMembers.getMyId(), machingMembers);
//            System.out.println("실패!!");
            return false;
        }
    }


    public void kafkaListenerInSendMachingMessage(MachingMembers members) {
        System.out.println("MachingRepository ==> kafkaListenerInSendMachingMessage");
        Set<String> IDS = redisRepository.getMsSubKey();
        System.out.println("IDS size = "+IDS.size());
        Iterator<String> key = IDS.iterator();
        if (members.getFunction().equals("MatchingCompleted")){
            while (key.hasNext()) {
                String name = key.next();
                messagingTemplate.convertAndSend("/sub/matching/location/"+name, members);
            }
        }else if(members.getFunction().equals("MatchingFail")) {
            messagingTemplate.convertAndSend("/sub/matching/location/"+members.getMyId(), members);
        }

    }
    //  데이터 감지 시 정보 전달
    public void kafkaListenerInSendMsMessage(MsMembers data){
        System.out.println("MachingRepository ==> kafkaListenerInSendMsMessage");




        double hardness = Double.parseDouble(data.getMyHardness());
        double latitude = Double.parseDouble(data.getMyLatitude());



        Set<String> IDS = redisRepository.getMsSubKey();
        Iterator<String> key = IDS.iterator();

        while (key.hasNext()){
            MsMembers msMembers = redisRepository.getMS_MEMBER(key.next());
            if(data.getMyId().equals(msMembers.getMyId())){
               continue; // 자신의 아이디 제외
            }
            System.out.println("msMembers.getMachingYn() = "+msMembers.getMachingYn());
            String resultStreet = distanceCalculation.DistanceCalculation(hardness, latitude,
                    Double.parseDouble(msMembers.getMyHardness()),
                    Double.parseDouble(msMembers.getMyLatitude()), "m", data , msMembers);
            System.out.println("resultStreet = "+resultStreet);
            if(resultStreet != null){
                data.setDFO(resultStreet);
                messagingTemplate.convertAndSend("/sub/matching/location/"+msMembers.getMyId(), data);

                msMembers.setDFO(resultStreet);
                messagingTemplate.convertAndSend("/sub/matching/location/"+data.getMyId(), msMembers);
            }
        }


    }

}
