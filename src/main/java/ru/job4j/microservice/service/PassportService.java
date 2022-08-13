package ru.job4j.microservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.microservice.model.Passport;
import ru.job4j.microservice.repositories.PassportRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Main business logic for working with Passport models
 */
@Service
@RequiredArgsConstructor
public class PassportService {

    /**
     * DAO for Passport
     */
    private final PassportRepository passportRepository;

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    /**
     * Method for getting all passports
     * @return List of passports
     */
    public List<Passport> findAll() {
        String anchor = UUID.randomUUID().toString();
        List<Passport> passportList = StreamSupport.stream(
                this.passportRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
        if (passportList == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return passportList;
    }

    /**
     * Method for getting passport by passport ID
     * @param passportId - passport ID
     * @return object of Passport
     */
    public Passport findById(int passportId) {
        String anchor = UUID.randomUUID().toString();
        var passport = passportRepository.findById(passportId);
        if (passport.isEmpty()) {
            throw new IllegalArgumentException("Passport not found. Actual parameters: passport ID - " + passportId + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return passport.get();
    }

    /**
     * Method for getting passport by series of passport
     * @param series - series of passport
     * @return object of Passport
     */
    public Passport findBySeries(String series) {
        String anchor = UUID.randomUUID().toString();
        var passport = passportRepository.findBySeries(series);
        if (passport.isEmpty()) {
            throw new IllegalArgumentException("Passport not found. Actual parameters: series - " + series + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return passport.get();
    }

    /**
     * Method for adding new passport
     * @param passport - object of Passport
     * @return created object of Passport
     */
    public Passport create(Passport passport) {
        String anchor = UUID.randomUUID().toString();
        Passport tempPassport =  Passport.of(
                passport.getName(),
                passport.getSurname(),
                passport.getBirthday());
        Passport created = passportRepository.save(tempPassport);
        if (created == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return created;
    }

    /**
     * PUT method for updating passport
     * @param passportId - passport ID
     * @param passport - object of Passport
     */
    public void update(int passportId, Passport passport) {
        String anchor = UUID.randomUUID().toString();
        var current = passportRepository.findById(passportId);
        if (current.isEmpty()) {
            throw new IllegalArgumentException("Passport not found. Actual parameters: passportId - " + passportId + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        var buffPassport = current.get();
        var methods = buffPassport.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
                }
                Object newValue = null;
                try {
                    newValue = getMethod.invoke(passport);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
                }
                if (newValue != null) {
                    try {
                        setMethod.invoke(buffPassport, newValue);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
                    }
                }
            }
        }
        passportRepository.save(buffPassport);
    }

    /**
     * Method for deleting passport
     * @param passportId - passport ID
     */
    public Passport delete(int passportId) {
        String anchor = UUID.randomUUID().toString();
        Optional<Passport> passport = this.passportRepository.findById(passportId);
        if (passport.isEmpty()) {
            throw new IllegalArgumentException("Passport not found. Actual parameters: passportId - " + passportId + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        passport.ifPresent(passportRepository::delete);
        return passport.get();
    }

    /**
     * Method for getting unavailable passport
     * @return List of unavailable passports
     */
    public List<Passport> findUnavailable() {
        String anchor = UUID.randomUUID().toString();
        List<Passport> unavailablePassportsList = StreamSupport.stream(
                this.passportRepository.findUnavailable().spliterator(), false
        ).collect(Collectors.toList());
        if (unavailablePassportsList == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        if (!unavailablePassportsList.isEmpty()) {
            kafkaTemplate.send("mail", "send notify");
        }
        return unavailablePassportsList;
    }

    /**
     * Method for getting passports which expire in the next 3 months
     * @return List of Passports
     */
    public List<Passport> findReplaceable() {
        String anchor = UUID.randomUUID().toString();
        List<Passport> replaceablePassportsList = StreamSupport.stream(
                passportRepository.findReplaceable().spliterator(), false
        ).collect(Collectors.toList());
        if (replaceablePassportsList == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return replaceablePassportsList;
    }
}
