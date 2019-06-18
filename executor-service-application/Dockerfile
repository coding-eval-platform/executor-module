FROM openjdk:11.0.3-jdk-slim
ARG JAR_FILE

# Set up the necessary environment variables
ENV CONFIG_LOCATION=config \
    RUNNERS_CONFIG_FILE=runners.properties \
    RUNNERS_PATH=/usr/local/runners \
    JAR=app.jar \
    RUNNERS_PREFIX=code-runner.commands \
    JAVA_RUNNER=run-java.sh \
    RUBY_RUNNER=run-ruby.sh \
    C_RUNNER=run-c.sh

# Path must be set in another layer as it uses stuff from the previous layer
ENV PATH $RUNNERS_PATH:$PATH

# Set up the additional configuration directory
RUN set -eux; \
        mkdir $CONFIG_LOCATION;

# Set up the runners configuration
RUN set -eux; \
        touch $CONFIG_LOCATION/$RUNNERS_CONFIG_FILE; \
        echo "$RUNNERS_PREFIX.java=$JAVA_RUNNER" >> $CONFIG_LOCATION/$RUNNERS_CONFIG_FILE; \
        echo "$RUNNERS_PREFIX.ruby=$RUBY_RUNNER" >> $CONFIG_LOCATION/$RUNNERS_CONFIG_FILE; \
        echo "$RUNNERS_PREFIX.c=$C_RUNNER" >> $CONFIG_LOCATION/$RUNNERS_CONFIG_FILE;

# Copy executables
COPY target/${JAR_FILE} $JAR
COPY java.sh $RUNNERS_PATH/$JAVA_RUNNER
COPY ruby.sh $RUNNERS_PATH/$RUBY_RUNNER
COPY c.sh $RUNNERS_PATH/$C_RUNNER

# Install needed packages
RUN set -eux; \
        apt-get update; \
        apt-get install -y coreutils ruby gcc;

ENTRYPOINT java -jar $JAR --spring.config.additional-location=file:$CONFIG_LOCATION/$RUNNERS_CONFIG_FILE
EXPOSE 8000
