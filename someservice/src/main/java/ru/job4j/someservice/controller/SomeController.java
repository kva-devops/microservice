package ru.job4j.someservice.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import ru.job4j.someservice.model.Passport;
import ru.job4j.someservice.service.SomeService;

import java.util.List;

/**
 * Main controller
 */
@EnableScheduling
@RestController
@RequestMapping("/some")
@RequiredArgsConstructor
public class SomeController {

    private final SomeService someService;

    /**
     * GET method for getting passport by passport ID
     * @param id passport ID
     * @return object of Passport
     */
    @GetMapping("/id/{id}")
    public Passport getPassportById(@PathVariable int id) {
        return someService.getPassportById(id);
    }

    /**
     * GET method for getting passport by series
     * @param series series of passport
     * @return object of Passport
     */
    @GetMapping("/series/{series}")
    public Passport getPassportBySeries(@PathVariable String series) {
        return someService.getPassportBySeries(series);
    }

    /**
     * GET method for getting all passports
     * @return List of passports
     */
    @GetMapping("/all")
    public List<Passport> allPassport() {
        return someService.allPassport();
    }

    /**
     * POST method for creating new passport
     * @param passport object of Passport
     * @return object of Passport
     */
    @PostMapping("/new")
    public Passport createNewPassport(@RequestBody Passport passport) {
        return someService.createNewPassport(passport);
    }

    /**
     * PUT method for updating passport
     * @param id - passport ID
     * @param passport - object of Passport
     * @return boolean result of updating
     */
    @PutMapping("/change/{id}")
    public boolean changePassport(@PathVariable int id, @RequestBody Passport passport) {
        return someService.changePassport(id, passport);
    }

    /**
     * DELETE method for deleting passport
     * @param id - passport ID
     * @return boolean result of deleting
     */
    @DeleteMapping("/remove/{id}")
    public boolean removePassport(@PathVariable int id) {
        return someService.removePassport(id);
    }

    /**
     * GET method for getting not active passport
     * @return List of passport
     */
    @GetMapping("/not-active")
    public List<Passport> findNotActivePassports() {
        return someService.findNotActivePassports();
    }

    /**
     * GET method for getting passports which will soon need to be changed
     * @return List of passports
     */
    @GetMapping("/change-soon")
    public List<Passport> findChangeSoonPassports() {
        return someService.findChangeSoonPassports();
    }
}
