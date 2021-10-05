package hello.hellospring.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Getter
@Setter
@Component
public class Distributed {
    private String data = "kafka"; // "kafka" // "redis"

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
