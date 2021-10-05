package hello.hellospring.maching;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nu.art.storage.PreferencesModule;
import hello.hellospring.kafka.Producer;
import hello.hellospring.members.Member;
import hello.hellospring.members.SessionMember;
import hello.hellospring.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;


@RequiredArgsConstructor
@Controller
public class MachingDetail {


    private final RedisRepository redisRepository;
    private final MachingRepository machingRepository;
    private final String MS_TOPIC = "MATCHING_MS_MEMBERS"; //  매칭 대기자 전용 토픽
    private final String MACHING_TOPIC = "MACHING_S_MEMBERS"; // 매칭 완료자 전용 토픽
    private final Producer producer;
    private final SessionMember member;

    @GetMapping("/chat")
    public void chatGET(){
        System.out.println("chatGET");
    }





//    private final Consumer consumer;

//    private final MachingRepository machingRepository;




    //REDIS 에 저장되어있는 매칭 전용 카프카 토픽 리스트 이름




//    @MessageMapping("/matching/myMachingData")
//    public void myMachingData(MachingMember myMachingData) throws JsonProcessingException {
//        System.out.println("myid = "+myMachingData.getMyId());
//
//        MachingMember machingMember = redisMatchingInformation.get(matching_information,myMachingData.getMyId());
//        ObjectMapper objectMapper = new ObjectMapper();
//        String personJson = objectMapper.writeValueAsString(machingMember);
//
//        System.out.println(personJson);
//
//
//        if(machingMember.getMatchOrNot().equals("Y")){
//            messagingTemplate.convertAndSend("/sub/matching/location/"+machingMember.getMyId(), machingMember);
//        }
//
//    }
//
//    @MessageMapping("/matching/sendMatching")
//    public void sendMaching(MachingMember machingData) throws JsonProcessingException {
//        System.out.println("sendMaching");
//        System.out.println(machingData.getMyId());
//        System.out.println(machingData.getReceivedId());
//
//        for (MachingMember value : redisMatchingInformation.values(matching_information)) {
//
//            if(value.getMyId().equals(machingData.getReceivedId())){
//                if(value.getMatchOrNot().equals("n") || value.getMatchOrNot().equals("N")){
//                    MachingMember machingMember = new MachingMember();
//                    machingMember.setMyId(value.getMyId());
//                    machingMember.setMyWantStreet(value.getMyWantStreet());
//                    machingMember.setMyLatitude(value.getMyLatitude());
//                    machingMember.setMyHardness(value.getMyHardness());
//
//                    machingMember.setReceivedId(machingData.getMyId());
//                    machingMember.setReceiveStreet(machingData.getMyWantStreet());
//                    machingMember.setReceiveHardness(machingData.getMyHardness());
//                    machingMember.setReceiveLatitude(machingData.getMyLatitude());
//
//
//                    machingMember.setMatchOrNot("Y");
//                    machingMember.setReceiveStreet(machingData.getReceiveStreet());
//                    machingMember.setFunction("MatchingCompleted");
//
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String personJson = objectMapper.writeValueAsString(machingMember);
//                    System.out.println(personJson);
//
//
//
//
//
//                    redisMatchingInformation.put(matching_information, machingMember.getMyId(), machingMember);
//                    messagingTemplate.convertAndSend("/sub/matching/location/"+machingMember.getMyId(), machingMember);
//
//                    machingMember.setMyId(machingData.getMyId());
//                    machingMember.setMyWantStreet(machingData.getMyWantStreet());
//                    machingMember.setMyLatitude(machingData.getMyLatitude());
//                    machingMember.setMyHardness(machingData.getMyHardness());
//
//                    machingMember.setReceivedId(value.getMyId());
//                    machingMember.setReceiveStreet(value.getMyWantStreet());
//                    machingMember.setReceiveHardness(value.getMyHardness());
//                    machingMember.setReceiveLatitude(value.getMyLatitude());
//
//                    objectMapper = new ObjectMapper();
//                    personJson = objectMapper.writeValueAsString(machingMember);
//                    System.out.println(personJson);
//
//
//                    redisMatchingInformation.put(matching_information, machingMember.getMyId(), machingMember);
//                    messagingTemplate.convertAndSend("/sub/matching/location/"+machingMember.getMyId(), machingMember);
//
//                    System.out.println("end");
//                    break;
//                }else{
//                    System.out.println("이미 매칭 완료");
//                    System.out.println("/sub/matching/location/"+machingData.getMyId());
//                    messagingTemplate.convertAndSend("/sub/matching/location/"+machingData.getMyId(), "선택한 사람은 이미 매칭이 완료 되었습니다.");
//
//                }
//            }
//        }
//
//
//    }



    // 다른 사람 데이터 삽입용
    @MessageMapping("/matching/imsidatasend")
    public void imsidatasend() throws JsonProcessingException {

        //임시 데이터
        int i = 0;
        while (i < 12) {
            Random random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
            random.setSeed(System.currentTimeMillis());
            MsMembers machingReq = new MsMembers();
            machingReq.setMyId("test" + i);
            machingReq.setMyHardness(String.valueOf(37 + random.nextDouble()));
            machingReq.setMyLatitude(String.valueOf(126 + random.nextDouble()));
            String myStreet = (random.nextInt(5)+1)+"0";
            machingReq.setMyWantStreet(myStreet);
            System.out.println("machingReq .wnarsettr"+machingReq.getMyWantStreet());
            producer.sendMessage(MS_TOPIC,machingReq);
            redisRepository.setMS_MEMBER_Template(machingReq.getMyId(), machingReq);
            i++;
        }
    }

    @MessageMapping("/matching/sendMaching")
    @ResponseBody
    public String sendMaching(MachingMembers data) throws JsonProcessingException {
//        System.out.println(data.getMyId());
//        System.out.println(data.getMyHardness());
//        System.out.println(data.getMyLatitude());
//        System.out.println(data.getMsId());
//        System.out.println(data.getMsHardness());
//        System.out.println(data.getMsLatitude());

        if(machingRepository.MachingData(data)){
            return "매칭 완료 되었습니다";
        }else{
            return "이미 매칭 된 사람입니다";
        }
    }

    @MessageMapping("/matching/matchReq")
    public void matchReq(MsMembers param) throws JsonProcessingException {


        System.out.println("machingDetail");
        System.out.println("my id : "+param.getMyId());
        System.out.println("my location : "+param.getMyHardness()+", "+param.getMyLatitude());
        System.out.println("myStreet : "+param.getMyWantStreet());







//
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        System.out.println(attr.getRequest());

//        if(member.getId().equals(param.getMyId())){
//            producer.sendMessage(MS_TOPIC,param);
//            redisRepository.setMS_MEMBER_Template(param.getMyId(), param);
//            return null;
//        }else{
//            return "redirect:/members/login";
//        }
    }


}

