package ru.job4j.microservice.service;

import org.springframework.stereotype.Service;
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

    public Optional<Passport> findById(int id) {
        return this.passportRepository.findById(id);
    }


    public Optional<Passport> findBySeries(String series) {
        return this.passportRepository.findBySeries(series);
    }


    public Passport create(Passport passport) {
        Passport buff =  Passport.of(
                passport.getName(),
                passport.getSurname(),
                passport.getBirthday());
        return this.passportRepository.save(buff);
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


    public Optional<Passport> delete(int id) {
        Optional<Passport> passport = this.passportRepository.findById(id);
        this.passportRepository.delete(passport.get());
        return passport;
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
