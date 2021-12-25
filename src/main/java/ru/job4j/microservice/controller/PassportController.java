package ru.job4j.microservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.microservice.model.Passport;
import ru.job4j.microservice.service.PassportService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

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
        var passport = passportService.findById(id);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport.get());
    }

    @GetMapping("/find/series/{series}")
    public ResponseEntity<Passport> findBySeries(@PathVariable String series) {
        var passport = this.passportService.findBySeries(series);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
                return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(passport.get());
    }

    @PostMapping("/save")
    public ResponseEntity<Passport> create(@RequestBody Passport passport) {
        return new ResponseEntity<>(
                passportService.create(passport),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Passport passport) throws InvocationTargetException, IllegalAccessException {
        passportService.update(id, passport);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        var passport = passportService.delete(id);
        if (passport.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport not found");
        }
        return ResponseEntity.ok().build();
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
