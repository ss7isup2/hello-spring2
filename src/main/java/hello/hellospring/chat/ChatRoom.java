package hello.hellospring.chat;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements Serializable {

    public static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private String topic;

    public static ChatRoom create(String name, String topic) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.topic = topic;
        return chatRoom;
    }
}