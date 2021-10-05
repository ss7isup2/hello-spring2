package hello.hellospring.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER("ENTER"),
        TALK("TALK"),
        QUIT("QUIT");

        private String messageTypeValue;

        private MessageType(String messageTypeValue) {
            this.messageTypeValue = messageTypeValue;
        }

    }



    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
}