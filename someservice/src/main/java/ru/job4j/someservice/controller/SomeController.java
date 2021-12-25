package ru.job4j.someservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.job4j.someservice.model.Passport;
import ru.job4j.someservice.service.SomeService;

import java.util.List;

@EnableScheduling
@RestController
@RequestMapping("/some")
public class SomeController {

    private final SomeService someService;

    public SomeController(SomeService someService) {
        this.someService = someService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Passport> getPassportById(@PathVariable int id) {
        return someService.getPassportById(id);
    }

    @GetMapping("/series/{series}")
    public ResponseEntity<Passport> getPassportBySeries(@PathVariable String series) {
        return someService.getPassportBySeries(series);
    }

    @GetMapping("/all")
    public List<Passport> allPassport() {
        return someService.allPassport();
    }

    @PostMapping("/new")
    public ResponseEntity<Passport> createNewPassport(@RequestBody Passport passport) {
        return someService.createNewPassport(passport);
    }

    @PutMapping("/change/{id}")
    public boolean changePassport(@PathVariable int id, @RequestBody Passport passport) {
        return someService.changePassport(id, passport);
    }

    @DeleteMapping("/remove/{id}")
    public boolean removePassport(@PathVariable int id) {
        return someService.removePassport(id);
    }

    @Scheduled(fixedDelay = 20000)
    @GetMapping("/not-active")
    public void findNotActivePassports() {
        someService.findNotActivePassports();
    }

    @GetMapping("/change-soon")
    public List<Passport> findChangeSoonPassports() {
        return someService.findChangeSoonPassports();
    }
}
