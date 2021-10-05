package hello.hellospring.websocket;

import com.google.gson.Gson;
import hello.hellospring.chat.ChatMessage;
import hello.hellospring.chat.ChatRoomRepository;
import hello.hellospring.kafka.Producer;
import hello.hellospring.members.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {


//    public static Map<WebSocketSession, String> chatSessionList = new HashMap<WebSocketSession,String>();// 채팅 세션 리스트
    public static Map<WebSocketSession,String> matchingSessionList = new HashMap<WebSocketSession,String>(); // 매칭 세션 리스트
    private final Gson gson = new Gson();
    private final Producer producer;
    private final ChatRoomRepository chatRoomRepository;

    private final WebSocketMessageSend messageSend;
    private final ChatSessionList chatSessionList;
    public void sendMessage(Object message) throws IOException {
        log.info("sendMessage");

            ChatMessage chatMessage = (ChatMessage) message;






        messageSend.setMessage(chatMessage);
//                threadA.run();
        messageSend.start();
        System.out.println("messageSend end");

//        }

//            threadA.setMessage(chatMessage);
//            threadA.run();


//        }
//        System.out.println(chatSessionList.get(message.));
//        System.out.println(chatSessionList.values());

    }

    /*  메세지 수신 및 카프카 전송 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage");

        String payload = message.getPayload();
        log.info("payload : "+payload);
        String url = String.valueOf(session.getUri());
        System.out.printf("%s 로부터 [%s]받음", session.getId(), message.getPayload());


        if(url.contains("chat")){

            ChatMessage chatMessage = gson.fromJson(payload, ChatMessage.class);
            String topic =  chatRoomRepository.getTopic(chatMessage.getRoomId());
//
//         if(chatMessage.getSender().equals(chatSessionList.(session.getId()))){
//                log.info("아이디가 변조가 되었습니다");
//                return;
//         }


            if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)){

                chatSessionList.put(chatMessage.getRoomId(),session);
                chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다");

            }else if(chatMessage.getType().equals(ChatMessage.MessageType.TALK)){

            }else if(chatMessage.getType().equals(ChatMessage.MessageType.QUIT)){
                log.info("????????");
            }else{
                log.info("No Type");
                return;
            }
            producer.sendMessage(topic, chatMessage);
        }




        Map map = session.getAttributes();
        Member member = (Member) map.get("members");
        System.out.println(message);
    }

    /* 웹 소켓 연결 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("ChatHandler ==> afterConnectionEstablished");
//        String url = String.valueOf(session.getUri());
//        Map map = session.getAttributes();
//        Member member = (Member) map.get("members");
//        log.info(session + " 클라이언트 접속");
    }

    /* 웹 소켓 연결 해제 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        String url = String.valueOf(session.getUri());

        if(url.contains("chat")){
            String[] data = url.split("chat/");
            String key = data[data.length-1];
            chatSessionList.remove(key,session);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.QUIT);
            Map map = session.getAttributes();
            Member member = (Member) map.get("members");
            chatMessage.setSender("[알림]");
            chatMessage.setRoomId(key);
            chatMessage.setMessage(member.getId()+"님이 방을 나갔습니다");
            String topic =  chatRoomRepository.getTopic(chatMessage.getRoomId());
            producer.sendMessage(topic, chatMessage);

        }else if(url.contains("matching")){
            matchingSessionList.remove(session);
        }

    }
}



























