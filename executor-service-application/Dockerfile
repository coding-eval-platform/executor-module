FROM openjdk:11.0.3-jdk-slim
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar

ENV RUNNERS_PATH = /usr/local/runners
COPY java.sh $RUNNERS_PATH/run-java
COPY ruby.sh $RUNNERS_PATH/run-ruby
COPY c.sh $RUNNERS_PATH/run-c
ENV PATH $RUNNERS_PATH:$PATH

RUN apt-get update && apt-get install -y coreutils


VOLUME /tmp
ENV JAVA_RUNNER "--code-runner.commands.java=run-java"
ENV RUBY_RUNNER "--code-runner.commands.ruby=run-ruby"
ENV C_RUNNER    "--code-runner.commands.c=run-c"


ENTRYPOINT ["java","-jar", "app.jar", "$JAVA_RUNNER", "$RUBY_RUNNER", "$C_RUNNER"]
EXPOSE 8000
