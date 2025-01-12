package is.rares.kumo.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    // TODO: make sure a user can only subscribe to his topics
    // TODO: verify token for login into websocket

    @Value("${kumo.cors.origin:http://localhost:4200}")
    String origin;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/websocket");
        config.setApplicationDestinationPrefixes("/api");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("/api/websocket").setAllowedOrigins(origin);
    }
}
