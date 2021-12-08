# Counter API

### Requirements

 - Java 11
 - Maven 3

### Build and run with Docker

```
mvn spring-boot:build-image
```

```
docker run -p 8080:8080 counter:0.0.1-SNAPSHOT
```

### Up and Running

```
mvn spring-boot:run
```

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev  // ---> loads `dev` profile
```

```
mvn spring-boot:run -Dspring-boot.run.profiles=prod  // ---> loads `prod` profile
```

### Unit tests

```
mvn test
```

### API docs

```
http://localhost:8080/swagger-ui.html
```
