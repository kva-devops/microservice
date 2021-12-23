package ru.job4j.microservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.microservice.model.Passport;
import ru.job4j.microservice.service.PassportService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/passport")
public class PassportController {

    private final PassportService passportService;

    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @GetMapping("/find")
    public List<Passport> findAll() {
        return passportService.findAll();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Passport> findById(@PathVariable int id) {
        return passportService.findById(id);
    }

    @GetMapping("/find/series/{series}")
    public ResponseEntity<Passport> findBySeries(@PathVariable String series) {
        return passportService.findBySeries(series);
    }

    @PostMapping("/save")
    public ResponseEntity<Passport> create(@RequestBody Passport passport) {
        return passportService.create(passport);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Passport passport) throws InvocationTargetException, IllegalAccessException {
        return passportService.update(id, passport);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return passportService.delete(id);
    }

    @GetMapping("/unavailable")
    public List<Passport> findUnavailable() {
        return passportService.findUnavailable();
    }

    @GetMapping("/replaceable")
    public List<Passport> findReplaceable() {
        return passportService.findReplaceable();
    }

}
