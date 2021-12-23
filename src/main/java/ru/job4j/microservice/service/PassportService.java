package ru.job4j.microservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Service
public class PassportService {

    private final PassportRepository passportRepository;

    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    public List<Passport> findAll() {
        return StreamSupport.stream(
                this.passportRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public ResponseEntity<Passport> findById(int id) {
        var passport = this.passportRepository.findById(id);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport.get());
    }


    public ResponseEntity<Passport> findBySeries(String series) {
        var passport = this.passportRepository.findBySeries(series);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport.get());
    }


    public ResponseEntity<Passport> create(Passport passport) {
        Passport buff =  Passport.of(
                passport.getName(),
                passport.getSurname(),
                passport.getBirthday());
        this.passportRepository.save(buff);
        return new ResponseEntity<>(
                buff,
                HttpStatus.CREATED
        );
    }


    public ResponseEntity<Void> update(int id, Passport passport) throws InvocationTargetException, IllegalAccessException {
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
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Void> delete(int id) {
        Optional<Passport> passport = this.passportRepository.findById(id);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        this.passportRepository.delete(passport.get());
        return ResponseEntity.ok().build();
    }

    public List<Passport> findUnavailable() {
        return StreamSupport.stream(
                this.passportRepository.findUnavailable().spliterator(), false
        ).collect(Collectors.toList());
    }

    public List<Passport> findReplaceable() {
        return StreamSupport.stream(
                this.passportRepository.findReplaceable().spliterator(), false
        ).collect(Collectors.toList());
    }
}
