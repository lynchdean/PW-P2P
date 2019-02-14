FROM openjdk:latest
ENV CATALINA_OPTS -Dsecurerandom.source=file:/dev/urandom
COPY ./target/classes/com/lynchd49/syncsafe/gui/AppMain.class /tmp
WORKDIR /tmp
ENTRYPOINT ["java","AppMain"]