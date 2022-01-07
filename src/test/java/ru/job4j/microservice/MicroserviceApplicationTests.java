package ru.job4j.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.MockProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.microservice.model.Passport;
import ru.job4j.microservice.repositories.PassportRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MicroserviceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class MicroserviceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassportRepository passportRepository;

    @Autowired
    private ObjectMapper mapper;


    @Test
    public void simpleTest() {
        assertNotNull(mockMvc);
    }

    @Test
    public void addPassportAndFindByIdThenReturnOk() throws Exception {
        LocalDate data = LocalDate.of(1999, 12, 31);
        Passport passport = Passport.of(
                "name", "surname", new Timestamp(data.toEpochDay()));
        passportRepository.save(passport);
        Mockito.when(passportRepository.findById(passport.getId())).thenReturn(Optional.of(passport));
        mockMvc.perform(
                get("/passport/find/0")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(passport)));
    }
}
