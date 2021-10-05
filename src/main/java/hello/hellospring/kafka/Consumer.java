package hello.hellospring.kafka;

import com.google.common.collect.Lists;
import hello.hellospring.chat.ChatMessage;
import hello.hellospring.maching.DistanceCalculation;
import hello.hellospring.maching.MachingMembers;
//import hello.hellospring.maching.MachingRepository;
import hello.hellospring.maching.MsMembers;
import hello.hellospring.redis.RedisRepository;
import hello.hellospring.websocket.SocketHandler;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final RedisTemplate<String, String> redisTopicListTemplate; // 레디스에 저장되어 있는 토픽 list template
    private SetOperations<String, String> kafkaTopicList;
    private static final String KAFKA_TOPIC = "KAFKA_TOPIC_LIST";
    private final SimpMessageSendingOperations messagingTemplate;
    private final SocketHandler socketHandler;
    private final DistanceCalculation distanceCalculation;
    private final RedisRepository redisRepository;




    private Map<String, Object> consumerProperties(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, " 10.211.55.6:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group1"); // 서버마다 다르게설정
        return props;
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> messageListenerContainer() {
        System.out.println("CustomKafkaListener ==> KafkaMessageListenerContainer ");

        kafkaTopicList = redisTopicListTemplate.opsForSet();


        List<String> topicList1 = Lists.newArrayList(kafkaTopicList.members(KAFKA_TOPIC));
        for (int i=0; i< topicList1.size(); i++){
            System.out.println(topicList1.get(i));
        }
        String[] topicList = Lists.newArrayList(kafkaTopicList.members(KAFKA_TOPIC)).toArray(new String[kafkaTopicList.members(KAFKA_TOPIC).size()]);

        ContainerProperties containerProperties = new ContainerProperties(topicList);
        containerProperties.setMessageListener(new MyMessageListener());

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties());
        KafkaMessageListenerContainer<String, String> listenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setAutoStartup(true);
        return listenerContainer;
    }


    class MyMessageListener implements MessageListener<String, String> {
        @Override
        public void onMessage(ConsumerRecord<String, String> data) {

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject recordData = null;
                recordData = (JSONObject) jsonParser.parse(data.value());
                System.out.println(data.value());
//                System.out.println(data.topic());
                if (data.topic().contains("CHAT")) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage((String) recordData.get("message"));
                    chatMessage.setSender((String) recordData.get("sender"));
                    chatMessage.setRoomId((String) recordData.get("roomId"));
                    chatMessage.setType(ChatMessage.MessageType.valueOf((String) recordData.get("type")));
                    socketHandler.sendMessage(chatMessage);




                    // 매칭 요청자
                } else if (data.topic().contains("MATCHING_MS_MEMBERS")) {
                    MsMembers msMembers = new MsMembers();

                    msMembers.setMyId((String) recordData.get("myId"));
                    msMembers.setMyHardness((String) recordData.get("myHardness"));
                    msMembers.setMyLatitude((String) recordData.get("myLatitude"));
                    msMembers.setMyWantStreet((String) recordData.get("myWantStreet"));
                    msMembers.setMachingYn((String) recordData.get("machingYn"));
                    msMembers.setFunction((String) recordData.get("function"));
//                    machingRepository.kafkaListenerInSendMsMessage(msMembers);

                    //매칭 수신자
                } else if(data.topic().contains("MATCHING_MEMBERS")) {
                    MachingMembers member = new MachingMembers();
                    member.setFunction((String) recordData.get("function"));
                    member.setMyId((String) recordData.get("myId"));
                    member.setMyHardness((String) recordData.get("myHardness"));
                    member.setMyLatitude((String) recordData.get("myLatitude"));
                    member.setMsId((String) recordData.get("msId"));
                    member.setMsHardness((String) recordData.get("msHardness"));
                    member.setMsLatitude((String) recordData.get("msLatitude"));
                    member.setDFO((String) recordData.get("dfo"));
//                    machingRepository.kafkaListenerInSendMachingMessage(member);
                }else{

                }

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            logger.info("MyMessageListener : "+data.value());
        }
    }

}
