FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY README.md /app/README.md
COPY charts /app/charts
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
ENV BASE_CURRENCY=${BASE_CURRENCY}
ENV TARGET_CURRENCY=${TARGET_CURRENCY}
WORKDIR /app
COPY --from=builder /app/target/exchangerate-0.0.1-SNAPSHOT.jar exchangerate.jar
COPY README.md /app/README.md
COPY charts /app/charts
ENTRYPOINT ["java", "-jar", "/app/exchangerate.jar"]