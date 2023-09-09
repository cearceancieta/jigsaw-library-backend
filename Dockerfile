FROM eclipse-temurin:17 as layerer
COPY ./target/jigsaw-library-backend-1.0-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-alpine
COPY --from=layerer dependencies/ ./
COPY --from=layerer snapshot-dependencies/ ./
COPY --from=layerer spring-boot-loader/ ./
COPY --from=layerer application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
