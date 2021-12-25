package ru.job4j.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class MailApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }

    @KafkaListener(topics = "mail", groupId = "app.1")
    public void listen() {
        System.out.println("Send notify about not active passport in database");
    }

}
