package com.adik993.mytorrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyTorrentApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyTorrentApplication.class, args);
    }
}
