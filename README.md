# Приложение Microservice
[![Build Status](https://app.travis-ci.com/kva-devops/microservice.svg?branch=master)](https://app.travis-ci.com/kva-devops/microservice)

## О проекте
#### Описание
Учебный проект - Микросервисы.
Решаемые задачи:
Асинхронное взаимодействие сервисов через брокер сообщений Kafka.
Взаимодействие рассматривается на примере приложения, работающего с базой данных паспортов.

Основная цель:
Проект состоит из трех микросервисов: *microservice*, *someservice*, *mail*. 
При появлении в базе данных паспорта с истёкшим сроком действия, модуль *mircoservice*, отправляет
сообщение в тему "mail" брокера Kafka. Модуль *mail* отслеживает данную тему и при появлении нового
сообщения посылает уведомление об истёкшем сроке действия (функционал упрощен).
Управление приложением осуществляется через модуль *someservice*, все управляющие запросы идут от данного модуля.

#### Технологии
>JDK14, Maven, PostgreSQL, Liquibase, Spring Boot (Web, Data, Kafka), Java Servlet, Microservices, Kafka, Zookeeper

## Сборка 
0. Убедиться, что на хосте запущены службы Zookeeper и Kafka: 
`sudo systemctl start zookeeper`
`sudo systemctl start kafka`
1. Скачать исходные файлы проекта с репозитория GitHub
2. Собрать микросервисы: 
    -microservice `mvn install`
    -someservice `mvn package spring-boot:repackage`
    -mail `mvn package spring-boot:repackage`
3. Запускаем микросервисы в отдельных терминалах:
    `java -jar target/microservice-1.0.jar`
    `java -jar someservice/target/someservice-1.0-exec.jar`
    `java -jar mail/target/mail-1.0.jar`

## Как пользоваться 
Заполняем базу данных необходимым количеством записей.

![addItems](images/Selection_203.png)

В ручном режиме добавляем в базу запись паспорта с истёкшим сроком действия.

![addCustomItems](images/Selection_204.png)

Проверяем наличие всех записей в базе данных:

![checkAllItems](images/Selection_205.png)

Далее осуществляем запрос на наличие в базе просроченных паспортов:

![checkNotActive](images/Selection_206.png)

При положительном ответе, в сервисе Mail сработает триггер, и сервис отправит уведомление.

![viewResult](images/Selection_207.png) 

## Контакты.
Кутявин Владимир Анатольевич

skype: tribuna87

email: tribuna87@mail.ru

telegram: @kutiavinvladimir