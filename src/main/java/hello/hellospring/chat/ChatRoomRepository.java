package hello.hellospring.chat;

import com.google.common.collect.Lists;
import hello.hellospring.config.Distributed;
import hello.hellospring.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;


@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private static final String KAFKA_TOPIC = "KAFKA_TOPIC_LIST";

    private final RedisTemplate<String, Object> redisTemplate; // redis object 용
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;


    private final RedisTemplate<String, String> redisTopicListTemplate; // 레디스에 저장되어 있는 토픽 list template
    private SetOperations<String, String> kafkaTopicList;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;
    private final Distributed Distributed;


    @PostConstruct
    private void init() {
        System.out.println("ChatRoomRepository.init");
        opsHashChatRoom = redisTemplate.opsForHash();
        kafkaTopicList = redisTopicListTemplate.opsForSet();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        System.out.println("ChatRoomRepository.findAllRoom");
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public List<ChatRoom> findTopic(String roomId){
        System.out.println("room first name = "+roomId);
        List<ChatRoom> data = opsHashChatRoom.values(roomId);
        System.out.println(data.get(0).getTopic());
        return opsHashChatRoom.values(roomId);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        List<String> topicList = Lists.newArrayList(kafkaTopicList.members(KAFKA_TOPIC));


        //채팅 토픽 이름만 남기기
        for(Iterator<String> it = topicList.iterator() ; it.hasNext() ; ) {
            String value = it.next();
            if(!value.contains("CHAT")) {
                it.remove();
            }
        }


        int num=0;
        for(int i=0; i<topicList.size()-1; i++){
            if(kafkaTopicList.members(topicList.get(num)).stream().count() >= kafkaTopicList.members(topicList.get(i+1)).stream().count()){
                num = i+1;
            }
        }

        System.out.println(" topicList.get(num) = "+  topicList.get(num));

        ChatRoom chatRoom = ChatRoom.create(name, topicList.get(num));
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        kafkaTopicList.add(topicList.get(num), chatRoom.getRoomId());
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        System.out.println("ChatRoomRepository.enterChatRoom");
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            if (Distributed.getData() == "redis"){
                redisMessageListener.addMessageListener(redisSubscriber, topic);
            }
            topics.put(roomId, topic);
        }
    }

    public ChatRoom getChanner(String roomId){
        return (opsHashChatRoom.get(CHAT_ROOMS,roomId));
    }
    public String getTopic(String roomId) {
        ChatRoom chatRoom = opsHashChatRoom.get(CHAT_ROOMS,roomId);
        return  chatRoom.getTopic();
    }
}


