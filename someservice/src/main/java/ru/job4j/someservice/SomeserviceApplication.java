package ru.job4j.someservice;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class SomeserviceApplication {

    @Bean
    public RestTemplate getTemplate() {
        HttpClient client = HttpClients.createDefault();
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
        return template;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource ds) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
        liquibase.setDataSource(ds);
        return liquibase;
    }

    public static void main(String[] args) {
        SpringApplication.run(SomeserviceApplication.class, args);
    }

}
