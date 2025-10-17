FROM openjdk:8-jdk-alpine

#https://rominirani.com/docker-on-windows-mounting-host-directories-d96f3f056a2c
VOLUME /data

MAINTAINER Ravindar Vangala<rvangala@gmail.com>

ENV SPRING_PROFILE_NAME=""
ENV SPRING_CONFIG_NAME="fhirapi"
ENV JAVA_OPTS="-Dspring.profiles.active=$SPRING_PROFILE_NAME -Dspring.config.name=$SPRING_CONFIG_NAME"

ENV APP_FILE fhirapi.jar
ENV APP_HOME /app

RUN mkdir -p $APP_HOME

COPY build/libs/fhir_api-*.jar $APP_HOME/$APP_FILE
COPY "${SPRING_CONFIG_NAME}.yaml" $APP_HOME/config/

WORKDIR $APP_HOME

# change port with -p 8080:8080
EXPOSE 8009

# http://www.johnzaccone.io/entrypoint-vs-cmd-back-to-basics/
#CMD ["exec java -Dspring.profiles.active=${SPRING_PROFILE} -Djava.security.egd=file:/dev/./urandom -jar $APP_FILE"]
# docker build --tag flowsigma/fhirapi:latest .
# docker run ---env SPRING_CONFIG_NAME=fhirapi -publish 8009:8009 --name fhirapi flowsigma/fhirapi:latest

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $APP_FILE" ]
