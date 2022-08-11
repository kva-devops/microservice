package ru.job4j.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Model of passport
 */
@Entity
@Table(name = "passports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String series;

    private String number;

    private String name;

    private String surname;

    private Timestamp birthday;

    private Timestamp dateOfIssue;

    private Timestamp validityPeriod;

    public static Passport of(String name, String surname, Timestamp birthday) {
        Passport passport = new Passport();
        passport.name = name;
        passport.surname = surname;
        passport.birthday = birthday;
        passport.series = genSeries();
        passport.number = genNumbers();
        passport.dateOfIssue = new Timestamp(System.currentTimeMillis());
        passport.validityPeriod = calcValidityPeriod(passport.dateOfIssue);
        return passport;
    }

    /**
     * Method for generating random series of passport
     * @return series of passport (String)
     */
    private static String genSeries() {
        int length = 4;
        return RandomStringUtils.random(length, false, true);
    }

    /**
     * Method for generating random number of passport
     * @return number of passport (String)
     */
    private static String genNumbers() {
        int length = 8;
        return RandomStringUtils.random(length, false, true);
    }

    /**
     * Method for calculating validity period of passport
     * @param dateOfIssue - date of issue passport (Timestamp)
     * @return expiration date of passport (Timestamp)
     */
    private static Timestamp calcValidityPeriod(Timestamp dateOfIssue) {
        LocalDate expirationLocalDate = dateOfIssue.toLocalDateTime().toLocalDate().plusYears(10);
        return Timestamp.valueOf(expirationLocalDate.atStartOfDay());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Passport passport = (Passport) o;
        return id == passport.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
