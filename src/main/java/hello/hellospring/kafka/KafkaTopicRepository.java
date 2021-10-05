package hello.hellospring.kafka;


import com.google.common.collect.Lists;
import hello.hellospring.chat.ChatRoom;
import hello.hellospring.config.Distributed;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Repository
public class KafkaTopicRepository {


    private String KAFKA_TOPIC = "";

    public void setKAFKA_TOPIC(String KAFKA_TOPIC) {
        this.KAFKA_TOPIC = KAFKA_TOPIC;
    }

    private SetOperations<String, String> kafkaTopicList;
    private final RedisTemplate<String, String> redisTopicListTemplate; // 레디스에 저장되어 있는 토픽 list template


    @PostConstruct
    private void init() {
        kafkaTopicList = redisTopicListTemplate.opsForSet();
    }



    public String getTopic(){
        List<String> topicList = Lists.newArrayList(kafkaTopicList.members(KAFKA_TOPIC));
        int num=0;
        for(int i=0; i<topicList.size()-1; i++){
            if(kafkaTopicList.members(topicList.get(num)).stream().count() >= kafkaTopicList.members(topicList.get(i+1)).stream().count()){
                num = i+1;
            }
        }
        return topicList.get(num);

    }
}
