# Application "Microservices"
[![Build Status](https://app.travis-ci.com/kva-devops/microservice.svg?branch=master)](https://app.travis-ci.com/kva-devops/microservice)

## About project
#### Description
Project - "Microservices".
Tasks to be solved:
Asynchronous communication of services through the Kafka message broker.
Interaction is considered on the example of an application that works with a database of passports.

The project consists of three microservices: *microservice*, *someservice*, *mail*.
The *mircoservice* module monitors the addition of new records to the database. In case the record does not satisfy
certain conditions (eg expired passports), the service sends a message to the "mail" subject of the Kafka broker.
The *mail* module keeps track of this topic and when a new one appears
message sends an expired notification (functionality simplified).
The application is controlled through the *someservice* module, all control requests come from this module.

#### Technologies
>JDK14, Maven, PostgreSQL, Liquibase, Spring Boot (Web, Data, Kafka), Java Servlet, Microservices, Kafka, Zookeeper

## Init 
0. Make sure the Zookeeper and Kafka services are running on the host:
`sudo systemctl start zookeeper`
`sudo systemctl start kafka`
1. Download project source files from GitHub repository
2. Assemble microservices: 
    -microservice `mvn install`
    -someservice `mvn package spring-boot:repackage`
    -mail `mvn package spring-boot:repackage`
3. We start microservices in separate terminals:
    `java -jar target/microservice-1.0.jar`
    `java -jar someservice/target/someservice-1.0-exec.jar`
    `java -jar mail/target/mail-1.0.jar`

## How use
We fill the database with the required number of records.

![addItems](images/Selection_203.png)

In manual mode, add an expired passport record to the database.

![addCustomItems](images/Selection_204.png)

Checking if all records are in the database:

![checkAllItems](images/Selection_205.png)

Next, we make a request for the presence of expired passports in the database:

![checkNotActive](images/Selection_206.png)

If the answer is positive, the trigger will work in the Mail service, and the service will send a notification.

![viewResult](images/Selection_207.png) 

## Contact.

Kutiavin Vladimir

telegram: @kutiavinvladimir