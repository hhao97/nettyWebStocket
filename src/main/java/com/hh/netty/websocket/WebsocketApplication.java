package com.hh.netty.websocket;

import com.hh.netty.websocket.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WebsocketApplication {

	public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebsocketApplication.class, args);
        NettyServer.run();
    }

}
