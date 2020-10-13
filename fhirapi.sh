#!/bin/bash

export JAVA_HOME=/c/programs/jdk1.8.0_162
export PATH=$JAVA_HOME/bin:$PATH

echo $(java -version)

./gradlew clean assemble

cp build/libs/ewocs_fhir_api-*.jar build/libs/ewocsfhirapi.jar

java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5007 build/libs/ewocsfhirapi.jar --spring.config.name=ewocsfhirapi

# To Debug app Refer: https://www.linkedin.com/pulse/debug-jar-files-intellij-idea-maksym-lushpenko/