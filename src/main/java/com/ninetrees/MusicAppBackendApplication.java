package com.ninetrees;
import io.github.cdimascio.dotenv.Dotenv;
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
        // 加载 .env 文件
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("ALIYUN_ACCESS_KEY_ID", dotenv.get("ALIYUN_ACCESS_KEY_ID"));
        System.setProperty("ALIYUN_ACCESS_KEY_SECRET", dotenv.get("ALIYUN_ACCESS_KEY_SECRET"));

        SpringApplication.run(MusicAppBackendApplication.class, args);
    }

}
