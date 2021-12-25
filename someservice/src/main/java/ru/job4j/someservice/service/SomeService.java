package ru.job4j.someservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.someservice.model.Passport;

import java.util.Collections;
import java.util.List;

@Service
public class SomeService {

    private final RestTemplate client;

    @Value("${url.passport.service}")
    private String url;

    public SomeService(RestTemplate client) {
        this.client = client;
    }

    public ResponseEntity<Passport> getPassportById(int id) {
        Passport passport = client
                .getForObject(url + "/find/" + id, Passport.class);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport);
    }

    public ResponseEntity<Passport> getPassportBySeries(String series) {
        Passport passport = client.getForObject(
                url + "/find/series/" + series,
                Passport.class
        );
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport);
    }

    public List<Passport> allPassport() {
        return getList(url + "/find");
    }

    public ResponseEntity<Passport> createNewPassport(Passport passport) {
        Passport buff = client.postForEntity(
                url + "/save", passport, Passport.class
        ).getBody();
        return new ResponseEntity<>(
                buff,
                HttpStatus.CREATED
        );
    }

    public boolean changePassport(int id, Passport passport) {
        return client.exchange(
                url + "/update/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() == HttpStatus.OK;
    }

    public boolean removePassport(int id) {
        return client.exchange(
                url + "/delete/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() == HttpStatus.OK;
    }

    public List<Passport> findNotActivePassports() {
        return getList(url + "/unavailable");
    }

    public List<Passport> findChangeSoonPassports() {
        return getList(url + "/replaceable");
    }

    private List<Passport> getList(String url) {
        List<Passport> body = client.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
