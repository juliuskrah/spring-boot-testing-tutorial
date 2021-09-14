# Spring Boot Testing Tutorial

This repository contains a simple `spring boot` web application for creating clients and services.
The information presented here is to demonstrate how you test various aspects of your spring boot
application such as `data access objects`, `services`, `controllers` and `utility classes`.

It also demonstrates concerns such as `unit tests`, `test slices`, `integration tests` and `mocks`.

## Branches

There are two branches in this repository: 

- `complete`: this contains the complete project with test cases
- `initial`: this does not contain any test cases

## Structure of the code

```
.
|__src/
|  |__main/
|  |  |__java/
|  |  |  |__com/juliuskrah/demos/springboottestingtraining/
|  |  |  |  |__SpringBootTestingTrainingApplication.java
|  |  |  |  |  |__dto/
|  |  |  |  |  |__model/
|  |  |  |  |  |  |__Client.java
|  |  |  |  |  |  |__Service.java
|  |  |  |  |  |  |__ServiceSetting.java
|  |  |  |  |  |  |__ServiceSettingId.java
|  |  |  |  |  |__repository/
|  |  |  |  |  |  |__ClientRepository.java
|  |  |  |  |  |  |__ServiceRepository.java
|  |  |  |  |  |  |__ServiceSettingRepository.
|  |  |  |  |  |__service/
|  |  |  |  |  |  |__ClientService.java
|  |  |  |  |  |  |__ClientServiceImpl.java
|  |  |__resources/
|  |  |  |__db/
|  |  |  |  |__changelog/
|  |  |  |  |  |__db.changelog-master.yaml
|  |  |  |  |__data/
|  |  |  |  |  |__client.csv
|  |  |  |  |  |__service.csv
|  |  |  |  |  |__service_setting.csv
|  |  |  |__application.properties
|  |__test/
|  |  |__java/
|  |  |  |__com/juliuskrah/demos/springboottestingtraining/
|  |  |  |  |__SpringBootTestingTrainingApplication.java
|  |  |  |  |  |__repository/
|  |  |  |  |  |  |__ClientRepositoryTest.java
|  |  |  |  |  |  |__ServiceRepositoryTest.java
|  |  |  |  |  |  |__ServiceSettingRepositoryTest.java
|  |  |  |  |  |__service/
|  |  |  |  |  |  |__ClientServiceTest.java
|  |  |__resources/
|  |  |  |__application.yaml
|__pom.xml
```

- `repository`: contains the `data access objects`
- `model`: contains the `domain objects`
- `controler`: contains the anotated controllers
- `service`: contains the `business objects`
- `utilities`: contains the utility classes

## What is covered

1. Unit tests  
  - slice tests  
  - testcontainers
  - mockito: `@mock`, `@mockBean`, `@spy`
  - wiremock
2. Integration tests
  - @SpringBootTest
  - Maven failsafe plugin

## Maven goals

1. `test`: Using Surefire plugin
2. `verify`: using Failsafe plugin

## Takeaways

- Don't test framework code
- Reuse application context or Dirties Context
- `@Transactional`
- `ArgumentCaptor`
- Testing exceptions