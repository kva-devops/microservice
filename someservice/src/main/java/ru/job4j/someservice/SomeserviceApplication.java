package ru.job4j.someservice;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SomeserviceApplication {

    @Bean
    public RestTemplate getTemplate() {
        HttpClient client = HttpClients.createDefault();
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
        return template;
    }

    public static void main(String[] args) {
        SpringApplication.run(SomeserviceApplication.class, args);
    }

}
