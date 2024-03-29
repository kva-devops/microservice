package ru.job4j.someservice.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "documents")
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

    private static String genSeries() {
        int length = 4;
        return RandomStringUtils.random(length, false, true);
    }

    private static String genNumbers() {
        int length = 8;
        return RandomStringUtils.random(length, false, true);
    }

    private static Timestamp calcValidityPeriod(Timestamp dateOfIssue) {
        LocalDate buff = dateOfIssue.toLocalDateTime().toLocalDate().plusYears(10);
        return Timestamp.valueOf(buff.atStartOfDay());
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
