package com.ninetrees;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan("com.ninetrees.musicapp.mapper")
@EnableWebSocket
public class MusicAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicAppBackendApplication.class, args);
    }

}
