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

    public List<Passport> findAll() {
        return StreamSupport.stream(
                this.passportRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Passport findById(int passportId) {
        var passport = passportRepository.findById(passportId);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return passport.get();
    }

    public Passport findBySeries(String series) {
        var passport = passportRepository.findBySeries(series);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return passport.get();
    }

    public Passport create(Passport passport) {
        Passport createdPassport =  Passport.of(
                passport.getName(),
                passport.getSurname(),
                passport.getBirthday());
        return passportRepository.save(createdPassport);
    }

    public void update(int id, Passport passport) throws InvocationTargetException, IllegalAccessException {
        var current = passportRepository.findById(id);
        if (current.isEmpty()) {
            throw new NullPointerException("Passport with this id not found");
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
                    throw new NullPointerException("Invalid properties");
                }
                var newValue = getMethod.invoke(passport);
                if (newValue != null) {
                    setMethod.invoke(buffPassport, newValue);
                }
            }
        }
        passportRepository.save(buffPassport);
    }

    public Passport delete(int passportId) {
        Optional<Passport> passport = this.passportRepository.findById(passportId);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        passport.ifPresent(passportRepository::delete);
        return passport.get();
    }

    public List<Passport> findUnavailable() {
        List<Passport> unavailablePassportsList = StreamSupport.stream(
                this.passportRepository.findUnavailable().spliterator(), false
        ).collect(Collectors.toList());
        // add validation
        if (!unavailablePassportsList.isEmpty()) {
            kafkaTemplate.send("mail", "send notify");
        }
        return unavailablePassportsList;
    }

    public List<Passport> findReplaceable() {
        List<Passport> replaceablePassportsList = StreamSupport.stream(
                passportRepository.findReplaceable().spliterator(), false
        ).collect(Collectors.toList());
        // add validation
        return replaceablePassportsList;
    }
}
