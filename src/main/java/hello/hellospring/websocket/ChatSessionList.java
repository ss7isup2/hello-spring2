package hello.hellospring.websocket;


import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Component
public class ChatSessionList {
    public void put(String id,WebSocketSession socketSession){
        ArrayList<WebSocketSession> sessionData = this.chatSessionList.get(id) == null ? new ArrayList<>(): this.chatSessionList.get(id) ;
        sessionData.add(socketSession);
        Set<WebSocketSession> set = new HashSet<WebSocketSession>(sessionData);
        ArrayList<WebSocketSession> newList =new ArrayList<WebSocketSession>(set);
        this.chatSessionList.put(id, newList);
    }

    public void remove(String key, WebSocketSession socketSession){
        ArrayList<WebSocketSession> sessionData = this.chatSessionList.get(key);
        Set<WebSocketSession> set = new HashSet<WebSocketSession>(sessionData);
        set.remove(socketSession);
        ArrayList<WebSocketSession> newList =new ArrayList<WebSocketSession>(set);
        this.chatSessionList.put(key, newList);
    }

    public Set<String> keySet(){
        return this.chatSessionList.keySet();
    }

    public ArrayList<WebSocketSession> get(String key) {


        return this.chatSessionList.get(key);
    }



    public Map<String, ArrayList<WebSocketSession>> chatSessionList = new HashMap<>();// 채팅 세션 리스트

}














