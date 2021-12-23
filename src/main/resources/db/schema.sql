create table passports (
    id serial primary key,
    series varchar (4),
    number varchar (8),
    name varchar (2000),
    surname varchar (2000),
    birthday timestamp,
    date_of_issue timestamp,
    validity_period timestamp
);