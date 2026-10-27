FROM maven:4.0.0-rc-5-amazoncorretto-25-al2023 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:25.0.4
WORKDIR /app
COPY --from=build ./build/target/*.jar ./sistemapedidosapi.jar

EXPOSE 8080
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar sistemapedidosapi.jar