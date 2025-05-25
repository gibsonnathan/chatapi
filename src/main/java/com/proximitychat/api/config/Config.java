package com.proximitychat.api.config;

import com.proximitychat.api.handler.WsHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class Config implements WebSocketConfigurer {

  private final WsHandler wsHandler;

  public Config(WsHandler wsHandler) {
    this.wsHandler = wsHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(wsHandler, "/chat").setAllowedOrigins("*");
  }
}
