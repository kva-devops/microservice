package ru.job4j.microservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.microservice.model.Passport;
import ru.job4j.microservice.service.PassportService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Controller for working "Passport" models
 */
@RestController
@RequestMapping("/passport")
@RequiredArgsConstructor
public class PassportController {

    private final PassportService passportService;

    /**
     * GET method for getting all passports
     * @return List of passports
     */
    @GetMapping("/find")
    public List<Passport> findAll() {
        return passportService.findAll();
    }

    /**
     * GET method for getting passport by passport ID
     * @param id passport ID
     * @return object of Passport
     */
    @GetMapping("/find/{id}")
    public Passport findById(@PathVariable int id) {
        return passportService.findById(id);
    }

    /**
     * GET method for getting passport by series of passport
     * @param series - series of passport
     * @return object of Passport
     */
    @GetMapping("/find/series/{series}")
    public Passport findBySeries(@PathVariable String series) {
        return passportService.findBySeries(series);
    }

    /**
     * POST method for adding new passport
     * @param passport - object of Passport
     * @return created object of Passport
     */
    @PostMapping("/save")
    public Passport create(@RequestBody Passport passport) {
        return passportService.create(passport);
    }

    /**
     * PUT method for updating passport
     * @param id passport ID
     * @param passport object of Passport
     */
    @PutMapping("/update/{id}")
    public void update(@PathVariable int id, @RequestBody Passport passport) throws InvocationTargetException, IllegalAccessException {
        passportService.update(id, passport);
    }

    /**
     * DELETE method for deleting passport
     * @param id passport ID
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        passportService.delete(id);
    }

    /**
     * GET method for getting unavailable passport
     * @return List of unavailable passports
     */
    @GetMapping("/unavailable")
    public List<Passport> findUnavailable() {
        return passportService.findUnavailable();
    }

    /**
     * GET method for getting passports which expire in the next 3 months
     * @return List of Passports
     */
    @GetMapping("/replaceable")
    public List<Passport> findReplaceable() {
        return passportService.findReplaceable();
    }

}
