package ru.job4j.someservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.someservice.model.Passport;

import java.util.Collections;
import java.util.List;

/**
 * Business logic
 */
@Service
public class SomeService {

    private final RestTemplate client;

    @Value("${url.passport.service}")
    private String url;

    public SomeService(RestTemplate client) {
        this.client = client;
    }

    /**
     * Method for getting passport by passport ID
     * @param id - passport ID
     * @return object of Passport
     */
    public Passport getPassportById(int id) {
        return client.getForObject(url + "/find/" + id, Passport.class);
    }

    /**
     * Method for getting passport by series of passport
     * @param series - series of passport (String)
     * @return object of Passport
     */
    public Passport getPassportBySeries(String series) {
        return client.getForObject(url + "/find/series/" + series, Passport.class);
    }

    /**
     * Method for getting all passports
     * @return List of passports
     */
    public List<Passport> allPassport() {
        return getList(url + "/find");
    }

    /**
     * Method for creating new passport
     * @param passport - object of Passport
     * @return object of Passport
     */
    public Passport createNewPassport(Passport passport) {
        return client.postForEntity(url + "/save", passport, Passport.class).getBody();
    }

    /**
     * Method for updating passport
     * @param passportId - passport ID
     * @param passport - object of Passport
     * @return boolean result of updating
     */
    public boolean changePassport(int passportId, Passport passport) {
        return client.exchange(
                url + "/update/" + passportId,
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() == HttpStatus.OK;
    }

    /**
     * Method for removing passport by passport ID
     * @param passportId - passport ID
     * @return boolean result of deleting
     */
    public boolean removePassport(int passportId) {
        return client.exchange(
                url + "/delete/" + passportId,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() == HttpStatus.OK;
    }

    /**
     * Method for getting not active passports
     * @return List of passports
     */
    public List<Passport> findNotActivePassports() {
        return getList(url + "/unavailable");
    }

    /**
     * Method for getting which will soon need to be changed
     * @return List of passports
     */
    public List<Passport> findChangeSoonPassports() {
        return getList(url + "/replaceable");
    }

    /**
     * Private method for getting lists of passports from different links
     * @param url - link (String)
     * @return List of passports
     */
    private List<Passport> getList(String url) {
        List<Passport> body = client.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
