package hello.hellospring.websocket;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "ws/chat/**").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }
}