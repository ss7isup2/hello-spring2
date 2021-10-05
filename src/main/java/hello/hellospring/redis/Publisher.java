package hello.hellospring.redis;

import hello.hellospring.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Publisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        System.out.println("RedisPublisher.publish");
        System.out.println(topic);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}