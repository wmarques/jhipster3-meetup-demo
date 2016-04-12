# JHipster 3rd Paris Meetup - Microservice presentation by Pierre Besson & William Marques

Refer to the slides at: [wmarques.github.io/meetup-jhipster-3](http://wmarques.github.io/meetup-jhipster-3).

## Pre-requisites:

Install docker and docker-compose if not installed (see [docker docs](https://docs.docker.com/)).

Build the jar and docker images in each of the following directories:
- accounts-microservice
- accounts-microservice-v2
- hipster-bank-gateway
- insurances-microservice

using the command:

      ./mvnw package -Pprod -DskipTests docker:build

## Part 1: Docker Compose usage for microservices

### Run all microservices at once:

```
   cd docker-config
   yo jhipster:docker-compose

   docker-compose up -d
```
### Scale a microservice:
```
   docker-compose scale accountsmicroservice-app=2
```
Then run `docker-compose logs accountsmicroservice` and see the second instance of the microservice starting, and their Hazelcast clustered caches getting in sync thanks to Eureka discovery.

**Sample Hazelcast Logs:**
```
accountsmicroservice-app_1 | Members [2] {
accountsmicroservice-app_1 | 	Member [172.22.0.8]:5701 this
accountsmicroservice-app_1 | 	Member [172.22.0.15]:5701
accountsmicroservice-app_1 | }
accountsmicroservice-app_1 | 
accountsmicroservice-app_2 | 2016-04-12 08:33:46.592  INFO 1 --- [ration.thread-3] com.hazelcast.cluster.ClusterService     : [172.22.0.15]:5701 [dev] [3.6.1] 
accountsmicroservice-app_2 | 
accountsmicroservice-app_2 | Members [2] {
accountsmicroservice-app_2 | 	Member [172.22.0.8]:5701
accountsmicroservice-app_2 | 	Member [172.22.0.15]:5701 this
accountsmicroservice-app_2 | }
```

### Scale a Mongodb database with docker:

Refer to https://github.com/jhipster/jhipster.github.io/pull/243/commits/8bc5bc9bbb39b803d71036c985c70d01c729f89f

## Part 2: Microservice monitoring

Open [http://localhost:5601](http://localhost:5601) and have fun !

## Part 3: Microservice configuration

### Sample property configuration

* Open [http://localhost:8080/property/greeting](http://localhost:8080/property/greeting).
* Tweak the value of `property.greeting` in `docker-config/central-server-config/hipsterBangGateway.yml`
* Trigger refreshing of the gateway's config: `curl -X POST http://localhost:8080/refresh`

### Distributed Feature Toggle demo

* Regenerate the docker-compose file using `yo jhipster:docker-compose` to add the account-v2 service.
* Run `docker-compose up -d` to start `account-v2` and its database and elasticsearch dependencies.

* In `hipsterBangGateway.yml`, edit the zuul config as follows and refresh the configuration using the `curl` command above.

```
zuul:
  ignoredServices: '*'
  routes:
    accountsmicroservicev2:
      path: /accountsmicroservice/**
      serviceId: accountsmicroservicev2
```

* Open [http://localhost:8080/#/gateway](http://localhost:8080/#/gateway) to see the gateway route changing from `account` to `account-v2`.
* On the BankAcount entity screen, the `account-v2` back-end service is now used but there is still no search bar to make use of elasticsearch.
* Finally, change `feature-toggle.account` to `v2` and refresh once again with `curl` to toggle the appearance of the search bar on the front-end .
