package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
        // System.out.println(1/10.0); -> 0.1 ceil시 1
        // 1/10 -> 0.0 -> ceil시 0
    }

}
