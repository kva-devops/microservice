package ru.job4j.microservice.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.microservice.model.Passport;

import java.util.Collection;
import java.util.Optional;

/**
 * DAO interface for working with Passport models
 */
public interface PassportRepository extends CrudRepository<Passport, Integer> {
    /**
     * Find passport by series of passport
     * @param series - series of passport
     * @return Optional of Passport
     */
    @Query("SELECT p FROM Passport p WHERE p.series = :series")
    Optional<Passport> findBySeries(@Param("series") String series);

    /**
     * Find unavailable passports
     * @return Collection of unavailable passports
     */
    @Query("SELECT p FROM Passport p WHERE p.validityPeriod < current_timestamp ")
    Collection<Passport> findUnavailable();

    /**
     * Find passports which expire in the next 3 months
     * @return Collection of passports
     */
    @Query(
            value = "select * from passports "
                    + "where validity_period - now() < make_interval(months => 3)"
                    + "and validity_period > now()", nativeQuery = true)
    Collection<Passport> findReplaceable();
}
