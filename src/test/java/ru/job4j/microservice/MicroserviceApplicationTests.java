package ru.job4j.microservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.job4j.microservice.config.KafkaProducerConfig;
import ru.job4j.microservice.config.KafkaTopicConfig;

@SpringBootTest(classes = {
        KafkaProducerConfig.class, KafkaTopicConfig.class
})
@DirtiesContext
@EmbeddedKafka(partitions = 1)

class MicroserviceApplicationTests {
    static {
        System.setProperty(
                EmbeddedKafkaBroker.BROKER_LIST_PROPERTY,
                "spring.kafka.bootstrap-servers");
    }
    @Test
    void contextLoads() {
    }
}
