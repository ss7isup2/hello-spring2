package hello.hellospring.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
public class Producer {

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    KafkaProducer<String, String> producer;


    @Value("${kafka.server}")
    private String bootstrapServers;


    @Bean
    public void setting(){
        System.out.println("producer ==> setting");
        Properties properties = new Properties();
        // create Producer properties
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
         producer = new KafkaProducer<>(properties);
    }






    //데이터 전송
    public void sendMessage(String topic, Object message) throws JsonProcessingException {
        System.out.println("Producer ==> data");
        System.out.println("topic = "+topic);

        ProducerRecord<String, String > record = new ProducerRecord<String, String>(topic,objectMapper.writeValueAsString(message));
        producer.send(record);


    }

}
